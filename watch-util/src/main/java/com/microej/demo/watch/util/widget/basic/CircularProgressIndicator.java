/*
 * Java
 *
 * Copyright 2019-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.demo.watch.util.widget.basic;

import com.microej.demo.watch.util.widget.basic.model.DefaultBoundedRangeModel;

import ej.drawing.ShapePainter;
import ej.drawing.ShapePainter.Cap;
import ej.microui.display.GraphicsContext;
import ej.mwt.style.Style;
import ej.mwt.util.Alignment;
import ej.mwt.util.Size;

/**
 * Circular representation of a progress bar.
 */
public class CircularProgressIndicator extends BoundedRange {

	private static final int START_ANGLE = 90;
	private static final int FULL_ANGLE = 360;

	private final int thickness;
	private final boolean roundCaps;
	private final int accentColor;
	private final boolean background;

	/**
	 * Creates a circular progress bar with a default bounded range as model.
	 *
	 * @param min
	 *            the minimum value of the progress bar.
	 * @param max
	 *            the maximum value of the progress bar.
	 * @param initialValue
	 *            the initial value of the progress bar.
	 * @param thickness
	 *            the thickness of the progress bar.
	 * @param roundCaps
	 *            whether the progress bar has round starting and ending caps.
	 * @param accentColor
	 *            the color of the progress bar.
	 * @param background
	 *            the boolean if the draw of a background is needed or not.
	 */
	public CircularProgressIndicator(int min, int max, int initialValue, int accentColor, int thickness,
			boolean roundCaps, boolean background) {
		super(new DefaultBoundedRangeModel(min, max, initialValue));
		this.thickness = thickness;
		this.roundCaps = roundCaps;
		this.accentColor = accentColor;
		this.background = background;
	}

	@Override
	protected void renderContent(GraphicsContext g, int contentWidth, int contentHeight) {
		int diameter = Math.min(contentWidth, contentHeight);
		Style style = getStyle();
		int shiftX = Alignment.computeLeftX(diameter, 0, contentWidth, style.getHorizontalAlignment());
		int shiftY = Alignment.computeTopY(diameter, 0, contentHeight, style.getVerticalAlignment());

		// Fills the complete part, from 90° anti-clockwise.
		Cap cap = this.roundCaps ? Cap.ROUNDED : Cap.PERPENDICULAR;
		int thickness = this.thickness;
		int startAngle;
		int arcAngle;

		arcAngle = (int) (-FULL_ANGLE * getPercentComplete());
		startAngle = START_ANGLE;

		int finalDiameter = diameter - thickness - 2;
		if ((finalDiameter & 0x1) == 0x0) {
			finalDiameter -= 1;
		}

		int x = shiftX + (thickness / 2) + 1;
		int y = shiftY + (thickness / 2) + 1;
		if (this.background) {
			g.setColor(style.getColor());
			ShapePainter.drawThickFadedCircle(g, x, y, finalDiameter, thickness, 1);
		}

		g.setColor(this.accentColor);
		ShapePainter.drawThickFadedCircleArc(g, x, y, finalDiameter, startAngle, arcAngle, thickness, 1, cap, cap);
	}

	@Override
	protected void computeContentOptimalSize(Size size) {
		// nothing to do: take available size
	}
}
