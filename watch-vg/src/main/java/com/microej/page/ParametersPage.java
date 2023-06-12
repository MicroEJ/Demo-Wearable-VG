/*
 * Java
 *
 * Copyright 2019-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.page;

import com.microej.demo.nls.Messages;
import com.microej.demo.watch.util.widget.basic.VectorLabel;
import com.microej.demo.watch.util.widget.basic.listener.OnStateChangeListener;
import com.microej.helper.MainListPositions;
import com.microej.style.ClassSelectors;
import com.microej.widget.basic.SlideImageToggle;
import com.microej.widget.composed.VectorToggle;

import ej.mwt.Widget;
import ej.widget.basic.Box;
import ej.widget.container.LayoutOrientation;
import ej.widget.container.List;
import ej.widget.container.SimpleDock;
import ej.widget.toggle.RadioModel;
import ej.widget.toggle.ToggleGroup;

/**
 * Parameters page providing locale selection and other settings edition.
 */
public class ParametersPage extends SimplePage {

	private static final int PAGE_TITLE_HEIGHT = 92;

	private VectorLabel titleLabel;

	private SimpleDock dock;

	private VectorLabel languagesTitle;

	/**
	 * Creates a language page.
	 */
	public ParametersPage() {
		super(MainListPositions.PARAMETERS);

		createTitle();

		createLanguagesList();
	}

	private void createLanguagesList() {
		SimpleDock dock = new SimpleDock(false);
		dock.addClassSelector(ClassSelectors.PARAMETERS_LOCALES_DOCK);

		List list = new List(LayoutOrientation.VERTICAL);
		list.addClassSelector(ClassSelectors.PARAMETERS_LOCALES_LIST);

		VectorLabel title = new VectorLabel(Messages.NLS.getMessage(Messages.Languages));
		title.addClassSelector(ClassSelectors.PARAMETERS_SECTION_LABEL);
		dock.setFirstChild(title);
		this.languagesTitle = title;

		ToggleGroup toggleGroup = new ToggleGroup();
		for (String locale : Messages.NLS.getAvailableLocales()) {
			list.addChild(createLocaleWidget(locale, toggleGroup));
		}
		dock.setCenterChild(list);
		addChild(dock);
		this.dock = dock;
	}

	private void createTitle() {
		VectorLabel label = new VectorLabel(Messages.NLS.getMessage(Messages.ParametersTitle));
		label.addClassSelector(ClassSelectors.PAGE_TITLE);
		label.addClassSelector(ClassSelectors.PARAMETERS_TITLE);
		addChild(label);
		this.titleLabel = label;
	}

	@Override
	protected void layOutChildren(int contentWidth, int contentHeight) {
		layOutChild(this.titleLabel, 0, 0, contentWidth, PAGE_TITLE_HEIGHT);
		layOutChild(this.dock, 0, PAGE_TITLE_HEIGHT, contentWidth, contentHeight - PAGE_TITLE_HEIGHT);
	}

	private Widget createLocaleWidget(final String locale, ToggleGroup toggleGroup) {
		// create box
		Box box = new SlideImageToggle();
		box.addClassSelector(ClassSelectors.TOGGLE_BOX);

		// create model
		RadioModel model = new RadioModel();
		model.addOnStateChangeListener(new OnStateChangeListener() {
			@Override
			public void onStateChange(boolean newState) {
				if (newState) {
					Messages.NLS.setCurrentLocale(locale);
					updateLanguage();
				}
			}
		});
		toggleGroup.addToggle(model);

		// create toggle
		String localeName = Messages.NLS.getDisplayName(locale);
		VectorToggle toggle = new VectorToggle(model, box, localeName);
		toggle.addClassSelector(ClassSelectors.PARAMETERS_LOCALE_TOGGLE);
		toggle.getLabel().addClassSelector(ClassSelectors.PARAMETERS_LOCALE_LABEL);
		toggle.setChecked(Messages.NLS.getCurrentLocale().equals(locale));
		return toggle;
	}

	private void updateLanguage() {
		ParametersPage.this.titleLabel.setText(Messages.NLS.getMessage(Messages.ParametersTitle));
		this.languagesTitle.setText(Messages.NLS.getMessage(Messages.Languages));
	}
}
