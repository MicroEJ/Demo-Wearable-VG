/*
 * Java
 *
 * Copyright 2020-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.style;

import com.microej.demo.watch.util.WatchImageLoader;
import com.microej.demo.watch.util.font.VectorFontLoader;
import com.microej.demo.watch.util.path.GradientStyle;
import com.microej.demo.watch.util.services.ActivityService;
import com.microej.demo.watch.util.style.text.DynamicVectorTextStyle;
import com.microej.demo.watch.util.style.text.GradientVectorTextStyle;
import com.microej.demo.watch.util.style.text.PatternVectorTextSizeComputer;
import com.microej.demo.watch.util.widget.basic.VectorLabel;
import com.microej.page.CompassPage;
import com.microej.page.EmptyPage;
import com.microej.page.HeartRatePage;
import com.microej.page.ParametersPage;
import com.microej.path.basic.CircleArcBuilder;
import com.microej.style.background.NoBackgroundOpaque;
import com.microej.widget.activity.ActivityPageBackground;
import com.microej.widget.basic.VectorCircularProgressBar;
import com.microej.widget.carousel.ApplicationListCarousel;
import com.microej.widget.carousel.ApplicationListItem;
import com.microej.widget.chart.VectorLineChart;
import com.microej.widget.compass.CompassMarkers;
import com.microej.widget.vectorfont.VectorFontGreeting;
import com.microej.widget.vectorfont.VectorFontHexagons;

import ej.drawing.ShapePainter.Cap;
import ej.microui.display.Colors;
import ej.microui.display.Image;
import ej.mwt.Widget;
import ej.mwt.style.EditableStyle;
import ej.mwt.style.background.ImageBackground;
import ej.mwt.style.background.NoBackground;
import ej.mwt.style.background.RectangularBackground;
import ej.mwt.style.dimension.FixedDimension;
import ej.mwt.style.dimension.OptimalDimension;
import ej.mwt.style.outline.FlexibleOutline;
import ej.mwt.style.outline.border.FlexibleRectangularBorder;
import ej.mwt.stylesheet.Stylesheet;
import ej.mwt.stylesheet.cascading.CascadingStylesheet;
import ej.mwt.stylesheet.selector.ClassSelector;
import ej.mwt.stylesheet.selector.TypeSelector;
import ej.mwt.util.Alignment;
import ej.widget.basic.drawing.BulletPagingIndicator;

/**
 * A convenient class for creating the stylesheet.
 */
public class StylesheetFactory {

	/* General constants */
	private static final int DEFAULT_FOREGROUND_COLOR = 0x4b5357;
	private static final int DEFAULT_BACKGROUND_COLOR = Colors.WHITE;
	private static final int DARK_BACKGROUND_COLOR = Colors.BLACK;
	private static final int CURSOR_COLOR = 0xcbd3d7;
	private static final String BASE_BACKGROUND_PATH = "wf_vg-base_preview"; //$NON-NLS-1$
	private static final String APPLIST_BACKGROUND_PATH = "wf_vg-base_bkg"; //$NON-NLS-1$

	/* Truetype fonts */
	private static final String SOURCE_SANS_PRO_BOLDITALIC = "SourceSansPro-BoldItalic"; //$NON-NLS-1$
	private static final String SOURCE_SANS_PRO_LIGHT = "SourceSansPro-Light"; //$NON-NLS-1$
	private static final String ZCOOLKUAILE_REGULAR = "ZCOOLKuaiLe-Regular"; //$NON-NLS-1$
	private static final String ZHIMANGXING_REGULAR = "ZhiMangXing-Regular"; //$NON-NLS-1$
	private static final String ROBOTO_CONDENSED_REGULAR = "RobotoCondensed-Regular"; //$NON-NLS-1$

	/* Heart Rate constants */
	private static final String HEART_RATE_BACKGROUND = "heartrate/wf_vg-hr-bkg-header-opt"; //$NON-NLS-1$
	private static final String HR_PATTERN = "999 "; //$NON-NLS-1$
	private static final int HR_TEXTS_LEFT_PADDING = 6;
	private static final int HR_UNIT_COLOR = 0xb1bdc3;
	private static final int CURVE_COLOR = 0xffee502e;
	private static final float CURVE_THICKNESS = 2.5f;
	private static final int OPAQUE_WHITE = 0xffffffff;
	private static final int TRANSPARENT_WHITE = 0x00ffffff;
	private static final GradientStyle CURVE_GRADIENT = new GradientStyle(
			new int[] { 0xffff90e2, 0xffff90e2, 0xffff6d84, 0x66ff6d84, TRANSPARENT_WHITE, TRANSPARENT_WHITE },
			new float[] { 0, 0.08f, 0.24f, 0.59f, 0.86f, 1 }, 90);
	private static final GradientStyle TIMEZONE_B_GRADIENT = new GradientStyle(
			new int[] { OPAQUE_WHITE, OPAQUE_WHITE, 0xffc5cdd1, 0xffc5cdd1 }, new float[] { 0, 0.51f, 0.98f, 1 }, 90);
	private static final GradientStyle TIMEZONE_A_GRADIENT = new GradientStyle(
			new int[] { TRANSPARENT_WHITE, TRANSPARENT_WHITE, OPAQUE_WHITE, OPAQUE_WHITE },
			new float[] { 0, 0.16f, 0.71f, 1 }, 90);
	private static final GradientStyle AXIS_GRADIENT_STYLE = new GradientStyle(
			new int[] { 0x00000000, 0x20000000, 0x20000000, 0x00000000 }, new float[] { 0, 0.34f, 0.66f, 1 }, 0);

	/* Application list constants */
	private static final int APPLIST_SELECTED_COLOR = 0xEE502E;
	private static final int APPLICATION_LIST_ITEM_PADDING_SIDE = 30;

	/* Compass constants */
	private static final int COMPASS_DIRECTION_COLOR = 0xFFE8502E;

	/* Activity constants */
	private static final int[] TIME_GRADIENT_COLORS = new int[] { 0xffff0056, 0xffffc800 };
	private static final float[] TIME_GRADIENT_STOPS = new float[] { 0.04f, 1 };
	private static final GradientStyle ACTIVITY_TIME_TEXT_GRADIENT = new GradientStyle(TIME_GRADIENT_COLORS,
			TIME_GRADIENT_STOPS, 15);
	private static final int ACTIVITY_TEXT_COLOR = 0xabb7bd;
	private static final int ACTIVITY_ICON_COLOR = 0xcbd3d7;
	private static final int ACTIVITY_VALUES_TEXT_SIZE = 47;
	private static final GradientStyle ACTIVITY_TIME_PROGRESS_GRADIENT = new GradientStyle(TIME_GRADIENT_COLORS,
			TIME_GRADIENT_STOPS, -60);
	private static final int ACTIVITY_TIME_BACKGROUND_COLOR = 0xff717d83;
	private static final GradientStyle ACTIVITY_CALORIES_PROGRESS_GRADIENT = new GradientStyle(
			new int[] { 0xff29a1d8, 0xff29a1d8, 0xffb70079, 0xffff008a }, new float[] { 0, 0.5f, 0.9f, 1 }, 0);
	private static final int ACTIVITY_CALORIES_BACKGROUND_COLOR = 0xffe5e9eb;
	private static final int ACTIVITY_CALORIES_VALUE_COLOR = 0x29a1d8;

	/* VectorFont constants */
	private static final GradientStyle SMALL_HEXAGON_GRADIENT = new GradientStyle(new int[] { 0x99eb141e, 0x99eb141e },
			new float[] { 0, 1 }, 90);
	private static final GradientStyle MEDIUM_HEXAGON_GRADIENT = new GradientStyle(
			new int[] { 0xff1ec814, 0x001ec814, 0x001ec814 }, new float[] { 0, 0.66f, 1 }, 90);
	private static final GradientStyle BIG_HEXAGON_GRADIENT = new GradientStyle(
			new int[] { 0x001e14e6, 0x001e14e6, 0xff1e14e6 }, new float[] { 0, 0.08f, 1 }, 90);

	/* Parameters constants */
	private static final String PARAMETERS_HEADER_PATH = "parameters/header"; //$NON-NLS-1$

	private StylesheetFactory() {
		// prevents instantiation
	}

	/**
	 * Creates a new {@link CascadingStylesheet} instance for the application.
	 *
	 * <p>
	 * The stylesheet is initialized with all the application styles.
	 *
	 * @return a new stylesheet instance.
	 */
	public static Stylesheet newStylesheet() {
		CascadingStylesheet stylesheet = new CascadingStylesheet();

		setGeneralStyle(stylesheet);
		setWatchfaceCarouselStyle(stylesheet);
		setApplicationListStyle(stylesheet);
		setActivityStyle(stylesheet);
		setHeartRateSyle(stylesheet);
		setCompassStyle(stylesheet);
		setVectorFontStyle(stylesheet);
		setSettingsStyle(stylesheet);

		return stylesheet;
	}

	private static void setSettingsStyle(CascadingStylesheet stylesheet) {
		/* Parameters Page */
		RectangularBackground darkBackground = new RectangularBackground(DARK_BACKGROUND_COLOR);
		EditableStyle style = stylesheet.getSelectorStyle(new TypeSelector(ParametersPage.class));
		style.setBackground(new RectangularBackground(DARK_BACKGROUND_COLOR));

		// Parameters locale list
		style = stylesheet.getSelectorStyle(new ClassSelector(ClassSelectors.PARAMETERS_LOCALES_LIST));
		style.setBackground(darkBackground);
		style.setHorizontalAlignment(Alignment.LEFT);
		style.setVerticalAlignment(Alignment.TOP);
		style.setDimension(new FixedDimension(Widget.NO_CONSTRAINT, 216));

		// Parameters locale toggles
		style = stylesheet.getSelectorStyle(new ClassSelector(ClassSelectors.PARAMETERS_LOCALE_TOGGLE));
		style.setBackground(darkBackground);
		style.setColor(Colors.WHITE);

		// Parameters locale labels
		style = stylesheet.getSelectorStyle(new ClassSelector(ClassSelectors.PARAMETERS_LOCALE_LABEL));
		style.setBackground(darkBackground);
		style.setHorizontalAlignment(Alignment.LEFT);
		style.setExtraObject(VectorLabel.FONT_STYLE, new VectorFontLoader(ROBOTO_CONDENSED_REGULAR));
		style.setExtraInt(VectorLabel.TEXT_SIZE_STYLE, 28);
		style.setDimension(new FixedDimension(110, Widget.NO_CONSTRAINT));
		style.setMargin(new FlexibleOutline(0, 0, 0, 21));
		style.setBorder(new FlexibleRectangularBorder(Colors.WHITE, 0, 0, 1, 0));
		style.setColor(Colors.WHITE);

		// Parameters page title
		style = stylesheet.getSelectorStyle(new ClassSelector(ClassSelectors.PARAMETERS_TITLE));
		style.setBackground(new ImageBackground(WatchImageLoader.loadImage(PARAMETERS_HEADER_PATH), Alignment.HCENTER,
				Alignment.VCENTER));

		// Parameters sections title
		style = stylesheet.getSelectorStyle(new ClassSelector(ClassSelectors.PARAMETERS_SECTION_LABEL));
		style.setBackground(darkBackground);
		style.setColor(Colors.WHITE);
		style.setHorizontalAlignment(Alignment.LEFT);
		style.setMargin(new FlexibleOutline(15, 0, 0, 0));
		style.setExtraObject(VectorLabel.FONT_STYLE, new VectorFontLoader(SOURCE_SANS_PRO_LIGHT, ZCOOLKUAILE_REGULAR));
		style.setExtraInt(VectorLabel.TEXT_SIZE_STYLE, 26);
		style.setDimension(new FixedDimension(Widget.NO_CONSTRAINT, 34));

		// Parameters dock container
		style = stylesheet.getSelectorStyle(new ClassSelector(ClassSelectors.PARAMETERS_LOCALES_DOCK));
		style.setBackground(darkBackground);
		style.setPadding(new FlexibleOutline(0, 0, 0, 61));

		/* Toggles */
		style = stylesheet.getSelectorStyle(new ClassSelector(ClassSelectors.TOGGLE_BOX));
		style.setBackground(darkBackground);
	}

	private static void setActivityStyle(CascadingStylesheet stylesheet) {
		/* Activity Page */

		// ActivityPage
		EditableStyle style = stylesheet.getSelectorStyle(new TypeSelector(ActivityPageBackground.class));
		Image activityBackground = WatchImageLoader.loadImage(BASE_BACKGROUND_PATH);
		style.setBackground(new ImageBackground(activityBackground));

		// Activity texts
		style = stylesheet.getSelectorStyle(new ClassSelector(ClassSelectors.ACTIVITY_TEXT));
		style.setColor(ACTIVITY_TEXT_COLOR);
		style.setBackground(NoBackground.NO_BACKGROUND);
		style.setHorizontalAlignment(Alignment.HCENTER);
		style.setVerticalAlignment(Alignment.VCENTER);
		style.setPadding(new FlexibleOutline(5, 0, 0, 0));
		style.setExtraObject(VectorLabel.FONT_STYLE,
				new VectorFontLoader(ROBOTO_CONDENSED_REGULAR, ZCOOLKUAILE_REGULAR));
		style.setExtraInt(VectorLabel.TEXT_SIZE_STYLE, 26);

		// Activity icons
		style = stylesheet.getSelectorStyle(new ClassSelector(ClassSelectors.ACTIVITY_ICONS));
		style.setColor(ACTIVITY_ICON_COLOR);
		style.setHorizontalAlignment(Alignment.HCENTER);
		style.setVerticalAlignment(Alignment.VCENTER);

		// Time value
		style = stylesheet.getSelectorStyle(new ClassSelector(ClassSelectors.ACTIVITY_TIME));
		style.setHorizontalAlignment(Alignment.LEFT);
		style.setVerticalAlignment(Alignment.VCENTER);
		style.setPadding(new FlexibleOutline(12, 0, 0, 0));
		style.setExtraObject(VectorLabel.FONT_STYLE, new VectorFontLoader(SOURCE_SANS_PRO_BOLDITALIC));
		style.setExtraInt(VectorLabel.TEXT_SIZE_STYLE, ACTIVITY_VALUES_TEXT_SIZE);
		style.setExtraObject(VectorLabel.TEXT_STYLE, new GradientVectorTextStyle(ACTIVITY_TIME_TEXT_GRADIENT));
		style.setBackground(NoBackground.NO_BACKGROUND);

		// Time progress background
		style = stylesheet.getSelectorStyle(new ClassSelector(ClassSelectors.ACTIVITY_TIME_PROGRESS_BACKGROUND));
		style.setColor(ACTIVITY_TIME_BACKGROUND_COLOR);
		CircleArcBuilder circleArcBuilder = new CircleArcBuilder();
		circleArcBuilder.setThickness(3);
		circleArcBuilder.setColor(ACTIVITY_TIME_BACKGROUND_COLOR);
		style.setExtraObject(VectorCircularProgressBar.CIRCLE_ARC_STYLE, circleArcBuilder);
		style.setBackground(NoBackground.NO_BACKGROUND);

		// Time progress
		style = stylesheet.getSelectorStyle(new ClassSelector(ClassSelectors.ACTIVITY_TIME_PROGRESS));
		circleArcBuilder = new CircleArcBuilder();
		circleArcBuilder.setThickness(10);
		circleArcBuilder.setGradient(ACTIVITY_TIME_PROGRESS_GRADIENT);
		style.setExtraObject(VectorCircularProgressBar.CIRCLE_ARC_STYLE, circleArcBuilder);
		style.setBackground(NoBackground.NO_BACKGROUND);

		// Calories value
		style = stylesheet.getSelectorStyle(new ClassSelector(ClassSelectors.ACTIVITY_CALORIES));
		style.setHorizontalAlignment(Alignment.RIGHT);
		style.setVerticalAlignment(Alignment.VCENTER);
		style.setPadding(new FlexibleOutline(12, 0, 0, 0));
		style.setExtraObject(VectorLabel.FONT_STYLE, new VectorFontLoader(SOURCE_SANS_PRO_BOLDITALIC));
		style.setExtraObject(VectorLabel.TEXT_STYLE,
				new DynamicVectorTextStyle(ACTIVITY_CALORIES_PROGRESS_GRADIENT, 0, ActivityService.MAX_CALORIES));
		style.setExtraInt(VectorLabel.TEXT_SIZE_STYLE, ACTIVITY_VALUES_TEXT_SIZE);
		style.setColor(ACTIVITY_CALORIES_VALUE_COLOR);
		style.setBackground(NoBackground.NO_BACKGROUND);

		// Calories progress background
		style = stylesheet.getSelectorStyle(new ClassSelector(ClassSelectors.ACTIVITY_CALORIES_PROGRESS_BACKGROUND));
		circleArcBuilder = new CircleArcBuilder();
		circleArcBuilder.setThickness(6);
		circleArcBuilder.setColor(ACTIVITY_CALORIES_BACKGROUND_COLOR);
		circleArcBuilder.setCap(Cap.PERPENDICULAR);
		style.setExtraObject(VectorCircularProgressBar.CIRCLE_ARC_STYLE, circleArcBuilder);
		style.setBackground(NoBackground.NO_BACKGROUND);

		// Calories progress
		style = stylesheet.getSelectorStyle(new ClassSelector(ClassSelectors.ACTIVITY_CALORIES_PROGRESS));
		circleArcBuilder = new CircleArcBuilder();
		circleArcBuilder.setThickness(2);
		circleArcBuilder.setGradient(ACTIVITY_CALORIES_PROGRESS_GRADIENT);
		circleArcBuilder.setFixedGradient(false);
		circleArcBuilder.setCap(Cap.PERPENDICULAR);
		style.setExtraObject(VectorCircularProgressBar.CIRCLE_ARC_STYLE, circleArcBuilder);
		style.setBackground(NoBackground.NO_BACKGROUND);
	}

	private static void setCompassStyle(CascadingStylesheet stylesheet) {
		/* Compass Page */

		// CompassPage
		EditableStyle style = stylesheet.getSelectorStyle(new TypeSelector(CompassPage.class));
		style.setBackground(new RectangularBackground(DARK_BACKGROUND_COLOR));

		// Compass markers
		style = stylesheet.getSelectorStyle(new TypeSelector(CompassMarkers.class));
		style.setColor(COMPASS_DIRECTION_COLOR);

		// COMPASS_ANGLE_LABEL
		style = stylesheet.getSelectorStyle(new ClassSelector(ClassSelectors.COMPASS_ANGLE_LABEL));
		style.setColor(Colors.WHITE);
		style.setHorizontalAlignment(Alignment.HCENTER);
		style.setVerticalAlignment(Alignment.VCENTER);
		style.setPadding(new FlexibleOutline(6, 0, 5, 0));
		style.setExtraObject(VectorLabel.FONT_STYLE, new VectorFontLoader(SOURCE_SANS_PRO_BOLDITALIC));
		style.setExtraInt(VectorLabel.TEXT_SIZE_STYLE, 45);

		// Compass center
		style = stylesheet.getSelectorStyle(new ClassSelector(ClassSelectors.COMPASS_CENTER));
		style.setHorizontalAlignment(Alignment.HCENTER);
		style.setVerticalAlignment(Alignment.VCENTER);
	}

	private static void setVectorFontStyle(CascadingStylesheet stylesheet) {
		/* VectorFont Page */

		// VectorPage
		EditableStyle style = stylesheet.getSelectorStyle(new ClassSelector(ClassSelectors.VECTORFONT_BACKGROUND));
		style.setBackground(new RectangularBackground(Colors.WHITE));

		// hexagons
		style = stylesheet.getSelectorStyle(new ClassSelector(ClassSelectors.VECTORFONT_HEXAGONS));
		style.setBackground(NoBackground.NO_BACKGROUND);
		style.setExtraObject(VectorFontHexagons.SMALL_HEXAGON_GRADIENT_STYLE, SMALL_HEXAGON_GRADIENT);
		style.setExtraObject(VectorFontHexagons.MEDIUM_HEXAGON_GRADIENT_STYLE, MEDIUM_HEXAGON_GRADIENT);
		style.setExtraObject(VectorFontHexagons.BIG_HEXAGON_GRADIENT_STYLE, BIG_HEXAGON_GRADIENT);

		// text1
		style = stylesheet.getSelectorStyle(new ClassSelector(ClassSelectors.VECTORFONT_TEXT1_VALUE));
		style.setColor(0x80dc00ff);
		style.setBackground(NoBackground.NO_BACKGROUND);
		style.setExtraObject(VectorFontGreeting.FONT_STYLE, new VectorFontLoader(SOURCE_SANS_PRO_BOLDITALIC));
		style.setExtraInt(VectorFontGreeting.TEXT_SIZE_STYLE, 56);
		style.setExtraInt(VectorFontGreeting.TEXT_RENDERING_STYLE, VectorFontGreeting.RENDERING_ADVANCED_1);

		// text2
		style = stylesheet.getSelectorStyle(new ClassSelector(ClassSelectors.VECTORFONT_TEXT2_VALUE));
		style.setColor(0xa6ffeb00);
		style.setBackground(NoBackground.NO_BACKGROUND);
		style.setExtraObject(VectorFontGreeting.FONT_STYLE, new VectorFontLoader(SOURCE_SANS_PRO_BOLDITALIC));
		style.setExtraInt(VectorFontGreeting.TEXT_SIZE_STYLE, 56);
		style.setExtraInt(VectorFontGreeting.TEXT_RENDERING_STYLE, VectorFontGreeting.RENDERING_SIMPLE);

		// text3
		style = stylesheet.getSelectorStyle(new ClassSelector(ClassSelectors.VECTORFONT_TEXT3_VALUE));
		style.setColor(0x800a29ff);
		style.setBackground(NoBackground.NO_BACKGROUND);
		style.setExtraObject(VectorFontGreeting.FONT_STYLE, new VectorFontLoader(ZHIMANGXING_REGULAR));
		style.setExtraInt(VectorFontGreeting.TEXT_SIZE_STYLE, 56);
		style.setExtraInt(VectorFontGreeting.TEXT_RENDERING_STYLE, VectorFontGreeting.RENDERING_ADVANCED_2);
	}

	private static void setHeartRateSyle(CascadingStylesheet stylesheet) {
		/* Heart rate page */
		EditableStyle style = stylesheet.getSelectorStyle(new TypeSelector(HeartRatePage.class));
		Image heartRatePageBackground = WatchImageLoader.loadImage(HEART_RATE_BACKGROUND);
		style.setBackground(new ImageBackground(heartRatePageBackground));

		// Heart rate chart
		style = stylesheet.getSelectorStyle(new ClassSelector(ClassSelectors.HEART_RATE_CHART));
		style.setBackground(new RectangularBackground(Colors.WHITE));
		style.setExtraObject(VectorLineChart.CURVE_COLOR_STYLE, CURVE_COLOR);
		style.setExtraObject(VectorLineChart.CURVE_THICKNESS_STYLE, CURVE_THICKNESS);
		style.setExtraObject(VectorLineChart.CURVE_GRADIENT_STYLE, CURVE_GRADIENT);
		style.setExtraObject(VectorLineChart.TIMEZONE_B_STYLE, TIMEZONE_B_GRADIENT);
		style.setExtraObject(VectorLineChart.TIMEZONE_A_STYLE, TIMEZONE_A_GRADIENT);
		style.setExtraObject(VectorLineChart.AXIS_GRADIENT_STYLE, AXIS_GRADIENT_STYLE);
		style.setExtraObject(VectorLineChart.FONT_STYLE, new VectorFontLoader(ROBOTO_CONDENSED_REGULAR));
		style.setColor(HR_UNIT_COLOR);

		// Current HR value
		style = stylesheet.getSelectorStyle(new ClassSelector(ClassSelectors.HR_CURRENT_VALUE));
		style.setExtraObject(VectorLabel.FONT_STYLE, new VectorFontLoader(SOURCE_SANS_PRO_BOLDITALIC));
		style.setExtraInt(VectorLabel.TEXT_SIZE_STYLE, 51);
		style.setExtraObject(VectorLabel.SIZE_COMPUTER_STYLE, new PatternVectorTextSizeComputer(HR_PATTERN));
		style.setHorizontalAlignment(Alignment.LEFT);
		style.setVerticalAlignment(Alignment.VCENTER);
		style.setPadding(new FlexibleOutline(6, 0, 0, HR_TEXTS_LEFT_PADDING));

		// Unit of current HR value
		style = stylesheet.getSelectorStyle(new ClassSelector(ClassSelectors.HR_CURRENT_VALUE_UNIT));
		style.setColor(HR_UNIT_COLOR);
		style.setExtraObject(VectorLabel.FONT_STYLE, new VectorFontLoader(ROBOTO_CONDENSED_REGULAR));
		style.setExtraInt(VectorLabel.TEXT_SIZE_STYLE, 15);
		style.setHorizontalAlignment(Alignment.LEFT);
		style.setVerticalAlignment(Alignment.VCENTER);
		style.setPadding(new FlexibleOutline(7, 0, 0, HR_TEXTS_LEFT_PADDING));

		// Unit of Min/Max HR values
		style = stylesheet.getSelectorStyle(new ClassSelector(ClassSelectors.HR_UNIT));
		style.setColor(HR_UNIT_COLOR);
		style.setHorizontalAlignment(Alignment.LEFT);
		style.setVerticalAlignment(Alignment.VCENTER);
		style.setExtraObject(VectorLabel.FONT_STYLE, new VectorFontLoader(ROBOTO_CONDENSED_REGULAR));
		style.setExtraInt(VectorLabel.TEXT_SIZE_STYLE, 14);
		style.setPadding(new FlexibleOutline(5, 0, 0, HR_TEXTS_LEFT_PADDING));

		// Min/Max HR values
		style = stylesheet.getSelectorStyle(new ClassSelector(ClassSelectors.HR_MIN_MAX_VALUE));
		style.setExtraObject(VectorLabel.FONT_STYLE, new VectorFontLoader(SOURCE_SANS_PRO_BOLDITALIC));
		style.setExtraObject(VectorLabel.SIZE_COMPUTER_STYLE, new PatternVectorTextSizeComputer(HR_PATTERN));
		style.setExtraInt(VectorLabel.TEXT_SIZE_STYLE, 26);
		style.setHorizontalAlignment(Alignment.LEFT);
		style.setVerticalAlignment(Alignment.TOP);
		style.setPadding(new FlexibleOutline(7, 0, 0, HR_TEXTS_LEFT_PADDING));

		// Containers of HR units
		style = stylesheet.getSelectorStyle(new ClassSelector(ClassSelectors.HR_UNIT_DOCKS));
		style.setVerticalAlignment(Alignment.TOP);
		style.setPadding(new FlexibleOutline(10, 10, 10, 10));

		// Container of HR value
		style = stylesheet.getSelectorStyle(new ClassSelector(ClassSelectors.HR_DOCK));
		style.setDimension(OptimalDimension.OPTIMAL_DIMENSION_XY);
	}

	private static void setApplicationListStyle(CascadingStylesheet stylesheet) {
		// ApplicationListCarousel style
		EditableStyle style = stylesheet.getSelectorStyle(new TypeSelector(ApplicationListCarousel.class));
		style.setBackground(new ImageBackground(WatchImageLoader.loadImage(APPLIST_BACKGROUND_PATH)));

		// ApplicationListItem style
		style = stylesheet.getSelectorStyle(new TypeSelector(ApplicationListItem.class));
		style.setColor(Colors.BLACK);
		style.setPadding(new FlexibleOutline(0, 0, 0, APPLICATION_LIST_ITEM_PADDING_SIDE));
		style.setBackground(NoBackground.NO_BACKGROUND);
		style.setHorizontalAlignment(Alignment.LEFT);
		style.setVerticalAlignment(Alignment.VCENTER);
		style.setExtraObject(ApplicationListItem.FONT_STYLE,
				new VectorFontLoader(SOURCE_SANS_PRO_LIGHT, ZCOOLKUAILE_REGULAR));
		style.setExtraInt(ApplicationListItem.TEXT_SIZE_STYLE, 32);
		style.setExtraFloat(ApplicationListItem.SPLIT_FACTOR_STYLE, 0.425f);
		style.setExtraInt(ApplicationListItem.SPACING_STYLE, 20);
		style.setExtraInt(ApplicationListItem.SELECTED_COLOR_STYLE, APPLIST_SELECTED_COLOR);
	}

	private static void setWatchfaceCarouselStyle(CascadingStylesheet stylesheet) {
		// Carousel cursor
		EditableStyle style = stylesheet.getSelectorStyle(new ClassSelector(ClassSelectors.WATCHFACE_CAROUSEL_CURSOR));
		style.setColor(CURSOR_COLOR);
		style.setExtraInt(BulletPagingIndicator.CURSOR_SIZE_STYLE, 20);
		style.setPadding(new FlexibleOutline(0, 0, 35, 0));

		// Carousel items
		style = stylesheet.getSelectorStyle(new ClassSelector(ClassSelectors.WATCHFACE_CAROUSEL_ITEMS));
		style.setPadding(new FlexibleOutline(45, 0, 0, 0));
	}

	private static void setGeneralStyle(CascadingStylesheet stylesheet) {
		// Sets the default style.
		EditableStyle style = stylesheet.getDefaultStyle();
		style.setHorizontalAlignment(Alignment.HCENTER);
		style.setVerticalAlignment(Alignment.VCENTER);
		style.setBackground(new RectangularBackground(DEFAULT_BACKGROUND_COLOR));
		style.setColor(DEFAULT_FOREGROUND_COLOR);

		// PAGE_TITLE
		style = stylesheet.getSelectorStyle(new ClassSelector(ClassSelectors.PAGE_TITLE));
		style.setColor(Colors.WHITE);
		style.setBackground(NoBackground.NO_BACKGROUND);
		style.setHorizontalAlignment(Alignment.HCENTER);
		style.setVerticalAlignment(Alignment.VCENTER);
		style.setExtraObject(VectorLabel.FONT_STYLE, new VectorFontLoader(SOURCE_SANS_PRO_LIGHT, ZCOOLKUAILE_REGULAR));
		style.setExtraInt(VectorLabel.TEXT_SIZE_STYLE, 26);
		style.setPadding(new FlexibleOutline(6, 0, 0, 0));

		// NO_BACKGROUND_TRANSPARENT
		style = stylesheet.getSelectorStyle(new ClassSelector(ClassSelectors.NO_BACKGROUND_TRANSPARENT));
		style.setBackground(NoBackground.NO_BACKGROUND);

		// NO_BACKGROUND_OPAQUE
		style = stylesheet.getSelectorStyle(new ClassSelector(ClassSelectors.NO_BACKGROUND_OPAQUE));
		style.setBackground(NoBackgroundOpaque.NO_BACKGROUND_OPAQUE);

		// Left Alignment
		style = stylesheet.getSelectorStyle(new ClassSelector(ClassSelectors.LEFT));
		style.setHorizontalAlignment(Alignment.LEFT);

		// Right Alignment
		style = stylesheet.getSelectorStyle(new ClassSelector(ClassSelectors.RIGHT));
		style.setHorizontalAlignment(Alignment.RIGHT);

		/* Empty page */
		style = stylesheet.getSelectorStyle(new ClassSelector(ClassSelectors.EMPTY_MESSAGE));
		style.setExtraObject(EmptyPage.FONT_STYLE,
				new VectorFontLoader(SOURCE_SANS_PRO_BOLDITALIC, ZCOOLKUAILE_REGULAR));
		style.setExtraInt(EmptyPage.TEXT_SIZE_STYLE, 33);
		style.setBackground(new RectangularBackground(DARK_BACKGROUND_COLOR));
		style.setColor(Colors.WHITE);
	}
}
