/*
 * Java
 *
 * Copyright 2020-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.demo.watch.util.widget.basic;

import com.microej.demo.watch.util.font.VectorFontLoader;
import com.microej.demo.watch.util.style.text.OptimalVectorTextSizeComputer;
import com.microej.demo.watch.util.style.text.PlainVectorTextStyle;
import com.microej.demo.watch.util.style.text.VectorTextSizeComputer;
import com.microej.demo.watch.util.style.text.VectorTextStyle;

import ej.microui.display.GraphicsContext;
import ej.microvg.VectorFont;
import ej.mwt.Widget;
import ej.mwt.style.Style;
import ej.mwt.util.Size;

/**
 * A vector label with a text to display.
 */
public class VectorLabel extends Widget {

	/** The constant value for the default label text size. */
	public static final int DEFAULT_TEXT_SIZE = 20;

	/** The extra field ID for the font. */
	public static final int FONT_STYLE = 0;

	/** The extra field ID for the text size. */
	public static final int TEXT_SIZE_STYLE = 1;

	/** The extra field ID for the text style. */
	public static final int TEXT_STYLE = 2;

	/** The extra field ID for the size computer style. */
	public static final int SIZE_COMPUTER_STYLE = 3;

	private String text;

	/**
	 * Creates a label with an empty text.
	 */
	public VectorLabel() {
		this(""); //$NON-NLS-1$
	}

	/**
	 * Creates a label with the given text to display.
	 *
	 * @param text
	 *            the text to display.
	 */
	public VectorLabel(String text) {
		super();
		this.text = text;
	}

	/**
	 * Gets the text.
	 *
	 * @return the text.
	 */
	public String getText() {
		return this.text;
	}

	/**
	 * Sets the text to display for this label.
	 *
	 * @param text
	 *            the text to display, it cannot be <code>null</code>.
	 * @see #updateText(String)
	 */
	public void setText(String text) {
		updateText(text);
		requestRender();
	}

	/**
	 * Updates the text to display for this label without asking for a new render.
	 *
	 * @param text
	 *            the text to display, it cannot be <code>null</code>.
	 */
	public void updateText(String text) {
		assert text != null;
		this.text = text;
	}

	@Override
	protected void computeContentOptimalSize(Size size) {
		Style style = getStyle();
		VectorFont font = getFont(style);
		int fontHeight = getFontHeight(style);

		VectorTextSizeComputer textSizeComputer = style.getExtraObject(SIZE_COMPUTER_STYLE,
				VectorTextSizeComputer.class, OptimalVectorTextSizeComputer.OPTIMAL_SIZE_COMPUTER);
		textSizeComputer.computeSize(this.text, font, fontHeight, size);
	}

	private static VectorFont getFont(Style style) {
		VectorFontLoader fontLoader = style.getExtraObject(FONT_STYLE, VectorFontLoader.class,
				VectorFontLoader.DEFAULT_FONT_LOADER);
		return fontLoader.getFont();
	}

	private static VectorTextStyle getTextStyle(Style style) {
		return style.getExtraObject(TEXT_STYLE, VectorTextStyle.class, PlainVectorTextStyle.PLAIN_VECTOR_TEXT_STYLE);
	}

	private static int getFontHeight(Style style) {
		return style.getExtraInt(TEXT_SIZE_STYLE, DEFAULT_TEXT_SIZE);
	}

	@Override
	protected void renderContent(GraphicsContext g, int contentWidth, int contentHeight) {
		Style style = getStyle();
		VectorFont font = getFont(style);
		if (font != null) {
			g.setColor(style.getColor());
			int fontHeight = getFontHeight(style);
			VectorTextStyle textStyle = getTextStyle(style);
			textStyle.drawText(g, this.text, font, fontHeight, contentWidth, contentHeight,
					style.getHorizontalAlignment(), style.getVerticalAlignment());
		}
	}

}
