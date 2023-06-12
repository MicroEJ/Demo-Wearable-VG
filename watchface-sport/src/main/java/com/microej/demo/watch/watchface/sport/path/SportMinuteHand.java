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
 * A vector graphics that represents the minute hand of the sport watchface.
 */
public class SportMinuteHand extends Hand {

	/** Constant value for the relative x-coordinate of the center of rotation. */
	private static final float X_OFFSET = 13.5f;

	/** Constant value for the relative y-coordinate of the center of rotation. */
	private static final float Y_OFFSET = 175.5f;

	/**
	 * Creates the hand by describing its path.
	 */
	public SportMinuteHand() {
		super(VectorImage.getImage("/images/sport_minute.xml"), X_OFFSET, Y_OFFSET); //$NON-NLS-1$
	}
}
