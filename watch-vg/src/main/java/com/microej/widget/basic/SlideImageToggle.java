/*
 * Java
 *
 * Copyright 2020-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.widget.basic;

import com.microej.demo.watch.util.WatchImageLoader;

import ej.microui.display.GraphicsContext;
import ej.microui.display.Image;
import ej.microui.display.Painter;
import ej.microui.display.ResourceImage;
import ej.motion.Motion;
import ej.motion.quart.QuartEaseInOutMotion;
import ej.mwt.animation.Animation;
import ej.mwt.util.Alignment;
import ej.mwt.util.Size;
import ej.widget.basic.Box;

/**
 * A toggle using images to render a toggle box, with a slide animation.
 */
public class SlideImageToggle extends Box implements Animation {

	private static final String TOGGLE_OFF_PATH = "parameters/switch-dot-off"; //$NON-NLS-1$

	private static final String TOGGLE_ON_PATH = "parameters/switch-dot-on"; //$NON-NLS-1$

	private static final String TOGGLE_BACKGROUND_PATH = "parameters/switch-bkg"; //$NON-NLS-1$

	private static final int MAX_TRANSLATION = 29;

	private static final long ANIM_DURATION = 300;

	private ResourceImage checkedImage;

	private ResourceImage uncheckedImage;

	private ResourceImage backgroundImage;

	private Motion motion;

	private int offset;

	private boolean isAnimating;

	@Override
	protected void onAttached() {
		this.backgroundImage = WatchImageLoader.loadImage(TOGGLE_BACKGROUND_PATH);
		this.checkedImage = WatchImageLoader.loadImage(TOGGLE_ON_PATH);
		this.uncheckedImage = WatchImageLoader.loadImage(TOGGLE_OFF_PATH);
	}

	@Override
	protected void onDetached() {
		this.backgroundImage.close();
		this.checkedImage.close();
		this.uncheckedImage.close();
	}

	@Override
	public void setChecked(boolean checked) {
		boolean wasChecked = isChecked();
		if (checked != wasChecked) {
			if (isShown()) {
				startAnimation();
			} else {
				this.offset = checked ? MAX_TRANSLATION : 0;
			}
			super.setChecked(checked);
		}
	}

	private void startAnimation() {
		this.isAnimating = true;
		boolean checked = isChecked();
		int start = this.offset;
		int stop = checked ? 0 : MAX_TRANSLATION;
		this.motion = new QuartEaseInOutMotion(start, stop, ANIM_DURATION);
		getDesktop().getAnimator().startAnimation(this);
	}

	private void stopAnimation() {
		this.isAnimating = false;
		getDesktop().getAnimator().stopAnimation(this);
		this.motion = null;
	}

	@Override
	public boolean tick(long currentTimeMillis) {
		Motion motion = this.motion;
		if (motion == null) {
			return false;
		}
		boolean finished = motion.isFinished();
		if (finished) {
			this.isAnimating = false;
			updateStep(motion.getStopValue());
			return false;
		} else {
			updateStep(motion.getCurrentValue());
			return true;
		}
	}

	private void updateStep(int value) {
		this.offset = value;
		requestRender();
	}

	@Override
	protected void renderContent(GraphicsContext g, int contentWidth, int contentHeight) {

		// draw background
		ResourceImage background = this.backgroundImage;
		int x = Alignment.computeLeftX(background.getWidth(), 0, contentWidth, Alignment.HCENTER);
		int y = Alignment.computeTopY(background.getHeight(), 0, contentHeight, Alignment.VCENTER);
		Painter.drawImage(g, background, x, y);

		// draw sliding dot (on or off)
		boolean checked = isChecked();
		Image currentImage = (checked && !this.isAnimating ? this.checkedImage : this.uncheckedImage);
		Painter.drawImage(g, currentImage, x + this.offset, y);

	}

	@Override
	protected void computeContentOptimalSize(Size size) {
		ResourceImage background = this.backgroundImage;
		size.setSize(background.getWidth(), background.getHeight());
	}

	@Override
	protected void onHidden() {
		super.onHidden();
		stopAnimation();
	}
}
