/*
 * Java
 *
 * Copyright 2021-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.demo.watch.util.services;

/**
 * Represents the activity of the user of the watch.
 */
public interface ActivityService extends ObservableService {

	/** The constant for the minimum calories count. */
	public static final int MIN_CALORIES = 0;

	/** The constant for the maximum calories count. */
	public static final int MAX_CALORIES = 4000;

	/**
	 * Gets the calories count.
	 *
	 * @return the calories count.
	 */
	public int getCalories();
}
