/*
 * Java
 *
 * Copyright 2021-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.demo.watch.util.services;

/**
 * Represents the Heart Rate activity of the user of the watch.
 */
public interface HeartRateService extends ObservableService {

	/** The maximum possible heart rate value. */
	int MAX_HR = 225;

	/**
	 * Gets the current heart rate.
	 *
	 * @return the current heart rate value.
	 */
	int getCurrentHeartRate();

	/**
	 * Gets the minimum heart rate value.
	 *
	 * @return the minimum heart heart value.
	 */
	int getMinimumHeartRate();

	/**
	 * Gets the maximum heart rate value.
	 *
	 * @return the maximum heart heart value.
	 */
	int getMaximumHeartRate();

	/**
	 * Gets the heart rate data.
	 *
	 * <p>
	 * This method returns an array of pseudo-random heart rate values.
	 *
	 * @return an array of random heart rate values.
	 */
	public float[] getData();
}
