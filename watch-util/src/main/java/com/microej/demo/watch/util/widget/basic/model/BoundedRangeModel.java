/*
 * Java
 *
 * Copyright 2015-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.demo.watch.util.widget.basic.model;

import com.microej.demo.watch.util.widget.basic.listener.OnValueChangeListener;

/**
 * Defines the data model used by components such as sliders and progress bars. Defines three interrelated integers:
 * minimum, maximum and value. These three integers define the following range:
 * <code>minimum &lt;= value &lt;= maximum</code>
 */
public interface BoundedRangeModel {

	/**
	 * Gets the range maximum.
	 *
	 * @return the value of the maximum property.
	 */
	int getMaximum();

	/**
	 * Gets the range minimum.
	 *
	 * @return the value of the minimum property.
	 */
	int getMinimum();

	/**
	 * Gets the current value.
	 *
	 * @return the current value.
	 */
	int getValue();

	/**
	 * Gets the percent complete for the range. Note that this number is between 0.0 and 1.0.
	 *
	 * @return the percent complete.
	 */
	float getPercentComplete();

	/**
	 * Sets the range maximum. The other two properties may be changed as well to ensure that
	 * <code>minimum &lt;= value &lt;= maximum</code>. Notifies the listener if the model changes.
	 *
	 * @param maximum
	 *            the maximum to set.
	 */
	void setMaximum(int maximum);

	/**
	 * Sets the range minimum. The other two properties may be changed as well to ensure that
	 * <code>minimum &lt;= value &lt;= maximum</code>. Notifies the listener if the model changes.
	 *
	 * @param minimum
	 *            the minimum to set.
	 */
	void setMinimum(int minimum);

	/**
	 * Sets the current value. The value may be changed if it does not satisfy the model's constraints. Those
	 * constraints are that <code>minimum &lt;= value &lt;= maximum</code>. That means that the value will be bounded to
	 * the range [minimum; maximum]. Notifies the listener if the model changes.
	 *
	 * @param value
	 *            the value to set.
	 */
	void setValue(int value);

	/**
	 * Adds a listener on the value change events of the range.
	 *
	 * @param onValueChangeListener
	 *            the value listener to add.
	 * @throws NullPointerException
	 *             if the given listener is <code>null</code>.
	 */
	void addOnValueChangeListener(OnValueChangeListener onValueChangeListener);

	/**
	 * Removes a listener on the value change events of the range.
	 *
	 * @param onValueChangeListener
	 *            the value listener to remove.
	 */
	void removeOnValueChangeListener(OnValueChangeListener onValueChangeListener);

}
