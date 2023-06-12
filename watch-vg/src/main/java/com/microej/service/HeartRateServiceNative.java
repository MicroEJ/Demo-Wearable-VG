/*
 * Java
 *
 * Copyright 2020-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.service;

import java.util.Random;

/**
 * Groups native methods for {@link HeartRateServiceImpl}.
 */
public class HeartRateServiceNative {

	private static final int MIN_HEART_RATE = 80;
	private static final int MAX_HEART_RATE = 200;

	private static Random random = new Random();

	/**
	 * Retrieves the current heart rate.
	 *
	 * @return the heart rate
	 */
	public static int getHeartRate() {
		return MIN_HEART_RATE + random.nextInt(MAX_HEART_RATE - MIN_HEART_RATE);
	}

	private HeartRateServiceNative() {
		// Prevent instantiation
	}
}
