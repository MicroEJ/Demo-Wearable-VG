/*
 * Java
 *
 * Copyright 2019-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.widget.container;

import ej.mwt.Container;
import ej.mwt.Widget;
import ej.mwt.util.Size;

/**
 * A container made of layers.
 */
public class LayeredContainer extends Container {

	@Override
	protected void computeContentOptimalSize(Size size) {
		int width = size.getWidth();
		int height = size.getHeight();

		for (Widget widget : getChildren()) {
			computeChildOptimalSize(widget, width, height);
		}
	}

	@Override
	protected void layOutChildren(int contentWidth, int contentHeight) {
		for (Widget widget : getChildren()) {
			layOutChild(widget, 0, 0, contentWidth, contentHeight);
		}
	}

	@Override
	public void addChild(Widget child) {
		super.addChild(child);
	}

	@Override
	public void removeChild(Widget child) {
		super.removeChild(child);
	}

	@Override
	public void insertChild(Widget child, int index) {
		super.insertChild(child, index);
	}

	@Override
	public void replaceChild(int index, Widget child) {
		super.replaceChild(index, child);
	}

	@Override
	public void removeAllChildren() {
		super.removeAllChildren();
	}
}
