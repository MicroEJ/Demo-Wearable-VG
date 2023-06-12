/*
 * Java
 *
 * Copyright 2020-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.demo.watch.watchface.flower;

import com.microej.demo.watch.util.WatchImageLoader;
import com.microej.demo.watch.util.WatchfaceDesktop;
import com.microej.demo.watch.util.font.VectorFontLoader;
import com.microej.demo.watch.util.path.GradientStyle;

import ej.microui.display.Image;
import ej.mwt.style.EditableStyle;
import ej.mwt.style.background.ImageBackground;
import ej.mwt.stylesheet.Stylesheet;
import ej.mwt.stylesheet.cascading.CascadingStylesheet;
import ej.mwt.stylesheet.selector.TypeSelector;

/**
 * A desktop that shows the Flower watchface.
 */
public class FlowerWatchfaceDesktop extends WatchfaceDesktop {

	private static final String OPEN_SANS_REGULAR = "OpenSans-Regular"; //$NON-NLS-1$
	private static final int TEXT_COLOR = 0xd6d6d6;
	private static final String BACKGROUND_PATH = "flower_background"; //$NON-NLS-1$
	private static final GradientStyle SECOND_HAND_GRADIENT = new GradientStyle(
			new int[] { 0xFF000000, 0xCC000000, 0x00FF007D, 0x00000000 }, new float[] { 0, 0.16f, 0.84f, 1 }, 105);

	/**
	 * Creates a Flower watchface desktop.
	 */
	public FlowerWatchfaceDesktop() {
		super(new FlowerWatchface(), createStylesheet());
	}

	private static Stylesheet createStylesheet() {
		CascadingStylesheet stylesheet = new CascadingStylesheet();

		// watchface
		EditableStyle style = stylesheet.getSelectorStyle(new TypeSelector(FlowerWatchface.class));
		Image backgroundImage = WatchImageLoader.loadImage(BACKGROUND_PATH);
		style.setColor(TEXT_COLOR);
		style.setBackground(new ImageBackground(backgroundImage));
		style.setExtraObject(FlowerWatchface.FONT_STYLE, new VectorFontLoader(OPEN_SANS_REGULAR));
		style.setExtraInt(FlowerWatchface.TEXT_SIZE_STYLE, 14); // NOSONAR stylesheet is more readable with magic
		// numbers.
		style.setExtraObject(FlowerWatchface.SECOND_HAND_GRADIENT_STYLE, SECOND_HAND_GRADIENT);

		return stylesheet;
	}
}
