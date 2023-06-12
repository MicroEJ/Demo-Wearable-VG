/*
 * Java
 *
 * Copyright 2019-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.widget;

import ej.bon.Util;
import ej.microui.display.GraphicsContext;
import ej.microvg.Matrix;
import ej.microvg.VectorGraphicsPainter;
import ej.microvg.VectorImage;
import ej.mwt.Widget;
import ej.mwt.animation.Animation;
import ej.mwt.util.Size;

/**
 * A widget that animates the MicroEJ vector mascot.
 */
public class VectorMascot extends Widget {

	private VectorImage mascot;
	private long startTime;
	private long currentTime;

	private Animation animation;

	@Override
	protected void renderContent(GraphicsContext g, int contentWidth, int contentHeight) {
		Matrix matrix = new Matrix();
		long elapsed = (this.currentTime - this.startTime);
		matrix.setScale(contentWidth / this.mascot.getWidth(), contentHeight / this.mascot.getHeight());
		VectorGraphicsPainter.drawAnimatedImage(g, this.mascot, matrix, elapsed);

		if (elapsed > this.mascot.getDuration()) {
			this.startTime = this.currentTime;
		}
	}

	@Override
	protected void computeContentOptimalSize(Size size) {
		// nothing to do: take available size
	}

	@Override
	protected void onShown() {
		super.onShown();
		Animation animation = new Animation() {

			@Override
			public boolean tick(long currentTimeMillis) {
				VectorMascot.this.currentTime = currentTimeMillis;
				requestRender();
				return true;
			}
		};
		this.animation = animation;
		this.startTime = Util.platformTimeMillis();
		getDesktop().getAnimator().startAnimation(animation);
	}

	@Override
	protected void onHidden() {
		getDesktop().getAnimator().stopAnimation(this.animation);
		super.onHidden();
	}

	@Override
	protected void onLaidOut() {
		super.onLaidOut();

		this.mascot = VectorImage.getImage("/images/mascot/mascot.xml"); //$NON-NLS-1$
		this.currentTime = 0;
	}

	@Override
	protected void onDetached() {
		super.onDetached();
	}
}
