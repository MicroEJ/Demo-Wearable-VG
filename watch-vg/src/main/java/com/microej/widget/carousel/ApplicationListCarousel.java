/*
 * Java
 *
 * Copyright 2019-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.widget.carousel;

import com.microej.demo.watch.util.WatchImageLoader;
import com.microej.helper.ApplicationTraceEvents;
import com.microej.service.TracerService;

import ej.annotation.Nullable;
import ej.bon.XMath;
import ej.drawing.ShapePainter;
import ej.drawing.ShapePainter.Cap;
import ej.microui.MicroUI;
import ej.microui.display.GraphicsContext;
import ej.microui.display.Painter;
import ej.microui.display.ResourceImage;
import ej.microui.event.Event;
import ej.microui.event.generator.Pointer;
import ej.mwt.Container;
import ej.mwt.Widget;
import ej.mwt.util.Size;
import ej.trace.Tracer;
import ej.widget.util.SwipeEventHandler;
import ej.widget.util.swipe.Swipeable;

/**
 * A composite that contains a list of widgets that can be scrolled vertically.
 */
public class ApplicationListCarousel extends Container implements Swipeable {

	private static final String TOP_STOP_IMAGE = "list/wf_vg-applist_level2-scroll-down"; //$NON-NLS-1$
	private static final String BOTTOM_STOP_IMAGE = "list/wf_vg-applist_level2-scroll-up"; //$NON-NLS-1$
	private static final float MAGNIFY_RATIO = 1.2f;
	private static final float MINIMAL_RATIO = 0.1f;

	private static final int ARC_SPACING = 20;
	private static final int ARC_THICKNESS = 6;
	private static final int ARC_FADE = 1;
	private static final Cap ARC_CAPS = Cap.PERPENDICULAR;
	private static final float ARC_BACKGROUND_ANGLE = 30.0f;
	private static final float ARC_CURSOR_ANGLE = 5.0f;
	private static final int ARC_BACKGROUND_COLOR = 0xCBD3D7;
	private static final int ARC_CURSOR_COLOR = 0xEE502E;

	@Nullable
	private SwipeEventHandler swipeEventHandler;

	private int selectedIndex;
	private int widgetHeight;

	private final Tracer tracer;

	private int position;

	private ResourceImage topStopImage;
	private ResourceImage bottomStopImage;

	/**
	 * Creates a carousel with the item at given index as the selected item.
	 *
	 * @param selectedIndex
	 *            the index of the selected item (0-based) when opening the carousel.
	 */
	public ApplicationListCarousel(int selectedIndex) {
		this.selectedIndex = selectedIndex;

		this.tracer = TracerService.getTracer();

		setEnabled(true);
	}

	/**
	 * Adds a list item.
	 *
	 * @param item
	 *            the list item to add.
	 * @see #addChild(Widget)
	 */
	public void addListItem(ApplicationListItem item) {
		super.addChild(item);
	}

	@Override
	protected void onAttached() {
		super.onAttached();
		this.topStopImage = WatchImageLoader.loadImage(TOP_STOP_IMAGE);
		this.bottomStopImage = WatchImageLoader.loadImage(BOTTOM_STOP_IMAGE);
	}

	@Override
	protected void onDetached() {
		super.onDetached();
		this.topStopImage.close();
		this.bottomStopImage.close();
	}

	@Override
	protected void onHidden() {
		super.onHidden();

		SwipeEventHandler swipeEventHandler = this.swipeEventHandler;
		if (swipeEventHandler != null) {
			swipeEventHandler.stop();
		}
	}

	/**
	 * Sets the currently selected widget index.
	 *
	 * @param selectedIndex
	 *            the index to set.
	 */
	protected void setSelectedIndex(int selectedIndex) {
		this.selectedIndex = selectedIndex;
	}

	/**
	 * Gets the currently selected widget index.
	 *
	 * @return the selected widget index.
	 */
	public int getSelectedIndex() {
		return this.selectedIndex;
	}

	@Override
	protected void computeContentOptimalSize(Size size) {
		int boundsWidth = size.getWidth();
		int boundsHeight = size.getHeight();

		boolean computeWidth = boundsWidth == Widget.NO_CONSTRAINT;
		boolean computeHeight = boundsHeight == Widget.NO_CONSTRAINT;

		int width = boundsWidth;
		int height = 0;

		// Each child takes the full height and the width it needs when horizontal, the opposite when vertical.
		for (Widget widget : getChildren()) {
			computeChildOptimalSize(widget, width, Widget.NO_CONSTRAINT);
			width = Math.max(width, widget.getWidth());
			height = Math.max(height, widget.getHeight());
		}
		this.widgetHeight = height;
		height *= getChildrenCount();

		if (computeWidth) {
			size.setWidth(width);
		}
		if (computeHeight) {
			size.setHeight(height);
		}

	}

	@Override
	protected void layOutChildren(int contentWidth, int contentHeight) {
		int widgetsCount = getChildrenCount();
		this.selectedIndex = Math.min(this.selectedIndex, widgetsCount - 1);

		if (widgetsCount > 0) {
			int widgetHeight = this.widgetHeight;
			// Each child takes the full width and all have the same height.
			for (Widget widget : getChildren()) {
				layOutChild(widget, 0, 0, contentWidth, widgetHeight);
			}

			onMoveInternal(widgetHeight * this.selectedIndex);

			this.swipeEventHandler = new SwipeEventHandler(widgetsCount, widgetHeight, false, true, false, this);
			this.swipeEventHandler.setAnimator(getDesktop().getAnimator());
			if (this.selectedIndex != -1) {
				goTo(this.selectedIndex);
			}
		} else {
			this.swipeEventHandler = null;
		}
	}

	/**
	 * Moves a widget when scrolling.
	 *
	 * @param widget
	 *            the widget to move.
	 * @param totalSize
	 *            the cumulated size of the children (width if horizontal, height if vertical).
	 * @param shift
	 *            the move shift.
	 * @return the size of the widget (width if horizontal, height if vertical).
	 */
	protected int moveWidget(Widget widget, int totalSize, int shift) {
		int widgetX = widget.getX();
		int widgetY = widget.getY() - shift;
		int widgetSize = widget.getHeight();
		widget.setPosition(widgetX, widgetY);
		return widgetSize;
	}

	@Override
	public boolean handleEvent(int event) {
		SwipeEventHandler swipeEventHandler = this.swipeEventHandler;
		if (swipeEventHandler != null && swipeEventHandler.handleEvent(event)) {
			return true;
		}
		if (Event.getType(event) == Pointer.EVENT_TYPE && Pointer.isDragged(event)) {
			return true;
		}
		return super.handleEvent(event);
	}

	/**
	 * Selects the previous widget.
	 */
	public void goToPrevious() {
		goToIncrement(false, 0);
	}

	/**
	 * Selects the next widget.
	 */
	public void goToNext() {
		goToIncrement(true, 0);
	}

	/**
	 * Animates the selection of the previous widget.
	 *
	 * @param duration
	 *            the duration of the animation.
	 * @throws IllegalArgumentException
	 *             if the given duration is less or equal than <code>0</code>.
	 */
	public void goToPrevious(int duration) {
		if (duration <= 0) {
			throw new IllegalArgumentException();
		}
		goToIncrement(false, duration);
	}

	/**
	 * Animates the selection of the next widget.
	 *
	 * @param duration
	 *            the duration of the animation.
	 * @throws IllegalArgumentException
	 *             if the given duration is less or equal than <code>0</code>.
	 */
	public void goToNext(int duration) {
		if (duration <= 0) {
			throw new IllegalArgumentException();
		}
		goToIncrement(true, duration);
	}

	private void goToIncrement(boolean next, int duration) {
		int increment = next ? 1 : -1;
		int nextItem = this.selectedIndex + increment;
		int widgetsCount = getChildrenCount();

		if (next) {
			if (nextItem == widgetsCount) {
				// Cannot go downward.
				return;
			}
		} else {
			if (nextItem == -1) {
				// Cannot go upward.
				return;
			}
		}
		goToInternal(nextItem, duration);
	}

	/**
	 * Selects a widget from its index.
	 *
	 * @param index
	 *            the widget index.
	 * @throws IllegalArgumentException
	 *             if the given index is not valid (between <code>0</code> and the number of items in the carousel).
	 */
	public void goTo(int index) {
		goToInternal(index, 0);
	}

	/**
	 * Animates the selection of a widget from its index.
	 *
	 * @param index
	 *            the widget index.
	 * @param duration
	 *            the duration of the animation.
	 * @throws IllegalArgumentException
	 *             if the given duration is less or equal than <code>0</code>.
	 * @throws IllegalArgumentException
	 *             if the given index is not valid (between <code>0</code> and the number of items in the carousel).
	 */
	public void goTo(int index, long duration) {
		if (duration <= 0) {
			throw new IllegalArgumentException();
		}
		goToInternal(index, duration);
	}

	private void goToInternal(int index, long duration) {
		int widgetsCount = getChildrenCount();
		if (index < 0 || index >= widgetsCount) {
			throw new IllegalArgumentException();
		}

		SwipeEventHandler swipeEventHandler = this.swipeEventHandler;
		if (swipeEventHandler == null) {
			this.selectedIndex = index;
			return;
		}

		int size = 0;
		int currentIndex = 0;
		Widget[] children = getChildren();
		for (int i = 0; i < children.length; i++) {
			if (currentIndex == index) {
				if (duration == 0) {
					swipeEventHandler.moveTo(size);
				} else {
					swipeEventHandler.moveTo(size, duration);
				}
				break;
			}
			currentIndex++;

			size += this.widgetHeight;
		}
	}

	@Override
	public synchronized void onMove(final int position) {
		onMoveInternal(position);
		requestRender();
	}

	private void onMoveInternal(int position) {
		int widgetsCount = getChildrenCount();
		int widgetHeight = this.widgetHeight;
		int totalHeight = (widgetsCount - 1) * widgetHeight;
		if (position < 0) {
			position /= 4;
		} else if (position > totalHeight) {
			int diff = position - totalHeight;
			position = totalHeight + diff / 4;
		}
		this.position = position;

		int width = getContentWidth();
		int height = getContentHeight();
		int center = Math.min(width, height) / 2;
		int widgetHalfHeight = widgetHeight / 2;

		int selectedIndex = Math.round((float) position / widgetHeight);
		selectedIndex = XMath.limit(selectedIndex, 0, widgetsCount - 1);
		int remaining = (selectedIndex * widgetHeight) - position;
		int selectedY = center - widgetHalfHeight + remaining;

		int y = selectedY;
		for (int i = selectedIndex; i < widgetsCount; i++) {
			ApplicationListItem widget = (ApplicationListItem) getChild(i);
			int distanceToCenter = Math.abs((y + widgetHalfHeight) - center);
			float percent = getPercent(distanceToCenter, height);
			widget.setPercent(percent);
			int currentWidgetHeight = (int) (widgetHeight * percent);
			layOutChild(widget, 0, y, width, currentWidgetHeight);
			y += currentWidgetHeight;
		}
		y = selectedY;
		for (int i = selectedIndex - 1; i >= 0; i--) {
			ApplicationListItem widget = (ApplicationListItem) getChild(i);
			int distanceToCenter = Math.abs((y - widgetHalfHeight) - center);
			float percent = getPercent(distanceToCenter, height);
			widget.setPercent(percent);
			int currentWidgetHeight = (int) (widgetHeight * percent);
			y -= currentWidgetHeight;
			layOutChild(widget, 0, y, width, currentWidgetHeight);
		}
		setSelectedIndex(selectedIndex);
	}

	private float getPercent(int distanceToCenter, int height) {
		float percent = (float) Math.cos(distanceToCenter * Math.PI / (MAGNIFY_RATIO * height));
		percent = XMath.limit(percent, 0, 1);
		if (percent < MINIMAL_RATIO) {
			percent = 0;
		}
		return percent;
	}

	@Override
	protected void renderContent(GraphicsContext g, int contentWidth, int contentHeight) {
		this.tracer.recordEvent(ApplicationTraceEvents.DRAW_MAIN_LIST_PAGE);

		// Draw stop images on top and bottom.
		int position = this.position;
		int widgetsCount = getChildrenCount();
		int widgetHeight = this.widgetHeight;
		int totalHeight = (widgetsCount - 1) * widgetHeight;

		int width = getContentWidth();
		int height = getContentHeight();
		g.setColor(this.topStopImage.readPixel(0, 0));
		Painter.fillRectangle(g, 0, -position - height, width, height);
		Painter.drawImage(g, this.topStopImage, 0, -position);
		int bottomImageHeight = this.bottomStopImage.getHeight();
		Painter.fillRectangle(g, 0, height + totalHeight - position, width, height);
		Painter.drawImage(g, this.bottomStopImage, 0, height - bottomImageHeight + totalHeight - position);

		g.setColor(ARC_BACKGROUND_COLOR);
		ShapePainter.drawThickFadedCircleArc(g, 0, ARC_SPACING, contentWidth - 2 * ARC_SPACING, -ARC_BACKGROUND_ANGLE,
				2 * ARC_BACKGROUND_ANGLE, ARC_THICKNESS, ARC_FADE, ARC_CAPS, ARC_CAPS);
		g.setColor(ARC_CURSOR_COLOR);
		float positionAngle = (2 * ARC_BACKGROUND_ANGLE - ARC_CURSOR_ANGLE) * position / totalHeight
				- (ARC_BACKGROUND_ANGLE - ARC_CURSOR_ANGLE);
		ShapePainter.drawThickFadedCircleArc(g, 0, ARC_SPACING, contentWidth - 2 * ARC_SPACING, -positionAngle,
				ARC_CURSOR_ANGLE, ARC_THICKNESS, ARC_FADE, ARC_CAPS, ARC_CAPS);

		super.renderContent(g, contentWidth, contentHeight);
		MicroUI.callSerially(new Runnable() {

			@Override
			public void run() {
				ApplicationListCarousel.this.tracer.recordEventEnd(ApplicationTraceEvents.DRAW_MAIN_LIST_PAGE);
			}
		});
	}

}
