/*
 * Java
 *
 * Copyright 2020-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.demo.watch.watchface.flowerlp;

import com.microej.demo.watch.util.WatchfaceDesktop;
import com.nxp.rt595.util.PostPonedRenderPolicy;

import ej.mwt.render.RenderPolicy;
import ej.mwt.style.EditableStyle;
import ej.mwt.style.background.NoBackground;
import ej.mwt.stylesheet.Stylesheet;
import ej.mwt.stylesheet.cascading.CascadingStylesheet;
import ej.mwt.stylesheet.selector.TypeSelector;

/**
 * A desktop that shows the FlowerLP watchface.
 */
public class FlowerLPWatchfaceDesktop extends WatchfaceDesktop {

	/**
	 * Creates a FlowerLP watchface desktop.
	 */
	public FlowerLPWatchfaceDesktop() {
		super(new FlowerLPWatchface(), createStylesheet());
	}

	@Override
	protected RenderPolicy createRenderPolicy() {
		return new PostPonedRenderPolicy(this);
	}

	private static Stylesheet createStylesheet() {
		CascadingStylesheet stylesheet = new CascadingStylesheet();

		// watchface
		EditableStyle style = stylesheet.getSelectorStyle(new TypeSelector(FlowerLPWatchface.class));
		style.setBackground(NoBackground.NO_BACKGROUND);

		return stylesheet;
	}
}
