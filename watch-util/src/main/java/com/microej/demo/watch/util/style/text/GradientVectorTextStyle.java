/*
 * Java
 *
 * Copyright 2020-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.demo.watch.util.style.text;

import java.io.IOException;

import com.microej.demo.watch.util.path.GradientStyle;

import ej.microui.display.GraphicsContext;
import ej.microvg.BlendMode;
import ej.microvg.LinearGradient;
import ej.microvg.Matrix;
import ej.microvg.VectorFont;
import ej.microvg.VectorGraphicsPainter;
import ej.mwt.util.Alignment;

/**
 * Draws a TrueType text with a gradient.
 */
public class GradientVectorTextStyle implements VectorTextStyle {

	private final GradientStyle gradientStyle;

	private LinearGradient gradient;

	/**
	 * Creates a text style for vector drawing given the gradient information.
	 *
	 * <p>
	 * Gradient style cannot be <code>null</code>.
	 *
	 * @param gradientStyle
	 *            the gradient information.
	 */
	public GradientVectorTextStyle(GradientStyle gradientStyle) {
		this.gradientStyle = gradientStyle;
	}

	@Override
	public void drawText(GraphicsContext g, String text, VectorFont font, int fontHeight, int contentWidth,
			int contentHeight, int horizontalAlignment, int verticalAlignment) {
		int stringWidth = (int) font.measureStringWidth(text, fontHeight);
		int stringHeight = (int) font.getHeight(fontHeight);
		int x = Alignment.computeLeftX(stringWidth, 0, contentWidth, horizontalAlignment);
		int y = Alignment.computeTopY(stringHeight, 0, contentHeight, verticalAlignment);

		if (this.gradient == null) {
			GradientStyle grandientStyle = this.gradientStyle;
			int angle = grandientStyle.getAngle();
			int stopX = computeStopX(0, angle, stringWidth, fontHeight);
			int stopY = computeStopY(0, angle, stringWidth, fontHeight);

			this.gradient = new LinearGradient(0, 0, stopX, stopY, grandientStyle.getColors(),
					this.gradientStyle.getFloatStops());

		}

		Matrix matrix = new Matrix();
		matrix.setTranslate(x, y);
		VectorGraphicsPainter.drawGradientString(g, text, font, fontHeight, matrix, this.gradient,
				GraphicsContext.OPAQUE, BlendMode.SRC_OVER, 0);
	}

	private int computeStopX(int startX, float angle, int width, int height) {
		double alphaRadians = Math.atan((double) height / width);
		double angleRadians = Math.toRadians(angle);

		int x = startX;

		if (angleRadians < alphaRadians) {
			x += width;
		} else {
			double tan = Math.tan(angleRadians);
			if (!Double.isNaN(tan)) {
				x += height / tan;
			}
		}

		return x;
	}

	private int computeStopY(int startY, float angle, int width, int height) {
		double alphaRadians = Math.atan((double) height / width);
		double angleRadians = Math.toRadians(angle);

		int y = startY;

		if (angleRadians < alphaRadians) {
			y += (int) (Math.tan(angleRadians) * width);
		} else {
			y += height;
		}

		return y;
	}

	@Override
	public void close() throws IOException {
		// Not implemented.
	}

}
