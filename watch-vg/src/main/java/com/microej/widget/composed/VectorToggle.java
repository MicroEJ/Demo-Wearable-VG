/*
 * Java
 *
 * Copyright 2020-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.widget.composed;

import com.microej.demo.watch.util.widget.basic.VectorLabel;

import ej.annotation.Nullable;
import ej.mwt.Widget;
import ej.mwt.util.Size;
import ej.widget.basic.Box;
import ej.widget.composed.ToggleWrapper;
import ej.widget.toggle.ToggleModel;

/**
 * A facility to use a toggle that displays a text.
 * <p>
 * It is simply a toggle wrapper that contains a vector label, based on a toggle model.
 *
 * @see Box
 * @see VectorLabel
 * @see ToggleModel
 */
public class VectorToggle extends ToggleWrapper {

	private final Box box;
	private final VectorLabel label;

	/**
	 * Creates a toggle with the given text to display. The text cannot be <code>null</code>.
	 * <p>
	 * The box state is updated along with the state of the toggle.
	 *
	 * @param box
	 *            the widget representing the state of the toggle.
	 * @param text
	 *            the text to display.
	 * @throws NullPointerException
	 *             if a parameter is <code>null</code>.
	 */
	public VectorToggle(Box box, String text) {
		this(new ToggleModel(), box, text);
	}

	/**
	 * Creates a toggle with the given text to display. The text cannot be <code>null</code>.
	 * <p>
	 * The box state is updated along with the state of the toggle.
	 *
	 * @param toggleModel
	 *            the toggle model.
	 * @param box
	 *            the widget representing the state of the toggle.
	 * @param text
	 *            the text to display.
	 * @throws NullPointerException
	 *             if a parameter is <code>null</code>.
	 */
	public VectorToggle(ToggleModel toggleModel, Box box, String text) {
		super(toggleModel);
		this.box = box;
		this.label = new VectorLabel(text);
		addChild(box);
		addChild(this.label);
	}

	/**
	 * Creates a toggle with the given text to display. The text cannot be <code>null</code>.
	 * <p>
	 * The box state is updated along with the state of the toggle.
	 *
	 * @param toggleModel
	 *            the toggle model.
	 * @param box
	 *            the widget representing the state of the toggle.
	 * @param text
	 *            the text to display.
	 * @param group
	 *            the name of the toggle group.
	 * @throws NullPointerException
	 *             if a parameter is <code>null</code>.
	 */
	public VectorToggle(ToggleModel toggleModel, Box box, String text, String group) {
		super(toggleModel, group);
		this.box = box;
		this.label = new VectorLabel(text);
		addChild(box);
		addChild(this.label);
	}

	@Override
	public void setChild(Widget widget) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Nullable
	public Widget getChild() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void removeChild() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Gets the box contained in the toggle.
	 *
	 * @return the box contained in the toggle.
	 */
	public Box getBox() {
		return this.box;
	}

	/**
	 * Gets the label contained in the toggle.
	 *
	 * @return the label contained in the toggle.
	 */
	public VectorLabel getLabel() {
		return this.label;
	}

	/**
	 * Sets the text displayed by this toggle.
	 *
	 * @param text
	 *            the text to display.
	 * @throws NullPointerException
	 *             if the text is <code>null</code>.
	 * @see VectorLabel#setText(String)
	 */
	public void setText(String text) {
		this.label.setText(text);
	}

	/**
	 * Gets the text displayed by this toggle.
	 * <p>
	 * The text is hold by the inner label.
	 *
	 * @return the text.
	 * @see VectorLabel#getText()
	 */
	public String getText() {
		return this.label.getText();
	}

	@Override
	public void update() {
		this.box.setChecked(isChecked());
	}

	@Override
	protected void setPressed(boolean pressed) {
		super.setPressed(pressed);
		this.box.setPressed(pressed);
	}

	@Override
	protected void computeContentOptimalSize(Size size) {
		int widthHint = size.getWidth();
		int heightHint = size.getHeight();

		Box box = this.box;
		VectorLabel label = this.label;

		// compute box optimal size
		computeChildOptimalSize(box, Widget.NO_CONSTRAINT, heightHint);

		// compute label optimal size
		int boxOptimalWidth = box.getWidth();
		if (widthHint != Widget.NO_CONSTRAINT) {
			widthHint = computeLabelSize(widthHint, boxOptimalWidth);
		}
		computeChildOptimalSize(label, widthHint, heightHint);

		// compute container optimal size
		int width = boxOptimalWidth + label.getWidth();
		int height = Math.max(box.getHeight(), label.getHeight());

		// set container optimal size
		size.setSize(width, height);
	}

	@Override
	protected void layOutChildren(int contentWidth, int contentHeight) {
		int boxWidth = this.box.getWidth();

		// Set widgets bounds.
		layOutChild(this.box, 0, 0, boxWidth, contentHeight);
		layOutChild(this.label, boxWidth, 0, computeLabelSize(contentWidth, boxWidth), contentHeight);
	}

	private static int computeLabelSize(int totalSize, int boxSize) {
		return Math.max(0, totalSize - boxSize);
	}
}
