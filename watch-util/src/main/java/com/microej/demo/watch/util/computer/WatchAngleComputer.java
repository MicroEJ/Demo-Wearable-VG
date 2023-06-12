/*
 * Java
 *
 * Copyright 2020-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.demo.watch.util.computer;

/**
 * A type that helps computing hand angles for watchfaces.
 */
public interface WatchAngleComputer {

	/**
	 * Gets the hour hand angle given an hour.
	 *
	 * @param hour
	 *            the hour to compute the angle for.
	 * @return the angle of the hour hand.
	 */
	float getHourAngle(float hour);

	/**
	 * Gets the minute hand angle given a minute.
	 *
	 * @param minute
	 *            the minute to compute the angle for.
	 * @return the angle of the minute hand.
	 */
	float getMinuteAngle(float minute);

	/**
	 * Gets the second hand angle given a second.
	 *
	 * @param second
	 *            the second to compute the angle for.
	 * @return the angle of the second hand.
	 */
	float getSecondAngle(float second);
}