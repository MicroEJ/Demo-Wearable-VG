/*
 * Java
 *
 * Copyright 2020-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.demo.watch.util.path;

import ej.annotation.NonNull;
import ej.annotation.Nullable;
import ej.bon.IllegalStateException;
import ej.microui.display.GraphicsContext;
import ej.microvg.Matrix;
import ej.microvg.VectorGraphicsPainter;
import ej.microvg.VectorImage;

/**
 * A vector graphics holds a {@link Matrix} and a {@link VectorImage}. The image is composed of one or more paths.
 *
 * <p>
 * The vector graphics render the image using the matrix.
 */
public abstract class VectorGraphics {

	private final VectorImage image;

	private final Matrix pathMatrix;

	/**
	 * Creates a new vector graphics.
	 *
	 * <p>
	 * This method creates a new matrix that client code can use to apply transformations on this vector graphics.
	 *
	 *
	 * @param image
	 *            the SVG vector image that compose this vector graphics.
	 */
	public VectorGraphics(@NonNull VectorImage image) {
		this.pathMatrix = new Matrix();
		this.image = image;
	}

	/**
	 * Updates the path matrices.
	 *
	 * <p>
	 * Call this method when the transformation parameters has changed (i.e scale factor, rotation angle) and you want
	 * the vector graphics to reflect that change.
	 *
	 * <p>
	 * This method should be called in the UI thread to avoid concurrency issues.
	 */
	protected void updateMatrices() {
		// does nothing by default, subclasses may override this behavior to transform the vector graphics.
	}

	/**
	 * Renders the vector graphics on the given {@link GraphicsContext}.
	 *
	 * <p>
	 * The default implementation renders the SVG image applying the matrix on it.
	 *
	 * @param g
	 *            the graphics context where to render the content of the widget.
	 */
	public void render(GraphicsContext g) {
		VectorGraphicsPainter.drawImage(g, getImage(), getMatrix());
	}

	/**
	 * Gets the SVG image.
	 *
	 * @return the image.
	 */
	@NonNull
	protected VectorImage getImage() {
		return this.image;
	}

	/**
	 * Gets the matrix.
	 *
	 * <p>
	 * Use the matrix returned by this method to apply transformations to your vector graphics.
	 *
	 * @return the matrix.
	 * @throws IllegalStateException
	 *             if this vector graphics has been closed.
	 */
	@Nullable
	protected Matrix getMatrix() throws IllegalStateException {
		Matrix matrix = this.pathMatrix;
		if (matrix == null) {
			throw new IllegalStateException();
		}

		return matrix;
	}
}
