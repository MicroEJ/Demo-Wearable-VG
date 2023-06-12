/*
 * Java
 *
 * Copyright 2019-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.demo.watch.watchface.flower;

import com.microej.demo.watch.util.KernelServiceRegistry;
import com.microej.demo.watch.util.WatchImageLoader;
import com.microej.demo.watch.util.font.VectorFontLoader;
import com.microej.demo.watch.util.helper.TimeHelper;
import com.microej.demo.watch.util.path.GradientStyle;
import com.microej.demo.watch.util.path.watchface.Hand;
import com.microej.demo.watch.util.services.TimeService;
import com.microej.demo.watch.util.widget.watchface.VectorWatchface;
import com.microej.demo.watch.watchface.flower.path.FlowerHourHand;
import com.microej.demo.watch.watchface.flower.path.FlowerMinuteHand;
import com.microej.demo.watch.watchface.flower.path.FlowerSecondGradient;
import com.microej.demo.watch.watchface.flower.path.FlowerSecondHand;

import ej.basictool.ArrayTools;
import ej.bon.Util;
import ej.microui.MicroUI;
import ej.microui.display.GraphicsContext;
import ej.microui.display.Painter;
import ej.microui.display.ResourceImage;
import ej.microvg.Matrix;
import ej.microvg.VectorFont;
import ej.microvg.VectorGraphicsPainter;
import ej.microvg.VectorGraphicsPainter.Direction;
import ej.motion.Motion;
import ej.motion.cubic.CubicEaseInOutMotion;
import ej.mwt.animation.Animation;
import ej.mwt.style.Style;
import ej.mwt.util.Alignment;
import ej.observable.Observer;
import ej.service.ServiceLoader;

/**
 * The "Flower" watchface displays vector hands and a "radar effect" using vector gradient.
 */
public class FlowerWatchface extends VectorWatchface implements Observer {

	private static final char SPACE = ' ';

	/** The extra field ID for the font. */
	public static final int FONT_STYLE = 3;

	/** The extra field ID for the text size. */
	public static final int TEXT_SIZE_STYLE = 4;

	/** The extra field ID for the second hand gradient style. */
	public static final int SECOND_HAND_GRADIENT_STYLE = 5;

	private static final int DEFAULT_FONT_SIZE = 14;

	private static final int OPENING_ANIMATION_DURATION = 900;

	private static final int ANIMATION_STOP_VALUE = 1000;

	/** The angle in degrees at which the date text starts. */
	private static final int TEXT_ANGLE = 165;

	/** The offset of the text baseline from the widget bounds, in pixels. */
	private static final int BASELINE_OFFSET = 8;

	private static final String FOREGROUND_IMAGE_PATH = "flower_center"; //$NON-NLS-1$

	private ResourceImage foregroundImage;

	private FlowerSecondGradient secondGradient;

	private String date;

	private final TimeService timeService;

	/**
	 * Constructs the flower watchface.
	 */
	public FlowerWatchface() {
		setAnimateOpening(true);

		// Services
		ServiceLoader kernelServiceLoader = KernelServiceRegistry.getServiceLoader();
		this.timeService = kernelServiceLoader.getService(TimeService.class);

		this.date = TimeHelper.formatDate(Util.currentTimeMillis());
	}

	@Override
	protected void renderContent(GraphicsContext g, int contentWidth, int contentHeight) {
		renderHourHand(g);
		renderMinuteHand(g);
		this.secondGradient.render(g);
		renderSecondHand(g);
		renderCenterImage(g, contentWidth, contentHeight);
		renderDate(g, contentWidth, contentHeight);
	}

	private void renderDate(GraphicsContext g, int contentWidth, int contentHeight) {
		Style style = getStyle();
		VectorFontLoader fontLoader = style.getExtraObject(FONT_STYLE, VectorFontLoader.class,
				VectorFontLoader.DEFAULT_FONT_LOADER);
		VectorFont font = fontLoader.getFont();
		if (font != null) {
			g.setColor(style.getColor());
			int fontHeight = style.getExtraInt(TEXT_SIZE_STYLE, DEFAULT_FONT_SIZE);
			int diameter = Math.min(contentWidth, contentHeight) - BASELINE_OFFSET;
			Matrix matrix = new Matrix();
			matrix.setTranslate(contentWidth / 2.0f, contentHeight / 2.0f);
			matrix.preRotate(TEXT_ANGLE);
			VectorGraphicsPainter.drawStringOnCircle(g, formatDateTime(), font, fontHeight, matrix, diameter / 2.0f,
					Direction.COUNTER_CLOCKWISE);
		}
	}

	private String formatDateTime() {
		return this.date + SPACE + SPACE + TimeHelper.formatTime(Util.currentTimeMillis());
	}

	private void renderCenterImage(GraphicsContext g, int contentWidth, int contentHeight) {
		ResourceImage image = this.foregroundImage;
		assert image != null;

		int x = Alignment.computeLeftX(image.getWidth(), 0, contentWidth, Alignment.HCENTER);
		int y = Alignment.computeTopY(image.getHeight(), 0, contentHeight, Alignment.VCENTER);
		Painter.drawImage(g, image, x, y);
	}

	@Override
	protected void onAttached() {
		super.onAttached();
		this.foregroundImage = WatchImageLoader.loadImage(FOREGROUND_IMAGE_PATH);
	}

	@Override
	protected void onLaidOut() {
		super.onLaidOut();

		Style style = getStyle();
		GradientStyle gradientStyle = style.getExtraObject(SECOND_HAND_GRADIENT_STYLE, GradientStyle.class,
				GradientStyle.DEFAULT_GRADIENT_STYLE);
		this.secondGradient = new FlowerSecondGradient(gradientStyle, Math.min(getWidth(), getHeight() / 2));
		this.secondGradient.setRotationCenter(getWidth() / 2, getHeight() / 2);

		if (isAnimateOpening()) {
			updateHandsAngle(0);
		}
	}

	@Override
	protected void onDetached() {
		super.onDetached();
		ResourceImage image = this.foregroundImage;
		assert image != null;
		image.close();
	}


	@Override
	protected Hand createHourHand() {
		return new FlowerHourHand();
	}

	@Override
	protected Hand createMinuteHand() {
		return new FlowerMinuteHand();
	}

	@Override
	protected Hand createSecondHand() {
		return new FlowerSecondHand();
	}

	@Override
	protected void setSecondAngle(float angle) {
		super.setSecondAngle(angle);
		if (this.secondGradient != null) {
			this.secondGradient.setAngle(angle);
		}
	}

	@Override
	protected Animation[] createOpeningAnimation() {
		Animation[] animations = new Animation[0];

		animations = ArrayTools.add(animations, createHourAnimation());
		animations = ArrayTools.add(animations, createMinuteAnimation());
		animations = ArrayTools.add(animations, createSecondAnimation());

		return animations;
	}

	private Animation createSecondAnimation() {
		float stopSeconds = TimeHelper.computeSeconds(Util.currentTimeMillis() + OPENING_ANIMATION_DURATION);
		final float stopAngle = getAngleComputer().getSecondAngle(stopSeconds);
		final Motion motion = createMotion((int) (stopAngle * ANIMATION_STOP_VALUE));
		return new Animation() {

			@Override
			public boolean tick(final long currentTimeMillis) {
				setSecondAngle((float) motion.getValue(currentTimeMillis) / ANIMATION_STOP_VALUE);
				return !motion.isFinished();
			}
		};
	}

	private Animation createMinuteAnimation() {
		float stopMinutes = TimeHelper.computeMinute(Util.currentTimeMillis());
		final float stopAngle = getAngleComputer().getMinuteAngle(stopMinutes);
		final Motion motion = createMotion((int) (stopAngle * ANIMATION_STOP_VALUE));

		return new Animation() {

			@Override
			public boolean tick(final long currentTimeMillis) {
				setMinuteAngle((float) motion.getValue(currentTimeMillis) / ANIMATION_STOP_VALUE);
				return !motion.isFinished();
			}
		};
	}

	private Animation createHourAnimation() {
		float stopHour = TimeHelper.computeHour(Util.currentTimeMillis());
		final float stopAngle = getAngleComputer().getHourAngle(stopHour);
		final Motion motion = createMotion((int) (stopAngle * ANIMATION_STOP_VALUE));

		return new Animation() {

			@Override
			public boolean tick(final long currentTimeMillis) {
				setHourAngle((float) motion.getValue(currentTimeMillis) / ANIMATION_STOP_VALUE);
				return !motion.isFinished();
			}
		};
	}

	private Motion createMotion(int stop) {
		return new CubicEaseInOutMotion(0, stop, OPENING_ANIMATION_DURATION);
	}

	@Override
	protected void onShown() {
		super.onShown();
		this.timeService.setObserver(this);
	}

	@Override
	protected void onHidden() {
		super.onHidden();
		this.timeService.unsetObserver(this);
	}

	@Override
	public void update() {
		MicroUI.callSerially(new Runnable() {

			@Override
			public void run() {
				FlowerWatchface.this.date = TimeHelper.formatDate(Util.currentTimeMillis());
			}
		});
	}
}
