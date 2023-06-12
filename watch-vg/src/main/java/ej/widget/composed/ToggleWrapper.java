/*
 * Java
 *
 * Copyright 2015-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package ej.widget.composed;

import com.microej.demo.watch.util.widget.basic.listener.OnStateChangeListener;

import ej.annotation.Nullable;
import ej.widget.toggle.ToggleModel;
import ej.widget.util.GenericListener;
import ej.widget.util.ToggleHelper;

/**
 * A toggle container is a two states button: checked and unchecked. This state is toggled on each click event.
 */
public class ToggleWrapper extends Wrapper implements GenericListener {

	/**
	 * Active state.
	 */
	public static final int ACTIVE = 1;

	/**
	 * Checked state.
	 */
	public static final int CHECKED = 2;

	private final ToggleHelper toggleHelper;

	/**
	 * Creates a toggle button with a default toggle model.
	 * <p>
	 * Events received by the toggle button are forwarded to the referent toggle.
	 */
	public ToggleWrapper() {
		this.toggleHelper = new ToggleHelper(this);
		setEnabled(true);
	}

	/**
	 * Creates a toggle button with a referent toggle model.
	 * <p>
	 * Events received by the toggle button are forwarded to the referent toggle.
	 *
	 * @param toggle
	 *            the referent toggle.
	 * @throws NullPointerException
	 *             if the toggle is <code>null</code>.
	 */
	public ToggleWrapper(ToggleModel toggle) {
		this.toggleHelper = new ToggleHelper(this, toggle);
		setEnabled(true);
	}

	/**
	 * Creates a toggle button with a referent toggle model and a group.
	 * <p>
	 * Events received by the toggle button are forwarded to the referent toggle.
	 * <p>
	 * The toggle is registered in the group with the given name.
	 *
	 * @param toggle
	 *            the referent toggle.
	 * @param group
	 *            the name of the toggle group.
	 * @throws NullPointerException
	 *             if a parameter is <code>null</code>.
	 */
	public ToggleWrapper(ToggleModel toggle, String group) {
		ToggleHelper helper = new ToggleHelper(this, toggle);
		helper.setGroup(group);
		this.toggleHelper = helper;
		setEnabled(true);
	}

	/**
	 * Gets the toggle.
	 *
	 * @return the toggle.
	 */
	public ToggleModel getToggle() {
		return this.toggleHelper.getToggle();
	}

	/**
	 * Sets the group of this toggle.
	 * <p>
	 * For each name, a toggle group is created to group the toggles (only one toggle selected at a time).
	 *
	 * @param groupName
	 *            the name of the toggle group.
	 * @see ej.widget.toggle.ToggleGroup
	 */
	public void setGroup(@Nullable String groupName) {
		this.toggleHelper.setGroup(groupName);
	}

	/**
	 * Adds a listener on the state change event of the toggle.
	 *
	 * @param listener
	 *            the listener to add.
	 * @throws NullPointerException
	 *             if the given listener is <code>null</code>.
	 */
	public void addOnStateChangeListener(OnStateChangeListener listener) {
		this.toggleHelper.addOnStateChangeListener(listener);
	}

	/**
	 * Removes a listener on the state change event of the toggle.
	 *
	 * @param listener
	 *            the listener to add.
	 */
	public void removeOnStateChangeListener(OnStateChangeListener listener) {
		this.toggleHelper.removeOnStateChangeListener(listener);
	}

	/**
	 * Sets the state of the toggle.
	 * <p>
	 * If the toggle is not enabled or is already in the given state, nothing change.
	 *
	 * @param checked
	 *            the new state of the toggle.
	 */
	public void setChecked(boolean checked) {
		this.toggleHelper.setChecked(checked);
	}

	/**
	 * Changes the state of the toggle to the inverse of the current state.
	 * <p>
	 * If the toggle is not enabled, nothing change.
	 */
	public void toggle() {
		this.toggleHelper.toggle();
	}

	/**
	 * Gets whether or not the toggle button is checked.
	 *
	 * @return <code>true</code> if the toggle button is checked otherwise <code>false</code>.
	 */
	public boolean isChecked() {
		return this.toggleHelper.isChecked();
	}

	@Override
	public boolean isInState(int state) {
		return (this.toggleHelper.isPressed() && state == ACTIVE) || (state == CHECKED && isChecked())
				|| super.isInState(state);
	}

	/**
	 * Sets the pressed state of the toggle.
	 *
	 * @param pressed
	 *            the new pressed state of the toggle.
	 */
	protected void setPressed(boolean pressed) {
		this.toggleHelper.setPressed(pressed);
	}

	@Override
	public void update() {
		updateStyle();
		requestRender();
	}

	@Override
	public boolean handleEvent(int event) {
		if (this.toggleHelper.handleEvent(event)) {
			return true;
		}
		return super.handleEvent(event);
	}

}
