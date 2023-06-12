/*
 * Java
 *
 * Copyright 2020-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.demo.watch.util;

import com.microej.demo.watch.util.services.NavigationService;

import ej.bon.Timer;
import ej.bon.TimerTask;
import ej.microui.MicroUI;
import ej.microui.event.Event;
import ej.microui.event.generator.Command;
import ej.microui.event.generator.Pointer;
import ej.mwt.Desktop;
import ej.mwt.Widget;
import ej.mwt.stylesheet.Stylesheet;

/**
 * A desktop that displays a watchface.
 */
public abstract class WatchfaceDesktop extends Desktop {

	private static final int LONG_PRESS_DELAY = 550;

	private TimerTask longPressTask;

	/***
	 * Creates a watchface desktop.
	 *
	 * @param widget
	 *            the widget that represents the watchface.
	 * @param stylesheet
	 *            the stylesheet of this watchface desktop.
	 */
	public WatchfaceDesktop(Widget widget, Stylesheet stylesheet) {
		setWidget(widget);
		setStylesheet(stylesheet);
	}

	@Override
	public boolean handleEvent(int event) {
		if (Event.getType(event) == Command.EVENT_TYPE) {
			NavigationService navigationService = KernelServiceRegistry.getServiceLoader()
					.getService(NavigationService.class);
			if (navigationService != null) {
				navigationService.showMainPage();
			}
			return true;
		} else if (Event.getType(event) == Pointer.EVENT_TYPE) {
			int action = Pointer.getAction(event);
			if (action == Pointer.PRESSED) {
				startLongPressTask();
				return true;
			} else if (action == Pointer.RELEASED) {
				stopLongPressTask();
			}
		}

		return super.handleEvent(event);
	}

	@Override
	protected void onHidden() {
		super.onHidden();
		stopLongPressTask();
	}

	private void startLongPressTask() {
		stopLongPressTask();

		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				MicroUI.callSerially(new Runnable() {
					@Override
					public void run() {
						runLongPressTask();
					}
				});
			}
		};
		this.longPressTask = task;

		Timer timer = KernelServiceRegistry.getServiceLoader().getService(Timer.class);
		timer.schedule(task, LONG_PRESS_DELAY);
	}

	private void runLongPressTask() {
		NavigationService navigationService = KernelServiceRegistry.getServiceLoader()
				.getService(NavigationService.class);
		if (navigationService != null && isShown()) {
			navigationService.showWatchfacesCarousel();
		}
	}

	private void stopLongPressTask() {
		TimerTask task = this.longPressTask;
		if (task != null) {
			task.cancel();
			this.longPressTask = null;
		}
	}
}
