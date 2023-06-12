/*
 * Java
 *
 * Copyright 2021-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.demo.watch.util.services;

import com.microej.demo.watch.util.WatchfaceApp;

/**
 * The navigation service allows to navigate between the pages of the watch.
 */
public interface NavigationService {

	/**
	 * Shows the main page (application list).
	 */
	void showMainPage();

	/**
	 * Shows the watchface carousel.
	 */
	void showWatchfacesCarousel();

	/**
	 * Registers the given watchface.
	 *
	 * @param watchface
	 *            the watchface to register.
	 */
	void registerWatchface(WatchfaceApp watchface);
}
