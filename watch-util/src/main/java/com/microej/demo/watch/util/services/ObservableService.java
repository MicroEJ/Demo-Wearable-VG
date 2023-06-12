/*
 * Java
 *
 * Copyright 2021-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.demo.watch.util.services;

import ej.observable.Observer;

/**
 * An observable service allows to be notified when new data is available.
 */
public interface ObservableService {

	/**
	 * Sets the observer of this observable service.
	 *
	 * @param observer
	 *            the observer to set.
	 */
	void setObserver(Observer observer);

	/**
	 * Unsets the observer of this observable service if it the same instance as the given observer.
	 *
	 * @param observer
	 *            the observer to unset.
	 */
	void unsetObserver(Observer observer);

	/**
	 * Start the Service
	 *
	 */
	public void start();

	/**
	 * Stop the Service
	 *
	 */
	public void stop();
}
