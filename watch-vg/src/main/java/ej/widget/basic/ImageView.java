/*
 * Java
 *
 * Copyright 2015-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package ej.widget.basic;

import com.microej.demo.watch.util.ImageLoader;

import ej.microui.display.GraphicsContext;
import ej.microui.display.ResourceImage;
import ej.mwt.style.Style;
import ej.mwt.util.Size;

/**
 * A widget that displays an image.
 * <p>
 * The image is loaded when the widget is attached and is closed when the widget is detached.
 */
public class ImageView extends AbstractImage {

	private final ImageLoader imageLoader;

	private ResourceImage image;

	/**
	 * Creates an image view.
	 *
	 * @param imageLoader
	 *            the image loader to use to load the image.
	 */
	public ImageView(ImageLoader imageLoader) {
		this.imageLoader = imageLoader;
	}

	@Override
	protected void onAttached() {
		super.onAttached();
		if (this.image != null) {
			this.image.close();
		}
		this.image = this.imageLoader.loadImage();
	}

	@Override
	protected void onDetached() {
		super.onDetached();
		if (this.image != null) {
			this.image.close();
			this.image = null;
		}
	}

	@Override
	protected void renderContent(GraphicsContext g, int contentWidth, int contentHeight) {
		if (this.image != null) {
			Style style = getStyle();
			renderImage(g, style, contentWidth, contentHeight, this.image);
		}
	}

	@Override
	protected void computeContentOptimalSize(Size size) {
		if (this.image != null) {
			Style style = getStyle();
			computeImageSize(style, size, this.image);
		} else {
			size.setSize(0, 0);
		}
	}
}
