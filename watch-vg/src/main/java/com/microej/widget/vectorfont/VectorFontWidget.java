/*
 * Java
 *
 * Copyright 2020-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.widget.vectorfont;

import ej.mwt.Widget;
import ej.mwt.util.Size;

/**
 * Abstract widget for VectorFont page. Its rendering changes according an angle.
 */
public abstract class VectorFontWidget extends Widget {

	private int angle;

	/**
	 * Creates the widget by describing the initial angle.
	 *
	 * @param angle
	 *            the initial angle in degrees.
	 */
	public VectorFontWidget(int angle) {
		this.angle = angle;
	}

	@Override
	protected void computeContentOptimalSize(Size size) {
		// Take all available space
	}

	/**
	 * Updates the angle without asking for a new render.
	 *
	 * @param angle
	 *            the new angle to render in degrees.
	 */
	public void updateAngle(int angle) {
		this.angle = angle;
	}

	/**
	 * Gets the current angle.
	 *
	 * @return the angle to render.
	 */
	protected int getAngle() {
		return this.angle;
	}

	/**
	 * Computes the transformation to apply, given the specified angle and a bit count.
	 *
	 * @param angle
	 *            the angle of the transformation.
	 * @param bitCount
	 *            the bit count.
	 * @return a number representing the transformation to apply.
	 */
	protected float computeTransformation(int angle, int bitCount) {
		final int x = angle;
		final int max = (1 << bitCount);
		final int mask = max - 1;

		final int direction = (x >> (bitCount - 1)) & 1;

		float dynamicTransformation = x & mask;
		dynamicTransformation = (direction << bitCount) - dynamicTransformation;
		dynamicTransformation *= (2 * direction) - 1;
		dynamicTransformation /= max;

		return dynamicTransformation;
	}

}
