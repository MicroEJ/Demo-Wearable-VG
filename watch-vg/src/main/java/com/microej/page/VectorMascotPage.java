/*
 * Java
 *
 * Copyright 2019-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.page;

import com.microej.helper.MainListPositions;
import com.microej.widget.VectorMascot;

/**
 * A page that shows the MicroEJ mascot represented as a vector graphics.
 */
public class VectorMascotPage extends SimplePage {

	private final VectorMascot widget;

	/**
	 * Creates the mascot page.
	 */
	public VectorMascotPage() {
		super(MainListPositions.VECTOR_MASCOT);
		this.widget = new VectorMascot();
		addChild(this.widget);
	}

}
