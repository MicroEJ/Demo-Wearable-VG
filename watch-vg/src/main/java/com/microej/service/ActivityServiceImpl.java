/*
 * Java
 *
 * Copyright 2019-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.service;

import java.util.Random;

import com.microej.demo.watch.util.KernelServiceRegistry;
import com.microej.demo.watch.util.services.ActivityService;

import ej.bon.Timer;
import ej.bon.TimerTask;
import ej.bon.XMath;
import ej.observable.SimpleObservable;

/**
 * A service that allows the client classes to retrieve the activity counters.
 */
public class ActivityServiceImpl extends SimpleObservable implements ActivityService {

	private static final int MAX_CALORIES_STEP = 1000;

	private static final int UPDATE_PERIOD = 2000;

	private final Random random;

	private TimerTask timerTask;

	private int currentCalories;

	/**
	 * Creates an activity service.
	 */
	public ActivityServiceImpl() {
		this.random = new Random();

		this.currentCalories = MIN_CALORIES;
	}

	/**
	 * Gets the calories count.
	 *
	 * @return the calories count.
	 */
	@Override
	public int getCalories() {
		return this.currentCalories;
	}

	@Override
	public void start() {
		stop();

		this.timerTask = new TimerTask() {
			@Override
			public void run() {
				updateCounters();
			}
		};

		Timer timer = KernelServiceRegistry.getServiceLoader().getService(Timer.class);
		timer.schedule(this.timerTask, UPDATE_PERIOD, UPDATE_PERIOD);
	}

	@Override
	public void stop() {
		TimerTask task = this.timerTask;
		if (task != null) {
			task.cancel();
			this.timerTask = null;
		}
	}

	private void updateCounters() {
		updateCalories();
		setChanged();
		notifyObserver();
	}

	private void updateCalories() {
		int calories = this.currentCalories;
		if (calories < MAX_CALORIES) {
			int step = this.random.nextInt(MAX_CALORIES_STEP);
			this.currentCalories = XMath.limit(calories + step, MIN_CALORIES, MAX_CALORIES);
		} else {
			this.currentCalories = 0;
		}
	}
}
