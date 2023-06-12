/*
 * Java
 *
 * Copyright 2019-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.widget.activity;

import com.microej.demo.nls.Messages;
import com.microej.demo.watch.util.services.ActivityService;
import com.microej.demo.watch.util.widget.basic.VectorLabel;
import com.microej.style.ClassSelectors;
import com.microej.widget.basic.VectorCircularProgressBar;

import ej.mwt.Container;
import ej.mwt.Widget;
import ej.mwt.util.Alignment;
import ej.mwt.util.Size;
import ej.widget.basic.ImagePath;

/**
 * Draws the background of the activity page.
 */
public class ActivityPageBackground extends Container {

	private static final int TITLE_Y = 35;

	/* Progress bar constants */
	private static final int PROGRESS_BAR_START_ANGLE = 52;
	private static final int PROGRESS_BAR_ARC_ANGLE = -284;

	/* Stopwatch constants */
	private static final String TIME_ICON_PATH = "activity/ic-chrono"; //$NON-NLS-1$
	private static final int TIME_ICON_X = 126;
	private static final int TIME_ICON_Y = 120;
	private static final int TIME_LABEL_Y = 123;
	private static final int TIME_BACKGROUND_BAR_SIZE = 348;
	private static final int TIME_MIN_VALUE = 0;
	private static final int TIME_MAX_DURATION = 8_000;

	/* Calories constants */
	private static final String CALORIES_ICON_PATH = "activity/ic-calories"; //$NON-NLS-1$
	private static final int CALORIES_ICON_X = 118;
	private static final int CALORIES_ICON_Y = 211;
	private static final int CALORIES_LABEL_Y = 214;
	private static final int CALORIES_UNIT_X = 240;
	private static final int CALORIES_UNIT_Y = 253;
	private static final int CALORIES_BACKGROUND_BAR_SIZE = 313;

	/* Time fields */
	private VectorLabel titleLabel;
	private VectorLabel timeLabel;
	private ImagePath timeIcon;
	private VectorCircularProgressBar timeBackgroundBar;

	/* Calories fields */
	private VectorCircularProgressBar caloriesBackgroundBar;
	private VectorLabel caloriesLabel;
	private VectorLabel caloriesUnitLabel;
	private ImagePath caloriesIcon;

	/**
	 * Creates the activity background elements.
	 */
	public ActivityPageBackground() {
		createTitle();
		createStopwatch();
		createCalories();
	}

	private void createTitle() {
		VectorLabel label = new VectorLabel(Messages.NLS.getMessage(Messages.ActivityTitle));
		label.addClassSelector(ClassSelectors.PAGE_TITLE);
		addChild(label);
		this.titleLabel = label;
	}

	private void createStopwatch() {
		VectorCircularProgressBar backgroundBar = new VectorCircularProgressBar(TIME_MIN_VALUE, TIME_MAX_DURATION,
				TIME_MAX_DURATION, PROGRESS_BAR_START_ANGLE, PROGRESS_BAR_ARC_ANGLE);
		backgroundBar.addClassSelector(ClassSelectors.ACTIVITY_TIME_PROGRESS_BACKGROUND);
		addChild(backgroundBar);
		this.timeBackgroundBar = backgroundBar;

		VectorLabel label = new VectorLabel(Messages.NLS.getMessage(Messages.Time));
		label.addClassSelector(ClassSelectors.ACTIVITY_TEXT);
		addChild(label);
		this.timeLabel = label;

		ImagePath icon = new ImagePath(TIME_ICON_PATH);
		icon.addClassSelector(ClassSelectors.ACTIVITY_ICONS);
		addChild(icon);
		this.timeIcon = icon;
	}

	private void createCalories() {
		VectorCircularProgressBar backgroundBar = new VectorCircularProgressBar(TIME_MIN_VALUE,
				ActivityService.MAX_CALORIES, ActivityService.MAX_CALORIES, PROGRESS_BAR_START_ANGLE,
				PROGRESS_BAR_ARC_ANGLE);
		backgroundBar.addClassSelector(ClassSelectors.ACTIVITY_CALORIES_PROGRESS_BACKGROUND);
		addChild(backgroundBar);
		this.caloriesBackgroundBar = backgroundBar;

		VectorLabel label = new VectorLabel(Messages.NLS.getMessage(Messages.Calories));
		label.addClassSelector(ClassSelectors.ACTIVITY_TEXT);
		addChild(label);
		this.caloriesLabel = label;

		VectorLabel unitLabel = new VectorLabel(Messages.NLS.getMessage(Messages.Kcal));
		unitLabel.addClassSelector(ClassSelectors.ACTIVITY_TEXT);
		addChild(unitLabel);
		this.caloriesUnitLabel = unitLabel;

		ImagePath icon = new ImagePath(CALORIES_ICON_PATH);
		icon.addClassSelector(ClassSelectors.ACTIVITY_ICONS);
		addChild(icon);
		this.caloriesIcon = icon;
	}

	@Override
	protected void layOutChildren(int contentWidth, int contentHeight) {
		layOutChild(this.titleLabel, 0, TITLE_Y, contentWidth, this.titleLabel.getHeight());

		layoutStopwatch(contentWidth, contentHeight);
		layoutCalories(contentWidth, contentHeight);
	}

	private void layoutStopwatch(int contentWidth, int contentHeight) {
		int x = Alignment.computeLeftX(TIME_BACKGROUND_BAR_SIZE, 0, contentWidth, Alignment.HCENTER);
		int y = Alignment.computeTopY(TIME_BACKGROUND_BAR_SIZE, 0, contentHeight, Alignment.VCENTER);
		layOutChild(this.timeBackgroundBar, x, y, TIME_BACKGROUND_BAR_SIZE, TIME_BACKGROUND_BAR_SIZE);

		layOutChild(this.timeIcon, TIME_ICON_X, TIME_ICON_Y, this.timeIcon.getWidth(), this.timeIcon.getHeight());

		layOutChild(this.timeLabel, 0, TIME_LABEL_Y, contentWidth, this.timeLabel.getHeight());

	}

	private void layoutCalories(int contentWidth, int contentHeight) {
		int x = Alignment.computeLeftX(CALORIES_BACKGROUND_BAR_SIZE, 0, contentWidth, Alignment.HCENTER);
		int y = Alignment.computeTopY(CALORIES_BACKGROUND_BAR_SIZE, 0, contentHeight, Alignment.VCENTER);
		layOutChild(this.caloriesBackgroundBar, x, y, CALORIES_BACKGROUND_BAR_SIZE, CALORIES_BACKGROUND_BAR_SIZE);

		ImagePath icon = this.caloriesIcon;
		layOutChild(icon, CALORIES_ICON_X, CALORIES_ICON_Y, icon.getWidth(), icon.getHeight());

		VectorLabel label = this.caloriesLabel;
		layOutChild(label, 0, CALORIES_LABEL_Y, contentWidth, label.getHeight());

		VectorLabel unitLabel = this.caloriesUnitLabel;

		layOutChild(unitLabel, CALORIES_UNIT_X, CALORIES_UNIT_Y, unitLabel.getWidth(), unitLabel.getHeight());
	}

	@Override
	protected void computeContentOptimalSize(Size size) {
		int width = size.getWidth();
		int height = size.getHeight();

		for (Widget widget : getChildren()) {
			computeChildOptimalSize(widget, width, height);
		}
	}

}
