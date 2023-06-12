/*
 * Java
 *
 * Copyright 2021-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.demo.watch.util.services;

/**
 * Represents the power service of the watch.
 */
public interface PowerService extends ObservableService {

	/**
	 * Gets the powerLevel.
	 *
	 * @return the powerLevel.
	 */
	int getPowerLevel();
}
