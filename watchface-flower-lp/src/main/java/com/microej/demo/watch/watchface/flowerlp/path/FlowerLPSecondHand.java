/*
 * Java
 *
 * Copyright 2020-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.demo.watch.watchface.flowerlp.path;

import com.microej.demo.watch.util.path.watchface.Hand;

import ej.microvg.VectorImage;

/**
 * A vector graphics that represents the second hand of the flowerlp watchface.
 */
public class FlowerLPSecondHand extends Hand {

	/** Constant value for the relative x-coordinate of the center of rotation. */
	private static final int X_OFFSET = 2;

	/** Constant value for the relative y-coordinate of the center of rotation. */
	private static final int Y_OFFSET = 170;

	/**
	 * Creates the hand by describing its path.
	 */
	public FlowerLPSecondHand() {
		super(VectorImage.getImage("/images/flowerlp_second.svg"), X_OFFSET, Y_OFFSET); //$NON-NLS-1$
	}
}