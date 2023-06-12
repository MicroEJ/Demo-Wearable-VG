/*
 * Java
 *
 * Copyright 2020-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.path.compass;

import com.microej.demo.watch.util.path.VectorGraphics;

import ej.microvg.Matrix;
import ej.microvg.VectorImage;

/**
 * A vector path that represents the compass direction arrow.
 */
public class CompassDirection extends VectorGraphics {

	private int centerX;

	/**
	 * Creates the compass direction arrow by describing its path.
	 */
	public CompassDirection() {
		super(VectorImage.getImage("/images/compass/compass_marker_orange.svg")); //$NON-NLS-1$
	}

	@Override
	protected void updateMatrices() {
		Matrix matrix = getMatrix();
		matrix.setTranslate(this.centerX - this.getImage().getWidth() / 2, 0);
	}

	/**
	 * Sets the compass center point coordinates.
	 *
	 * @param x
	 *            the compass center x-coordinate to set.
	 * @param y
	 *            the compass center y-coordinate to set.
	 *
	 */
	public void setCenter(int x, int y) {
		this.centerX = x;
		updateMatrices();
	}

}
