/*
 * Java
 *
 * Copyright 2019-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.service;

import ej.trace.Tracer;

/**
 * A class that wraps a [@link {@link Tracer} instance for debugging/benchmarking with SEGGER System View.
 */
public class TracerService {

	private static final int NB_EVENTS = 100;

	private static final String APPLICATION_EVENTS = "Application"; //$NON-NLS-1$

	private static final Tracer tracer = new Tracer(APPLICATION_EVENTS, NB_EVENTS);

	private TracerService() {
		// Prevent instantiation
	}

	/**
	 * Gets the tracer.
	 *
	 * @return the tracer.
	 */
	public static Tracer getTracer() {
		return tracer;
	}
}
