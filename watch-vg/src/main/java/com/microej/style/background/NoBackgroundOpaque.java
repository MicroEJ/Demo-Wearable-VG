/*
 * Java
 *
 * Copyright 2019-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.style.background;

import ej.microui.display.GraphicsContext;
import ej.mwt.style.background.Background;

/**
 * An empty background but opaque.
 */
public class NoBackgroundOpaque implements Background {

	/**
	 * No background singleton to avoid creating several ones.
	 */
	public static final NoBackgroundOpaque NO_BACKGROUND_OPAQUE = new NoBackgroundOpaque();

	@Override
	public boolean isTransparent(int width, int height) {
		return false;
	}

	@Override
	public void apply(GraphicsContext g, int width, int height) {
		g.removeBackgroundColor();
	}

}
