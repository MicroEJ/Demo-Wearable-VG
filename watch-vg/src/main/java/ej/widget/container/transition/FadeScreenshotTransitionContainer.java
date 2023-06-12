/*
 * Java
 *
 * Copyright 2017-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package ej.widget.container.transition;

import ej.bon.XMath;
import ej.microui.display.GraphicsContext;
import ej.microui.display.Painter;

/**
 * A fade transition container makes widgets appear or disappear smoothly by using a fade in or fade out effect.
 *
 * @since 2.3.0
 */
public class FadeScreenshotTransitionContainer extends ScreenshotTransitionContainer {

	private int previousStep;

	@Override
	public void render(GraphicsContext g) {
		if (isAnimating()) {
			int contentX = getContentX();
			int contentY = getContentY();
			int contentWidth = getContentWidth();
			int contentHeight = getContentHeight();
			g.translate(contentX, contentY);
			g.setClip(0, 0, contentWidth, contentHeight);

			int alpha;
			if (this.previousStep == GraphicsContext.OPAQUE) {
				alpha = this.step;
			} else {
				alpha = -(this.step - this.previousStep) * GraphicsContext.OPAQUE
						/ (this.previousStep - GraphicsContext.OPAQUE);
				alpha = XMath.limit(alpha, GraphicsContext.TRANSPARENT, GraphicsContext.OPAQUE);
			}
			assert this.newScreenshot != null;
			Painter.drawImage(g, this.newScreenshot, 0, 0, alpha);
			this.previousStep = this.step;
		} else {
			super.render(g);
		}
	}

	@Override
	protected int getStop(int contentWidth, int contentHeight) {
		return GraphicsContext.OPAQUE;
	}

	@Override
	protected void resetContext() {
		super.resetContext();
		this.previousStep = GraphicsContext.TRANSPARENT;
	}

}
