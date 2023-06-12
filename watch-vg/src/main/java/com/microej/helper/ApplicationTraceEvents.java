/*
 * Java
 *
 * Copyright 2019-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */

package com.microej.helper;

import com.microej.page.ActivityPage;
import com.microej.page.CompassPage;
import com.microej.page.EmptyPage;
import com.microej.page.HeartRatePage;
import com.microej.page.MainList;
import com.microej.page.ParametersPage;
import com.microej.page.VectorCircleArcPage;
import com.microej.page.VectorFontPage;
import com.microej.page.VectorMascotPage;
import com.microej.page.WatchfacesCarouselPage;
import com.microej.widget.activity.ActivityPageBackground;

import ej.mwt.Widget;

/**
 * An utility class for defining traces identifiers.
 */
public class ApplicationTraceEvents {

	private ApplicationTraceEvents() {
		// prevents instantiation
	}

	/** Constant value for the event of drawing the ActivityPage. */
	public static final int DRAW_ACTIVITY_PAGE = 0;

	/** Constant value for the event of drawing the AnimatedWatchface. */
	public static final int DRAW_ANIMATED_WATCHFACE_PAGE = 1;

	/** Constant value for the event of drawing the CompassPage. */
	public static final int DRAW_COMPASS_PAGE = 2;

	/** Constant value for the event of drawing the CompleteWatchface. */
	public static final int DRAW_COMPLETE_WATCHFACE_PAGE = 3;

	/** Constant value for the event of drawing the HeartRatePage. */
	public static final int DRAW_HEART_RATE_PAGE = 4;

	/** Constant value for the event of drawing the MainList. */
	public static final int DRAW_MAIN_LIST_PAGE = 5;

	/** Constant value for the event of drawing the MessagePage. */
	public static final int DRAW_MESSAGE_PAGE = 6;

	/** Constant value for the event of drawing the WatchfacesCarouselPage. */
	public static final int DRAW_WATCHFACES_CAROUSEL_PAGE = 7;

	/** Constant value for the event of drawing the SimplePage. */
	public static final int DRAW_SIMPLE_PAGE = 8;

	private static final int TRACE_WATCHFACES_CAROUSEL_ID = 53;
	private static final int TRACE_MAINLIST_ID = 54;
	private static final int TRACE_HEARTRATE_ID = 55;
	private static final int TRACE_ACTIVITY_ID = 56;
	private static final int TRACE_COMPASS_ID = 57;
	private static final int TRACE_MASCOT_ID = 59;
	private static final int TRACE_CIRCLE_ARC_ID = 60;
	private static final int TRACE_VECTOR_FONT_ID = 61;
	private static final int TRACE_PARAMETERS_ID = 62;
	private static final int TRACE_EMPTY_ID = 99;

	/**
	 * Gets the event relative to the class of the object to trace.
	 *
	 * @param page
	 *            Object to trace
	 * @return The trace event id corresponding to the object's class
	 */
	public static int getEventId(Widget page) { // NOSONAR The cyclomatic complexity of this method is greater than 10
												// because there is 11 pages on the demo wearable.
		int ret = -1;
		Class<?> pageClass = page.getClass();
		if (pageClass == ActivityPage.class) {
			ret = TRACE_ACTIVITY_ID;
		} else if (pageClass == ActivityPageBackground.class) {
			ret = TRACE_ACTIVITY_ID;
		} else if (pageClass == CompassPage.class) {
			ret = TRACE_COMPASS_ID;
		} else if (pageClass == EmptyPage.class) {
			ret = TRACE_EMPTY_ID;
		} else if (pageClass == HeartRatePage.class) {
			ret = TRACE_HEARTRATE_ID;
		} else if (pageClass == MainList.class) {
			ret = TRACE_MAINLIST_ID;
		} else if (pageClass == VectorCircleArcPage.class) {
			ret = TRACE_CIRCLE_ARC_ID;
		} else if (pageClass == VectorFontPage.class) {
			ret = TRACE_VECTOR_FONT_ID;
		} else if (pageClass == VectorMascotPage.class) {
			ret = TRACE_MASCOT_ID;
		} else if (pageClass == WatchfacesCarouselPage.class) {
			ret = TRACE_WATCHFACES_CAROUSEL_ID;
		} else if (pageClass == ParametersPage.class) {
			ret = TRACE_PARAMETERS_ID;
		}

		return ret;
	}
}