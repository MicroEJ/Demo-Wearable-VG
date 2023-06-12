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
 * A type that is responsible for computing the size of text content with a vector font.
 */
public interface VectorTextSizeComputer {

	/**
	 * Computes the size of a text.
	 *
	 * <p>
	 * The given size is modified to set the size.
	 *
	 * @param text
	 *            the text to compute the size of.
	 * @param font
	 *            the font to use.
	 * @param fontHeight
	 *            the font height.
	 * @param size
	 *            the size available for the text.
	 */
	void computeSize(String text, VectorFont font, float fontHeight, Size size);

}
