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
 * A vector graphics that represents the gradient under the curve of a line chart.
 *
 * <p>
 * The path is built using Bézier curve from the data array in input.
 *
 */
public class ChartGradient {

	private static final int SMOOTH_FACTOR = 3;

	private static final int TOP_PADDING = 5;

	private final int hStep;

	private Path path;

	private final Matrix matrix;

	private final LinearGradient gradient;

	/**
	 * Constructs the vector curve, given the data array to plot.
	 *
	 * @param height
	 *            the height of the the vector graphics' bounding-box.
	 * @param data
	 *            the data array which contains the values to plot.
	 * @param step
	 *            the horizontal spacing to use between each data point.
	 * @param gradientStyle
	 *            the gradient information.
	 */
	public ChartGradient(int height, float[] data, int step, GradientStyle gradientStyle) {
		this.path = computePath(data, step);
		this.matrix = new Matrix();
		this.matrix.setTranslate(0, height - TOP_PADDING);

		this.gradient = new LinearGradient(0, 0, height, 0, gradientStyle.getColors(), gradientStyle.getFloatStops());
		Matrix gradientMatrix = this.gradient.getMatrix();
		gradientMatrix.setTranslate(0, 0);
		gradientMatrix.preRotate(gradientStyle.getAngle());
		gradientMatrix.postTranslate(0, -(height - TOP_PADDING));

		this.hStep = step;
	}

	private static Path computePath(float[] data, int hStep) {
		Path pathBuffer = new Path();
		pathBuffer.moveTo(0, 0);

		int i = 0;
		int x = hStep;
		int width = hStep * data.length;

		float firstData = getValueAtIndex(data, i);
		double sectionAngle = getSectionAngle(getValueAtIndex(data, i - 1), firstData, getValueAtIndex(data, i + 1),
				hStep);
		i++;

		pathBuffer.lineTo(0, -firstData);

		while (true) {
			float prev = getValueAtIndex(data, i - 1);
			float cur = getValueAtIndex(data, i);
			float next = getValueAtIndex(data, i + 1);

			sectionAngle = plot(pathBuffer, sectionAngle, prev, cur, next, x, hStep);

			if (x >= (width)) {
				break;
			}

			i++;
			x += hStep;
		}

		pathBuffer.lineTo(x, 0);
		return pathBuffer;
	}

	private static double plot(Path pb, double prevAngle, float prev, float cur, float next, int x, int hStep) {
		double tmpPrevAngle = Math.atan2(prev - cur, hStep);
		double tmpNextAngle = Math.atan2(next - cur, hStep);
		double sectionAngle = (-tmpPrevAngle + tmpNextAngle) / 2;

		float cx1 = (float) (x - hStep + hStep * Math.cos(prevAngle) / SMOOTH_FACTOR);
		float cy1 = (float) (prev + hStep * Math.sin(prevAngle) / SMOOTH_FACTOR);
		float cx2 = (float) (x - hStep * Math.cos(sectionAngle) / SMOOTH_FACTOR);
		float cy2 = (float) (cur - hStep * Math.sin(sectionAngle) / SMOOTH_FACTOR);
		pb.cubicTo(cx1, -cy1, cx2, -cy2, x, -cur);

		return sectionAngle;
	}

	private static double getSectionAngle(float prev, float cur, float next, int hStep) {
		double tmpPrevAngle = Math.atan2(prev - cur, hStep);
		double tmpNextAngle = Math.atan2(next - cur, hStep);

		return (-tmpPrevAngle + tmpNextAngle) / 2;
	}

	private static float getValueAtIndex(float[] data, int index) {
		int arrayLength = data.length;
		while (index < 0) {
			index += arrayLength;
		}

		while (index >= arrayLength) {
			index -= arrayLength;
		}

		return data[index];
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
		this.path = computePath(data, this.hStep);
	}

	/**
	 * Renders the content of the chart gradient.
	 *
	 * @param g
	 *            Graphic context.
	 */
	public void render(GraphicsContext g) {
		VectorGraphicsPainter.fillGradientPath(g, this.path, this.matrix, this.gradient);
	}

}
