/*
 * Java
 *
 * Copyright 2019-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.widget;

import ej.drawing.ShapePainter;
import ej.microui.display.GraphicsContext;
import ej.mwt.Widget;
import ej.mwt.animation.Animation;
import ej.mwt.util.Size;

/**
 * A widget that displays and animates vector circle arcs to demonstrates VG capabilities.
 */
public class VectorCircleArc extends Widget {

	private static final int SHAPE_THICKNESS = 15;

	private static final int SHAPE_COUNT = 10;

	private static final int DIAMETER = 300;

	private static final int BLUE = 0x800000FF;

	private static final int GREEN = 0x8000FF00;

	private static final int RED = 0x80FF0000;

	private static final int START_ANGLE = 120;

	private static final int ARC_ANGLE = 60;

	private static final int ANGLE_INCREMENT = 1;

	private int startAngle;

	private Animation animation;

	/**
	 * Creates a widget that shows vector circle arcs.
	 */
	public VectorCircleArc() {
		this.startAngle = 0;
	}

	@Override
	protected void computeContentOptimalSize(Size size) {
		// Take all available space
	}

	private void incrementAngle(int increment) {
		this.startAngle += increment;
	}

	@Override
	protected void renderContent(GraphicsContext g, int contentWidth, int contentHeight) {

		for (int i = 0; i < SHAPE_COUNT; i++) {
			int j = i + 1;

			int diameter = contentWidth - (j * (DIAMETER / SHAPE_COUNT));
			int x = (contentWidth - diameter) / 2;
			int y = (contentHeight - diameter) / 2;

			int angle = this.startAngle * j;
			g.setColor(RED);
			ShapePainter.drawThickCircleArc(g, x, y, diameter, angle, ARC_ANGLE, SHAPE_THICKNESS);

			angle += START_ANGLE;
			g.setColor(GREEN);
			ShapePainter.drawThickCircleArc(g, x, y, diameter, angle, ARC_ANGLE, SHAPE_THICKNESS);

			angle += START_ANGLE;
			g.setColor(BLUE);
			ShapePainter.drawThickCircleArc(g, x, y, diameter, angle, ARC_ANGLE, SHAPE_THICKNESS);
		}

		incrementAngle(ANGLE_INCREMENT);
	}

	@Override
	protected void onShown() {
		super.onShown();
		Animation animation = new Animation() {

			@Override
			public boolean tick(long currentTimeMillis) {
				requestRender();
				return true;
			}
		};
		this.animation = animation;
		getDesktop().getAnimator().startAnimation(animation);
	}

	@Override
	protected void onHidden() {
		getDesktop().getAnimator().stopAnimation(this.animation);
		super.onHidden();
	}
}
