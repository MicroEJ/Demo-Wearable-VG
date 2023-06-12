/*
 * Java
 *
 * Copyright 2020-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.demo.watch.watchface.sport.path;

import com.microej.demo.watch.util.path.watchface.Hand;

import ej.microvg.VectorImage;

/**
 * A vector graphics that represents the hour hand of the sport watchface.
 */
public class SportHourHand extends Hand {

	/** Constant value for the relative x-coordinate of the center of rotation. */
	private static final float X_OFFSET = 14.5f;

	/** Constant value for the relative y-coordinate of the center of rotation. */
	private static final float Y_OFFSET = 124.5f;

	/**
	 * Creates the hand by describing its path.
	 */
	public SportHourHand() {
		super(VectorImage.getImage("/images/sport_hour.xml"), X_OFFSET, Y_OFFSET); //$NON-NLS-1$
	}
}
