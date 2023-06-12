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
 * A vector graphics that represents the chart axis.
 * <p>
 * The vector graphics is made of one rectangle of 1 pixel height, which is drawn multiple times.
 */
public class ChartAxis {

	private static final int THICKNESS = 1;

	private static final int LINE_SPACING = 20;

	private static final int START_Y = 25;

	private static final int LINE_COUNT = 6;

	private final int translateY;

	private final Path path;

	private final Matrix matrix;

	private final LinearGradient gradient;

	/**
	 * Creates the axis lines.
	 *
	 * @param height
	 *            the height of the vector graphics.
	 * @param width
	 *            the width of the vector graphics.
	 * @param gradientStyle
	 *            the gradient information.
	 */
	public ChartAxis(int height, int width, GradientStyle gradientStyle) {
		this.path = createPath(width);
		this.matrix = new Matrix();
		this.translateY = height;
		this.gradient = new LinearGradient(0, 0, width, 0, gradientStyle.getColors(), gradientStyle.getFloatStops());
	}

	private static Path createPath(int width) {
		Path pathBuffer = new Path();
		pathBuffer.moveTo(0, 0);
		pathBuffer.lineTo(0, -THICKNESS);
		pathBuffer.lineTo(width, -THICKNESS);
		pathBuffer.lineTo(width, 0);
		pathBuffer.lineTo(0, 0);
		pathBuffer.close();
		return pathBuffer;
	}

	/**
	 * Renders the content of the chart axis.
	 *
	 * @param g
	 *            Graphic context.
	 */
	public void render(GraphicsContext g) {
		int lineY = START_Y;
		for (int i = 0; i < LINE_COUNT; i++) {
			drawLine(g, lineY);
			lineY += LINE_SPACING;
		}
	}

	private void drawLine(GraphicsContext g, int lineY) {
		this.matrix.setTranslate(0, (float) this.translateY - lineY);
		VectorGraphicsPainter.fillGradientPath(g, this.path, this.matrix, this.gradient);
	}
}
