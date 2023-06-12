/*
 * Java
 *
 * Copyright 2019-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.page;

import com.microej.demo.nls.Messages;
import com.microej.demo.watch.util.widget.basic.VectorLabel;
import com.microej.style.ClassSelectors;

/**
 * An empty page with just a message.
 */
public class EmptyPage extends SimplePage {

	/** The extra field ID for the font. */
	public static final int FONT_STYLE = 0;

	/** The extra field ID for the text size. */
	public static final int TEXT_SIZE_STYLE = 1;

	private final VectorLabel label;

	/**
	 * Creates the empty page.
	 *
	 * @param message
	 *            the message to show.
	 * @param listPosition
	 *            the position of the main list for this page.
	 */
	public EmptyPage(int message, int listPosition) {
		super(listPosition);
		this.label = new VectorLabel(Messages.NLS.getMessage(message));
		this.label.addClassSelector(ClassSelectors.EMPTY_MESSAGE);
		addChild(this.label);
	}

}
