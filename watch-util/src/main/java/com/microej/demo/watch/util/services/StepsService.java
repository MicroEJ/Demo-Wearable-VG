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
public interface StepsService extends ObservableService {

	/**
	 * Gets the step.
	 *
	 * @return the step.
	 */
	int getSteps();

	/**
	 * Gets the steps goal.
	 *
	 * @return the step goal.
	 */
	int getStepsGoal();
}
