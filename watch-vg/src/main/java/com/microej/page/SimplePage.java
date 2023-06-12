/*
 * Java
 *
 * Copyright 2019-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.page;

import com.microej.Demo;
import com.microej.helper.ApplicationTraceEvents;
import com.microej.service.TracerService;
import com.microej.widget.container.LayeredContainer;

import ej.microui.event.Event;
import ej.microui.event.generator.Command;

/**
 * A {@link LayeredContainer} with a specific eventHandler when the page you create need the possibility to go back to
 * the {@link MainList}.
 */
public abstract class SimplePage extends LayeredContainer {

	private final int position;

	/**
	 * Creates a page of the application, given its position in the list of applications.
	 *
	 * @param position
	 *            the position of the application in the main list.
	 */
	public SimplePage(int position) {
		this.position = position;
	}

	@Override
	public boolean handleEvent(int event) {
		if (Event.getType(event) == Command.EVENT_TYPE) {
			Demo.showMainPage();
			return true;
		}

		return super.handleEvent(event);
	}

	@Override
	protected void onAttached() {
		TracerService.getTracer().recordEvent(ApplicationTraceEvents.getEventId(this));
		super.onAttached();
		Demo.setCurrentPosition(this.position);
	}
}
