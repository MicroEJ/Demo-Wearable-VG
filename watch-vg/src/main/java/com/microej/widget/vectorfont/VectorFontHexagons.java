/*
 * Java
 *
 * Copyright 2020-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.widget.vectorfont;

import com.microej.demo.watch.util.path.GradientStyle;
import com.microej.path.vectorfont.Hexagon;

import ej.microui.display.GraphicsContext;
import ej.mwt.style.Style;

/**
 * A widget that renders three hexagons.
 */
public class VectorFontHexagons extends VectorFontWidget {

	/** The extra field ID for the small hexagon style. */
	public static final int SMALL_HEXAGON_GRADIENT_STYLE = 0;

	/** The extra field ID for the medium hexagon style. */
	public static final int MEDIUM_HEXAGON_GRADIENT_STYLE = 1;

	/** The extra field ID for the big hexagon style. */
	public static final int BIG_HEXAGON_GRADIENT_STYLE = 2;

	private static final int BIG_HEXAGON_X_TRANSLATE = 0;

	private static final int MEDIUM_HEXAGON_Y_TRANSLATE = 300;

	private static final int BIG_HEXAGON_Y_FACTOR = 200;

	private static final float BIG_HEXAGON_SCALE = 3f;

	private static final float MEDIUM_HEXAGON_SCALE = 2f;

	private static final int MEDIUM_HEXAGON_X_FACTOR = 240;

	private static final float SMALL_HEXAGON_SCALE = 1.5f;

	private static final int SMALL_HEXAGON_Y_FACTOR = 150;

	private static final int SMALL_HEXAGON_X_FACTOR = 280;

	private static final float FACTOR = 0.2f;

	private static final int BIT_COUNT = 7;

	private Hexagon smallHexagon;

	private Hexagon bigHexagon;

	private Hexagon mediumHexagon;

	/**
	 * Creates the widget, initializing it with the specified angle.
	 *
	 * @param angle
	 *            the initial angle to apply.
	 */
	public VectorFontHexagons(int angle) {
		super(angle);
	}

	@Override
	protected void renderContent(GraphicsContext g, int contentWidth, int contentHeight) {
		this.smallHexagon.render(g);
		this.mediumHexagon.render(g);
		this.bigHexagon.render(g);
	}

	@Override
	public void updateAngle(int angle) {
		super.updateAngle(angle);
		updateTransformation();
	}

	private void updateTransformation() {
		if (this.smallHexagon != null) {
			// update the hexagons transformation accordingly
			float translate = computeTransformation(getAngle(), BIT_COUNT);
			translate = 1 + FACTOR * translate;

			this.smallHexagon.setTransformation(translate * SMALL_HEXAGON_X_FACTOR, translate * SMALL_HEXAGON_Y_FACTOR,
					SMALL_HEXAGON_SCALE);
			this.mediumHexagon.setTransformation(translate * MEDIUM_HEXAGON_X_FACTOR, MEDIUM_HEXAGON_Y_TRANSLATE,
					MEDIUM_HEXAGON_SCALE);
			this.bigHexagon.setTransformation(BIG_HEXAGON_X_TRANSLATE, translate * BIG_HEXAGON_Y_FACTOR,
					BIG_HEXAGON_SCALE);
		}

	}

	@Override
	protected void onLaidOut() {
		super.onLaidOut();
		createHexagons();
		updateTransformation();
	}

	@Override
	protected void onDetached() {
		super.onDetached();
		closeHexagons();
	}

	private void createHexagons() {
		closeHexagons();
		Style style = getStyle();

		GradientStyle smallGradientStyle = style.getExtraObject(SMALL_HEXAGON_GRADIENT_STYLE, GradientStyle.class,
				GradientStyle.DEFAULT_GRADIENT_STYLE);
		this.smallHexagon = new Hexagon(smallGradientStyle);

		GradientStyle mediumGradientStyle = style.getExtraObject(MEDIUM_HEXAGON_GRADIENT_STYLE, GradientStyle.class,
				GradientStyle.DEFAULT_GRADIENT_STYLE);
		this.mediumHexagon = new Hexagon(mediumGradientStyle);

		GradientStyle bigGradientStyle = style.getExtraObject(BIG_HEXAGON_GRADIENT_STYLE, GradientStyle.class,
				GradientStyle.DEFAULT_GRADIENT_STYLE);
		this.bigHexagon = new Hexagon(bigGradientStyle);

	}

	private void closeHexagons() {
		this.smallHexagon = null;
		this.mediumHexagon = null;
		this.bigHexagon = null;
	}
}
