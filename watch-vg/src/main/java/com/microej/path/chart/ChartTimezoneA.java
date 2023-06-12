/*
 * Java
 *
 * Copyright 2020-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.path.chart;

import com.microej.demo.watch.util.path.GradientStyle;

import ej.microui.display.GraphicsContext;
import ej.microvg.LinearGradient;
import ej.microvg.Matrix;
import ej.microvg.Path;
import ej.microvg.VectorGraphicsPainter;

/**
 * A vector graphics that represents the chart's time indicators.
 */
public class ChartTimezoneA {

	private static final float SVG_BOUND_WIDTH = 400f;

	private final Path path;
	private final Matrix matrix;
	private LinearGradient linearGradient;

	private final float translateY;
	private final int width;

	private float scale;
	private final int[] colors;
	private final float[] stops;
	private final int height;

	/**
	 * Constructs the path of the vector graphics, given its y-position, its height and its gradient information.
	 *
	 * @param width
	 *            the width of the vector graphics.
	 * @param height
	 *            the height of the vector graphics.
	 * @param gradientStyle
	 *            the gradient information.
	 */
	public ChartTimezoneA(int width, int height, GradientStyle gradientStyle) {
		this.path = createPath();
		this.matrix = new Matrix();

		this.translateY = height;
		this.scale = 1f;
		this.width = width;
		this.height = height;
		this.colors = gradientStyle.getColors();
		this.stops = gradientStyle.getFloatStops();
	}

	private static Path createPath() {
		Path pathBuffer = new Path();
		pathBuffer.setOrigin(0F, -140F);
		pathBuffer.moveTo(398.0f, 0.0f);
		pathBuffer.lineToRelative(0.0f, 127.19999694824219f);
		pathBuffer.cubicToRelative(0.0f, 5.5f, -4.5f, 10.0f, -10.0f, 10.0f);
		pathBuffer.cubicToRelative(-5.5f, 0.0f, -10.0f, -4.5f, -10.0f, -10.0f);
		pathBuffer.lineToRelative(0.0f, 0.0f);
		pathBuffer.lineTo(378.0f, 0.0f);
		pathBuffer.lineToRelative(-5.0f, 0.0f);
		pathBuffer.lineToRelative(0.0f, 127.19999694824219f);
		pathBuffer.cubicToRelative(0.0f, 5.5f, -4.5f, 10.0f, -10.0f, 10.0f);
		pathBuffer.cubicToRelative(-5.5f, 0.0f, -10.0f, -4.5f, -10.0f, -10.0f);
		pathBuffer.lineToRelative(0.0f, 0.0f);
		pathBuffer.lineTo(353.0f, 0.0f);
		pathBuffer.lineToRelative(-5.0f, 0.0f);
		pathBuffer.lineToRelative(0.0f, 127.19999694824219f);
		pathBuffer.cubicToRelative(0.0f, 5.5f, -4.5f, 10.0f, -10.0f, 10.0f);
		pathBuffer.cubicToRelative(-5.5f, 0.0f, -10.0f, -4.5f, -10.0f, -10.0f);
		pathBuffer.lineToRelative(0.0f, 0.0f);
		pathBuffer.lineTo(328.0f, 0.0f);
		pathBuffer.lineToRelative(-5.0f, 0.0f);
		pathBuffer.lineToRelative(0.0f, 127.19999694824219f);
		pathBuffer.cubicToRelative(0.0f, 5.5f, -4.5f, 10.0f, -10.0f, 10.0f);
		pathBuffer.cubicToRelative(-5.5f, 0.0f, -10.0f, -4.5f, -10.0f, -10.0f);
		pathBuffer.lineToRelative(0.0f, 0.0f);
		pathBuffer.lineTo(303.0f, 0.0f);
		pathBuffer.lineToRelative(-5.0f, 0.0f);
		pathBuffer.lineToRelative(0.0f, 127.19999694824219f);
		pathBuffer.cubicToRelative(0.0f, 5.5f, -4.5f, 10.0f, -10.0f, 10.0f);
		pathBuffer.cubicToRelative(-5.5f, 0.0f, -10.0f, -4.5f, -10.0f, -10.0f);
		pathBuffer.lineToRelative(0.0f, 0.0f);
		pathBuffer.lineTo(278.0f, 0.0f);
		pathBuffer.lineToRelative(-5.0f, 0.0f);
		pathBuffer.lineToRelative(0.0f, 127.19999694824219f);
		pathBuffer.cubicToRelative(0.0f, 5.5f, -4.5f, 10.0f, -10.0f, 10.0f);
		pathBuffer.cubicToRelative(-5.5f, 0.0f, -10.0f, -4.5f, -10.0f, -10.0f);
		pathBuffer.lineToRelative(0.0f, 0.0f);
		pathBuffer.lineTo(253.0f, 0.0f);
		pathBuffer.lineToRelative(-5.0f, 0.0f);
		pathBuffer.lineToRelative(0.0f, 127.19999694824219f);
		pathBuffer.cubicToRelative(0.0f, 5.5f, -4.5f, 10.0f, -10.0f, 10.0f);
		pathBuffer.cubicToRelative(-5.5f, 0.0f, -10.0f, -4.5f, -10.0f, -10.0f);
		pathBuffer.lineToRelative(0.0f, 0.0f);
		pathBuffer.lineTo(228.0f, 0.0f);
		pathBuffer.lineToRelative(-5.0f, 0.0f);
		pathBuffer.lineToRelative(0.0f, 127.19999694824219f);
		pathBuffer.cubicToRelative(0.0f, 5.5f, -4.5f, 10.0f, -10.0f, 10.0f);
		pathBuffer.cubicToRelative(-5.5f, 0.0f, -10.0f, -4.5f, -10.0f, -10.0f);
		pathBuffer.lineToRelative(0.0f, 0.0f);
		pathBuffer.lineTo(203.0f, 0.0f);
		pathBuffer.lineToRelative(-5.0f, 0.0f);
		pathBuffer.lineToRelative(0.0f, 127.19999694824219f);
		pathBuffer.cubicToRelative(0.0f, 5.5f, -4.5f, 10.0f, -10.0f, 10.0f);
		pathBuffer.cubicToRelative(-5.5f, 0.0f, -10.0f, -4.5f, -10.0f, -10.0f);
		pathBuffer.lineToRelative(0.0f, 0.0f);
		pathBuffer.lineTo(178.0f, 0.0f);
		pathBuffer.lineToRelative(-5.0f, 0.0f);
		pathBuffer.lineToRelative(0.0f, 127.19999694824219f);
		pathBuffer.cubicToRelative(0.0f, 5.5f, -4.5f, 10.0f, -10.0f, 10.0f);
		pathBuffer.cubicToRelative(-5.5f, 0.0f, -10.0f, -4.5f, -10.0f, -10.0f);
		pathBuffer.lineToRelative(0.0f, 0.0f);
		pathBuffer.lineTo(153.0f, 0.0f);
		pathBuffer.lineToRelative(-5.0f, 0.0f);
		pathBuffer.lineToRelative(0.0f, 127.19999694824219f);
		pathBuffer.cubicToRelative(0.0f, 5.5f, -4.5f, 10.0f, -10.0f, 10.0f);
		pathBuffer.cubicToRelative(-5.5f, 0.0f, -10.0f, -4.5f, -10.0f, -10.0f);
		pathBuffer.lineToRelative(0.0f, 0.0f);
		pathBuffer.lineTo(128.0f, 0.0f);
		pathBuffer.lineToRelative(-5.0f, 0.0f);
		pathBuffer.lineToRelative(0.0f, 127.19999694824219f);
		pathBuffer.cubicToRelative(0.0f, 5.5f, -4.5f, 10.0f, -10.0f, 10.0f);
		pathBuffer.cubicToRelative(-5.5f, 0.0f, -10.0f, -4.5f, -10.0f, -10.0f);
		pathBuffer.lineToRelative(0.0f, 0.0f);
		pathBuffer.lineTo(103.0f, 0.0f);
		pathBuffer.lineToRelative(-5.0f, 0.0f);
		pathBuffer.lineToRelative(0.0f, 127.19999694824219f);
		pathBuffer.cubicToRelative(0.0f, 5.5f, -4.5f, 10.0f, -10.0f, 10.0f);
		pathBuffer.cubicToRelative(-5.5f, 0.0f, -10.0f, -4.5f, -10.0f, -10.0f);
		pathBuffer.lineToRelative(0.0f, 0.0f);
		pathBuffer.lineTo(78.0f, 0.0f);
		pathBuffer.lineToRelative(-5.0f, 0.0f);
		pathBuffer.lineToRelative(0.0f, 127.19999694824219f);
		pathBuffer.cubicToRelative(0.0f, 5.5f, -4.5f, 10.0f, -10.0f, 10.0f);
		pathBuffer.cubicToRelative(-5.5f, 0.0f, -10.0f, -4.5f, -10.0f, -10.0f);
		pathBuffer.lineToRelative(0.0f, 0.0f);
		pathBuffer.lineTo(53.0f, 0.0f);
		pathBuffer.lineToRelative(-5.0f, 0.0f);
		pathBuffer.lineToRelative(0.0f, 127.19999694824219f);
		pathBuffer.cubicToRelative(0.0f, 5.5f, -4.5f, 10.0f, -10.0f, 10.0f);
		pathBuffer.cubicToRelative(-5.5f, 0.0f, -10.0f, -4.5f, -10.0f, -10.0f);
		pathBuffer.lineToRelative(0.0f, 0.0f);
		pathBuffer.lineTo(28.0f, 0.0f);
		pathBuffer.lineToRelative(-5.0f, 0.0f);
		pathBuffer.lineToRelative(0.0f, 127.19999694824219f);
		pathBuffer.cubicToRelative(0.0f, 5.5f, -4.5f, 10.0f, -10.0f, 10.0f);
		pathBuffer.cubicToRelative(-5.5f, 0.0f, -10.0f, -4.5f, -10.0f, -10.0f);
		pathBuffer.lineToRelative(0.0f, 0.0f);
		pathBuffer.lineTo(3.0f, 0.0f);
		pathBuffer.lineTo(0.0f, 0.0f);
		pathBuffer.lineToRelative(0.0f, 140.0f);
		pathBuffer.lineToRelative(400.0f, 0.0f);
		pathBuffer.lineTo(400.0f, 0.0f);
		pathBuffer.lineTo(398.0f, 0.0f);

		return pathBuffer;
	}

	/**
	 *
	 */
	protected void updateMatrices() {
		Matrix matrix = this.matrix;
		float scale = this.scale;
		matrix.setTranslate(SVG_BOUND_WIDTH / 2 * (1f - scale), this.translateY);
		scale *= this.width;
		scale /= SVG_BOUND_WIDTH;
		matrix.preScale(scale, scale);

		this.linearGradient = new LinearGradient(0, 0, 0, this.height, this.colors, this.stops);
		Matrix gradientMatrix = this.linearGradient.getMatrix();
		gradientMatrix.setScale(1 / scale, 1 / scale);
		gradientMatrix.preTranslate(-SVG_BOUND_WIDTH / 2 * (1f - scale), -this.translateY);
	}

	/**
	 * Renders the content of the chart's time indicator A.
	 *
	 * @param g
	 *            Graphic context.
	 */
	public void render(GraphicsContext g) {
		VectorGraphicsPainter.fillGradientPath(g, this.path, this.matrix, this.linearGradient);
	}

	/**
	 * Sets the scale of the vector graphics.
	 *
	 * @param scale
	 *            the scale factor.
	 */
	public void setScale(float scale) {
		this.scale = scale;
		updateMatrices();
	}

}
