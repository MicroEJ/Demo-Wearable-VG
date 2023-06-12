/*
 * Java
 *
 * Copyright 2020-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.demo.watch.util.style.text;

import java.io.Closeable;

import ej.microui.display.GraphicsContext;
import ej.microvg.VectorFont;

/**
 * A type that is responsible for rendering text content with a vector font.
 */
public interface VectorTextStyle extends Closeable {

	/**
	 * Draws a text on a graphics context using the given vector font.
	 *
	 * @param g
	 *            the graphics context to draw the text on.
	 * @param text
	 *            the text to draw.
	 * @param font
	 *            the {@link VectorFont} to use to render the text.
	 * @param fontHeight
	 *            the font height.
	 * @param contentWidth
	 *            the width of the content area.
	 * @param contentHeight
	 *            the height of the content area.
	 * @param horizontalAlignment
	 *            the horizontal alignment of the text within the area.
	 * @param verticalAlignment
	 *            the vertical alignment of the text within the area.
	 */
	void drawText(GraphicsContext g, String text, VectorFont font, int fontHeight, int contentWidth, int contentHeight,
			int horizontalAlignment, int verticalAlignment);

}
