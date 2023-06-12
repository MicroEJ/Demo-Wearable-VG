/*
 * Java
 *
 * Copyright 2020-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.path.basic;

import com.microej.demo.watch.util.path.GradientStyle;

import ej.drawing.ShapePainter.Cap;
import ej.microui.display.Colors;
import ej.microvg.LinearGradient;
import ej.mwt.util.Size;

/**
 * A builder that creates vector circle arc, given the arc description.
 */
public class CircleArcBuilder {

	private static final int DEFAULT_THICKNESS = 1;

	private static final int DEFAULT_START_ANGLE = 0;

	private static final int DEFAULT_DIAMETER = 100;

	private static final double RIGHT_ANGLE = 90.0;

	private int centerX;

	private int centerY;

	private int diameter;

	private int startAngle;

	private int color;

	private int thickness;

	private Cap cap;

	private GradientStyle gradientStyle;

	private boolean fixedGradient;

	/**
	 * Creates the builder.
	 */
	public CircleArcBuilder() {
		this.thickness = DEFAULT_THICKNESS;
		this.diameter = DEFAULT_DIAMETER;
		this.startAngle = DEFAULT_START_ANGLE;
		this.color = Colors.BLACK;
		this.gradientStyle = null;
		this.cap = Cap.ROUNDED;
		this.fixedGradient = true;
	}

	/**
	 * Sets the color.
	 *
	 * @param color
	 *            the color to set.
	 */
	public void setColor(int color) {
		this.color = color;
	}

	/**
	 * Sets the start angle.
	 *
	 * <p>
	 * The arc is drawn from the given <code>startAngle</code>. <br>
	 * Angles are interpreted such that 0 degrees is at the 3 o'clock position.
	 *
	 * @param startAngle
	 *            the start angle to set.
	 */
	public void setStartAngle(int startAngle) {
		this.startAngle = startAngle;
	}

	/**
	 * Sets the thickness.
	 *
	 * @param thickness
	 *            the thickness to set.
	 */
	public void setThickness(int thickness) {
		this.thickness = thickness;
	}

	/**
	 * Sets the gradient.
	 *
	 * @param gradient
	 *            the gradient to set.
	 */
	public void setGradient(GradientStyle gradient) {
		this.gradientStyle = gradient;
	}

	/**
	 * Sets the cap for the start and end of the circle arc.
	 *
	 * @param cap
	 *            the cap to set.
	 */
	public void setCap(Cap cap) {
		this.cap = cap;
	}

	/**
	 * Builds a new instance of vector circle arc using the arguments given to the builder through setter methods.
	 *
	 * @return a new instance of vector circle arc.
	 */
	public CircleArc build() {
		GradientStyle gradientStyle = this.gradientStyle;
		CircleArc arc;
		if (gradientStyle != null) {
			int gradientAngle = this.fixedGradient ? gradientStyle.getAngle() : this.startAngle;
			double radians = Math.toRadians(gradientAngle - RIGHT_ANGLE);
			float gradientX = (float) (Math.cos(radians) * (this.diameter / 2.0));
			float gradientY = (float) (Math.sin(radians) * (this.diameter / 2.0));

			LinearGradient linearGradient = new LinearGradient(gradientX, gradientY, -gradientX, -gradientY,
					gradientStyle.getColors(), gradientStyle.getFloatStops());
			arc = new CircleArc(linearGradient, this.diameter, this.thickness, this.startAngle, this.cap);
		} else {
			arc = new CircleArc(this.color, this.diameter, this.thickness, this.startAngle, this.cap);
		}
		arc.setCenter(this.centerX, this.centerY);
		return arc;
	}

	/**
	 * Sets the size of the circle arc, including the thickness.
	 *
	 * @param size
	 *            the size of the circle arc.
	 */
	public void setSize(Size size) {
		this.diameter = Math.min(size.getWidth(), size.getHeight()) - this.thickness - 1;
	}

	/**
	 * Sets whether the arc gradient is fixed or not.
	 *
	 * <p>
	 * When set to <code>false</code>, the gradient angle has the same angle than the arc.
	 *
	 * <p>
	 * If the circle arc has no gradient information, calling this method has no effect.
	 *
	 * @param fixed
	 *            <code>true</code> to set a fixed gradient, <code>false</code> otherwise.
	 */
	public void setFixedGradient(boolean fixed) {
		this.fixedGradient = fixed;
	}

	/**
	 * Sets the arc center point
	 *
	 * @param x
	 *            center point's X coordinate
	 * @param y
	 *            center point's Y coordinate
	 */
	public void setCenter(int x, int y) {
		this.centerX = x;
		this.centerY = y;
	}
}
