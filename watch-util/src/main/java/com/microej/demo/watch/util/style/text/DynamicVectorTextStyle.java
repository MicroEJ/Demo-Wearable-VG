/*
 * Java
 *
 * Copyright 2020-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.demo.watch.util.style.text;

import java.io.IOException;

import com.microej.demo.watch.util.helper.ColorUtils;
import com.microej.demo.watch.util.path.GradientStyle;

import ej.basictool.ThreadUtils;
import ej.microui.display.GraphicsContext;
import ej.microvg.VectorFont;
import ej.microvg.VectorGraphicsPainter;
import ej.mwt.util.Alignment;

/**
 * Draws a vector text with color adapting to the represented value, following a given gradient. The text must represent
 * an integer value.
 *
 * <p>
 * If the text does not represent an integer, the current graphics context color is used instead.
 */
public class DynamicVectorTextStyle implements VectorTextStyle {

	private final GradientStyle gradientStyle;

	private final int maxValue;

	private final int minValue;

	/**
	 * Creates a text style that draws a text with a color picked in the specified gradient information and according to
	 * the text value.
	 *
	 * <p>
	 * Gradient style cannot be <code>null</code>.
	 *
	 * @param gradientStyle
	 *            the gradient information.
	 * @param minValue
	 *            the minimum value that the text can represent.
	 * @param maxValue
	 *            the maximum value that the text can represent.
	 */
	public DynamicVectorTextStyle(GradientStyle gradientStyle, int minValue, int maxValue) {
		this.gradientStyle = gradientStyle;
		this.maxValue = maxValue;
		this.minValue = minValue;
	}

	@Override
	public void drawText(GraphicsContext g, String text, VectorFont font, int fontHeight, int contentWidth,
			int contentHeight, int horizontalAlignment, int verticalAlignment) {
		int stringWidth = (int) font.measureStringWidth(text, fontHeight);
		int stringHeight = (int) font.getHeight(fontHeight);
		int x = Alignment.computeLeftX(stringWidth, 0, contentWidth, horizontalAlignment);
		int y = Alignment.computeTopY(stringHeight, 0, contentHeight, verticalAlignment);

		try {
			int color = computeTextColor(Integer.parseInt(text));
			g.setColor(color);
		} catch (NumberFormatException e) {
			// Print the stack trace and use current graphics context color.
			ThreadUtils.handleUncaughtException(e);
		}

		VectorGraphicsPainter.drawString(g, text, font, fontHeight, x, y);
	}

	@Override
	public void close() throws IOException {
		// Nothing to do.
	}

	private int computeTextColor(int value) {
		GradientStyle gradient = this.gradientStyle;
		int[] colors = gradient.getColors();
		float[] stops = gradient.getFloatStops();
		int length = colors.length;

		if (value <= this.minValue) {
			return colors[0];
		} else if (value >= this.maxValue) {
			return colors[length - 1];
		} else {
			float normalized = value * stops[length - 1] / this.maxValue;
			int index = getInsertionPoint(normalized, stops);
			float highStop = stops[index];
			float lowStop = stops[index - 1];
			float ratio = (normalized - lowStop) / (highStop - lowStop);

			return ColorUtils.blend(colors[index - 1], colors[index], ratio);
		}

	}

	private int getInsertionPoint(float value, float[] array) {
		int length = array.length;

		int index = 0;
		while (index < length && array[index] < value) {
			index++;
		}

		return index;
	}

}
