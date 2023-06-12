/*
 * Java
 *
 * Copyright 2019-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.page;

import com.microej.demo.nls.Messages;
import com.microej.demo.watch.util.KernelServiceRegistry;
import com.microej.demo.watch.util.services.HeartRateService;
import com.microej.demo.watch.util.widget.basic.VectorLabel;
import com.microej.helper.MainListPositions;
import com.microej.style.ClassSelectors;
import com.microej.widget.chart.VectorLineChart;

import ej.bon.Immutables;
import ej.mwt.Widget;
import ej.observable.Observer;
import ej.widget.basic.AnimatedImage;
import ej.widget.basic.ImagePath;
import ej.widget.container.Dock;
import ej.widget.container.SimpleDock;

/**
 * A page that shows the course of the heart rate along the day and the current heart rate.
 */
public class HeartRatePage extends SimplePage implements Observer {

	private static final int LABELS_Y = 255;

	private static final int LABELS_HEIGHT = 80;

	private static final int CHART_Y = 91;

	private static final int LINE_CHART_HEIGHT = 160;

	private static final int TITLE_Y = 34;

	private static final String HEART_RATE_IMAGE_PATH = "heartrate/"; //$NON-NLS-1$

	private static final String MAX_PM_PATH = HEART_RATE_IMAGE_PATH + "wf_vg-hr-ic-arrow-r"; //$NON-NLS-1$

	private static final String MIN_PM_PATH = HEART_RATE_IMAGE_PATH + "wf_vg-hr-ic-arrow-b"; //$NON-NLS-1$

	private static final String[] BUMPING_ICON_FRAMES = (String[]) Immutables.get("hrAnimationPaths"); //$NON-NLS-1$

	private static final long BUMPING_PERIOD = 40;

	private static final int CHART_HORIZONTAL_STEP = 20;

	private final HeartRateService heartRateService;

	private VectorLineChart chart;

	private VectorLabel currentValueLabel;

	private VectorLabel maxValueLabel;

	private VectorLabel minValueLabel;

	private VectorLabel title;

	private SimpleDock labels;

	/**
	 * Creates the page.
	 */
	public HeartRatePage() {
		super(MainListPositions.HEART_RATE);

		this.heartRateService = KernelServiceRegistry.getServiceLoader().getService(HeartRateService.class);

		createTitle();
		createChart();
		createLabels();
	}

	private void createTitle() {
		VectorLabel label = new VectorLabel(Messages.NLS.getMessage(Messages.HeartRateTitle));
		label.addClassSelector(ClassSelectors.PAGE_TITLE);
		addChild(label);
		this.title = label;
	}

	private void createLabels() {
		SimpleDock dock = new SimpleDock(true);
		dock.addClassSelector(ClassSelectors.HR_DOCK);

		Widget maxHRWidget = createMaxHR();
		dock.setFirstChild(maxHRWidget);

		Widget currentHRWidget = createCurrentHR();
		dock.setCenterChild(currentHRWidget);

		Widget minHRWidget = createMinHR();
		dock.setLastChild(minHRWidget);

		addChild(dock);
		this.labels = dock;
	}

	private Widget createMaxHR() {
		Dock dock = new Dock();
		dock.addClassSelector(ClassSelectors.HR_DOCK);
		dock.addClassSelector(ClassSelectors.HR_UNIT_DOCKS);
		dock.addClassSelector(ClassSelectors.RIGHT);
		ImagePath icon = new ImagePath(MAX_PM_PATH);
		dock.addChildOnLeft(icon);
		VectorLabel unitLabel = new VectorLabel(Messages.NLS.getMessage(Messages.BpmUnit));
		unitLabel.addClassSelector(ClassSelectors.HR_UNIT);
		dock.addChildOnBottom(unitLabel);
		VectorLabel valueLabel = new VectorLabel(getMaximumHeartRate());
		valueLabel.addClassSelector(ClassSelectors.HR_MIN_MAX_VALUE);
		dock.setCenterChild(valueLabel);
		this.maxValueLabel = valueLabel;
		return dock;
	}

	private Widget createMinHR() {
		Dock dock = new Dock();
		dock.addClassSelector(ClassSelectors.HR_DOCK);
		dock.addClassSelector(ClassSelectors.HR_UNIT_DOCKS);
		dock.addClassSelector(ClassSelectors.LEFT);
		ImagePath icon = new ImagePath(MIN_PM_PATH);
		dock.addChildOnLeft(icon);
		VectorLabel unitLabel = new VectorLabel(Messages.NLS.getMessage(Messages.BpmUnit));
		unitLabel.addClassSelector(ClassSelectors.HR_UNIT);
		dock.addChildOnBottom(unitLabel);
		VectorLabel valueLabel = new VectorLabel(getMinimumHeartRate());
		valueLabel.addClassSelector(ClassSelectors.HR_MIN_MAX_VALUE);
		dock.setCenterChild(valueLabel);
		this.minValueLabel = valueLabel;
		return dock;
	}

	private String getMinimumHeartRate() {
		return String.valueOf(this.heartRateService.getMinimumHeartRate());
	}

	private String getMaximumHeartRate() {
		return String.valueOf(this.heartRateService.getMaximumHeartRate());
	}

	private String getCurrentHeartRate() {
		return String.valueOf(this.heartRateService.getCurrentHeartRate());
	}

	private Widget createCurrentHR() {
		Dock dock = new Dock();
		dock.addClassSelector(ClassSelectors.HR_DOCK);
		AnimatedImage icon = new AnimatedImage(BUMPING_ICON_FRAMES, BUMPING_PERIOD);
		dock.addChildOnLeft(icon);
		VectorLabel unitLabel = new VectorLabel(Messages.NLS.getMessage(Messages.CurrentBpm));
		unitLabel.addClassSelector(ClassSelectors.HR_CURRENT_VALUE_UNIT);
		dock.addChildOnBottom(unitLabel);
		VectorLabel valueLabel = new VectorLabel(getCurrentHeartRate());
		valueLabel.addClassSelector(ClassSelectors.HR_CURRENT_VALUE);
		dock.setCenterChild(valueLabel);
		this.currentValueLabel = valueLabel;
		return dock;
	}

	private void createChart() {
		VectorLineChart vectorChart = new VectorLineChart(this.heartRateService.getData(), CHART_HORIZONTAL_STEP);
		vectorChart.addClassSelector(ClassSelectors.HEART_RATE_CHART);
		addChild(vectorChart);
		this.chart = vectorChart;
	}

	@Override
	protected void layOutChildren(int contentWidth, int contentHeight) {
		layOutChild(this.title, 0, TITLE_Y, contentWidth, this.title.getHeight());
		layOutChild(this.chart, 0, CHART_Y, contentWidth, LINE_CHART_HEIGHT);
		layOutChild(this.labels, 0, LABELS_Y, contentWidth, LABELS_HEIGHT);
	}

	@Override
	protected void onShown() {
		super.onShown();
		this.heartRateService.setObserver(this);
	}

	@Override
	protected void onHidden() {
		super.onHidden();
		this.heartRateService.unsetObserver(this);
	}

	@Override
	public void update() {
		HeartRatePage.this.currentValueLabel.updateText(getCurrentHeartRate());
		HeartRatePage.this.maxValueLabel.updateText(getMaximumHeartRate());
		HeartRatePage.this.minValueLabel.updateText(getMinimumHeartRate());
		requestRender();
	}

}
