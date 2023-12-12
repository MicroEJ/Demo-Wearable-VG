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

	/**
	 * Retrieves the current heart rate.
	 *
	 * @return the heart rate
	 */
	public static native int getHeartRate();

	private HeartRateServiceNative() {
		// Prevent instantiation
	}
}
