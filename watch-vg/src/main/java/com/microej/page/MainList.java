/*
 * Java
 *
 * Copyright 2019-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.page;

import com.microej.Demo;
import com.microej.demo.nls.Messages;
import com.microej.helper.ApplicationTraceEvents;
import com.microej.service.TracerService;
import com.microej.widget.carousel.ApplicationListCarousel;
import com.microej.widget.carousel.ApplicationListItem;

import ej.bon.Immutables;
import ej.microui.event.Event;
import ej.microui.event.generator.Command;
import ej.widget.basic.OnClickListener;
import ej.widget.composed.Wrapper;

/**
 * Carousel containing elements composed of an image and a text. The image zoom in when the corresponding item is
 * selected.
 */
public class MainList extends Wrapper {
	private static final String[] IMAGES_PATH = (String[]) Immutables.get("mainListImagesPath"); //$NON-NLS-1$
	private static final String[] MESSAGES = (String[]) Immutables.get("mainListMessages"); //$NON-NLS-1$
	private static final int[] APPLICATION_NAMES = new int[] { Messages.Clocks, Messages.HeartRate, Messages.Activity,
			Messages.Compass, Messages.VectorMascot, Messages.VectorCircleArc, Messages.VectorFont, Messages.Message,
			Messages.Gps, Messages.Light, Messages.Music, Messages.Twitter, Messages.Parameters };
	private final ApplicationListCarousel listCarousel;

	/**
	 * Initialize a MainList with 10 base items (5 real items and 5 placeholder) starting at a specific location of the
	 * list.
	 *
	 * @param initialPosition
	 *            the initially selected element on the list.
	 */
	public MainList(int initialPosition) {
		this.listCarousel = new ApplicationListCarousel(initialPosition);

		for (int i = 0; i < IMAGES_PATH.length; i++) {
			final String message = Messages.NLS.getMessage(APPLICATION_NAMES[i]);
			ApplicationListItem applicationListItem = new ApplicationListItem(IMAGES_PATH[i], message);
			final String url = MESSAGES[i];
			applicationListItem.addOnClickListener(new OnClickListener() {
				@Override
				public void onClick() {
					Demo.showWithAnimation(url);
				}
			});
			this.listCarousel.addListItem(applicationListItem);
		}
		addChild(this.listCarousel);
	}

	@Override
	public boolean handleEvent(int event) {
		if (Event.getType(event) == Command.EVENT_TYPE) {
			Demo.showWithAnimation(MESSAGES[0]);
			return true;
		}

		return super.handleEvent(event);
	}

	/**
	 * Gets the clockMessage.
	 *
	 * @return the clockMessage.
	 */
	public static String getClockMessage() {
		return MESSAGES[0];
	}

	@Override
	protected void onAttached() {
		TracerService.getTracer().recordEvent(ApplicationTraceEvents.getEventId(this));
		super.onAttached();
	}
}
