/*
 * Java
 *
 * Copyright 2019-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.page;

import com.microej.demo.watch.util.KernelServiceRegistry;
import com.microej.demo.watch.util.font.VectorFontLoader;
import com.microej.demo.watch.util.helper.TimeHelper;
import com.microej.demo.watch.util.services.ActivityService;
import com.microej.demo.watch.util.widget.basic.VectorLabel;
import com.microej.helper.MainListPositions;
import com.microej.style.ClassSelectors;
import com.microej.widget.activity.ActivityPageBackground;
import com.microej.widget.basic.VectorCircularProgressBar;

import ej.microui.display.BufferedImage;
import ej.microui.display.GraphicsContext;
import ej.motion.Motion;
import ej.motion.linear.LinearMotion;
import ej.motion.quart.QuartEaseInOutMotion;
import ej.mwt.animation.Animation;
import ej.mwt.style.Style;
import ej.mwt.util.Alignment;
import ej.observable.Observer;
import ej.widget.util.render.ImagePainter;

/**
 * A page that monitors the user activity (time in current lap, calories burned).
 */
public class ActivityPage extends SimplePage implements Observer {

	/* Progress bar constants */
	private static final int PROGRESS_BAR_START_ANGLE = 52;
	private static final int PROGRESS_BAR_ARC_ANGLE = -284;

	/* Stopwatch constants */
	private static final int TIME_VALUE_X = 110;
	private static final int TIME_VALUE_Y = 140;
	private static final int TIME_PROGRESS_BAR_SIZE = 356;
	private static final int TIME_MIN_VALUE = 0;
	private static final int TIME_MIN_DURATION = 1_000;
	private static final int TIME_MAX_DURATION = 8_000;
	private static final String TIME_PATTERN_STRING = "00.00.00";

	/* Calories constants */
	private static final int CALORIES_VALUE_X = 130;
	private static final int CALORIES_VALUE_Y = 231;
	private static final int CALORIES_PROGRESS_BAR_SIZE = 309;
	private static final int CALORIES_INITIAL_VALUE = 0;
	private static final int CALORIES_ANIMATION_DURATION = 1000;

	/* Time fields */
	private VectorLabel timeValue;
	private VectorCircularProgressBar timeProgressBar;
	private int duration;

	/* Calories fields */
	private VectorCircularProgressBar caloriesProgressBar;
	private VectorLabel caloriesValue;
	private int calories;

	/* Animation fields */
	private Animation animation;
	private Motion timeMotion;
	private Motion caloriesMotion;

	private final ActivityService activityService;
	private BufferedImage backgroundImage;

	/**
	 * Creates the activity page.
	 */
	public ActivityPage() {
		super(MainListPositions.ACTIVITY);
		this.calories = CALORIES_INITIAL_VALUE;
		this.duration = TIME_MIN_DURATION;

		createStopwatch();
		createCalories();

		this.activityService = KernelServiceRegistry.getServiceLoader().getService(ActivityService.class);
	}

	private void createStopwatch() {
		VectorCircularProgressBar progressBar = new VectorCircularProgressBar(TIME_MIN_VALUE, this.duration,
				TIME_MIN_VALUE, PROGRESS_BAR_START_ANGLE, PROGRESS_BAR_ARC_ANGLE);
		progressBar.addClassSelector(ClassSelectors.ACTIVITY_TIME_PROGRESS);
		addChild(progressBar);
		this.timeProgressBar = progressBar;

		VectorLabel value = new VectorLabel(TimeHelper.formatStopwatch(0));
		value.addClassSelector(ClassSelectors.ACTIVITY_TIME);
		addChild(value);
		this.timeValue = value;

	}

	private void createCalories() {
		VectorCircularProgressBar progressBar = new VectorCircularProgressBar(CALORIES_INITIAL_VALUE,
				ActivityService.MAX_CALORIES, CALORIES_INITIAL_VALUE, PROGRESS_BAR_START_ANGLE, PROGRESS_BAR_ARC_ANGLE);
		progressBar.addClassSelector(ClassSelectors.ACTIVITY_CALORIES_PROGRESS);
		addChild(progressBar);
		this.caloriesProgressBar = progressBar;

		VectorLabel value = new VectorLabel(String.valueOf(this.calories));
		value.addClassSelector(ClassSelectors.ACTIVITY_CALORIES);
		addChild(value);
		this.caloriesValue = value;

	}

	@Override
	protected void layOutChildren(int contentWidth, int contentHeight) {
		layoutStopwatch(contentWidth, contentHeight);
		layoutCalories(contentWidth, contentHeight);
	}

	private void layoutStopwatch(int contentWidth, int contentHeight) {
		int x = Alignment.computeLeftX(TIME_PROGRESS_BAR_SIZE, 0, contentWidth, Alignment.HCENTER);
		int y = Alignment.computeTopY(TIME_PROGRESS_BAR_SIZE, 0, contentHeight, Alignment.VCENTER);
		layOutChild(this.timeProgressBar, x, y, TIME_PROGRESS_BAR_SIZE, TIME_PROGRESS_BAR_SIZE);

		VectorLabel valueLabel = this.timeValue;

		int valueWidth = measureLabelWidth(valueLabel, TIME_PATTERN_STRING);
		layOutChild(this.timeValue, TIME_VALUE_X, TIME_VALUE_Y, valueWidth, this.timeValue.getHeight());
	}

	private void layoutCalories(int contentWidth, int contentHeight) {
		int x = Alignment.computeLeftX(CALORIES_PROGRESS_BAR_SIZE, 0, contentWidth, Alignment.HCENTER);
		int y = Alignment.computeTopY(CALORIES_PROGRESS_BAR_SIZE, 0, contentHeight, Alignment.VCENTER);
		layOutChild(this.caloriesProgressBar, x, y, CALORIES_PROGRESS_BAR_SIZE, CALORIES_PROGRESS_BAR_SIZE);

		VectorLabel valueLabel = this.caloriesValue;

		int valueWidth = measureLabelWidth(valueLabel, String.valueOf(ActivityService.MAX_CALORIES));
		layOutChild(valueLabel, CALORIES_VALUE_X, CALORIES_VALUE_Y, valueWidth, valueLabel.getHeight());
	}

	@Override
	protected void onShown() {
		super.onShown();
		startAnimation();
		ActivityService service = this.activityService;
		service.setObserver(this);
	}

	@Override
	protected void onHidden() {
		super.onHidden();
		stopAnimation();
		ActivityService service = this.activityService;
		service.unsetObserver(this);
	}

	@Override
	public void update() {
		this.caloriesMotion = new QuartEaseInOutMotion(this.calories, this.activityService.getCalories(),
				CALORIES_ANIMATION_DURATION);
	}

	private void startAnimation() {
		stopAnimation();

		createTimeMotion();
		Animation animation = new Animation() {

			@Override
			public boolean tick(long currentTimeMillis) {
				updateTime();
				updateCalories();

				requestRender();
				return true;
			}

		};

		this.animation = animation;
		getDesktop().getAnimator().startAnimation(animation);
	}

	private void createTimeMotion() {
		this.timeMotion = new LinearMotion(TIME_MIN_VALUE, this.duration, this.duration);
	}

	private void updateCalories() {
		Motion motion = this.caloriesMotion;
		if (motion != null) {
			int currentValue = motion.getCurrentValue();
			this.calories = currentValue;
			this.caloriesProgressBar.updateValue(currentValue);
			this.caloriesValue.updateText(String.valueOf(currentValue));
		}
	}

	private void updateTime() {
		Motion motion = this.timeMotion;
		int elapsed = motion.getCurrentValue();
		this.timeProgressBar.updateValue(elapsed);
		this.timeValue.updateText(TimeHelper.formatStopwatch(elapsed));
		if (motion.isFinished()) {
			this.duration *= 2;
			if (this.duration > TIME_MAX_DURATION) {
				this.duration = TIME_MIN_DURATION;
			}
			this.timeProgressBar.setMaximum(this.duration);
			createTimeMotion();
		}
	}

	private void stopAnimation() {
		Animation animation = this.animation;
		if (animation != null) {
			getDesktop().getAnimator().stopAnimation(animation);
		}
	}

	@Override
	protected void onLaidOut() {
		prepareBackground();
		super.onLaidOut();
	}

	private void prepareBackground() {
		ActivityPageBackground background = new ActivityPageBackground();
		addChild(background);

		int width = getWidth();
		int height = getHeight();
		BufferedImage screenshot = new BufferedImage(width, height);
		GraphicsContext screenshotGraphicsContext = screenshot.getGraphicsContext();

		computeChildOptimalSize(background, width, height);
		layOutChild(background, 0, 0, width, height);
		background.render(screenshotGraphicsContext);

		removeChild(background);

		this.backgroundImage = screenshot;
	}

	@Override
	protected void renderContent(GraphicsContext g, int contentWidth, int contentHeight) {
		drawBackground(g);
		super.renderContent(g, contentWidth, contentHeight);
	}

	@Override
	public boolean isTransparent() {
		return false;
	}

	private void drawBackground(GraphicsContext g) {
		BufferedImage image = this.backgroundImage;
		if (image != null) {
			ImagePainter.drawImageInArea(g, image, 0, 0, image.getWidth(), image.getHeight(), Alignment.LEFT,
					Alignment.TOP);
		}
	}

	@Override
	protected void onDetached() {
		super.onDetached();
		closeBackgroundImage();
	}

	private void closeBackgroundImage() {
		BufferedImage image = this.backgroundImage;
		if (image != null) {
			image.close();
			this.backgroundImage = null;
		}
	}

	private int measureLabelWidth(VectorLabel valueLabel, String pattern) {
		Style style = valueLabel.getStyle();
		VectorFontLoader fontLoader = style.getExtraObject(VectorLabel.FONT_STYLE, VectorFontLoader.class,
				VectorFontLoader.DEFAULT_FONT_LOADER);
		int fontHeight = style.getExtraInt(VectorLabel.TEXT_SIZE_STYLE, VectorLabel.DEFAULT_TEXT_SIZE);

		return (int) fontLoader.getFont().measureStringWidth(pattern, fontHeight);
	}
}
