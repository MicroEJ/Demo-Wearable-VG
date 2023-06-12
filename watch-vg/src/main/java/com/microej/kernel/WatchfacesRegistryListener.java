/*
 * Java
 *
 * Copyright 2021-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.kernel;

/**
 * Listens to watchfaces registry events.
 */
public interface WatchfacesRegistryListener {

	/**
	 * Called when a watchface has been installed.
	 *
	 * @param watchfaceIndex
	 *            the index of the watchface which has been installed.
	 */
	void onWatchfaceInstalled(int watchfaceIndex);

	/**
	 * Called when a watchface has been uninstalled.
	 */
	void onWatchfaceUninstalled();
}
