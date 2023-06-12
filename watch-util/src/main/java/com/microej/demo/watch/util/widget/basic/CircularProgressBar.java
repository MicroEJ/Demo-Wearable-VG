/*
 * Java
 *
 * Copyright 2019-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.demo.watch.util.widget.basic;

import com.microej.demo.watch.util.font.VectorFontLoader;
import com.microej.demo.watch.util.style.text.PlainVectorTextStyle;
import com.microej.demo.watch.util.style.text.VectorTextStyle;

import ej.drawing.ShapePainter;
import ej.drawing.ShapePainter.Cap;
import ej.microui.display.GraphicsContext;
import ej.microvg.VectorFont;
import ej.mwt.Widget;
import ej.mwt.style.Style;
import ej.mwt.util.Alignment;
import ej.mwt.util.Size;

/**
 * Circular representation of a progress bar.
 */
public class CircularProgressBar extends Widget {

	/** The extra field ID for the fill color. */
	public static final int FILL_COLOR = 0;

	/** The extra field ID for the font loader. */
	public static final int FONT_LOADER = 1;

	/** The extra field ID for the text size. */
	public static final int TEXT_SIZE = 2;

	private static final int DEFAULT_TEXT_SIZE = 20;

	private static final int THICKNESS = 4;
	private static final int START_ANGLE = 180;
	private static final int ARC_ANGLE = 180;
	private static final VectorTextStyle TEXT_STYLE = PlainVectorTextStyle.PLAIN_VECTOR_TEXT_STYLE;

	private final int minValue;
	private final int maxValue;
	private final String unit;

	private int currentValue;

	/**
	 * Creates a circular progress bar.
	 *
	 * @param minValue
	 *            the minimum value of the progress bar.
	 * @param maxValue
	 *            the maximum value of the progress bar.
	 * @param initialValue
	 *            the initial value of the progress bar.
	 * @param unit
	 *            the unit of the value.
	 */
	public CircularProgressBar(int minValue, int maxValue, int initialValue, String unit) {
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.currentValue = initialValue;
		this.unit = unit;
	}

	/**
	 * Changes the current value of the progress bar.
	 *
	 * @param value
	 *            the current value of the progress bar.
	 */
	public void setCurrentValue(int value) {
		this.currentValue = value;
	}

	@Override
	protected void renderContent(GraphicsContext g, int contentWidth, int contentHeight) {
		Style style = getStyle();
		int horizontalAlignment = style.getHorizontalAlignment();
		int verticalAlignment = style.getVerticalAlignment();

		int diameter = Math.min(contentWidth, contentHeight);
		int shiftX = Alignment.computeLeftX(diameter, 0, contentWidth, horizontalAlignment);
		int shiftY = Alignment.computeTopY(diameter, 0, contentHeight, verticalAlignment);

		Cap cap = Cap.PERPENDICULAR;
		int arcAngle = ARC_ANGLE * (this.currentValue - this.minValue) / (this.maxValue - this.minValue);

		int finalDiameter = diameter - THICKNESS - 2;
		if ((finalDiameter & 0x1) == 0x0) {
			finalDiameter -= 1;
		}

		int x = shiftX + (THICKNESS / 2) + 1;
		int y = shiftY + (THICKNESS / 2) + 1;

		g.setColor(style.getColor());
		ShapePainter.drawThickFadedCircleArc(g, x, y, finalDiameter, START_ANGLE, ARC_ANGLE, THICKNESS, 1, cap, cap);

		g.setColor(getFillColor(style));
		ShapePainter.drawThickFadedCircleArc(g, x, y, finalDiameter, START_ANGLE, arcAngle, THICKNESS, 1, cap, cap);

		VectorFont font = getFont(style);
		if (font != null) {
			String text = Integer.toString(this.currentValue) + this.unit;
			int fontHeight = getFontHeight(style);
			g.translate(0, contentHeight / 2);
			TEXT_STYLE.drawText(g, text, font, fontHeight, contentWidth, contentHeight / 2, horizontalAlignment,
					Alignment.TOP);
		}
	}

	@Override
	protected void computeContentOptimalSize(Size size) {
		// nothing to do: take available size
	}

	private static int getFillColor(Style style) {
		return style.getExtraInt(FILL_COLOR, style.getColor());
	}

	private static VectorFont getFont(Style style) {
		VectorFontLoader fontLoader = style.getExtraObject(FONT_LOADER, VectorFontLoader.class,
				VectorFontLoader.DEFAULT_FONT_LOADER);
		return fontLoader.getFont();
	}

	private static int getFontHeight(Style style) {
		return style.getExtraInt(TEXT_SIZE, DEFAULT_TEXT_SIZE);
	}
}
