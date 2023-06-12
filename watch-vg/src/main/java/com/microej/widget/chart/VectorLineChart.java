/*
 * Java
 *
 * Copyright 2020-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.widget.chart;

import com.microej.demo.watch.util.font.VectorFontLoader;
import com.microej.demo.watch.util.path.GradientStyle;
import com.microej.demo.watch.util.services.HeartRateService;
import com.microej.path.chart.ChartAxis;
import com.microej.path.chart.ChartCurve;
import com.microej.path.chart.ChartGradient;
import com.microej.path.chart.ChartTimezoneA;
import com.microej.path.chart.ChartTimezoneB;

import ej.basictool.ArrayTools;
import ej.microui.MicroUI;
import ej.microui.display.Colors;
import ej.microui.display.GraphicsContext;
import ej.microvg.VectorFont;
import ej.microvg.VectorGraphicsPainter;
import ej.motion.Motion;
import ej.motion.circ.CircEaseOutMotion;
import ej.mwt.Widget;
import ej.mwt.animation.Animation;
import ej.mwt.style.Style;
import ej.mwt.util.Size;

/**
 * A line chart that plots the user's values.
 *
 * <p>
 * The widget uses vector path to render the layers of the chart.
 *
 * <p>
 * The chart is composed of 5 layers, in this order (bottom-up):
 * <ul>
 * <li>the background, called "Timezone B"
 * <li>the gradient under the curve
 * <li>the time indicators, called "Timezone A"
 * <li>the axis
 * <li>the line curve
 * </ul>
 */
public class VectorLineChart extends Widget {

	/** The extra field ID for the curve color style. */
	public static final int CURVE_COLOR_STYLE = 0;

	/** The extra field ID for the curve gradient style. */
	public static final int CURVE_GRADIENT_STYLE = 1;

	/** The extra field ID for the time-zone B style. */
	public static final int TIMEZONE_B_STYLE = 2;

	/** The extra field ID for the time-zone A style. */
	public static final int TIMEZONE_A_STYLE = 3;

	/** The extra field ID for the curve thickness style. */
	public static final int CURVE_THICKNESS_STYLE = 4;

	/** The extra field ID for the font style. */
	public static final int FONT_STYLE = 5;

	/** The extra field ID for the axis gradient style. */
	public static final int AXIS_GRADIENT_STYLE = 6;

	private static final int INITIAL_CLIP = 0;

	private static final float INITIAL_TIMEZONE_SCALE = 3f;

	private static final int LEGEND_MARKERS_COUNT = 8;

	private static final char HOUR_SYMBOL = 'h';

	private static final int HOURS_12H = 12;

	private static final int LEGEND_HOUR_INCREMENT = 2;

	private static final int LEGEND_INITIAL_HOUR = 6;

	private static final int LEGEND_FONT_HEIGHT = 17;

	private static final float LEGEND_SPACING = 48.4f;

	private static final int LEGEND_START_X = 41;

	private static final float TIMEZONE_A_INITIAL_SCALE = 3f;

	private static final int FIRST_STEP_STOP_VALUE = 2000;

	private static final int SECOND_STEP_STOP_VALUE = 1000;

	private static final int ANIMATION_STEP_DURATION = 750;

	private static final float INITIAL_CURVE_SCALE = 0f;

	private static final int BOTTOM_MARGIN = 20;

	private static final int DEFAULT_CURVE_COLOR = Colors.RED;

	private static final float DEFAULT_CURVE_THICKNESS = 2.5f;

	private final float[] data;

	private ChartCurve curve;

	private Animation chartAnimation;

	private int clip;

	private ChartGradient gradient;

	private ChartTimezoneB timezoneB;

	private final int horizontalStep;

	private ChartTimezoneA timezoneA;

	private float scale;

	private ChartAxis axis;

	private VectorFont font;

	/**
	 * Creates the vector chart, given the data array to plot and the spacing between each data point.
	 *
	 * @param data
	 *            the data array to plot.
	 * @param horizontalStep
	 *            the horizontal spacing to use when rendering the points.
	 */
	public VectorLineChart(float[] data, int horizontalStep) {
		this.data = data.clone();
		this.horizontalStep = horizontalStep;
	}

	private void normalizeData() {
		float ratio = (float) (getHeight() - BOTTOM_MARGIN) / (HeartRateService.MAX_HR + BOTTOM_MARGIN);
		int length = this.data.length;
		for (int i = 0; i < length; i++) {
			this.data[i] *= ratio;
		}
	}

	@Override
	protected void computeContentOptimalSize(Size size) {
		size.setWidth(this.horizontalStep * this.data.length);
	}

	@Override
	protected void renderContent(GraphicsContext g, int contentWidth, int contentHeight) {
		int clip = this.clip;
		int clipX = contentWidth / 2 - clip;
		int clipWidth = 2 * clip;
		int clipY = g.getClipY();
		int clipHeight = g.getClipHeight();

		this.timezoneB.render(g);
		if (clipWidth >= contentWidth) {
			this.gradient.render(g);
		}

		this.timezoneA.render(g);
		g.setClip(clipX, clipY, clipWidth, clipHeight);
		this.axis.render(g);
		this.curve.render(g);
		g.resetClip();
		drawLegend(g);
	}

	@Override
	protected void onLaidOut() {
		super.onLaidOut();
		this.clip = INITIAL_CLIP;
		this.scale = INITIAL_TIMEZONE_SCALE;
		this.font = getFont();
		normalizeData();
		createVectorGraphics();
	}

	private void createVectorGraphics() {
		closeVectorGraphics();
		float[] array = getScaledData(this.data, INITIAL_CURVE_SCALE);
		createCurve(array);
		createGradient(array);
		createTimezoneB();
		createTimezoneA();
		createAxis();
	}

	private void createAxis() {
		Style style = getStyle();
		assert style != null;
		GradientStyle gradientStyle = style.getExtraObject(AXIS_GRADIENT_STYLE, GradientStyle.class,
				GradientStyle.DEFAULT_GRADIENT_STYLE);
		this.axis = new ChartAxis(getChartHeight(), getWidth(), gradientStyle);
	}

	private void createGradient(float[] array) {
		Style style = getStyle();
		assert style != null;

		GradientStyle gradientStyle = style.getExtraObject(CURVE_GRADIENT_STYLE, GradientStyle.class,
				GradientStyle.DEFAULT_GRADIENT_STYLE);
		this.gradient = new ChartGradient(getChartHeight(), array, this.horizontalStep, gradientStyle);
	}

	private void createCurve(float[] array) {
		Style style = getStyle();
		assert style != null;

		int color = style.getExtraInt(CURVE_COLOR_STYLE, DEFAULT_CURVE_COLOR);
		float thickness = style.getExtraFloat(CURVE_THICKNESS_STYLE, DEFAULT_CURVE_THICKNESS);
		this.curve = new ChartCurve(getChartHeight(), array, this.horizontalStep, color, thickness);
	}

	private void createTimezoneB() {
		Style style = getStyle();
		assert style != null;

		GradientStyle gradientStyle = style.getExtraObject(TIMEZONE_B_STYLE, GradientStyle.class,
				GradientStyle.DEFAULT_GRADIENT_STYLE);
		this.timezoneB = new ChartTimezoneB(getWidth(), getHeight() - 20, gradientStyle);
	}

	private void createTimezoneA() {
		Style style = getStyle();
		assert style != null;

		GradientStyle gradientStyle = style.getExtraObject(TIMEZONE_A_STYLE, GradientStyle.class,
				GradientStyle.DEFAULT_GRADIENT_STYLE);
		this.timezoneA = new ChartTimezoneA(getWidth(), getHeight() - 20, gradientStyle);
		this.timezoneA.setScale(this.scale);

	}

	private void closeVectorGraphics() {
		this.curve = null;
		this.gradient = null;
		this.timezoneB = null;
		this.timezoneA = null;
		this.axis = null;
	}

	private int getChartHeight() {
		return getHeight() - BOTTOM_MARGIN;
	}

	@Override
	protected void onDetached() {
		super.onDetached();
		closeVectorGraphics();
	}

	private void startAnimation() {
		stopAnimation();

		Animation animation = createAnimation();
		getDesktop().getAnimator().startAnimation(animation);

		this.chartAnimation = animation;
	}

	private Animation createAnimation() {
		StackAnimation animation = new StackAnimation();

		// the first step scales-down the "timezone A" layer and increase the clip to fullscreen size.
		final Motion firstMotion = new CircEaseOutMotion(0, FIRST_STEP_STOP_VALUE, ANIMATION_STEP_DURATION);
		Animation firstStep = new Animation() {

			@Override
			public boolean tick(long currentTimeMillis) {
				int currentValue = firstMotion.getCurrentValue();
				updateTimezoneAScale(currentValue);
				VectorLineChart.this.clip = currentValue * (getWidth() / 2) / FIRST_STEP_STOP_VALUE;
				requestRender();
				return !firstMotion.isFinished();
			}

		};
		animation.addStep(firstStep, firstMotion);

		// the second step scales-up the curve and gradient layers.
		final Motion secondMotion = new CircEaseOutMotion(0, SECOND_STEP_STOP_VALUE, ANIMATION_STEP_DURATION);
		Animation secondStep = new Animation() {

			@Override
			public boolean tick(long currentTimeMillis) {
				updateCurveScale(secondMotion);
				requestRender();
				return !secondMotion.isFinished();
			}
		};
		animation.addStep(secondStep, secondMotion);

		return animation;
	}

	private void updateCurveScale(final Motion motion) {
		MicroUI.callSerially(new Runnable() {

			@Override
			public void run() {
				float ratio = (float) motion.getCurrentValue() / SECOND_STEP_STOP_VALUE;
				if (VectorLineChart.this.curve != null) {
					float[] scaledData = getScaledData(VectorLineChart.this.data, ratio);
					VectorLineChart.this.curve.setData(scaledData);
					VectorLineChart.this.gradient.setData(scaledData);
				}
			}
		});
	}

	private float[] getScaledData(float[] data, float ratio) {
		int length = data.length;
		float[] array = new float[length];
		for (int i = 0; i < length; i++) {
			array[i] = data[i] * ratio;
		}

		return array;
	}

	private void updateTimezoneAScale(final int motionValue) {
		float value = motionValue / (FIRST_STEP_STOP_VALUE / 2f);
		VectorLineChart.this.scale = TIMEZONE_A_INITIAL_SCALE - value;
		ChartTimezoneA timezone = VectorLineChart.this.timezoneA;
		if (timezone != null) {
			timezone.setScale(VectorLineChart.this.scale);
		}
	}

	private void stopAnimation() {
		Animation animation = this.chartAnimation;
		if (animation != null) {
			getDesktop().getAnimator().stopAnimation(animation);
		}
	}

	@Override
	protected void onShown() {
		super.onShown();
		startAnimation();
	}

	@Override
	protected void onHidden() {
		super.onHidden();
		stopAnimation();
	}

	private class StackAnimation implements Animation {

		private Animation[] animations;

		private Motion[] motions;

		private int index;

		public StackAnimation() {
			this.animations = new Animation[0];
			this.motions = new Motion[0];
			this.index = 0;
		}

		/**
		 * Add an animation step to the Vector Line Chart animation.
		 *
		 * @param animation
		 *            the animation of the step.
		 * @param motion
		 *            the motion of the step.
		 */
		public void addStep(Animation animation, Motion motion) {
			this.animations = ArrayTools.add(this.animations, animation);
			this.motions = ArrayTools.add(this.motions, motion);
		}

		@Override
		public boolean tick(long currentTimeMillis) {
			int length = this.animations.length;
			assert this.index < length;

			Animation animation = this.animations[this.index];
			boolean stepFinished = !animation.tick(currentTimeMillis);
			requestRender();

			if (stepFinished) {
				this.index++;
				if (this.index < length) {
					// start next step
					Motion motion = this.motions[this.index];
					motion.start();
				}
			}

			return this.index < length;
		}
	}

	private void drawLegend(GraphicsContext g) {
		Style style = getStyle();
		g.setColor(style.getColor());
		for (int i = 0; i < LEGEND_MARKERS_COUNT; i++) {
			final String text = getLegendAtIndex(i);
			int x = computeLegendX(i, text);
			VectorGraphicsPainter.drawString(g, text, this.font, LEGEND_FONT_HEIGHT, x, getChartHeight());
		}
	}

	private String getLegendAtIndex(int i) {
		int hour = (LEGEND_INITIAL_HOUR + i * LEGEND_HOUR_INCREMENT) % HOURS_12H;
		hour += hour == 0 ? HOURS_12H : 0;
		return String.valueOf(hour) + HOUR_SYMBOL;
	}

	private int computeLegendX(int index, String text) {
		float stringWidth = this.font.measureStringWidth(text, LEGEND_FONT_HEIGHT);
		int xCenter = getWidth() / 2;
		return (int) (xCenter * (1 - this.scale) + LEGEND_START_X * this.scale + index * (LEGEND_SPACING * this.scale)
				- stringWidth / 2f);
	}

	private VectorFont getFont() {
		Style style = getStyle();
		VectorFontLoader fontLoader = style.getExtraObject(FONT_STYLE, VectorFontLoader.class,
				VectorFontLoader.DEFAULT_FONT_LOADER);
		return fontLoader.getFont();
	}

}
