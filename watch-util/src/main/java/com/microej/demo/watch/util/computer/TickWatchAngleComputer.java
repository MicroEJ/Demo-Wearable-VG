/*
 * Java
 *
 * Copyright 2020-2023 IS2T.-2020 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.demo.watch.util.computer;

/**
 * An implementation of {@link WatchAngleComputer} that computes the second hands ticks angle.
 */
public class TickWatchAngleComputer extends LinearWatchAngleComputer {

	/** Constant value for the angle between two successive seconds (in degrees). */
	protected static final float DEGREES_SECOND = 6;

	@Override
	public float getSecondAngle(float second) {
		return (int) second * DEGREES_SECOND;
	}
}
