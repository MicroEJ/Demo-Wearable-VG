/*
 * Java
 *
 * Copyright 2021-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.demo.watch.util.path;

import ej.microui.display.GraphicsContext;
import ej.microvg.Matrix;
import ej.microvg.Path;
import ej.microvg.VectorGraphicsPainter;

/**
 * A vector graphics that represents a filled circle.
 */
public class FilledCircle implements AutoCloseable {

	private static final float TANGENT = 0.5522847498307933F; // Magic number to approximate circle arcs from Bezier

	private static final Path PATH = computePath();

	private final Matrix matrix;
	private final float radius;

	/**
	 * Creates a filled circle.
	 *
	 * @param radius
	 *            the radius of the circle.
	 */
	public FilledCircle(float radius) {
		this.matrix = new Matrix();
		this.radius = radius;
	}

	@Override
	public void close() {
		// Not implemented.
	}

	/**
	 * Renders this filled circle.
	 *
	 * @param g
	 *            the graphics context to use.
	 * @param x
	 *            the x coordinate of the center of the circle.
	 * @param y
	 *            the y coordinate of the center of the circle.
	 */
	public void render(GraphicsContext g, float x, float y) {
		// Create matrix
		Matrix matrix = this.matrix;
		matrix.setTranslate(x, y);
		matrix.preScale(this.radius, this.radius);

		// Draw path
		VectorGraphicsPainter.fillPath(g, PATH, matrix);
	}

	private static Path computePath() {
		// Create path: approximation of a circle using Bezier cubic curves
		Path path = new Path();
		path.moveTo(0, -1.0f);
		path.cubicToRelative(TANGENT, 0, 1.0f, 1.0f - TANGENT, 1.0f, 1.0f);
		path.cubicToRelative(0, TANGENT, -1.0f + TANGENT, 1.0f, -1.0f, 1.0f);
		path.cubicToRelative(-TANGENT, 0, -1.0f, -1.0f + TANGENT, -1.0f, -1.0f);
		path.cubicToRelative(0, -TANGENT, 1.0f - TANGENT, -1.0f, 1.0f, -1.0f);
		return path;
	}
}
