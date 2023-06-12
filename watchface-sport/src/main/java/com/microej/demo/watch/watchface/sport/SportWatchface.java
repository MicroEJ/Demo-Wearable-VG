/*
 * Java
 *
 * Copyright 2019-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.demo.watch.watchface.sport;

import com.microej.demo.watch.util.computer.QuarticWatchAngleComputer;
import com.microej.demo.watch.util.path.watchface.Hand;
import com.microej.demo.watch.util.widget.watchface.VectorWatchface;
import com.microej.demo.watch.watchface.sport.motion.WatchBounceMotion;
import com.microej.demo.watch.watchface.sport.path.SportHourHand;
import com.microej.demo.watch.watchface.sport.path.SportMinuteHand;
import com.microej.demo.watch.watchface.sport.path.SportSecondHand;

import ej.basictool.ArrayTools;
import ej.bon.Util;
import ej.motion.Motion;
import ej.mwt.animation.Animation;

/**
 * The "Sport" watchface, that displays vector hands and four dials (steps, heart rate, battery, notifications).
 */
public class SportWatchface extends VectorWatchface {

	private static final int HOUR_HAND_AMPLITUDE = 15;

	private static final int SECOND_HAND_AMPLITUDE = 35;

	private static final int MINUTE_HAND_AMPLITUDE = 25;

	private static final int SECOND_ANIMATION_DURATION = 1500;

	private static final int MINUTE_ANIMATION_DURATION = 900;

	private static final int HOUR_ANIMATION_DURATION = 1100;

	private static final int ANIMATION_STOP_VALUE = 10_000;

	/**
	 * Constructs the sport watchface.
	 */
	public SportWatchface() {
		setAngleComputer(new QuarticWatchAngleComputer());
		setAnimateOpening(true);
	}

	@Override
	protected Hand createHourHand() {
		return new SportHourHand();
	}

	@Override
	protected Hand createMinuteHand() {
		return new SportMinuteHand();
	}

	@Override
	protected Hand createSecondHand() {
		return new SportSecondHand();
	}

	@Override
	protected Animation[] createOpeningAnimation() {
		Animation[] animations = new Animation[0];

		animations = ArrayTools.add(animations, createHourAnimation());
		animations = ArrayTools.add(animations, createMinuteAnimation());
		animations = ArrayTools.add(animations, createSecondAnimation());

		return animations;
	}

	private Animation createHourAnimation() {
		final Motion motion = createMotion(HOUR_ANIMATION_DURATION, HOUR_HAND_AMPLITUDE);

		return new Animation() {

			@Override
			public boolean tick(long currentTimeMillis) {
				float ratio = getScaleRatio(motion, currentTimeMillis);
				getHourHand().setScaleRatio(ratio);
				return !motion.isFinished();
			}

		};
	}

	private Animation createMinuteAnimation() {
		final Motion motion = createMotion(MINUTE_ANIMATION_DURATION, MINUTE_HAND_AMPLITUDE);

		return new Animation() {

			@Override
			public boolean tick(long currentTimeMillis) {
				float ratio = getScaleRatio(motion, currentTimeMillis);
				getMinuteHand().setScaleRatio(ratio);
				return !motion.isFinished();
			}
		};

	}

	private Animation createSecondAnimation() {
		final Motion motion = createMotion(SECOND_ANIMATION_DURATION, SECOND_HAND_AMPLITUDE);

		return new Animation() {

			@Override
			public boolean tick(long currentTimeMillis) {
				final float ratio = getScaleRatio(motion, currentTimeMillis);
				getSecondHand().setScaleRatio(ratio);
				updateHandsAngle(Util.currentTimeMillis());
				return !motion.isFinished();
			}
		};

	}

	private Motion createMotion(long duration, int amplitude) {
		return new WatchBounceMotion(ANIMATION_STOP_VALUE, duration, amplitude);
	}

	private float getScaleRatio(final Motion motion, long currentTimeMillis) {
		return (float) motion.getValue(currentTimeMillis) / ANIMATION_STOP_VALUE;
	}

}
