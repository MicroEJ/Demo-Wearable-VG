/*
 * Java
 *
 * Copyright 2021-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.demo.watch.util.services;

/**
 * Represents the notifications system of the watch.
 */
public interface NotificationService extends ObservableService {

	/**
	 * Gets the alert boolean value.
	 *
	 * @return the alert boolean.
	 */
	boolean needAlert();
}
