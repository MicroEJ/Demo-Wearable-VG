/*
 * Java
 *
 * Copyright 2021-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.page;

import com.microej.Demo;
import com.microej.demo.watch.util.ImageLoader;
import com.microej.helper.ApplicationTraceEvents;
import com.microej.service.TracerService;
import com.microej.style.ClassSelectors;

import ej.widget.basic.ButtonImageView;
import ej.widget.basic.OnClickListener;
import ej.widget.basic.drawing.BulletPagingIndicator;
import ej.widget.container.FillCarousel;

/**
 * Carousel that will show previews of different watchfaces and allow the user to go to the watchface by clicking on the
 * image.
 */
public class WatchfacesCarouselPage extends FillCarousel {

	/**
	 * Simple constructor for {@link WatchfacesCarouselPage}.
	 */
	public WatchfacesCarouselPage() {
		super(true, true);

		BulletPagingIndicator cursor = new BulletPagingIndicator();
		cursor.addClassSelector(ClassSelectors.WATCHFACE_CAROUSEL_CURSOR);
		setCursor(cursor, false);
	}

	@Override
	protected void onAttached() {
		TracerService.getTracer().recordEvent(ApplicationTraceEvents.getEventId(this));
		super.onAttached();
	}

	/**
	 * Adds a watchface to this carousel.
	 *
	 * @param position
	 *            the position of the watchface to add.
	 * @param previewImageLoader
	 *            the image loader returning the preview image for this watchface.
	 */
	public void addWatchface(final int position, ImageLoader previewImageLoader) {
		ButtonImageView button = new ButtonImageView(previewImageLoader);
		button.addClassSelector(ClassSelectors.WATCHFACE_CAROUSEL_ITEMS);
		button.addOnClickListener(new OnClickListener() {
			@Override
			public void onClick() {
				Demo.showWatchface(position);
			}
		});
		addChild(button);
	}
}
