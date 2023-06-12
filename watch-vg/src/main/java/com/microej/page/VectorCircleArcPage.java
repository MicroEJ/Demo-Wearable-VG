/*
 * Java
 *
 * Copyright 2019-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.page;

import com.microej.helper.MainListPositions;
import com.microej.widget.VectorCircleArc;

/**
 * A page that draws circle arcs using vector paths.
 */
public class VectorCircleArcPage extends SimplePage {

	private final VectorCircleArc widget;

	/**
	 * Creates the vector circle arc page.
	 */
	public VectorCircleArcPage() {
		super(MainListPositions.VECTOR_CIRCLE_ARC);
		this.widget = new VectorCircleArc();
		addChild(this.widget);
	}

}
