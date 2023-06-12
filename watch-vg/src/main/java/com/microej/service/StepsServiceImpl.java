/*
 * Java
 *
 * Copyright 2019-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.service;

import java.util.Random;

import com.microej.demo.watch.util.KernelServiceRegistry;
import com.microej.demo.watch.util.services.StepsService;

import ej.bon.Timer;
import ej.bon.TimerTask;
import ej.observable.SimpleObservable;

/**
 * Represents the activity of the user of the watch.
 */
public class StepsServiceImpl extends SimpleObservable implements StepsService {
	private static final int STEP_INCREMENT = 14;
	private static final int STEPS_GOAL = 250;
	private static final int PERIOD = 2_000;

	private int step;

	private TimerTask timerTask;
	private final Random random;

	/**
	 * Creates an activity service.
	 */
	public StepsServiceImpl() {
		this.step = 0;
		this.random = new Random();
	}

	private void randomUpdate() {
		this.step = updateValue(this.step, STEP_INCREMENT, STEPS_GOAL);
		setChanged();
		notifyObserver();
	}

	private int updateValue(int value, int increment, int goal) {
		value += this.random.nextInt(increment);
		if (value > goal) {
			value -= goal;
		}
		return value;
	}

	@Override
	public int getSteps() {
		return this.step;
	}

	@Override
	public int getStepsGoal() {
		return STEPS_GOAL;
	}

	@Override
	public void start() {
		stop();
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
