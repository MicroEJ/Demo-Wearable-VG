/*
 * Java
 *
 * Copyright 2016-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package ej.widget.basic;

import ej.mwt.Widget;

/**
 * A box is a widget that can be checked or unchecked.
 */
public abstract class Box extends Widget {

	/**
	 * Active state.
	 */
	public static final int ACTIVE = 1;

	/**
	 * Checked state.
	 */
	public static final int CHECKED = 2;

	private boolean checked;
	private boolean pressed;

	/**
	 * Sets the state of the toggle without rendering it.
	 *
	 * @param checked
	 *            the new state of the toggle.
	 */
	public void updateState(boolean checked) {
		this.checked = checked;
	}

	/**
	 * Sets the state of the toggle and requests a render it if its style changes.
	 *
	 * @param checked
	 *            the new state of the toggle.
	 */
	public void setChecked(boolean checked) {
		if (this.checked != checked) {
			this.checked = checked;
			// Always render the widget even if the style has not changed because the checked state modifies the
			// rendering of the box (see render methods in subclasses).
			updateStyle();
			requestRender();
		}
	}

	/**
	 * Sets the pressed state of the box.
	 *
	 * @param pressed
	 *            the new pressed state of the box.
	 */
	public void setPressed(boolean pressed) {
		if (this.pressed != pressed) {
			this.pressed = pressed;
			updateStyle();
			requestRender();
		}
	}

	/**
	 * Gets whether or not the box is checked.
	 *
	 * @return <code>true</code> if the box is checked otherwise <code>false</code>.
	 */
	public boolean isChecked() {
		return this.checked;
	}

	@Override
	public boolean isInState(int state) {
		return (this.pressed && state == ACTIVE) || (state == CHECKED && this.checked) || super.isInState(state);
	}

}