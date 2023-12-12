/*
 * Java
 *
 * Copyright 2020-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.service;

import java.util.Random;

/**
 * Groups native methods for {@link PowerServiceImpl}.
 */
public class PowerServiceNative {

	private static final int MAX_POWER_LEVEL = 100;
	private static final int MIN_POWER_LEVEL = 0;
	private static final int INITIAL_BATTERY_LEVEL = 75;

	private static int batteryLevel = INITIAL_BATTERY_LEVEL;
	private static Random random = new Random();

	private PowerServiceNative() {
		// Prevent instantiation
	}

	/**
	 * Retrieves the current power level.
	 *
	 * @return the percentage of battery charge
	 */
	public static native int getPowerLevel();

	/**
	 * Tells whether the battery is charging.
	 *
	 * @return true if the battery is charging, false otherwise
	 */
	public static native boolean isCharging();
}
