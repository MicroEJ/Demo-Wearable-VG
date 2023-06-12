/*
 * Java
 *
 * Copyright 2020-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.widget.container;

import ej.microui.event.Event;
import ej.microui.event.generator.Command;
import ej.mwt.Widget;
import ej.widget.container.transition.FadeScreenshotTransitionContainer;

/**
 * A transition container that makes widgets appear or disappear smoothly by using a fade in or fade out effect.
 *
 */
public class WatchTransitionContainer extends FadeScreenshotTransitionContainer {

	/**
	 * Shows a widget instantly, that is, without any animation.
	 *
	 * @param widget
	 *            the widget to show.
	 */
	public void showInstantly(Widget widget) {
		cancelAnimation();

		if (!isAttached()) {
			removeAllChildren();
			addChild(widget);
		} else {
			removeAllChildren();
			addChild(widget);
			requestLayOut();
		}
	}

	/**
	 * Shows a widget and animates the transition toward this widget.
	 *
	 * @param widget
	 *            the widget to show.
	 * @param forward
	 *            <code>true</code> if going forward, <code>false</code> otherwise.
	 */
	public void showWithAnimation(Widget widget, boolean forward) {
		if (getDuration() > 0) {
			show(widget, forward);
		} else {
			showInstantly(widget);
		}
	}

	@Override
	public boolean handleEvent(int event) {
		if (Event.getType(event) == Command.EVENT_TYPE) {
			Widget widget = getChild(0);
			if (widget != null) {
				widget.handleEvent(event);
			}
		}

		return super.handleEvent(event);
	}

}
