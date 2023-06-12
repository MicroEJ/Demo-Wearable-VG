/*
 * Java
 *
 * Copyright 2021-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.demo.watch.util;

import ej.microui.display.ResourceImage;

/**
 * Interface to load an image.
 */
public interface ImageLoader {

	/**
	 * Loads the image.
	 *
	 * @return the image.
	 */
	ResourceImage loadImage();
}
