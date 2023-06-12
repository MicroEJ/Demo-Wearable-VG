/*
 * Java
 *
 * Copyright 2021-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.service;

import com.microej.demo.watch.util.services.TimeService;

import ej.observable.Observer;

/**
 * Notifies when the time has been updated.
 */
public class DefaultTimeService implements TimeService {

	@Override
	public void setObserver(Observer observer) {
		// do nothing
	}

	@Override
	public void unsetObserver(Observer observer) {
		// do nothing
	}

	@Override
	public void start() {
		// No need in that context
	}

	@Override
	public void stop() {
		// No need in that context
	}
}
