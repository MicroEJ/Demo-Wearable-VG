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
 * A vector graphics that represents the background of the chart.
 */
public class ChartTimezoneB {

	private final Path path;
	private final Matrix matrix;
	private final LinearGradient linearGradient;

	/**
	 * Creates the background of the chart using a rectangular path and a gradient.
	 *
	 * @param width
	 *            the width of the vector graphics.
	 * @param height
	 *            the height of the vector graphics.
	 * @param gradientStyle
	 *            the gradient information.
	 *
	 */
	public ChartTimezoneB(int width, int height, GradientStyle gradientStyle) {
		this.path = createPath(width, height);
		this.matrix = new Matrix();
		this.linearGradient = new LinearGradient(0, 0, 0, height, gradientStyle.getColors(),
				gradientStyle.getFloatStops());
	}

	private static Path createPath(int width, int height) {
		Path pathBuffer = new Path();

		pathBuffer.moveTo(0, 0);
		pathBuffer.lineTo(width, 0);
		pathBuffer.lineTo(width, height);
		pathBuffer.lineTo(0, height);

		return pathBuffer;
	}

	/**
	 * Renders the content of the chart's time indicator B.
	 *
	 * @param g
	 *            Graphic context.
	 */
	public void render(GraphicsContext g) {
		VectorGraphicsPainter.fillGradientPath(g, this.path, this.matrix, this.linearGradient);
	}

}
