/*
 * Java
 *
 * Copyright 2020-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.demo.watch.util.style.text;

import ej.microvg.VectorFont;
import ej.mwt.util.Size;

/**
 * A class that computes the optimal size of the bounding box for vector texts.
 *
 * <p>
 * The width is computed so that it matched the exact text width on screen for a given font.<br>
 * The height is the given font height.
 */
public class OptimalVectorTextSizeComputer implements VectorTextSizeComputer {

	/** A singleton instance of the size computer to avoid creating multiple ones. */
	public static final VectorTextSizeComputer OPTIMAL_SIZE_COMPUTER = new OptimalVectorTextSizeComputer();

	@Override
	public void computeSize(String text, VectorFont font, float fontHeight, Size size) {
		size.setWidth((int) font.measureStringWidth(text, fontHeight));
		size.setHeight((int) font.getHeight(fontHeight));
	}

}
