/*
 * Java
 *
 * Copyright 2019-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.service;

import java.util.Random;

import com.microej.demo.watch.util.KernelServiceRegistry;
import com.microej.demo.watch.util.services.HeartRateService;

import ej.bon.Constants;
import ej.bon.Timer;
import ej.bon.TimerTask;
import ej.bon.XMath;
import ej.observable.SimpleObservable;

/**
 * Represents the Heart Rate activity of the user of the watch.
 */
public class HeartRateServiceImpl extends SimpleObservable implements HeartRateService {

	private static final int ARRAY_LENGTH = 20;

	private static final String MICROUI_PROPERTY_RUN_ON_S3 = "com.microej.library.microui.onS3"; //$NON-NLS-1$

	private static final int UPDATE_PERIOD = 1_000;

	private static final int BASE_HR = 160;

	private static final int MAX_AMPLITUDE = 20;

	private static final int MIN_HR = 40;

	private final Random random;

	private TimerTask timerTask;

	private int seedValue;

	private int currentValue;

	private int minValue;

	private int maxValue;

	/**
	 * Creates a Heart Rate service.
	 */
	public HeartRateServiceImpl() {
		this.seedValue = BASE_HR;
		this.currentValue = BASE_HR;
		this.minValue = this.currentValue;
		this.maxValue = this.currentValue;

		this.random = new Random();
	}

	@Override
	public int getCurrentHeartRate() {
		return this.currentValue;
	}

	@Override
	public int getMinimumHeartRate() {
		return this.minValue;
	}

	@Override
	public int getMaximumHeartRate() {
		return this.maxValue;
	}

	private int randomUpdate(int seed) {
		int offset = (int) (this.random.nextGaussian() * MAX_AMPLITUDE);
		return XMath.limit(seed + offset, MIN_HR, MAX_HR);
	}

	private void update() {
		if (Constants.getBoolean(MICROUI_PROPERTY_RUN_ON_S3)) {
			this.currentValue = XMath.limit(HeartRateServiceNative.getHeartRate(), MIN_HR, MAX_HR);
		} else {
			int previousValue = this.currentValue;
			this.currentValue = XMath.limit(randomUpdate(this.seedValue), MIN_HR, MAX_HR);
			if (Math.abs(previousValue - this.currentValue) > MAX_AMPLITUDE / 2) {
				this.seedValue = this.currentValue;
			}
		}

		if (this.currentValue > this.maxValue) {
			this.maxValue = this.currentValue;
		} else if (this.currentValue < this.minValue) {
			this.minValue = this.currentValue;
		}

		setChanged();
		notifyObserver();
	}

	@Override
	public float[] getData() {
		float[] data = new float[ARRAY_LENGTH];
		int seed = BASE_HR;
		int previousValue = seed;
		for (int i = 0; i < ARRAY_LENGTH; i++) {
			int value = randomUpdate(seed);
			data[i] = value;
			if (Math.abs(value - previousValue) > MAX_AMPLITUDE / 2) {
				seed = value;
			}
			previousValue = value;
		}
		return data;
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

}
