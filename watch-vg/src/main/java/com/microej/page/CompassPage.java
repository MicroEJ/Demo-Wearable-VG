/*
 * Java
 *
 * Copyright 2019-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.page;

import com.microej.demo.nls.Messages;
import com.microej.demo.watch.util.widget.basic.VectorLabel;
import com.microej.helper.MainListPositions;
import com.microej.style.ClassSelectors;
import com.microej.widget.compass.CompassMarkers;
import com.microej.widget.compass.CompassWidget;
import com.microej.widget.container.LayeredContainer;

import ej.mwt.animation.Animation;
import ej.widget.basic.ImagePath;

/**
 * A page that shows a Compass.
 */
public class CompassPage extends SimplePage {
	private static final String IMAGE_PATH = "compass/"; //$NON-NLS-1$
	private static final String BACKGROUND_IMAGE_PATH = IMAGE_PATH + "circle_rotate_black"; //$NON-NLS-1$
	private static final String FOREGROUND_IMAGE_PATH = IMAGE_PATH + "compass-center"; //$NON-NLS-1$

	private static final int NORTH_ANGLE_LOW = 23;
	private static final int NORTH_ANGLE_HIGH = 337;
	private static final int NORTH_EAST_ANGLE = 68;
	private static final int EAST_ANGLE = 115;
	private static final int SOUTH_EAST_ANGLE = 158;
	private static final int SOUTH_ANGLE = 203;
	private static final int SOUTH_WEST_ANGLE = 248;
	private static final int WEST_ANGLE = 291;

	private final CompassWidget compass;
	private final CompassMarkers markers;
	private final VectorLabel angleLabel;

	private final ImagePath centerImage;
	private Animation animation;
	private final LayeredContainer layers;

	/**
	 * Simple constructor.
	 */
	public CompassPage() {
		super(MainListPositions.COMPASS);

		LayeredContainer container = new LayeredContainer();
		container.addClassSelector(ClassSelectors.NO_BACKGROUND_OPAQUE);

		this.centerImage = new ImagePath(FOREGROUND_IMAGE_PATH);
		this.centerImage.addClassSelector(ClassSelectors.COMPASS_CENTER);
		this.centerImage.addClassSelector(ClassSelectors.NO_BACKGROUND_OPAQUE);
		container.addChild(this.centerImage);

		// Compass widget (rotating part)
		this.compass = new CompassWidget(BACKGROUND_IMAGE_PATH);
		this.compass.addClassSelector(ClassSelectors.NO_BACKGROUND_OPAQUE);
		container.addChild(this.compass);

		// Compass markers (gray arrows and orange marker)
		this.markers = new CompassMarkers();
		this.markers.addClassSelector(ClassSelectors.NO_BACKGROUND_OPAQUE);
		container.addChild(this.markers);

		// Angle label
		this.angleLabel = new VectorLabel(getCompassText(this.compass.getAngle()));
		this.angleLabel.addClassSelector(ClassSelectors.COMPASS_ANGLE_LABEL);
		this.angleLabel.addClassSelector(ClassSelectors.NO_BACKGROUND_OPAQUE);
		container.addChild(this.angleLabel);

		this.layers = container;
		addChild(container);
	}

	private String getCompassText(int currentAngle) {
		StringBuilder text = new StringBuilder();
		int cardinalPointMessage;
		if (currentAngle < NORTH_ANGLE_LOW || currentAngle > NORTH_ANGLE_HIGH) {
			cardinalPointMessage = Messages.North;
		} else if (currentAngle < NORTH_EAST_ANGLE) {
			cardinalPointMessage = Messages.NorthEast;
		} else if (currentAngle < EAST_ANGLE) {
			cardinalPointMessage = Messages.East;
		} else if (currentAngle < SOUTH_EAST_ANGLE) {
			cardinalPointMessage = Messages.SouthEast;
		} else if (currentAngle < SOUTH_ANGLE) {
			cardinalPointMessage = Messages.South;
		} else if (currentAngle < SOUTH_WEST_ANGLE) {
			cardinalPointMessage = Messages.SouthWest;
		} else if (currentAngle < WEST_ANGLE) {
			cardinalPointMessage = Messages.West;
		} else {
			cardinalPointMessage = Messages.NorthWest;
		}
		text.append(Messages.NLS.getMessage(cardinalPointMessage));
		text.append(' ');
		text.append(currentAngle);
		return text.toString();
	}

	@Override
	protected void onShown() {
		super.onShown();
		Animation animation = new Animation() {

			@Override
			public boolean tick(long currentTimeMillis) {
				String compassText = getCompassText(CompassPage.this.compass.getAngle());
				CompassPage.this.angleLabel.updateText(compassText);
				CompassPage.this.layers.requestRender();
				return true;
			}
		};
		this.animation = animation;
		getDesktop().getAnimator().startAnimation(animation);
	}

	@Override
	protected void onHidden() {
		getDesktop().getAnimator().stopAnimation(this.animation);
		super.onHidden();
	}

}
