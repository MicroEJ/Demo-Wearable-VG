/*
 * Java
 *
 * Copyright 2020-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.path.vectorfont;

import com.microej.demo.watch.util.path.GradientStyle;

import ej.microui.display.GraphicsContext;
import ej.microvg.LinearGradient;
import ej.microvg.Matrix;
import ej.microvg.Path;
import ej.microvg.VectorGraphicsPainter;

/**
 * A vector graphics that represents an hexagon.
 */
public class Hexagon {

	private static final int GRADIENT_SCALE = 2;
	private static final int GRADIENT_SIZE = 512;

	private static final int TRANSLATE = 50;

	private final Path path;
	private final Matrix matrix;
	private LinearGradient linearGradient;

	private float translateX;
	private float translateY;

	private float scale;
	private final int angle;
	private final int[] colors;
	private final float[] stops;

	/**
	 * Creates an hexagon, using the specified gradient information .
	 *
	 * @param gradientStyle
	 *            the gradient information.
	 *
	 */
	public Hexagon(GradientStyle gradientStyle) {
		this.path = createPath();
		this.matrix = new Matrix();
		this.angle = gradientStyle.getAngle();
		this.colors = gradientStyle.getColors();
		this.stops = gradientStyle.getFloatStops();
	}

	/**
	 * @return
	 */
	private static Path createPath() {
		Path path = new Path();

		path.moveTo(75, 97);
		path.cubicTo((byte) 68.504808, 100, (byte) 31.495188, 100, 25, 98);
		path.cubicTo((byte) 18.504807, 100, 0, (byte) (255.13205 - 197), 0, 50);
		path.cubicTo(0, (byte) (238.86794 - 197), (byte) 18.504811, (byte) (204.11555 - 197), 25, 3);
		path.cubicTo((byte) 31.495193, 0, (byte) 68.504812, 0, 75, 3);
		path.cubicTo((byte) 81.495193, (byte) (204.11555 - 197), 100, (byte) (238.86794 - 197), 100, 50);
		path.cubicTo(100, (byte) (255.13206 - 197), (byte) 81.495189, (byte) (289.88444 - 197), 75, 97);

		return path;
	}

	/**
	 *
	 */
	protected void updateMatrices() {
		Matrix matrix = this.matrix;
		matrix.setTranslate(this.translateX, this.translateY);
		matrix.preScale(this.scale, this.scale);
		matrix.preTranslate(-TRANSLATE, -TRANSLATE);

		this.linearGradient = new LinearGradient(0, 0, GRADIENT_SIZE, 0, this.colors, this.stops);
		Matrix gradientMatrix = this.linearGradient.getMatrix();
		gradientMatrix.setTranslate(0, TRANSLATE);
		gradientMatrix.preScale(GRADIENT_SCALE, GRADIENT_SCALE);
		gradientMatrix.preRotate(this.angle);
		gradientMatrix.postTranslate(TRANSLATE, TRANSLATE);
		gradientMatrix.postScale(1 / this.scale, 1 / this.scale);
		gradientMatrix.postTranslate(-this.translateX, -this.translateY);
	}

	/**
	 * Sets the transformation to apply to this vector graphics.
	 *
	 * @param translateX
	 *            the horizontal translation to apply.
	 * @param translateY
	 *            the vertical translation to apply.
	 * @param scale
	 *            the scale to apply.
	 */
	public void setTransformation(float translateX, float translateY, float scale) {
		this.translateX = translateX;
		this.translateY = translateY;
		this.scale = scale;
		updateMatrices();
	}

	/**
	 * Renders the content of the hexagon.
	 *
	 * @param g
	 *            Graphic context.
	 */
	public void render(GraphicsContext g) {
		VectorGraphicsPainter.fillGradientPath(g, this.path, this.matrix, this.linearGradient);
	}

}
