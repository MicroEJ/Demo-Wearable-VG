/*
 * Java
 *
 * Copyright 2020-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.path.compass;

import com.microej.demo.watch.util.path.VectorGraphics;

import ej.microui.display.GraphicsContext;
import ej.microvg.Matrix;
import ej.microvg.VectorGraphicsPainter;
import ej.microvg.VectorImage;

/**
 * A vector path that represents the four compass collateral points arrows.
 * <p>
 * The four arrows are represented by the same path. A rotation is performed before each arrow rendering.
 */
public class CompassArrow extends VectorGraphics {

	private static final int NW_ROTATION_ANGLE = -45;
	private static final int NE_ROTATION_ANGLE = 45;
	private static final int SW_ROTATION_ANGLE = -135;
	private static final int SE_ROTATION_ANGLE = 135;

	private final int diameter;

	private int centerX;

	private int centerY;

	/**
	 * Creates the a compass collateral point arrows by describing its path.
	 *
	 * @param diameter
	 *            the arrow diameter
	 */
	public CompassArrow(int diameter) {
		super(VectorImage.getImage("/images/compass/compass_marker_white.svg")); //$NON-NLS-1$
		this.diameter = diameter;
	}

	@Override
	public void render(GraphicsContext g) {
		drawArrow(g, NW_ROTATION_ANGLE);
		drawArrow(g, NE_ROTATION_ANGLE);
		drawArrow(g, SW_ROTATION_ANGLE);
		drawArrow(g, SE_ROTATION_ANGLE);
	}

	private void drawArrow(GraphicsContext gc, int angle) {
		Matrix matrix = getMatrix();

		matrix.setRotate(angle);
		matrix.preTranslate(0, -this.diameter);
		matrix.postTranslate(this.centerX, this.centerY);
		VectorGraphicsPainter.drawImage(gc, getImage(), matrix);
	}

	/**
	 * Sets the compass center point coordinates.
	 *
	 * @param x
	 *            the compass center x-coordinate to set.
	 * @param y
	 *            the compass center y-coordinate to set.
	 *
	 */
	public void setCenter(int x, int y) {
		this.centerX = x;
		this.centerY = y;
	}
}
