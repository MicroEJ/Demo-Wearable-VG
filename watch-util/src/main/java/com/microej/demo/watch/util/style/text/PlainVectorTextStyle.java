/*
 * Java
 *
 * Copyright 2020-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.demo.watch.util.style.text;

import java.io.IOException;

import ej.microui.display.GraphicsContext;
import ej.microvg.VectorFont;
import ej.microvg.VectorGraphicsPainter;
import ej.mwt.util.Alignment;

/**
 * Draws a TrueType text using the {@link GraphicsContext}'s color.
 */
public class PlainVectorTextStyle implements VectorTextStyle {

	/**
	 * Plain vector text style singleton to avoid creating several ones.
	 */
	public static final VectorTextStyle PLAIN_VECTOR_TEXT_STYLE = new PlainVectorTextStyle();

	@Override
	public void drawText(GraphicsContext g, String text, VectorFont font, int fontHeight, int contentWidth,
			int contentHeight, int horizontalAlignment, int verticalAlignment) {
		int stringWidth = (int) font.measureStringWidth(text, fontHeight);
		int stringHeight = (int) font.getHeight(fontHeight);
		int x = Alignment.computeLeftX(stringWidth, 0, contentWidth, horizontalAlignment);
		int y = Alignment.computeTopY(stringHeight, 0, contentHeight, verticalAlignment);
		VectorGraphicsPainter.drawString(g, text, font, fontHeight, x, y);
	}

	@Override
	public void close() throws IOException {
		// Nothing to do.
	}

}
