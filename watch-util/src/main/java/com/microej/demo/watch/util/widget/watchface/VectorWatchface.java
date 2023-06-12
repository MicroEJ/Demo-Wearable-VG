/*
 * Java
 *
 * Copyright 2019-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.demo.watch.util.widget.watchface;

import com.microej.demo.watch.util.path.watchface.Hand;

import ej.bon.Util;
import ej.microui.display.GraphicsContext;

/**
 * A widget that displays a watchface, using vector graphics for representing the hour, minute and second hands.
 */
public abstract class VectorWatchface extends AbstractWatchface {

	private Hand hourHand;

	private Hand minuteHand;

	private Hand secondHand;

	/**
	 * Creates the vector graphics that represents the hour vector hand.
	 *
	 * @return the hour hand.
	 */
	protected abstract Hand createHourHand();

	/**
	 * Creates the vector graphics that represents the minute vector hand.
	 *
	 * @return the minute hand.
	 */
	protected abstract Hand createMinuteHand();

	/**
	 * Creates the vector graphics that represents the second vector hand.
	 *
	 * @return the second hand.
	 */
	protected abstract Hand createSecondHand();

	@Override
	protected void renderHourHand(GraphicsContext g) {
		this.hourHand.render(g);
	}

	@Override
	protected void renderMinuteHand(GraphicsContext g) {
		this.minuteHand.render(g);

	}

	@Override
	protected void renderSecondHand(GraphicsContext g) {
		this.secondHand.render(g);

	}

	@Override
	protected void onDetached() {
		super.onDetached();
		closeHands();
	}

	private void closeHands() {
		closeHand(this.hourHand);
		this.hourHand = null;

		closeHand(this.minuteHand);
		this.minuteHand = null;

		closeHand(this.secondHand);
		this.secondHand = null;
	}

	/**
	 * Closes the specified hand and frees any allocated memory used for its matrix.
	 *
	 * @param hand
	 *            the vector hand to close.
	 */
	protected void closeHand(Hand hand) {
		// Not implemented.
	}

	@Override
	protected void onLaidOut() {
		super.onLaidOut();
		createHands();
		updateHandsAngle(Util.currentTimeMillis());
	}

	private void createHands() {
		closeHands();

		int centerX = getWidth() / 2;
		int centerY = getHeight() / 2;

		Hand hand = createHourHand();
		hand.setRotationCenter(centerX, centerY);
		this.hourHand = hand;

		hand = createMinuteHand();
		hand.setRotationCenter(centerX, centerY);
		this.minuteHand = hand;

		hand = createSecondHand();
		hand.setRotationCenter(centerX, centerY);
		this.secondHand = hand;
	}

	@Override
	protected void setHourAngle(float angle) {
		super.setHourAngle(angle);
		setHandAngle(this.hourHand, angle);
	}

	@Override
	protected void setMinuteAngle(float angle) {
		super.setMinuteAngle(angle);
		setHandAngle(this.minuteHand, angle);
	}

	@Override
	protected void setSecondAngle(float angle) {
		super.setSecondAngle(angle);
		setHandAngle(this.secondHand, angle);
	}

	private void setHandAngle(Hand hand, float angle) {
		if (hand != null) {
			hand.setAngle(angle);
		}
	}

	/**
	 * Returns the vector graphics that represents the hour hand.
	 *
	 * @return the hour hand.
	 */
	protected Hand getHourHand() {
		return this.hourHand;
	}

	/**
	 * Returns the vector graphics that represents the minute hand.
	 *
	 * @return the minute hand.
	 */
	protected Hand getMinuteHand() {
		return this.minuteHand;
	}

	/**
	 * Returns the vector graphics that represents the second hand.
	 *
	 * @return the second hand.
	 */
	protected Hand getSecondHand() {
		return this.secondHand;
	}

}
