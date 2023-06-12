/*
 * Java
 *
 * Copyright 2020-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.demo.watch.util.style.text;

import ej.annotation.NonNull;
import ej.microvg.VectorFont;
import ej.mwt.util.Size;

/**
 * A class that computes the size of the bounding box for vector texts based on the given pattern string.
 *
 * <p>
 * The width is computed so that it matched the exact pattern width on screen for a given font.<br>
 * The height is the given font height.
 */
public class PatternVectorTextSizeComputer implements VectorTextSizeComputer {

	private final String pattern;

	/**
	 * Creates the size computer for the given string pattern.
	 *
	 * @param pattern
	 *            the pattern to use for computing vector text size.
	 */
	public PatternVectorTextSizeComputer(@NonNull String pattern) {
		this.pattern = pattern;
	}

	@Override
	public void computeSize(String text, VectorFont font, float fontHeight, Size size) {
		size.setWidth((int) font.measureStringWidth(this.pattern, fontHeight));
		size.setHeight((int) font.getHeight(fontHeight));
	}

}
