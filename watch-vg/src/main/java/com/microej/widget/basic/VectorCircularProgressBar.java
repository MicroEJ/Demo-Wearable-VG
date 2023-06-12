/*
 * Java
 *
 * Copyright 2020-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.widget.basic;

import com.microej.demo.watch.util.widget.basic.BoundedRange;
import com.microej.demo.watch.util.widget.basic.model.DefaultBoundedRangeModel;
import com.microej.path.basic.CircleArc;
import com.microej.path.basic.CircleArcBuilder;

import ej.microui.MicroUI;
import ej.microui.display.GraphicsContext;
import ej.mwt.style.Style;
import ej.mwt.util.Size;

/**
 * A widget that displays a circular progress bar using vector paths.
 */
public class VectorCircularProgressBar extends BoundedRange {

	/** The extra field ID for the circle arc style. */
	public static final int CIRCLE_ARC_STYLE = 0;

	private CircleArc circleArc;

	private final int startAngle;

	private final int endAngle;

	/**
	 * Creates a circular progress bar given the progress range (minimum, maximum), the initial value, and the circle
	 * arc start and maximum angles.
	 *
	 * <p>
	 * The arc is drawn from <code>startAngle</code> up to <code>maximumArcAngle</code> degrees. The center of the arc
	 * is defined as the center of the widget's bounds.<br>
	 * <br>
	 * Angles are interpreted such that 0 degrees is at the 3 o'clock position. A positive <code>maximumArcAngle</code>
	 * value indicates a counter-clockwise rotation while a negative value indicates a clockwise rotation.<br>
	 *
	 * @param minimum
	 *            the minimum value for this progress bar.
	 * @param maximum
	 *            the maximum value for this progress bar.
	 * @param initialValue
	 *            the initial progress value.
	 * @param startAngle
	 *            the start angle of the arc to draw, in degrees.
	 * @param maximumArcAngle
	 *            the maximum angular extent of the arc from <code>startAngle</code>, in degrees.
	 *
	 */
	public VectorCircularProgressBar(int minimum, int maximum, int initialValue, int startAngle, int maximumArcAngle) {
		super(new DefaultBoundedRangeModel(minimum, maximum, initialValue));

		this.startAngle = startAngle;
		this.endAngle = startAngle + maximumArcAngle;

	}

	@Override
	protected void computeContentOptimalSize(Size size) {
		// nothing to do, take all available size given by parent.
	}

	@Override
	protected void renderContent(GraphicsContext g, int contentWidth, int contentHeight) {
		assert this.circleArc != null;
		this.circleArc.render(g);
	}

	@Override
	protected void onLaidOut() {
		super.onLaidOut();
		createCircleArc();
	}

	private void createCircleArc() {
		closeCircleArc();

		Style style = getStyle();
		CircleArcBuilder builder = style.getExtraObject(CIRCLE_ARC_STYLE, CircleArcBuilder.class,
				new CircleArcBuilder());
		builder.setStartAngle(this.startAngle);
		builder.setSize(new Size(getWidth(), getHeight()));
		builder.setCenter(getWidth() / 2, getHeight() / 2);
		this.circleArc = builder.build();
		updateArcAngle();
	}

	private void updateArcAngle() {
		CircleArc arc = this.circleArc;
		if (arc != null) {
			float angle = (this.endAngle - this.startAngle) * getPercentComplete();
			arc.setArcAngle(angle);
		}
	}

	@Override
	protected void onDetached() {
		super.onDetached();
		closeCircleArc();
	}

	private void closeCircleArc() {
		CircleArc arc = this.circleArc;
		if (arc != null) {
			this.circleArc = null;
		}
	}

	@Override
	public void setValue(int value) {
		updateValue(value);
		requestRender();
	}

	@Override
	public void updateValue(int value) {
		super.updateValue(value);
		MicroUI.callSerially(new Runnable() {

			@Override
			public void run() {
				updateArcAngle();
			}
		});
	}
}
