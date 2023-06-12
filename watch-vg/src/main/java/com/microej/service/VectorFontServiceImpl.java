/*
 * Java
 *
 * Copyright 2021-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.service;

import com.microej.demo.nls.Messages;
import com.microej.demo.watch.util.services.VectorFontService;

import ej.basictool.ThreadUtils;
import ej.microvg.VectorFont;

/**
 * Implements the vector font service.
 */
public class VectorFontServiceImpl implements VectorFontService {

	private static final String FONT_ROOT_FOLDER = "/fonts/"; //$NON-NLS-1$
	private static final String FONT_EXTENSION = ".ttf"; //$NON-NLS-1$
	private static final String CHINESE_LOCALE_PREFIX = "zh"; //$NON-NLS-1$

	@Override
	public VectorFont getVectorFont(String occidentalFilename, String asianFilename) {
		String filename = getFilename(occidentalFilename, asianFilename);
		try {
			return VectorFont.loadFont(FONT_ROOT_FOLDER + filename + FONT_EXTENSION);
		} catch (Exception e) {
			ThreadUtils.handleUncaughtException(e);
			return null;
		}
	}

	private String getFilename(String occidentalFilename, String asianFilename) {
		String locale = Messages.NLS.getCurrentLocale();
		return (locale.startsWith(CHINESE_LOCALE_PREFIX) ? asianFilename : occidentalFilename);
	}
}
