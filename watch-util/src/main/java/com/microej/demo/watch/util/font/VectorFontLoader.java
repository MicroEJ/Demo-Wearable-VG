/*
 * Java
 *
 * Copyright 2020-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.demo.watch.util.font;

import com.microej.demo.watch.util.KernelServiceRegistry;
import com.microej.demo.watch.util.services.VectorFontService;

import ej.microvg.VectorFont;

/**
 * Returns the TrueType font that matches a filename.
 * <p>
 * Upon creation, the font loader can take two fonts: one used for occidental locales and one for asian locales.
 */
public class VectorFontLoader {

	/** The default font of the application. */
	public static final VectorFontLoader DEFAULT_FONT_LOADER = new VectorFontLoader("OpenSans-Regular"); //$NON-NLS-1$

	private final String occidentalFont;
	private final String asianFont;

	/**
	 * Creates a font loader given two fonts, for supporting occidental and asian locales.
	 *
	 * @param occidentalFont
	 *            the filename of the font that will be used for rendering occidental texts.
	 * @param asianFont
	 *            the filename of the font that will be used for rendering asian texts.
	 */
	public VectorFontLoader(String occidentalFont, String asianFont) {
		this.occidentalFont = occidentalFont;
		this.asianFont = asianFont;
	}

	/**
	 * Creates a font loader given the unique font to use for all texts.
	 *
	 * @param font
	 *            the filename of the font that will be used for rendering all texts.
	 */
	public VectorFontLoader(String font) {
		this(font, font);
	}

	/**
	 * Gets the TrueType font that matches with the current locale.
	 *
	 * @return the font for the current locale or <code>null</code> if not found.
	 */
	public VectorFont getFont() {
		VectorFontService vectorFontService = KernelServiceRegistry.getServiceLoader()
				.getService(VectorFontService.class);
		return vectorFontService.getVectorFont(this.occidentalFont, this.asianFont);
	}
}
