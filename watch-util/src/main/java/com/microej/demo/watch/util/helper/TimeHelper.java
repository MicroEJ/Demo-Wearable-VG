/*
 * Java
 *
 * Copyright 2020-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.demo.watch.util.helper;

import java.util.Calendar;

/**
 * An utility class that provides convenient methods for handling time.
 */
public class TimeHelper {

	private static final char HYPHEN = '-';

	private static final int PADDING_THRESHOLD = 10;

	private static final char PADDING = '0';

	private static final char COLON = ':';

	private static final char DOT = '.';

	private static final int SECONDS_IN_MINUTE = 60;

	private static final int MILLISECONDS_IN_SECOND = 1000;

	private static final int MINUTES_IN_HOUR = 60;

	private static final int MS_IN_MINUTES = MILLISECONDS_IN_SECOND * SECONDS_IN_MINUTE;

	private static final int MS_IN_HOUR = MS_IN_MINUTES * MINUTES_IN_HOUR;

	private static final int HOURS_IN_DAY = 24;

	private static final int MILLISECONDS_IN_DAY = HOURS_IN_DAY * MINUTES_IN_HOUR * SECONDS_IN_MINUTE
			* MILLISECONDS_IN_SECOND;

	private static final int CENTISECONDS_IN_SECOND = 100;

	private static final int MILLISECONDS_IN_CENTISECOND = 10;

	private TimeHelper() {
		// prevents instantiation.
	}

	/**
	 * Returns a string representation of the date for the specified time.
	 *
	 * <p>
	 * The pattern used is the international format defined by ISO (ISO 8601) : <code>"YYYY-MM-DD"</code> (see
	 * <code>SimpleDateFormat</code> documentation for date and time patterns).
	 *
	 * <p>
	 * Note that calling this method creates a new instance of {@link Calendar}.
	 *
	 * @param time
	 *            the time to format, in milliseconds since Epoch.
	 * @return the string representation of the specified time.
	 */
	public static String formatDate(long time) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		StringBuilder builder = new StringBuilder();

		builder.append(calendar.get(Calendar.YEAR));
		builder.append(HYPHEN);

		int month = calendar.get(Calendar.MONTH) + 1;
		pad(builder, month);
		builder.append(month);
		builder.append(HYPHEN);

		int day = calendar.get(Calendar.DAY_OF_MONTH);
		pad(builder, day);
		builder.append(day);

		return builder.toString();
	}

	/**
	 * Returns a string representation of the time for specified time.
	 *
	 * <p>
	 * The pattern used is <code>"hh:mm:ss.ss"</code> (see <code>SimpleDateFormat</code> documentation for date and time
	 * patterns).
	 *
	 * <p>
	 * Note that calling this method creates a new instance of {@link Calendar}.
	 *
	 * @param time
	 *            the time to format, in milliseconds since Epoch.
	 * @return the string representation of the specified time.
	 */
	public static String formatTime(long time) {
		StringBuilder builder = new StringBuilder();

		int hour = (int) computeHour(time);
		int minute = (int) computeMinute(time);
		int second = (int) computeSeconds(time);
		int centisecond = (int) computeCentiseconds(time);

		pad(builder, hour);
		builder.append(hour);
		builder.append(COLON);

		pad(builder, minute);
		builder.append(minute);
		builder.append(COLON);

		pad(builder, second);
		builder.append(second);
		builder.append(DOT);

		pad(builder, centisecond);
		builder.append(centisecond);

		return builder.toString();
	}

	/**
	 * Returns a string representation of the given elapsed time.
	 *
	 * <p>
	 * The pattern used is <code>"mm:ss.ss"</code> (see <code>SimpleDateFormat</code> documentation for date and time
	 * patterns).
	 *
	 * @param elapsed
	 *            the elapsed time to format, in milliseconds.
	 * @return the string representation of the specified time.
	 */
	public static String formatStopwatch(long elapsed) {
		StringBuilder builder = new StringBuilder();

		int seconds = (int) computeSeconds(elapsed);
		int minutes = (int) computeMinute(elapsed);
		int centiseconds = (int) computeCentiseconds(elapsed);

		pad(builder, minutes);
		builder.append(minutes);
		builder.append(COLON);

		seconds %= SECONDS_IN_MINUTE;
		pad(builder, seconds);
		builder.append(seconds);
		builder.append(DOT);

		centiseconds %= CENTISECONDS_IN_SECOND;
		pad(builder, centiseconds);
		builder.append(centiseconds);

		return builder.toString();
	}

	private static void pad(StringBuilder builder, int number) {
		if (number < PADDING_THRESHOLD) {
			builder.append(PADDING);
		}
	}

	/**
	 * Computes the centisecond given a time (milliseconds since Epoch).
	 *
	 * @param time
	 *            a time, in milliseconds since Epoch.
	 * @return the second value for the given time.
	 */
	public static float computeCentiseconds(long time) {
		return (float) (time % MILLISECONDS_IN_SECOND) / MILLISECONDS_IN_CENTISECOND;
	}

	/**
	 * Computes the second given a time (milliseconds since Epoch).
	 *
	 * @param time
	 *            a time, in milliseconds since Epoch.
	 * @return the second value for the given time.
	 */
	public static float computeSeconds(long time) {
		return (float) (time % MS_IN_MINUTES) / MILLISECONDS_IN_SECOND;
	}

	/**
	 * Computes the minute given a time (milliseconds since Epoch).
	 *
	 * @param time
	 *            a time, in milliseconds since Epoch.
	 * @return the minute value for the given time.
	 */
	public static float computeMinute(long time) {
		return (float) (time % MS_IN_HOUR) / MS_IN_MINUTES;
	}

	/**
	 * Computes the hour given a time (milliseconds since Epoch).
	 *
	 * @param time
	 *            a time, in milliseconds since Epoch.
	 * @return the hour value for the given time.
	 */
	public static float computeHour(long time) {
		return (float) (time % MILLISECONDS_IN_DAY) / MS_IN_HOUR;
	}

}
