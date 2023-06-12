/*
 * Java
 *
 * Copyright 2020-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.demo.watch.util.computer;

/**
 * An implementation of {@link LinearWatchAngleComputer} that computes the second hand angle with a polynomial function.
 */
public class QuarticWatchAngleComputer extends LinearWatchAngleComputer {

	private static final double DEGREE = 4;

	@Override
	public float getSecondAngle(float seconds) {
		float angle = seconds * DEGREES_SECOND;
		int intAngle = (int) angle / DEGREES_SECOND + 1;
		float decAngle = (angle - DEGREES_SECOND * intAngle) / DEGREES_SECOND;

		decAngle = (float) (DEGREES_SECOND * Math.pow(decAngle, DEGREE));

		return DEGREES_SECOND * intAngle - decAngle;
	}
}