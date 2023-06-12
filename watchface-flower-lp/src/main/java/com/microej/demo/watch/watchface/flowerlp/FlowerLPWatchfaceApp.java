/*
 * Java
 *
 * Copyright 2021-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.demo.watch.watchface.flowerlp;

import com.microej.demo.watch.util.WatchImageLoader;
import com.microej.demo.watch.util.WatchfaceApp;

import ej.microui.display.GraphicsContext;
import ej.microui.display.Painter;
import ej.microui.display.ResourceImage;
import ej.mwt.Desktop;

/**
 * A watchface app for the FlowerLP watchface.
 */
public class FlowerLPWatchfaceApp implements WatchfaceApp {

	private static final String PREVIEW_IMAGE = "flowerlp_preview"; //$NON-NLS-1$

	private Desktop desktop;

	@Override
	public String getName() {
		return "FlowerLPWatchface";
	}

	@Override
	public void drawPreview(GraphicsContext g) {
		try (ResourceImage image = WatchImageLoader.loadImage(PREVIEW_IMAGE)) {
			Painter.drawImage(g, image, 0, 0);
		}
	}

	@Override
	public void start() {
		this.desktop = new FlowerLPWatchfaceDesktop();
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
