/*
 * Java
 *
 * Copyright 2021-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */

package com.nxp.rt595.util;

/**
 * Groups native methods for {@link PowerManagementHelper}.
 */
public class PowerManagementHelperNative {

	private PowerManagementHelperNative() {
		// Prevent instantiation
	}

	/**
	 * Configures chip performance profile .
	 *
	 * @param profile selected performance profile
	 *
	 */
	public static native void setPerfProfile(int profile);
}
