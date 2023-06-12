/*
 * Java
 *
 * Copyright 2020-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.helper;

import com.microej.demo.nls.Messages;
import com.microej.page.ActivityPage;
import com.microej.page.CompassPage;
import com.microej.page.EmptyPage;
import com.microej.page.HeartRatePage;
import com.microej.page.MainList;
import com.microej.page.ParametersPage;
import com.microej.page.VectorCircleArcPage;
import com.microej.page.VectorFontPage;
import com.microej.page.VectorMascotPage;

import ej.mwt.Container;

/**
 * Class that resolve the page to show from a String.
 */
public class PageResolver {

	private PageResolver() {
		// Prevent initialization.
	}

	/**
	 * @param name
	 *            the name of the page to show.
	 * @return the page we need to show.
	 */
	public static Container resolvePage(String name) { // NOSONAR The cyclomatic complexity of this method is greater
														// than 10 because there is 11 pages on the demo wearable.
		if (name.equals(MainList.getClockMessage())) {
			throw new IllegalArgumentException(); // user should start watchface app instead
		}

		switch (name) {
		case PageNames.HEART_RATE_PAGE:
			return new HeartRatePage();
		case PageNames.COMPASS:
			return new CompassPage();
		case PageNames.MESSAGE_PAGE:
			return new EmptyPage(Messages.Message, MainListPositions.MESSAGE);
		case PageNames.ACTIVITY:
			return new ActivityPage();
		case PageNames.VECTOR_MASCOT:
			return new VectorMascotPage();
		case PageNames.VECTOR_CIRCLE_ARC:
			return new VectorCircleArcPage();
		case PageNames.VECTOR_FONT:
			return new VectorFontPage();
		case PageNames.PARAMETERS:
			return new ParametersPage();
		case PageNames.GPS:
			return new EmptyPage(Messages.Gps, MainListPositions.GPS);
		case PageNames.LIGHT:
			return new EmptyPage(Messages.Light, MainListPositions.LIGHT);
		case PageNames.MUSIC:
			return new EmptyPage(Messages.Music, MainListPositions.MUSIC);
		case PageNames.TWITTER:
			return new EmptyPage(Messages.Twitter, MainListPositions.TWITTER);
		default:
			throw new IllegalArgumentException();
		}
	}
}
