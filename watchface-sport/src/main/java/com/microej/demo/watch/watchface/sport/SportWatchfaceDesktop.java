/*
 * Java
 *
 * Copyright 2020-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.demo.watch.watchface.sport;

import com.microej.demo.watch.util.WatchImageLoader;
import com.microej.demo.watch.util.WatchfaceDesktop;
import com.microej.demo.watch.util.font.VectorFontLoader;
import com.microej.demo.watch.util.widget.basic.VectorLabel;

import ej.microui.display.Colors;
import ej.microui.display.Image;
import ej.mwt.style.EditableStyle;
import ej.mwt.style.background.ImageBackground;
import ej.mwt.style.background.NoBackground;
import ej.mwt.stylesheet.Stylesheet;
import ej.mwt.stylesheet.cascading.CascadingStylesheet;
import ej.mwt.stylesheet.selector.ClassSelector;
import ej.mwt.stylesheet.selector.TypeSelector;
import ej.mwt.util.Alignment;

/**
 * A desktop that shows the Sport watchface.
 */
public class SportWatchfaceDesktop extends WatchfaceDesktop {

	/** The class selector for the elements that have no background. */
	public static final int NO_BACKGROUND_CLASS = 0;

	/** The class selector for the labels. */
	public static final int STRINGS_CLASS = 1;

	private static final String ROBOTO_CONDENSED_REGULAR = "RobotoCondensed-Regular"; //$NON-NLS-1$
	private static final String BACKGROUND_PATH = "sport_background"; //$NON-NLS-1$

	/**
	 * Creates a Sport watchface desktop.
	 */
	public SportWatchfaceDesktop() {
		super(new SportWatchfacePage(), createStylesheet());
	}

	private static Stylesheet createStylesheet() {
		CascadingStylesheet stylesheet = new CascadingStylesheet();

		// watchface page
		EditableStyle style = stylesheet.getSelectorStyle(new TypeSelector(SportWatchfacePage.class));
		Image backgroundImage = WatchImageLoader.loadImage(BACKGROUND_PATH);
		style.setBackground(new ImageBackground(backgroundImage));

		// no background
		style = stylesheet.getSelectorStyle(new ClassSelector(NO_BACKGROUND_CLASS));
		style.setBackground(NoBackground.NO_BACKGROUND);

		// strings
		style = stylesheet.getSelectorStyle(new ClassSelector(STRINGS_CLASS));
		style.setHorizontalAlignment(Alignment.HCENTER);
		style.setVerticalAlignment(Alignment.VCENTER);
		style.setBackground(NoBackground.NO_BACKGROUND);
		style.setColor(Colors.WHITE);
		style.setExtraObject(VectorLabel.FONT_STYLE, new VectorFontLoader(ROBOTO_CONDENSED_REGULAR));
		style.setExtraInt(VectorLabel.TEXT_SIZE_STYLE, 21); // NOSONAR stylesheet is more readable with magic numbers.

		return stylesheet;
	}
}
