/*
 * Java
 *
 * Copyright 2017-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package ej.widget.basic.drawing;

import ej.annotation.Nullable;
import ej.drawing.ShapePainter;
import ej.microui.display.GraphicsContext;
import ej.motion.linear.LinearMotion;
import ej.mwt.animation.Animation;
import ej.mwt.style.Style;
import ej.mwt.util.Size;
import ej.widget.basic.PagingIndicator;

/**
 * The bullet paging indicator is a widget that displays a set of dots following a line. A bigger dot indicates the
 * selected index.
 * <p>
 * The bullet paging indicator is hidden by sliding smoothly the dots outside the drawing area.
 */
public class BulletPagingIndicator extends PagingIndicator implements Animation {

	/** The constant value for the cursor size. */
	public static final int CURSOR_SIZE_STYLE = 0;

	private static final int DEFAULT_CURSOR_SIZE = 20;

	private static final int HIDE_DURATION = 300;

	private int bulletsPosition;
	@Nullable
	private LinearMotion motion;

	/**
	 * Creates an horizontal bullet paging indicator.
	 */
	public BulletPagingIndicator() {
		super();
	}

	/**
	 * Creates a bullet paging indicator.
	 *
	 * @param horizontal
	 *            <code>true</code> if the paging indicator is horizontal, <code>false</code> otherwise.
	 */
	public BulletPagingIndicator(boolean horizontal) {
		super(horizontal);
	}

	@Override
	protected void onHidden() {
		super.onHidden();
		cancelHide();
	}

	@Override
	public void show() {
		cancelHide();
		this.bulletsPosition = 0;
	}

	private void cancelHide() {
		getDesktop().getAnimator().stopAnimation(this);
	}

	@Override
	public void hide() {
		int bulletSize;
		if (isHorizontal()) {
			bulletSize = getHeight();
		} else {
			bulletSize = getWidth();
		}
		this.motion = new LinearMotion(this.bulletsPosition, bulletSize, HIDE_DURATION);
		getDesktop().getAnimator().startAnimation(this);
	}

	@Override
	public boolean tick(long currentTimeMillis) {
		// Cursors showing/hiding tick.
		LinearMotion motion = this.motion;
		if (motion == null) {
			return false;
		}
		this.bulletsPosition = motion.getCurrentValue();
		requestRender();
		return !motion.isFinished();
	}

	@Override
	protected void renderContent(GraphicsContext g, int contentWidth, int contentHeight) {
		Style style = getStyle();
		g.setColor(style.getColor());

		boolean horizontal = isHorizontal();
		int itemsCount = getItemsCount();
		if (itemsCount == 0) {
			// Nothing to draw.
			return;
		}

		int currentItemIndex = getSelectedItem();
		int nextItemIndex = (currentItemIndex + 1) % itemsCount;

		// Compute sizes and initial position.
		int bigSize;
		int x;
		int y;
		if (horizontal) {
			bigSize = contentHeight - 1; // Minus 1 for antialiasing.
			x = (contentWidth - bigSize * itemsCount) / 2;
			y = contentHeight / 2 + this.bulletsPosition;
		} else {
			bigSize = contentWidth - 1; // Minus 1 for antialiasing.
			x = contentWidth / 2 + this.bulletsPosition;
			y = (contentHeight - bigSize * itemsCount) / 2;
		}
		int smallSize = bigSize / 2;
		int percentSize = (int) (smallSize * getPercent());
		int nextSize = bigSize - percentSize;
		int currentSize = smallSize + percentSize;
		// Draw dots.
		for (int i = 0; i < itemsCount; i++) {
			int pointSize;
			if (i == currentItemIndex) {
				pointSize = currentSize;
			} else if (i == nextItemIndex) {
				pointSize = nextSize;
			} else {
				pointSize = smallSize;
			}

			if (horizontal) {
				ShapePainter.drawThickFadedPoint(g, x + bigSize / 2, y, pointSize, 1);
				x += bigSize;
			} else {
				ShapePainter.drawThickFadedPoint(g, x, y + bigSize / 2, pointSize, 1);
				y += bigSize;
			}
		}
	}

	@Override
	protected void computeContentOptimalSize(Size size) {
		Style style = getStyle();

		int baseSize = style.getExtraInt(CURSOR_SIZE_STYLE, DEFAULT_CURSOR_SIZE);
		if ((baseSize & 0x1) == 1) {
			// Make this size event to make sure the half is exactly the half!
			baseSize++;
		}
		// Add 1 for antialiasing for every dimension.
		int referenceSize = baseSize * getItemsCount() + 1;
		if (isHorizontal()) {
			size.setSize(referenceSize, baseSize + 1);
		} else {
			size.setSize(baseSize + 1, referenceSize);
		}
	}

}
