/*
 * Java
 *
 * Copyright 2020-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.path.basic;

import com.microej.path.PathHelper;

import ej.drawing.ShapePainter.Cap;
import ej.microui.display.GraphicsContext;
import ej.microvg.LinearGradient;
import ej.microvg.Matrix;
import ej.microvg.Path;
import ej.microvg.VectorGraphicsPainter;

/**
 * A path that represents a circle arc.
 */
public class CircleArc {

	private Path path;
	private final LinearGradient gradient;
	private final int color;
	private final Matrix matrix;

	private float arcAngle;

	private final int diameter;

	private final int startAngle;

	private final int thickness;

	private final Cap cap;

	private int centerX;

	private int centerY;

	/**
	 * Creates a path that represents a circle arc.
	 *
	 * @param diameter
	 *            the diameter of the circular shape.
	 * @param thickness
	 *            the thickness of the shape.
	 * @param startAngle
	 *            the beginning angle of the arc, in degrees.
	 * @param color
	 *            the path color.
	 * @param cap
	 *            the cap to use for start and end of shape.
	 */
	/* package */ CircleArc(LinearGradient gradient, int diameter, int thickness, int startAngle, Cap cap) {
		this.gradient = gradient;
		this.color = 0;
		this.matrix = new Matrix();
		this.diameter = diameter;
		this.thickness = thickness;
		this.startAngle = startAngle;
		this.cap = cap;
		this.arcAngle = -1;
		this.path = getPath();
	}

	/**
	 * Creates a path that represents a circle arc.
	 *
	 * @param diameter
	 *            the diameter of the circular shape.
	 * @param thickness
	 *            the thickness of the shape.
	 * @param startAngle
	 *            the beginning angle of the arc, in degrees.
	 * @param color
	 *            the path color.
	 * @param cap
	 *            the cap to use for start and end of shape.
	 */
	/* package */ CircleArc(int color, int diameter, int thickness, int startAngle, Cap cap) {
		this.gradient = null;
		this.color = color;
		this.matrix = new Matrix();
		this.diameter = diameter;
		this.thickness = thickness;
		this.startAngle = startAngle;
		this.cap = cap;
		this.arcAngle = -1;
		this.path = getPath();
	}

	private Path getPath() {
		return PathHelper.computeThickShapeEllipseArc(this.diameter, this.diameter, this.thickness, this.startAngle,
				this.arcAngle, this.cap);
	}

	/**
	 * Sets the arc angle.
	 *
	 * <p>
	 * The arc starts at <code>startAngle</code> up to <code>arcAngle</code> degrees. <br>
	 * Angles are interpreted such that 0 degrees is at the 3 o'clock position. A positive <code>arcAngle</code> value
	 * indicates a counter-clockwise rotation whereas a negative value indicates a clockwise rotation.<br>
	 *
	 * @param arcAngle
	 *            the arc angle to set, in degrees.
	 */
	public void setArcAngle(float arcAngle) {
		this.arcAngle = arcAngle;
		this.path = getPath();
	}

	/**
	 * Gets the diameter.
	 *
	 * @return the diameter.
	 */
	protected int getDiameter() {
		return this.diameter;
	}

	/**
	 * Gets the thickness.
	 *
	 * @return the thickness.
	 */
	protected int getThickness() {
		return this.thickness;
	}

	/**
	 * Gets the start angle.
	 *
	 * @return the start angle.
	 */
	protected int getStartAngle() {
		return this.startAngle;
	}

	/**
	 * Sets the arc center point
	 *
	 * @param x
	 *            center point's X coordinate
	 * @param y
	 *            center point's Y coordinate
	 */
	protected void setCenter(int x, int y) {
		this.centerX = x;
		this.centerY = y;
		this.matrix.setTranslate(this.centerX, this.centerY);
	}

	/**
	 * Gets the arc center point's X coordinate
	 *
	 * @return the X coordinate
	 */
	protected int getCenterX() {
		return this.centerX;
	}

	/**
	 * Gets the arc center point's Y coordinate
	 *
	 * @return the Y coordinate
	 */
	protected int getCenterY() {
		return this.centerY;
	}

	/**
	 * Renders the content of a path that represents a circle arc.
	 *
	 * @param g
	 *            Graphic context.
	 */
	public void render(GraphicsContext g) {
		if (this.gradient != null) {
			VectorGraphicsPainter.fillGradientPath(g, this.path, this.matrix, this.gradient);
		} else {
			g.setColor(this.color);
			VectorGraphicsPainter.fillPath(g, this.path, this.matrix);
		}
	}

}
