/*
 * Java
 *
 * Copyright 2021-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.demo.watch.util;

import ej.microui.display.GraphicsContext;

/**
 * Represents a watchface app.
 */
public interface WatchfaceApp {

	/**
	 * Gets the name of this watchface app.
	 *
	 * @return the name of this watchface app.
	 */
	String getName();

	/**
	 * Draw the preview of this watchface app on the given graphics context.
	 *
	 * @param g
	 *            the graphics context to draw on.
	 */
	void drawPreview(GraphicsContext g);

	/**
	 * Starts this watchface app.
	 */
	void start();

	/**
	 * Stops this watchface app.
	 */
	void stop();
}
