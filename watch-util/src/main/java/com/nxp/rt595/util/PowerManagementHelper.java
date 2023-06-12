/*
 * Java
 *
 * Copyright 2021-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */

package com.nxp.rt595.util;

import com.microej.demo.watch.util.KernelServiceRegistry;
import com.microej.demo.watch.util.services.ActivityService;
import com.microej.demo.watch.util.services.HeartRateService;
import com.microej.demo.watch.util.services.NotificationService;
import com.microej.demo.watch.util.services.ObservableService;
import com.microej.demo.watch.util.services.PowerService;
import com.microej.demo.watch.util.services.StepsService;

import ej.service.ServiceLoader;

/**
 * Utility class which controls BSP performance profiles. The performance
 * profiles are aligned with BSP defines.
 */
public class PowerManagementHelper {

	/** The constant for the maximum performance profile. */
	public static final int PERF_PROFILE_MAX_PERFS = 0xFF;

	/** The constant for the power saving performance profile. */
	public static final int PERF_PROFILE_POWER_SAVING = 0x00;

	// on startup the maximum power profile is used.
	private static int currentProfile = PERF_PROFILE_MAX_PERFS;

	private PowerManagementHelper() {
		// Prevent instantiation
	}

	/**
	 * Initializes the power manager and set maximum performances profile by
	 * default.
	 */
	public static void initialize() {
		applyPerfProfile(PERF_PROFILE_MAX_PERFS);
	}

	/**
	 * Configures chip performance profile. Given profile is a value between
	 * {@link #PERF_PROFILE_MAX_PERFS} and {@link #PERF_PROFILE_POWER_SAVING}.
	 *
	 * @param profile
	 *            selected performance profile
	 * @throws IllegalArgumentException
	 *             when profile is not an expected value.
	 */
	public static synchronized void setPerfProfile(int profile) {

		// Execute only if profile is changed
		if (profile != currentProfile) {
			applyPerfProfile(profile);
		}
	}

	private static void applyPerfProfile(int profile) {

		// Start/Stop services depending of profile
		switch (profile) {
		case PERF_PROFILE_MAX_PERFS:

			startService(StepsService.class);
			startService(HeartRateService.class);
			startService(PowerService.class);
			startService(NotificationService.class);
			startService(ActivityService.class);
			break;

		case PERF_PROFILE_POWER_SAVING:

			stopService(StepsService.class);
			stopService(HeartRateService.class);
			stopService(PowerService.class);
			stopService(NotificationService.class);
			stopService(ActivityService.class);
			break;

		default:
			throw new IllegalArgumentException();
		}

		currentProfile = profile;
	}

	private static <T extends ObservableService> void stopService(Class<T> serviceClass) {
		ServiceLoader kernelServiceLoader = KernelServiceRegistry.getServiceLoader();
		T service = kernelServiceLoader.getService(serviceClass);
		service.stop();
	}

	private static <T extends ObservableService> void startService(Class<T> serviceClass) {
		ServiceLoader kernelServiceLoader = KernelServiceRegistry.getServiceLoader();
		T service = kernelServiceLoader.getService(serviceClass);
		service.start();
	}

}
