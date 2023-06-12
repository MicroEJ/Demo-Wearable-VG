/*
 * Java
 *
 * Copyright 2020-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.demo.watch.util.computer;

/**
 * An implementation of {@link WatchAngleComputer} that computes hand angles in a linear way.
 */
public class LinearWatchAngleComputer implements WatchAngleComputer {

	/** Constant value for the angle between two successive hours (in degrees). */
	private static final int DEGREES_HOUR = 30;

	/** Constant value for the angle between two successive minutes (in degrees). */
	private static final int DEGREES_MINUTE = 6;

	/** Constant value for the angle between two successive seconds (in degrees). */
	protected static final int DEGREES_SECOND = 6;

	private static final float MAX_LIMIT_HOURS = 12.0f;

	@Override
	public float getHourAngle(float hours) {
		if (hours >= MAX_LIMIT_HOURS) {
			hours -= MAX_LIMIT_HOURS;
		}
		return hours * DEGREES_HOUR;
	}

	@Override
	public float getMinuteAngle(float minutes) {
		return minutes * DEGREES_MINUTE;
	}

	@Override
	public float getSecondAngle(float seconds) {
		return seconds * DEGREES_SECOND;
	}

}