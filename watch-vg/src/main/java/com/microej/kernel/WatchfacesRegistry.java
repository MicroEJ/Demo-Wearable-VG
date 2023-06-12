/*
 * Java
 *
 * Copyright 2021-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.kernel;

import java.util.logging.Logger;

import com.microej.demo.watch.util.ImageLoader;
import com.microej.demo.watch.util.WatchImageLoader;
import com.microej.demo.watch.util.WatchfaceApp;

import ej.basictool.ArrayTools;
import ej.microui.display.BufferedImage;
import ej.microui.display.ResourceImage;

/**
 * Manages the list of installed watchfaces.
 */
public class WatchfacesRegistry {

	private static final Logger LOGGER = Logger.getLogger(WatchfacesRegistry.class.getName());

	private static final String UNKNOWN_PREVIEW_IMAGE = "unknown_preview"; //$NON-NLS-1$
	private static final int PREVIEW_SIZE = 244;

	private WatchfacesRegistryListener[] listeners;
	private WatchfaceApp[] watchfaces;
	private WatchfaceApp currentWatchface;

	/**
	 * Creates a watchfaces registry.
	 */
	public WatchfacesRegistry() {
		this.listeners = new WatchfacesRegistryListener[0];
		this.watchfaces = new WatchfaceApp[0];
		this.currentWatchface = null;
	}

	/**
	 * Registers the given listener to the events generated by this watchfaces registry.
	 *
	 * @param listener
	 *            the listener to add.
	 */
	public void addListener(WatchfacesRegistryListener listener) {
		this.listeners = ArrayTools.add(this.listeners, listener);
	}

	/**
	 * Unregisters the given listener to the events generated by this watchfaces registry.
	 *
	 * @param listener
	 *            the listener to remove.
	 */
	public void removeListener(WatchfacesRegistryListener listener) {
		this.listeners = ArrayTools.remove(this.listeners, listener);
	}

	/**
	 * Gets the watchface at the given index.
	 *
	 * @param index
	 *            the index of the watchface.
	 * @return the watchface at the given index.
	 */
	protected WatchfaceApp getWatchface(int index) {
		return this.watchfaces[index];
	}

	/**
	 * Gets the current watchface.
	 *
	 * @return the current watchface, or null if there is no watchface started.
	 */
	protected WatchfaceApp getCurrentWatchface() {
		return this.currentWatchface;
	}

	/**
	 * Adds the given watchface.
	 *
	 * @param watchface
	 *            the watchface to add.
	 */
	public void addWatchface(WatchfaceApp watchface) {
		LOGGER.info("[WF Registry] Adding " + watchface.getName()); //$NON-NLS-1$

		this.watchfaces = ArrayTools.add(this.watchfaces, watchface);

		int watchfaceIndex = this.watchfaces.length - 1;
		for (WatchfacesRegistryListener listener : this.listeners) {
			listener.onWatchfaceInstalled(watchfaceIndex);
		}
	}

	/**
	 * Removes the given watchface.
	 *
	 * @param watchface
	 *            the watchface to remove.
	 */
	public void removeWatchface(WatchfaceApp watchface) {
		LOGGER.info("[WF Registry] Removing " + watchface.getName()); //$NON-NLS-1$

		this.watchfaces = ArrayTools.remove(this.watchfaces, watchface);

		for (WatchfacesRegistryListener listener : this.listeners) {
			listener.onWatchfaceUninstalled();
		}
	}

	/**
	 * Gets the number of installed watchfaces.
	 *
	 * @return the number of installed watchfaces.
	 */
	public int getNumWatchfaces() {
		return this.watchfaces.length;
	}

	/**
	 * Gets the preview image of the watchface identified by the given index.
	 *
	 * @param index
	 *            the index of the watchface.
	 * @return the image loader returning the preview image of the watchface.
	 */
	public ImageLoader getWatchfacePreview(int index) {
		if (index < 0 || index >= this.watchfaces.length) {
			return new WatchImageLoader(UNKNOWN_PREVIEW_IMAGE);
		}

		final WatchfaceApp watchface = this.watchfaces[index];
		return new ImageLoader() {
			@Override
			public ResourceImage loadImage() {
				BufferedImage previewImage = new BufferedImage(PREVIEW_SIZE, PREVIEW_SIZE);
				watchface.drawPreview(previewImage.getGraphicsContext());
				return previewImage;
			}
		};
	}

	/**
	 * Starts the watchface identified by the given index.
	 *
	 * @param index
	 *            the index of the watchface.
	 * @return true if the watchface could be started, false otherwise.
	 */
	public boolean startWatchface(int index) {
		if (index < 0 || index >= this.watchfaces.length) {
			return false;
		}

		stopCurrentWatchface();

		WatchfaceApp watchface = this.watchfaces[index];
		LOGGER.info("[WF Registry] Starting " + watchface.getName()); //$NON-NLS-1$
		this.currentWatchface = watchface;
		watchface.start();
		return true;
	}

	/**
	 * Stops the current watchface.
	 *
	 * @return true if the watchface could be stopped, false otherwise.
	 */
	public boolean stopCurrentWatchface() {
		WatchfaceApp currentWatchface = this.currentWatchface;
		if (currentWatchface == null) {
			return false;
		}

		LOGGER.info("[WF Registry] Stopping " + currentWatchface.getName()); //$NON-NLS-1$
		currentWatchface.stop();
		this.currentWatchface = null;

		return true;
	}
}