/*
 * Java
 *
 * Copyright 2019-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.demo.watch.util.widget.watchface;

import com.microej.demo.watch.util.computer.LinearWatchAngleComputer;
import com.microej.demo.watch.util.computer.WatchAngleComputer;
import com.microej.demo.watch.util.helper.TimeHelper;

import ej.bon.Util;
import ej.microui.MicroUI;
import ej.microui.display.GraphicsContext;
import ej.mwt.Widget;
import ej.mwt.animation.Animation;
import ej.mwt.animation.Animator;
import ej.mwt.util.Size;

/**
 * An skeletal implementation of a widget that displays a watchface.
 *
 * <p>
 * Implementors of this class are required to define the hands rendering (hour, minute second).
 *
 * <p>
 * The animation of the hands starts immediately when the widget is shown.
 */
public abstract class AbstractWatchface extends Widget {

	private boolean animateOpening;

	private WatchAnimation openingAnimation;

	private Animation updateAnimation;

	/** The hour hand angle. */
	protected float hoursHandAngle;

	/** The minute hand angle. */
	protected float minutesHandAngle;

	/** The second hand angle. */
	protected float secondsHandAngle;

	/** The object that computes hour angles for the watchface. */
	protected WatchAngleComputer angleComputer;

	/**
	 * Constructs a new {@link AbstractWatchface}.
	 */
	public AbstractWatchface() {
		this.animateOpening = false;
		this.angleComputer = new LinearWatchAngleComputer();
	}

	@Override
	protected void computeContentOptimalSize(Size size) {
		// nothing to do:take available size
	}

	@Override
	protected void renderContent(GraphicsContext g, int contentWidth, int contentHeight) {
		g.removeBackgroundColor();
		renderHourHand(g);
		renderMinuteHand(g);
		renderSecondHand(g);
	}

	/**
	 * Renders the hour hand.
	 *
	 * @param g
	 *            the graphics context where to render the hand.
	 */
	protected abstract void renderHourHand(GraphicsContext g);

	/**
	 * Renders the minute hand.
	 *
	 * @param g
	 *            the graphics context where to render the hand.
	 */
	protected abstract void renderMinuteHand(GraphicsContext g);

	/**
	 * Renders the second hand.
	 *
	 * @param g
	 *            the graphics context where to render the hand.
	 */
	protected abstract void renderSecondHand(GraphicsContext g);

	/**
	 * Creates the watchface opening animation, as a list of animations (i.e one for animating each hand).
	 *
	 * @return the list of animations that compose the opening animation of the watchface.
	 */
	protected Animation[] createOpeningAnimation() {
		return new Animation[0];
	}

	/**
	 * Sets the angle computer to use for computing hand angles in this watchface.
	 *
	 * <p>
	 * By default, the {@link LinearWatchAngleComputer} will be used.
	 *
	 * @param angleComputer
	 *            the angle computer.
	 */
	protected void setAngleComputer(WatchAngleComputer angleComputer) {
		this.angleComputer = angleComputer;
	}

	/**
	 * Gets the angle computer, that computes the hand angles in this watchface.
	 *
	 * @return the angle computer.
	 */
	protected WatchAngleComputer getAngleComputer() {
		return this.angleComputer;
	}

	/**
	 * Sets whether the opening of this watchface should be animated or not.
	 *
	 * @param animate
	 *            <code>true</code> to animate the watchface opening, <code>false</code> otherwise.
	 */
	protected void setAnimateOpening(boolean animate) {
		this.animateOpening = animate;
	}

	/**
	 * Gets whether the watchface hands are animated on opening.
	 *
	 * @return <code>true</code> if the watchface is animated, <code>false</code> otherwise.
	 */
	protected boolean isAnimateOpening() {
		return this.animateOpening;
	}

	/**
	 * Updates the hands angle, given the specified time.
	 *
	 * @param time
	 *            the time to represent with the hands, in milliseconds since Epoch.
	 */
	protected void updateHandsAngle(long time) {
		WatchAngleComputer angleComputer = this.angleComputer;

		float hours = TimeHelper.computeHour(time);
		setHourAngle(angleComputer.getHourAngle(hours));

		float minutes = TimeHelper.computeMinute(time);
		setMinuteAngle(angleComputer.getMinuteAngle(minutes));

		float seconds = TimeHelper.computeSeconds(time);
		setSecondAngle(angleComputer.getSecondAngle(seconds));
	}

	/**
	 * Sets the angle for the hour hand.
	 *
	 * @param angle
	 *            the angle in degrees.
	 */
	protected void setHourAngle(float angle) {
		this.hoursHandAngle = angle;
	}

	/**
	 * Sets the angle for the minute hand.
	 *
	 * @param angle
	 *            the angle in degrees.
	 */
	protected void setMinuteAngle(float angle) {
		this.minutesHandAngle = angle;
	}

	/**
	 * Sets the angle for the second hand.
	 *
	 * @param angle
	 *            the angle in degrees.
	 */
	protected void setSecondAngle(float angle) {
		this.secondsHandAngle = angle;
	}

	@Override
	protected void onShown() {
		super.onShown();
		if (isAnimateOpening()) {
			startOpeningAnimation();
		} else {
			startUpdateAnimation();
		}
	}

	@Override
	protected void onHidden() {
		stopAnimations();
		super.onHidden();
	}

	private void stopAnimations() {
		Animator animator = getDesktop().getAnimator();

		WatchAnimation openingAnimation = this.openingAnimation;
		if (openingAnimation != null) {
			animator.stopAnimation(openingAnimation);
			this.openingAnimation = null;
		}

		Animation updateAnimation = this.updateAnimation;
		if (updateAnimation != null) {
			animator.stopAnimation(updateAnimation);
			this.updateAnimation = null;
		}
	}

	private void startOpeningAnimation() {
		stopAnimations();
		final Animation[] animations = createOpeningAnimation();
		WatchAnimation animation = new WatchAnimation(animations, Util.platformTimeMillis());
		this.openingAnimation = animation;
		getDesktop().getAnimator().startAnimation(animation);
	}

	private class WatchAnimation implements Animation {

		private final Animation[] animations;
		private final long startingTime;

		WatchAnimation(Animation[] animations, long startingTime) {
			this.animations = animations.clone();
			this.startingTime = startingTime;
		}

		@Override
		public boolean tick(long currentTimeMillis) {
			boolean finish = true;

			long elapsed = currentTimeMillis - this.startingTime;
			for (Animation animation : this.animations) {
				finish &= !animation.tick(elapsed);
			}

			requestRender();

			if (finish) {
				MicroUI.callSerially(new Runnable() {
					@Override
					public void run() {
						startUpdateAnimation();
					}
				});
			}

			return !finish;
		}
	}

	private void startUpdateAnimation() {
		stopAnimations();

		Animation animation = new Animation() {

			@Override
			public boolean tick(long currentTimeMillis) {
				updateHandsAngle(Util.currentTimeMillis());
				requestRender();
				return true;
			}
		};

		this.updateAnimation = animation;
		getDesktop().getAnimator().startAnimation(animation);
	}
}
