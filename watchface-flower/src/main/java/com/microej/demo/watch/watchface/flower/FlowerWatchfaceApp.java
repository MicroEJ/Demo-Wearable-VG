/*
 * Java
 *
 * Copyright 2021-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.demo.watch.watchface.flower;

import com.microej.demo.watch.util.WatchImageLoader;
import com.microej.demo.watch.util.WatchfaceApp;

import ej.microui.display.GraphicsContext;
import ej.microui.display.Painter;
import ej.microui.display.ResourceImage;
import ej.mwt.Desktop;

/**
 * A watchface app for the Flower watchface.
 */
public class FlowerWatchfaceApp implements WatchfaceApp {

	private static final String PREVIEW_IMAGE = "flower_preview"; //$NON-NLS-1$

	private Desktop desktop;

	@Override
	public String getName() {
		return "FlowerWatchface";
	}

	@Override
	public void drawPreview(GraphicsContext g) {
		try (ResourceImage image = WatchImageLoader.loadImage(PREVIEW_IMAGE)) {
			Painter.drawImage(g, image, 0, 0);
		}
	}

	@Override
	public void start() {
		this.desktop = new FlowerWatchfaceDesktop();
		this.desktop.requestShow();
	}

	@Override
	public void stop() {
		if (this.desktop != null) {
			this.desktop.requestHide();
			this.desktop = null;
		}
	}
}
