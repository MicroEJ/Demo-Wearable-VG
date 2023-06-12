/*
 * Java
 *
 * Copyright 2019-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.widget.carousel;

import com.microej.demo.watch.util.WatchImageLoader;
import com.microej.demo.watch.util.font.VectorFontLoader;
import com.microej.style.ClassSelectors;

import ej.drawing.TransformPainter;
import ej.microui.display.GraphicsContext;
import ej.microui.display.ResourceImage;
import ej.microvg.BlendMode;
import ej.microvg.Matrix;
import ej.microvg.VectorFont;
import ej.microvg.VectorGraphicsPainter;
import ej.mwt.Widget;
import ej.mwt.style.Style;
import ej.mwt.util.Alignment;
import ej.mwt.util.Size;
import ej.widget.basic.OnClickListener;
import ej.widget.util.ButtonHelper;
import ej.widget.util.GenericListener;
import ej.widget.util.color.GradientHelper;

/**
 * Represents a item of the watch application list.
 */
public class ApplicationListItem extends Widget implements GenericListener {

	private static final int DEFAULT_TEXT_SIZE = 40;
	private static final float DEFAULT_SPLIT_FACTOR = 0.5f;
	private static final int CHANGE_COLOR_THRESHOLD = 10;
	private static final float CHANGE_COLOR_THRESHOLD_RATIO = 0.9f;
	private static final int IMAGE_TEXT_SPACING = 20;

	/** The extra field ID for the font. */
	public static final int FONT_STYLE = 0;

	/** The extra field ID for the text size. */
	public static final int TEXT_SIZE_STYLE = 1;

	/** The extra field ID for the split factor. */
	public static final int SPLIT_FACTOR_STYLE = 2;

	/** The extra field ID for the spacing. */
	public static final int SPACING_STYLE = 3;

	/** The extra field ID for the selected color. */
	public static final int SELECTED_COLOR_STYLE = 4;

	private final String imagePath;
	private final String text;
	private final ButtonHelper buttonHelper;

	private ResourceImage image;

	private float percent;

	/**
	 * Creates a {@link ApplicationListItem} with the given icon and text.
	 *
	 * @param imagePath
	 *            the path to the icon resource.
	 * @param message
	 *            the NLS identifier of the text to display.
	 */
	public ApplicationListItem(String imagePath, String message) {
		this.imagePath = imagePath;
		this.text = message;

		addClassSelector(ClassSelectors.APPLIST_CONTAINER);
		setEnabled(true);
		this.buttonHelper = new ButtonHelper(this);
	}

	/**
	 * Adds a listener on the click events of the button.
	 *
	 * @param listener
	 *            the listener to add.
	 * @throws NullPointerException
	 *             if the given listener is <code>null</code>.
	 */
	public void addOnClickListener(OnClickListener listener) {
		this.buttonHelper.addOnClickListener(listener);
	}

	/**
	 * Sets the percent of the position of the item.
	 *
	 * @param percent
	 *            the percent to set.
	 */
	public void setPercent(float percent) {
		this.percent = percent;
	}

	@Override
	protected void onAttached() {
		super.onAttached();
		this.image = WatchImageLoader.loadImage(this.imagePath);
	}

	@Override
	protected void onDetached() {
		super.onDetached();
		this.image.close();
	}

	@Override
	protected void computeContentOptimalSize(Size size) {
		size.setSize(this.image.getWidth(), this.image.getHeight() + getSpacing(getStyle()));
		// TODO compute the size based on the text size (not available yet with FreeType implementation)
	}

	@Override
	protected void renderContent(GraphicsContext g, int contentWidth, int contentHeight) {
		float percent = this.percent;
		Style style = getStyle();

		float splitFactor = getSplitFactor(style);
		int imageAreaWidth = (int) (contentWidth * splitFactor);

		float reducedPercent = percent * percent;
		int imageAlpha = (int) (reducedPercent * GraphicsContext.OPAQUE);

		ResourceImage image = this.image;
		int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();
		int imageX = (int) ((1 - percent) * (imageAreaWidth - imageWidth) + (imageWidth - (imageWidth * percent)));
		int verticalAlignment = style.getVerticalAlignment();
		int imageY = Alignment.computeTopY((int) (imageHeight * percent), 0, contentHeight, verticalAlignment);
		TransformPainter.drawScaledImageBilinear(g, image, imageX, imageY, percent, percent, imageAlpha);

		int textAlpha = (int) (percent * GraphicsContext.OPAQUE);
		int color;
		int foregroundColor = style.getColor();
		if (percent < CHANGE_COLOR_THRESHOLD_RATIO) {
			color = foregroundColor;
		} else {
			int selectorColor = getSelectorColor(style);
			color = GradientHelper.blendColors(foregroundColor, selectorColor,
					(percent - CHANGE_COLOR_THRESHOLD_RATIO) * CHANGE_COLOR_THRESHOLD);
		}

		VectorFont font = getFont(style);
		if (font != null) {
			g.setColor(color);
			int fontHeight = getFontHeight(style);
			int textX = imageX + (int) (image.getWidth() * percent) + IMAGE_TEXT_SPACING;
			int textY = Alignment.computeTopY((int) Math.round((double) font.getHeight(fontHeight * percent)), 0,
					contentHeight, verticalAlignment);
			Matrix matrix = new Matrix();
			matrix.setTranslate(textX, textY);
			VectorGraphicsPainter.drawString(g, this.text, font, fontHeight * percent, matrix, textAlpha,
					BlendMode.SRC_OVER, 0);
		}
	}

	private VectorFont getFont(Style style) {
		VectorFontLoader fontLoader = style.getExtraObject(FONT_STYLE, VectorFontLoader.class,
				VectorFontLoader.DEFAULT_FONT_LOADER);
		return fontLoader.getFont();
	}

	private int getFontHeight(Style style) {
		return style.getExtraInt(TEXT_SIZE_STYLE, DEFAULT_TEXT_SIZE);
	}

	private float getSplitFactor(Style style) {
		return style.getExtraFloat(SPLIT_FACTOR_STYLE, DEFAULT_SPLIT_FACTOR);
	}

	private int getSpacing(Style style) {
		return style.getExtraInt(SPACING_STYLE, 0);
	}

	private int getSelectorColor(Style style) {
		return style.getExtraInt(SELECTED_COLOR_STYLE, style.getColor());
	}

	@Override
	public void update() {
		// Style does not change when pressed.
	}

	@Override
	public boolean handleEvent(int event) {
		return this.buttonHelper.handleEvent(event);
	}

}
