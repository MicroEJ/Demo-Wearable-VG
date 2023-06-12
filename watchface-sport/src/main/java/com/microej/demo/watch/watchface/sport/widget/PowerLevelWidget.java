/*
 * Java
 *
 * Copyright 2021-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.demo.watch.watchface.sport.widget;

import com.microej.demo.watch.util.path.FilledCircle;

import ej.annotation.Nullable;
import ej.bon.Util;
import ej.microui.display.GraphicsContext;
import ej.mwt.Widget;
import ej.mwt.animation.Animation;
import ej.mwt.util.Size;
import ej.widget.util.color.GradientHelper;

/**
 * Shows the percentage of power remaining on the device.
 */
public class PowerLevelWidget extends Widget {

	private static final int MAX_LEVEL = 15;
	private static final float PERCENTAGE_CONVERTER = 100.0f;

	private static final float CENTER_X = 46.5f;
	private static final float CENTER_Y = 22.5f;
	private static final float DISTANCE = 36.0f;
	private static final float RADIUS = 3.0f;

	private static final int BACK_COLOR = 0x202020;
	private static final int MIN_COLOR = 0xFF4060;
	private static final int MID_COLOR = 0xFFFF00;
	private static final int MAX_COLOR = 0x40FF60;

	private static final long ANIM_DURATION = 500;

	private static final int MIN_ANIM_FADE_RATIO = 0;
	private static final int MAX_ANIM_FADE_RATIO = 255;

	private static final float START_ANGLE = 150.0f;
	private static final float ARC_ANGLE = 240.0f;
	private static final float FLAT_ANGLE = 180.0f;

	private @Nullable FilledCircle filledCircle;
	private int level;
	private @Nullable Animation animation;
	private float animFadeRatio;
	private boolean animFadeIn;

	/**
	 * Creates a power level widget.
	 */
	public PowerLevelWidget() {
		this.level = 0;
	}

	/**
	 * Sets the current level of this widget.
	 *
	 * @param level
	 *            the level (in percents).
	 */
	public void setLevel(int level) {
		level = Math.round(level * MAX_LEVEL / PERCENTAGE_CONVERTER);
		if (level == this.level) {
			return;
		}

		stopAnimation();

		if (isShown()) {
			final long animStartTime = Util.platformTimeMillis();
			this.animFadeIn = (level > this.level);
			this.animFadeRatio = (this.animFadeIn ? MIN_ANIM_FADE_RATIO : MAX_ANIM_FADE_RATIO);

			this.animation = new Animation() {
				@Override
				public boolean tick(long currentTimeMillis) {
					long elapsedTime = currentTimeMillis - animStartTime;
					float fadeRatio = Math.min((float) elapsedTime / ANIM_DURATION, 1.0f);
					PowerLevelWidget.this.animFadeRatio = (PowerLevelWidget.this.animFadeIn ? fadeRatio : 1.0f - fadeRatio);
					return (elapsedTime < ANIM_DURATION);
				}
			};
			getDesktop().getAnimator().startAnimation(this.animation);
		} else {
			this.animFadeIn = true;
			this.animFadeRatio = 1.0f;
		}

		this.level = level;
	}

	@Override
	protected void onAttached() {
		super.onAttached();

		this.filledCircle = new FilledCircle(RADIUS);
	}

	@Override
	protected void onDetached() {
		super.onDetached();

		this.filledCircle.close();
	}

	@Override
	protected void onHidden() {
		super.onHidden();

		stopAnimation();
	}

	private void stopAnimation() {
		if (this.animation != null) {
			getDesktop().getAnimator().stopAnimation(this.animation);
			this.animation = null;
		}
	}

	@Override
	protected void computeContentOptimalSize(Size size) {
		float length = (DISTANCE + RADIUS) * 2.0f;
		int lengthCeil = (int) Math.ceil(length);
		size.setSize(lengthCeil, lengthCeil);
	}

	@Override
	protected void renderContent(GraphicsContext g, int contentWidth, int contentHeight) {
		int animLevel = this.level - (this.animFadeIn ? 1 : 0);

		for (int i = 0; i <= animLevel; i++) {
			int color = getStepColor(i);
			if (i == animLevel) {
				color = getFadedColor(color, this.animFadeRatio);
			}

			float angleDeg = START_ANGLE + i * ARC_ANGLE / (MAX_LEVEL - 1);
			float angleRad = (float) (angleDeg * Math.PI / FLAT_ANGLE);
			float cosAngle = (float) Math.cos(angleRad);
			float sinAngle = (float) Math.sin(angleRad);

			float x = CENTER_X + cosAngle * DISTANCE;
			float y = CENTER_Y - sinAngle * DISTANCE;

			g.setColor(color);
			this.filledCircle.render(g, x, y);
		}
	}

	private static int getStepColor(int level) {
		int midLevel = MAX_LEVEL / 2;
		if (level > midLevel) {
			return GradientHelper.blendColors(MID_COLOR, MAX_COLOR, (float) (level - midLevel) / midLevel);
		} else {
			return GradientHelper.blendColors(MIN_COLOR, MID_COLOR, (float) level / midLevel);
		}
	}

	private static int getFadedColor(int color, float fadeRatio) {
		return GradientHelper.blendColors(BACK_COLOR, color, fadeRatio);
	}
}
