/*
 * Java
 *
 * Copyright 2020-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.demo.watch.watchface.flower.path;

import com.microej.demo.watch.util.path.GradientStyle;

import ej.microui.display.GraphicsContext;
import ej.microvg.LinearGradient;
import ej.microvg.Matrix;
import ej.microvg.Path;
import ej.microvg.VectorGraphicsPainter;

/**
 * A vector graphics that represents the gradient hand of the flower watchface.
 */
public class FlowerSecondGradient {


	private final int gradientAngle;

	private int rotationCenterX;
	private int rotationCenterY;

	private final Path path;
	private final Matrix matrix;
	private final LinearGradient linearGradient;

	/**
	 * Creates the hand by describing its path and gradient.
	 *
	 * @param gradientStyle
	 *            the gradient information.
	 * @param diameter
	 *            face diameter
	 */
	public FlowerSecondGradient(GradientStyle gradientStyle, int diameter) {
		this.gradientAngle = gradientStyle.getAngle();

		this.path = new Path();
		this.path.moveTo(0, -diameter);
		this.path.lineTo(-diameter, -diameter);
		this.path.lineTo(-diameter, diameter);
		this.path.lineTo(0, diameter);

		this.matrix = new Matrix();

		int[] colors = gradientStyle.getColors();
		float[] stops = gradientStyle.getFloatStops();
		this.linearGradient = new LinearGradient(0, 0, 255, 0, colors, stops); // NOSONAR LinearGradient declaration is
		// more readable with magic numbers
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
	}

	/**
	 * Sets the angle of the gradient.
	 *
	 * @param angle
	 *            the angle of rotation.
	 */
	public void setAngle(float angle) {
		this.matrix.setTranslate(this.rotationCenterX, this.rotationCenterY);
		this.matrix.preRotate(angle);

		Matrix gradientMatrix = this.linearGradient.getMatrix();
		gradientMatrix.setTranslate(0, -this.rotationCenterY);
		gradientMatrix.preRotate(this.gradientAngle);
	}

	/**
	 * Renders the content of the FlowerSecondGradient.
	 *
	 * @param g the graphics context where to render the content of the
	 *          FlowerSecondGradient.
	 */
	public void render(GraphicsContext g) {
		VectorGraphicsPainter.fillGradientPath(g, this.path, this.matrix, this.linearGradient);
	}

}
