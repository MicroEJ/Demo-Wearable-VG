/*
 * Java
 *
 * Copyright 2020-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.widget.compass;

import com.microej.path.compass.CompassArrow;
import com.microej.path.compass.CompassDirection;

import ej.microui.display.GraphicsContext;
import ej.mwt.Widget;
import ej.mwt.util.Size;

/**
 * Widget representing the compass markers. It is composed of the marker that indicates the direction and of the four
 * markers that indicate the collateral points (SW, SE, NW and NE).
 */
public class CompassMarkers extends Widget {

	/** According compass' center image */
	private static final int ARROW_DIAMETER = 153;

	private CompassArrow arrows;
	private CompassDirection direction;

	@Override
	protected void computeContentOptimalSize(Size size) {
		// Take all available space
	}

	@Override
	protected void renderContent(GraphicsContext g, int contentWidth, int contentHeight) {
		assert this.arrows != null;
		assert this.direction != null;

		this.arrows.render(g);
		this.direction.render(g);
	}

	@Override
	protected void onLaidOut() {
		super.onLaidOut();

		int compassCenterX = getWidth() / 2;
		int compassCenterY = getHeight() / 2;

		this.direction = new CompassDirection();
		this.direction.setCenter(compassCenterX, compassCenterY);
		this.arrows = new CompassArrow(ARROW_DIAMETER);
		this.arrows.setCenter(compassCenterX, compassCenterY);
	}

	@Override
	protected void onDetached() {
		super.onDetached();

		CompassArrow arrows = this.arrows;
		if (arrows != null) {
			this.arrows = null;
		}

		CompassDirection direction = this.direction;
		if (direction != null) {
			this.direction = null;
		}

	}

	@Override
	public boolean isTransparent() {
		return false;
	}
}
