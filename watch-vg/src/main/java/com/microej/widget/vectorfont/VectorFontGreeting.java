/*
 * Java
 *
 * Copyright 2020-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.widget.vectorfont;

import com.microej.demo.watch.util.font.VectorFontLoader;

import ej.microui.display.GraphicsContext;
import ej.microvg.BlendMode;
import ej.microvg.Matrix;
import ej.microvg.VectorFont;
import ej.microvg.VectorGraphicsPainter;
import ej.mwt.style.Style;

/**
 * Creates a widget that shows a curved text.
 */
public class VectorFontGreeting extends VectorFontWidget {

	private static final int DEFAULT_OPACITY = 0xff;

	private static final int BIT_SHIFT = 5;

	/** The constant value to apply a simple rendering. */
	public static final int RENDERING_SIMPLE = 0;

	/** The constant value to apply an advanced rendering. */
	public static final int RENDERING_ADVANCED_1 = 1;

	/** The constant value to apply another advanced rendering. */
	public static final int RENDERING_ADVANCED_2 = 2;

	/** The constant value for the default text size. */
	public static final int DEFAULT_TEXT_SIZE = 75;

	/** The constant value for the default text rendering style. */
	public static final int DEFAULT_RENDERING = RENDERING_SIMPLE;

	/** The extra field ID for the font. */
	public static final int FONT_STYLE = 0;

	/** The extra field ID for the text size. */
	public static final int TEXT_SIZE_STYLE = 1;

	/** The extra field ID for the text size. */
	public static final int TEXT_RENDERING_STYLE = 2;

	/** The extra field ID for the text opacity style. */
	public static final int TEXT_OPACITY_STYLE = 3;

	private final char[] message;

	private VectorFont font;

	/**
	 * Creates a widget that shows curved text.
	 *
	 * @param text
	 *            the text to render.
	 * @param angle
	 *            the initial angle to apply.
	 */
	public VectorFontGreeting(String text, int angle) {
		super(angle);
		this.message = text.toCharArray();
	}

	@Override
	protected void renderContent(GraphicsContext g, int contentWidth, int contentHeight) {

		Style style = getStyle();
		VectorFont font = this.font;

		assert font != null;

		g.setColor(style.getColor());
		int fontHeight = getFontHeight(style);
		int kind = getRendering(style);
		int centerX = contentWidth / 2;
		int centerY = contentHeight / 2;
		int radius = Math.min(centerX, centerY);

		int angle = getAngle();
		float angleDegrees = angle;
		float dynamicTransformation = computeTransformation(angle, 5);
		int opacity = getOpacity(style);

		Matrix matrix = new Matrix();

		int i = 0;
		for (char character : this.message) {

			// reset transformations
			matrix.setTranslate(centerX, centerY);

			// set the angle of the glyph
			matrix.preRotate(angleDegrees);

			// move the glyph to the arc
			matrix.preTranslate(0, -radius);

			String string = Character.toString(character);
			float charWidth = font.measureStringWidth(string, fontHeight);

			switch (kind) {

			case RENDERING_ADVANCED_1:
				applyAdvancedTransform1(dynamicTransformation, matrix, i);
				i++;
				break;

			case RENDERING_ADVANCED_2:
				applysAdvancedTransform2(dynamicTransformation, matrix, i);
				i++;
				break;

			default:
			case RENDERING_SIMPLE:
				break;
			}

			// adjust baseline angle to the tangent
			float advanceAngle = (float) (2f * Math.toDegrees(Math.asin(charWidth / (2f * (radius - fontHeight)))));
			matrix.preRotate(advanceAngle / 2f);

			VectorGraphicsPainter.drawString(g, string, font, fontHeight, matrix, opacity, BlendMode.SRC_OVER, 0.0f);

			angleDegrees += advanceAngle;
		}
	}

	private void applysAdvancedTransform2(float dynamicTransformation, Matrix matrix, int i) {
		if (((i & 1) ^ ((getAngle() >> BIT_SHIFT) & 1)) != 0) {
			final float transformation = 1 + dynamicTransformation;
			matrix.preScale(transformation, transformation);
		} else {
			final float transformation = 1 / (1 + dynamicTransformation);
			matrix.preScale(transformation, transformation);
		}
	}

	private void applyAdvancedTransform1(float dynamicTransformation, Matrix matrix, int i) {
		if (((i & 1) ^ ((getAngle() >> BIT_SHIFT) & 1)) != 0) {
			final float xTransformation = 1 + dynamicTransformation;
			final float yTransformation = 1 / (1 + dynamicTransformation);
			matrix.preScale(xTransformation, yTransformation);
		} else {
			final float xTransformation = 1 / (1 + dynamicTransformation);
			final float yTransformation = 1 + dynamicTransformation;
			matrix.preScale(xTransformation, yTransformation);
		}
	}

	private static VectorFont getFont(Style style) {
		VectorFontLoader fontLoader = style.getExtraObject(FONT_STYLE, VectorFontLoader.class,
				VectorFontLoader.DEFAULT_FONT_LOADER);
		return fontLoader.getFont();
	}

	private static int getFontHeight(Style style) {
		return style.getExtraInt(TEXT_SIZE_STYLE, DEFAULT_TEXT_SIZE);
	}

	private static int getRendering(Style style) {
		return style.getExtraInt(TEXT_RENDERING_STYLE, DEFAULT_RENDERING);
	}

	private static int getOpacity(Style style) {
		return style.getExtraInt(TEXT_OPACITY_STYLE, DEFAULT_OPACITY);
	}

	private void loadFont() {
		this.font = getFont(getStyle());
	}

	@Override
	protected void onLaidOut() {
		super.onLaidOut();
		loadFont();
	}
}
