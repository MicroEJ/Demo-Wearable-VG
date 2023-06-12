/*
 * Java
 *
 * Copyright 2019-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.service;

import com.microej.demo.watch.util.KernelServiceRegistry;
import com.microej.demo.watch.util.services.PowerService;

import ej.bon.Constants;
import ej.bon.Timer;
import ej.bon.TimerTask;
import ej.observable.SimpleObservable;

/**
 * Represents the power service of the watch.
 */
public class PowerServiceImpl extends SimpleObservable implements PowerService {

	private static final String MICROUI_PROPERTY_RUN_ON_S3 = "com.microej.library.microui.onS3"; //$NON-NLS-1$

	private static final double RANDOM_UPDATE_THRESHOLD = 0.2;
	private static final int MAX_BATTERY_LEVEL = 100;
	private static final int PERIOD = 2_000;

	private int powerLevel;

	private TimerTask timerTask;

	/**
	 * Creates a power service.
	 */
	public PowerServiceImpl() {
		this.powerLevel = MAX_BATTERY_LEVEL;
	}

	private void update() {
		if (Constants.getBoolean(MICROUI_PROPERTY_RUN_ON_S3)) {
			this.powerLevel = PowerServiceNative.getPowerLevel();
		} else {
			if (Math.random() > RANDOM_UPDATE_THRESHOLD) {
				this.powerLevel--;
				if (this.powerLevel < 0) {
					this.powerLevel = MAX_BATTERY_LEVEL;
				}
				setChanged();
				notifyObserver();
			}
		}
	}

	@Override
	public int getPowerLevel() {
		return this.powerLevel;
	}

	@Override
	public void start() {
		stop();
		this.timerTask = new TimerTask() {
			@Override
			public void run() {
				update();
			}
		};
		Timer timer = KernelServiceRegistry.getServiceLoader().getService(Timer.class);
		timer.schedule(this.timerTask, PERIOD, PERIOD);
	}

	@Override
	public void stop() {
		TimerTask task = this.timerTask;
		if (task != null) {
			task.cancel();
			this.timerTask = null;
		}
	}
}
