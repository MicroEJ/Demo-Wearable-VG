/*
 * Java
 *
 * Copyright 2019-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.demo.watch.util;

import ej.annotation.Nullable;
import ej.basictool.ThreadUtils;
import ej.microui.MicroUIException;
import ej.microui.display.ResourceImage;

/**
 * An implementation of image loader which loads the images from the <code>/images/</code> folder and with the
 * <code>.png</code> extension.
 */
public class WatchImageLoader implements ImageLoader {

	private static final String PNG_EXTENSION = ".png"; //$NON-NLS-1$

	private static final String DEMO_WEARABLE_IMAGE_FOLDER = "/images/"; //$NON-NLS-1$

	private final String path;

	/**
	 * Creates a watch image loader.
	 *
	 * @param path
	 *            the path of the image, without the root folder and the extension.
	 */
	public WatchImageLoader(String path) {
		this.path = path;
	}

	@Override
	public ResourceImage loadImage() {
		return loadImage(this.path);
	}

	/**
	 * Loads the MicroUI image matching the given path.
	 *
	 * @param path
	 *            the path of the image.
	 * @return the image or <code>null</code> if not found.
	 * @throws NullPointerException
	 *             if the given path is <code>null</code>.
	 */
	@Nullable
	public static ResourceImage loadImage(String path) {
		try {
			return ResourceImage.loadImage(getAbsoluteImagePath(path));
		} catch (MicroUIException e) {
			ThreadUtils.handleUncaughtException(e);
			return null;
		}
	}

	/**
	 * Returns the absolute path of an image.
	 *
	 * @param path
	 *            the path of the image, without the root folder and the extension.
	 * @return the absolute path of the image.
	 */
	public static String getAbsoluteImagePath(String path) {
		return DEMO_WEARABLE_IMAGE_FOLDER + path + PNG_EXTENSION;
	}
}
