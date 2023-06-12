/*
 * Java
 *
 * Copyright 2020-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.page;

import com.microej.helper.MainListPositions;
import com.microej.style.ClassSelectors;
import com.microej.widget.vectorfont.VectorFontGreeting;
import com.microej.widget.vectorfont.VectorFontHexagons;

import ej.mwt.animation.Animation;

/**
 * A page that draws texts using vector fonts.
 */
public class VectorFontPage extends SimplePage {

	private static final String HELLO_ZH = "你们好"; //$NON-NLS-1$
	private static final String HELLO_FR = "Bonjour"; //$NON-NLS-1$
	private static final String HELLO_US = "Hello"; //$NON-NLS-1$
	private static final float FRENCH_SPEED_FACTOR = 0.8f;
	private static final float US_SPEED_FACTOR = 0.6f;
	private static final int ANGLE_TEXT1 = 0;
	private static final int ANGLE_TEXT2 = 90;
	private static final int ANGLE_TEXT3 = 220;

	private static final int ANGLE_INCREMENT = 1;

	private final VectorFontHexagons hexagons;
	private final VectorFontGreeting greetingUS;
	private final VectorFontGreeting greetingFR;
	private final VectorFontGreeting greetingCN;

	private Animation animation;
	private int angle;

	/**
	 * Creates the vector font page.
	 */
	public VectorFontPage() {
		super(MainListPositions.FREETYPE_TEXT);

		this.hexagons = new VectorFontHexagons(0);
		this.hexagons.addClassSelector(ClassSelectors.VECTORFONT_HEXAGONS);

		this.greetingUS = new VectorFontGreeting(HELLO_US, ANGLE_TEXT1);
		this.greetingFR = new VectorFontGreeting(HELLO_FR, ANGLE_TEXT2);
		this.greetingCN = new VectorFontGreeting(HELLO_ZH, ANGLE_TEXT3);

		this.greetingUS.addClassSelector(ClassSelectors.VECTORFONT_TEXT1_VALUE);
		this.greetingFR.addClassSelector(ClassSelectors.VECTORFONT_TEXT2_VALUE);
		this.greetingCN.addClassSelector(ClassSelectors.VECTORFONT_TEXT3_VALUE);

		addChild(this.greetingUS);
		addChild(this.greetingFR);
		addChild(this.greetingCN);
		addChild(this.hexagons);

		addClassSelector(ClassSelectors.VECTORFONT_BACKGROUND);
	}

	@Override
	protected void onShown() {
		super.onShown();
		Animation animation = new Animation() {

			@Override
			public boolean tick(long currentTimeMillis) {
				VectorFontPage.this.angle += ANGLE_INCREMENT;
				VectorFontPage.this.hexagons.updateAngle(VectorFontPage.this.angle);
				VectorFontPage.this.greetingUS
						.updateAngle((int) (VectorFontPage.this.angle * US_SPEED_FACTOR + ANGLE_TEXT1));
				VectorFontPage.this.greetingFR
						.updateAngle((int) (VectorFontPage.this.angle * FRENCH_SPEED_FACTOR + ANGLE_TEXT2));
				VectorFontPage.this.greetingCN.updateAngle(VectorFontPage.this.angle + ANGLE_TEXT3);
				requestRender();
				return true;
			}
		};
		this.animation = animation;
		getDesktop().getAnimator().startAnimation(animation);
	}

	@Override
	protected void onHidden() {
		getDesktop().getAnimator().stopAnimation(this.animation);
		super.onHidden();
	}

}
