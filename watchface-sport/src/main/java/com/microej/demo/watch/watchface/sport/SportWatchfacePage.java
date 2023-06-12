/*
 * Java
 *
 * Copyright 2020-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.demo.watch.watchface.sport;

import com.microej.demo.watch.util.KernelServiceRegistry;
import com.microej.demo.watch.util.WatchImageLoader;
import com.microej.demo.watch.util.services.HeartRateService;
import com.microej.demo.watch.util.services.NotificationService;
import com.microej.demo.watch.util.services.PowerService;
import com.microej.demo.watch.util.services.StepsService;
import com.microej.demo.watch.util.widget.basic.CircularProgressIndicator;
import com.microej.demo.watch.util.widget.basic.VectorLabel;
import com.microej.demo.watch.util.widget.watchface.AbstractWatchface;
import com.microej.demo.watch.util.widget.watchface.VectorWatchface;
import com.microej.demo.watch.watchface.sport.widget.PowerLevelWidget;

import ej.microui.MicroUI;
import ej.microui.display.Display;
import ej.microui.display.GraphicsContext;
import ej.mwt.Widget;
import ej.observable.Observer;
import ej.service.ServiceLoader;
import ej.widget.basic.ImageWidget;
import ej.widget.container.OverlapContainer;

/**
 * A page that shows the Sport watchface.
 */
public class SportWatchfacePage extends OverlapContainer implements Observer {

	private static final String MAIL_PATH = "sport_mail"; //$NON-NLS-1$
	private static final String ALERT_PATH = "sport_alert"; //$NON-NLS-1$
	private static final String PERCENT = "%"; //$NON-NLS-1$

	private static final int PROGRESS_BAR_DIAMETER = 102;
	private static final int PROGRESS_BAR_COLOR = 0x00dcff;
	private static final int MAIL_IMAGE_X_OFFSET_FROM_CENTER = 105;
	private static final int ALERT_IMAGE_X_OFFSET_FROM_CENTER = 64;
	private static final int ALERT_IMAGE_Y_OFFSET_FROM_CENTER = 24;
	private static final int BPM_Y_OFFSET_FROM_CENTER = 73;
	private static final int POWER_IMAGE_X_OFFSET_FROM_CENTER = 31;
	private static final int POWER_IMAGE_Y_OFFSET_FROM_CENTER = 23;
	private static final int POWER_LEVEL_X_OFFSET_FROM_CENTER = 78;
	private static final int PROGRESS_BAR_X_OFFSET_FROM_CENTER = 51;
	private static final int PROGRESS_BAR_Y_OFFSET_FROM_CENTER = 121;
	private static final int MAIL_IMAGE_WIDTH = 49;
	private static final int MAIL_IMAGE_HEIGHT = 37;
	private static final int ALERT_IMAGE_WIDTH = 14;
	private static final int ALERT_IMAGE_HEIGHT = 14;
	private static final int POWER_IMAGE_WIDTH = 94;
	private static final int POWER_IMAGE_HEIGHT = 64;
	private static final int POWER_LEVEL_LABEL_WIDTH = 51;
	private static final int POWER_LEVEL_LABEL_HEIGHT = 20;
	private static final int BPM_LABEL_WIDTH = 60;
	private static final int BPM_LABEL_HEIGHT = 32;
	private static final int STEPS_VALUE_MAX = 250;
	private static final int PROGRESS_BAR_THICKNESS = 5;

	private final CircularProgressIndicator progressBar;
	private final VectorLabel bpmLabel;
	private final VectorLabel powerLevelLabel;
	private final StepsService stepsService;
	private final HeartRateService heartRateService;
	private final PowerService powerService;
	private final NotificationService notificationService;
	private final ImageWidget mailImage;
	private final ImageWidget alertImage;
	private final PowerLevelWidget powerWidget;
	private final AbstractWatchface watchface;

	/**
	 * Creates a Sport watchface page.
	 */
	public SportWatchfacePage() {
		// Services
		ServiceLoader kernelServiceLoader = KernelServiceRegistry.getServiceLoader();
		this.stepsService = kernelServiceLoader.getService(StepsService.class);
		this.heartRateService = kernelServiceLoader.getService(HeartRateService.class);
		this.powerService = kernelServiceLoader.getService(PowerService.class);
		this.notificationService = kernelServiceLoader.getService(NotificationService.class);

		/* Notifications */
		ImageWidget mail = new ImageWidget(WatchImageLoader.getAbsoluteImagePath(MAIL_PATH));
		mail.addClassSelector(SportWatchfaceDesktop.NO_BACKGROUND_CLASS);
		this.mailImage = mail;
		addChild(mail);

		ImageWidget alert = new ImageWidget(WatchImageLoader.getAbsoluteImagePath(ALERT_PATH)) {
			@Override
			public void render(GraphicsContext g) {
				if (isShown()) {
					super.render(g);
				}
			}
		};
		alert.addClassSelector(SportWatchfaceDesktop.NO_BACKGROUND_CLASS);
		this.alertImage = alert;
		addChild(alert);

		/* Heart rate */
		int heartRate = this.heartRateService.getCurrentHeartRate();
		VectorLabel bpm = new VectorLabel(Integer.toString(heartRate));
		bpm.addClassSelector(SportWatchfaceDesktop.STRINGS_CLASS);
		this.bpmLabel = bpm;
		addChild(bpm);

		/* Power Level */
		int powerValue = this.powerService.getPowerLevel();
		PowerLevelWidget power = new PowerLevelWidget();
		power.setLevel(powerValue);
		power.addClassSelector(SportWatchfaceDesktop.NO_BACKGROUND_CLASS);
		this.powerWidget = power;
		addChild(power);

		VectorLabel powerLevel = new VectorLabel(powerValue + PERCENT);
		powerLevel.addClassSelector(SportWatchfaceDesktop.STRINGS_CLASS);
		this.powerLevelLabel = powerLevel;
		addChild(powerLevel);

		/* Steps */
		CircularProgressIndicator progress = new CircularProgressIndicator(0, STEPS_VALUE_MAX,
				this.stepsService.getSteps(), PROGRESS_BAR_COLOR, PROGRESS_BAR_THICKNESS, true, false);
		progress.addClassSelector(SportWatchfaceDesktop.NO_BACKGROUND_CLASS);
		this.progressBar = progress;
		addChild(progress);

		VectorWatchface watchface = new SportWatchface();
		watchface.addClassSelector(SportWatchfaceDesktop.NO_BACKGROUND_CLASS);
		this.watchface = watchface;
		addChild(watchface);
	}

	@Override
	protected void layOutChildren(int contentWidth, int contentHeight) {
		final int width = Display.getDisplay().getWidth();
		final int height = Display.getDisplay().getHeight();
		final int xCenter = width / 2;
		final int yCenter = height / 2;

		layOutChild(this.mailImage, xCenter - MAIL_IMAGE_X_OFFSET_FROM_CENTER, yCenter - MAIL_IMAGE_HEIGHT / 2,
				MAIL_IMAGE_WIDTH, MAIL_IMAGE_HEIGHT);

		layOutChild(this.alertImage, xCenter - ALERT_IMAGE_X_OFFSET_FROM_CENTER,
				yCenter - ALERT_IMAGE_Y_OFFSET_FROM_CENTER, ALERT_IMAGE_WIDTH, ALERT_IMAGE_HEIGHT);

		layOutChild(this.bpmLabel, xCenter - BPM_LABEL_WIDTH / 2,
				yCenter + BPM_Y_OFFSET_FROM_CENTER - BPM_LABEL_HEIGHT / 2, BPM_LABEL_WIDTH, BPM_LABEL_HEIGHT);

		layOutChild(this.powerWidget, xCenter + POWER_IMAGE_X_OFFSET_FROM_CENTER,
				yCenter - POWER_IMAGE_Y_OFFSET_FROM_CENTER, POWER_IMAGE_WIDTH, POWER_IMAGE_HEIGHT);

		layOutChild(this.powerLevelLabel, xCenter + POWER_LEVEL_X_OFFSET_FROM_CENTER - POWER_LEVEL_LABEL_WIDTH / 2,
				yCenter - POWER_LEVEL_LABEL_HEIGHT / 2, POWER_LEVEL_LABEL_WIDTH, POWER_LEVEL_LABEL_HEIGHT);

		layOutChild(this.progressBar, xCenter - PROGRESS_BAR_X_OFFSET_FROM_CENTER,
				yCenter - PROGRESS_BAR_Y_OFFSET_FROM_CENTER, PROGRESS_BAR_DIAMETER, PROGRESS_BAR_DIAMETER);

		layOutChild(this.watchface, 0, 0, width, height);
	}

	private void updateAlertImageVisibility(boolean visible) {
		if (visible) {
			setShownChild(this.alertImage);
		} else {
			setHiddenChild(this.alertImage);
		}
	}

	@Override
	protected void onShown() {
		super.onShown();
		this.stepsService.setObserver(this);
		this.heartRateService.setObserver(this);
		this.powerService.setObserver(this);
		this.notificationService.setObserver(this);
	}

	@Override
	protected void onHidden() {
		super.onHidden();
		this.stepsService.unsetObserver(this);
		this.heartRateService.unsetObserver(this);
		this.powerService.unsetObserver(this);
		this.notificationService.unsetObserver(this);
	}

	@Override
	public void update() {
		MicroUI.callSerially(new Runnable() {

			@Override
			public void run() {
				SportWatchfacePage.this.progressBar.updateValue(SportWatchfacePage.this.stepsService.getSteps());

				int heartRate = SportWatchfacePage.this.heartRateService.getCurrentHeartRate();
				SportWatchfacePage.this.bpmLabel.updateText(Integer.toString(heartRate));

				SportWatchfacePage.this.powerLevelLabel
				.updateText(SportWatchfacePage.this.powerService.getPowerLevel() + PERCENT);
				SportWatchfacePage.this.powerWidget.setLevel(SportWatchfacePage.this.powerService.getPowerLevel());

				updateAlertImageVisibility(SportWatchfacePage.this.notificationService.needAlert());
			}
		});
	}

	@Override
	protected void setShownChildren() {
		for (Widget widget : getChildren()) {
			if (widget != this.alertImage || this.notificationService.needAlert()) {
				setShownChild(widget);
			}
		}
	}
}
