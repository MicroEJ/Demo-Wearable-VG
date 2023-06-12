/*
 * Java
 *
 * Copyright 2015-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package ej.widget.basic;

import com.microej.demo.watch.util.ImageLoader;

import ej.widget.util.ButtonHelper;
import ej.widget.util.GenericListener;

/**
 * A widget that displays an image and reacts to click events.
 * <p>
 * The image is loaded when the widget is attached and is closed when the widget is detached.
 */
public class ButtonImageView extends ImageView implements GenericListener {

	/**
	 * Active state.
	 */
	public static final int ACTIVE = 1;

	private final ButtonHelper buttonHelper;

	/**
	 * Creates a button image view.
	 *
	 * @param imageLoader
	 *            the image loader to use to load the image.
	 */
	public ButtonImageView(ImageLoader imageLoader) {
		super(imageLoader);
		setEnabled(true);
		this.buttonHelper = new ButtonHelper(this);
	}

	@Override
	public void update() {
		updateStyle();
		requestRender();
	}

	/**
	 * Performs the actions associated to a click.
	 */
	public void performClick() {
		this.buttonHelper.performClick();
	}

	/**
	 * Adds a listener on the click events of the button.
	 *
	 * @param listener
	 *            the listener to add.
	 * @throws NullPointerException
	 *             if the given listener is <code>null</code>.
	 */
	public void addOnClickListener(OnClickListener listener) {
		this.buttonHelper.addOnClickListener(listener);
	}

	/**
	 * Removes a listener on the click events of the button.
	 *
	 * @param listener
	 *            the listener to remove.
	 */
	public void removeOnClickListener(OnClickListener listener) {
		this.buttonHelper.removeOnClickListener(listener);
	}

	@Override
	public boolean isInState(int state) {
		return (state == ACTIVE && this.buttonHelper.isPressed()) || super.isInState(state);
	}

	@Override
	public boolean handleEvent(int event) {
		if (this.buttonHelper.handleEvent(event)) {
			return true;
		}
		return super.handleEvent(event);
	}

}
