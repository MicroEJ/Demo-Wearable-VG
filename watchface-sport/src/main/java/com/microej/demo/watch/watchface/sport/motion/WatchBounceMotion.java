/*
 * Java
 *
 * Copyright 2020-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.demo.watch.watchface.sport.motion;

import ej.motion.AbstractMotion;

/**
 * Represents a bounce motion.
 */
public class WatchBounceMotion extends AbstractMotion {

	private static final float FIRST_FACTOR = 3f;

	private static final float SECOND_FACTOR = 8f;

	private static final float THIRD_FACTOR = 27f;

	private static final float FIRST_THRESHOLD = 0.25f;

	private static final float SECOND_THRESHOLD = 0.5f;

	private static final float THIRD_THRESHOLD = 0.75f;

	private static final float FOURTH_THRESHOLD = 1f;

	private final int amplitude;

	/**
	 * Creates a bounce motion starting and ending at specified value.
	 *
	 * @param value
	 *            the start and stop stop value.
	 * @param maxDuration
	 *            the maximum duration of the motion.
	 * @param maxAmplitude
	 *            the maximum amplitude of the motion.
	 *
	 */
	public WatchBounceMotion(int value, long maxDuration, int maxAmplitude) {
		super(value, value, maxDuration);
		this.amplitude = maxAmplitude;
	}

	@Override
	protected boolean isFinished(int value) {
		return false;
	}

	/**
	 * Gets the bounce ease out value for the given time.
	 *
	 * @param elapsed
	 *            the elapsed time.
	 * @return the value for the given time.
	 */
	private int bounce(long elapsed) {
		float ratio = (float) elapsed / this.duration;
		int stop = this.stop;
		int amplitude = this.amplitude;

		if (ratio < FIRST_THRESHOLD) {
			return (int) (stop + stop * amplitude * ratio * (ratio - FIRST_THRESHOLD));
		} else if (ratio < SECOND_THRESHOLD) {
			return (int) (stop
					+ stop * (amplitude / FIRST_FACTOR) * (ratio - FIRST_THRESHOLD) * (ratio - SECOND_THRESHOLD));
		} else if (ratio < THIRD_THRESHOLD) {
			return (int) (stop
					+ stop * (amplitude / SECOND_FACTOR) * (ratio - SECOND_THRESHOLD) * (ratio - THIRD_THRESHOLD));
		} else {
			return (int) (stop
					+ stop * (amplitude / THIRD_FACTOR) * (ratio - THIRD_THRESHOLD) * (ratio - FOURTH_THRESHOLD));
		}
	}

	@Override
	protected int computeCurrentValue(long elapsed) {
		return bounce(elapsed);
	}

}
