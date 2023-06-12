/*
 * Java
 *
 * Copyright 2021-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.demo.watch.util.services;

import ej.microvg.VectorFont;

/**
 * The vector font service allows to get vector fonts embedded in the kernel.
 */
public interface VectorFontService {

	/**
	 * Returns the desired occidental or asian font, depending on the current locale.
	 *
	 * @param occidentalFont
	 *            the filename of the occidental font.
	 * @param asianFont
	 *            the filename of the asian font.
	 * @return the vector font.
	 */
	VectorFont getVectorFont(String occidentalFont, String asianFont);
}
