/*
 * Java
 *
 * Copyright 2015-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package ej.widget.basic;

import com.microej.demo.watch.util.WatchImageLoader;

import ej.annotation.Nullable;
import ej.microui.display.GraphicsContext;
import ej.microui.display.ResourceImage;
import ej.mwt.style.Style;
import ej.mwt.util.Size;

/**
 * A widget that displays an image.
 * <p>
 * The widget holds a path to the image. The actual image is allocated only when the widget is attached (using the image
 * loader). It is also closed when the widget is detached.
 *
 * @see ej.microui.display.Image
 * @see #onAttached()
 * @see #onDetached()
 */
public class ImagePath extends AbstractImage {

	private String sourcePath;
	@Nullable
	private ResourceImage source;

	/**
	 * Creates an image with the path to the source to display.
	 * <p>
	 * The image loader service is used to load the image from the given path.
	 *
	 * @param sourcePath
	 *            the path to the source to display.
	 */
	public ImagePath(String sourcePath) {
		this.sourcePath = sourcePath;
	}

	/**
	 * Sets the source to display for this image without asking for a new render.
	 *
	 * @param sourcePath
	 *            the path to new source to display.
	 */
	public void setSourcePath(String sourcePath) {
		this.sourcePath = sourcePath;

		if (isAttached()) {
			loadImageSource();
		}
	}

	@Override
	protected void onAttached() {
		super.onAttached();
		loadImageSource();
	}

	@Override
	protected void onDetached() {
		super.onDetached();
		updateImageSource(null);
	}

	private void loadImageSource() {
		ResourceImage image = WatchImageLoader.loadImage(this.sourcePath);
		if (image == null) {
			throw new NullPointerException(this.sourcePath);
		}
		updateImageSource(image);
	}

	private void updateImageSource(@Nullable ResourceImage newImage) {
		ResourceImage source = this.source;
		if (source != null) {
			source.close();
		}
		this.source = newImage;
	}

	@Override
	protected void renderContent(GraphicsContext g, int contentWidth, int contentHeight) {
		ResourceImage source = this.source;
		if (source != null) {
			Style style = getStyle();
			renderImage(g, style, contentWidth, contentHeight, source);
		}
	}

	@Override
	protected void computeContentOptimalSize(Size size) {
		ResourceImage source = this.source;
		if (source != null) {
			Style style = getStyle();
			computeImageSize(style, size, source);
		} else {
			size.setSize(0, 0);
		}
	}

}
