/*
 * Java
 *
 * Copyright 2019-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.service;

import com.microej.demo.watch.util.KernelServiceRegistry;
import com.microej.demo.watch.util.services.NotificationService;

import ej.bon.Timer;
import ej.bon.TimerTask;
import ej.observable.SimpleObservable;

/**
 * Represents the notifications system of the watch.
 */
public class NotificationServiceImpl extends SimpleObservable implements NotificationService {
	private static final double RANDOM_UPDATE_THRESHOLD = 0.85;

	private static final int PERIOD = 5_000;

	private boolean alert;

	private TimerTask timerTask;

	/**
	 * Creates a notification service.
	 */
	public NotificationServiceImpl() {
		this.alert = false;
	}

	private void randomUpdate() {
		if (Math.random() > RANDOM_UPDATE_THRESHOLD) {
			this.alert = !this.alert;
			setChanged();
			notifyObserver();
		}
	}

	@Override
	public boolean needAlert() {
		return this.alert;
	}

	@Override
	public void start() {
		this.timerTask = new TimerTask() {
			@Override
			public void run() {
				randomUpdate();
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
