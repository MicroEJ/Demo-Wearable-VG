/*
 * Java
 *
 * Copyright 2020-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.path.chart;

import ej.microui.display.GraphicsContext;
import ej.microvg.Matrix;
import ej.microvg.Path;
import ej.microvg.VectorGraphicsPainter;

/**
 * A vector graphics that represents the curve of a line chart.
 *
 * <p>
 * The curve is built using Bézier curve from the data array in input.
 *
 */
public class ChartCurve {

	private static final double HALF_PI = Math.PI / 2;

	private static final int SMOOTH_FACTOR = 3;

	private static final int TOP_PADDING = 5;

	private final int hStep;

	private final float thickness;

	private Path path;

	private final int color;

	private final Matrix matrix;

	/**
	 * Constructs the vector curve, given the data array to plot.
	 *
	 * @param height
	 *            the height of the curve's bounding-box.
	 * @param data
	 *            the data array which contains the values to plot.
	 * @param step
	 *            the horizontal spacing to use between each data point.
	 * @param color
	 *            the color of the curve.
	 * @param thickness
	 *            the thickness of the curve.
	 */
	public ChartCurve(int height, float[] data, int step, int color, float thickness) {
		this.path = computePath(data, step, thickness);
		this.color = color;
		this.hStep = step;
		this.thickness = thickness;

		this.matrix = new Matrix();
		this.matrix.setTranslate(0, height - TOP_PADDING);
	}

	private static Path computePath(float[] data, int hStep, float thickness) {
		Path pathBuffer = new Path();
		int i = 0;
		int x = hStep;
		int width = hStep * data.length;

		float firstData = getValueAtIndex(data, i);
		double sectionAngle = getSectionAngle(getValueAtIndex(data, i - 1), firstData, getValueAtIndex(data, i + 1),
				hStep);
		i++;

		float halfThickness = thickness / 2;
		pathBuffer.moveTo(0, (-firstData) - halfThickness);

		while (true) {
			float previous = getValueAtIndex(data, i - 1);
			float current = getValueAtIndex(data, i);
			float next = getValueAtIndex(data, i + 1);

			sectionAngle = plot(pathBuffer, sectionAngle, previous, current, next, x, hStep, halfThickness);

			if (x >= (width)) {
				break;
			}

			i++;
			x += hStep;
		}

		pathBuffer.lineToRelative(0F, thickness);

		while (true) {
			i--;

			if (i < 0) {
				break;
			}

			x -= hStep;

			float prev = getValueAtIndex(data, i + 1);
			float cur = getValueAtIndex(data, i);
			float next = getValueAtIndex(data, i - 1);

			sectionAngle = plot(pathBuffer, sectionAngle, prev, cur, next, x, -hStep, -halfThickness);
		}

		return pathBuffer;
	}

	private static double getSectionAngle(float prev, float cur, float next, int hStep) {
		double tmpPrevAngle = Math.atan2(prev - cur, hStep);
		double tmpNextAngle = Math.atan2(next - cur, hStep);

		return (-tmpPrevAngle + tmpNextAngle) / 2;
	}

	private static float getValueAtIndex(float[] data, int index) {
		int length = data.length;
		while (index < 0) {
			index += length;
		}

		while (index >= length) {
			index -= length;
		}

		return data[index];
	}

	private static double plot(Path pathBuffer, double previousAngle, float previous, float current, float next, int x,
			int hStep, float offset) {
		double tmpPrevAngle = Math.atan2(previous - current, -hStep);
		double tmpNextAngle = Math.atan2(next - current, hStep);
		double sectionAngle = (Math.PI + tmpNextAngle + tmpPrevAngle) / 2;

		while (sectionAngle > HALF_PI) {
			sectionAngle -= Math.PI;
		}

		while (sectionAngle < (-HALF_PI)) {
			sectionAngle += Math.PI;
		}

		float xPrev = (float) (x - hStep + offset * Math.cos(previousAngle + HALF_PI));
		float yPrev = (float) (previous + offset * Math.sin(previousAngle + HALF_PI));

		float xNew = (float) (x + offset * Math.cos(sectionAngle + HALF_PI));
		float yNew = (float) (current + offset * Math.sin(sectionAngle + HALF_PI));

		float cx1 = (float) (xPrev + hStep * Math.cos(previousAngle) / SMOOTH_FACTOR);
		float cy1 = (float) (yPrev + hStep * Math.sin(previousAngle) / SMOOTH_FACTOR);
		float cx2 = (float) (xNew - hStep * Math.cos(sectionAngle) / SMOOTH_FACTOR);
		float cy2 = (float) (yNew - hStep * Math.sin(sectionAngle) / SMOOTH_FACTOR);

		pathBuffer.cubicTo(cx1, -cy1, cx2, -cy2, xNew, -yNew);

		return sectionAngle;
	}

	/**
	 * Sets the data to plot.
	 *
	 * <p>
	 * This method updates the vector graphics' path to represent the specified data points.
	 *
	 * <p>
	 * This method should be called in the UI thread.
	 *
	 * @param data
	 *            the data to plot.
	 *
	 */
	public void setData(float[] data) {
		this.path = computePath(data, this.hStep, this.thickness);
	}

	/**
	 * Renders the content of the chart curve.
	 *
	 * @param g
	 *            Graphic context.
	 */
	public void render(GraphicsContext g) {
		g.setColor(this.color);
		VectorGraphicsPainter.fillPath(g, this.path, this.matrix);
	}

}
