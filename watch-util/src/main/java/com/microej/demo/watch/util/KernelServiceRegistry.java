/*
 * Java
 *
 * Copyright 2021-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.demo.watch.util;

import ej.service.ServiceLoader;
import ej.service.ServiceRegistry;
import ej.service.registry.SimpleServiceRegistry;

/**
 * Allows the kernel to provide services to the features.
 */
public class KernelServiceRegistry {

	private static final ServiceRegistry SERVICE_REGISTRY = new SimpleServiceRegistry();

	private KernelServiceRegistry() {
		// private constructor
	}

	/**
	 * Returns the service loader which allows the kernel and the features to retrieve services.
	 *
	 * @return the service loader.
	 */
	public static ServiceLoader getServiceLoader() {
		return SERVICE_REGISTRY;
	}

	/**
	 * Returns the service registry which allows the kernel to register services.
	 *
	 * @return the service factory.
	 */
	public static ServiceRegistry getServiceRegistry() {
		return SERVICE_REGISTRY;
	}
}
