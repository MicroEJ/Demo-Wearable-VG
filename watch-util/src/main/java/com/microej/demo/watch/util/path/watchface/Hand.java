/*
 * Java
 *
 * Copyright 2020-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.demo.watch.util.path.watchface;

import com.microej.demo.watch.util.path.VectorGraphics;

import ej.microvg.Matrix;
import ej.microvg.VectorImage;

/**
 * A vector graphics that represents a watchface hand.
 */
public class Hand extends VectorGraphics {

	private static final float DEFAULT_SCALE_RATIO = 1f;

	private static final float SCALE = 1f;

	private int rotationCenterX;

	private int rotationCenterY;

	private final float offsetX;

	private final float offsetY;

	private float angle;

	private float scaleRatio;

	/**
	 * Creates a watchface hand, specifying the x and y coordinates of the rotation center relative to the shape bounds.
	 *
	 * @param image
	 *            the SVG image of the hand.
	 * @param offsetX
	 *            the pixel offset from left bounds.
	 * @param offsetY
	 *            the pixel offset from top bounds.
	 */
	public Hand(VectorImage image, float offsetX, float offsetY) {
		super(image);
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.scaleRatio = DEFAULT_SCALE_RATIO;
	}

	/**
	 * Sets the rotation center point coordinates.
	 *
	 * @param x
	 *            the rotation center x-coordinate to set.
	 * @param y
	 *            the rotation center y-coordinate to set.
	 *
	 */
	public void setRotationCenter(int x, int y) {
		this.rotationCenterX = x;
		this.rotationCenterY = y;
		updateMatrices();
	}

	/**
	 * Sets the angle.
	 *
	 * @param angle
	 *            the angle to set.
	 */
	public void setAngle(float angle) {
		this.angle = angle;
		updateMatrices();
	}

	/**
	 * Sets the scale.
	 *
	 * @param ratio
	 *            the scale ratio to set.
	 */
	public void setScaleRatio(float ratio) {
		this.scaleRatio = ratio;
		updateMatrices();
	}

	@Override
	protected void updateMatrices() {
		float scale = SCALE * this.scaleRatio;
		Matrix matrix = getMatrix();
		matrix.setScale(1 / scale, 1 / scale);
		matrix.preTranslate(scale * this.rotationCenterX, scale * this.rotationCenterY);
		matrix.preRotate(this.angle);
		matrix.preTranslate(SCALE * -this.offsetX, SCALE * -this.offsetY);
	}
}
