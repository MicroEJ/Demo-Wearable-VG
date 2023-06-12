/*
 * Java
 *
 * Copyright 2019-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.demo.watch.watchface.flowerlp;

import com.microej.demo.watch.util.KernelServiceRegistry;
import com.microej.demo.watch.util.WatchImageLoader;
import com.microej.demo.watch.util.computer.WatchAngleComputer;
import com.microej.demo.watch.util.helper.TimeHelper;
import com.microej.demo.watch.util.path.watchface.Hand;
import com.microej.demo.watch.util.services.TimeService;
import com.microej.demo.watch.util.widget.watchface.VectorWatchface;
import com.microej.demo.watch.watchface.flowerlp.path.FlowerLPHourHand;
import com.microej.demo.watch.watchface.flowerlp.path.FlowerLPMinuteHand;
import com.microej.demo.watch.watchface.flowerlp.path.FlowerLPSecondHand;
import com.nxp.rt595.util.PowerManagementHelper;

import ej.bon.Timer;
import ej.bon.TimerTask;
import ej.bon.Util;
import ej.microui.display.GraphicsContext;
import ej.microui.display.Painter;
import ej.microui.display.ResourceImage;
import ej.mwt.util.Alignment;
import ej.mwt.util.Rectangle;
import ej.observable.Observer;
import ej.service.ServiceLoader;

/**
 * The "FlowerLP" watchface displays vector hands and a "radar effect" using vector gradient.
 */
public class FlowerLPWatchface extends VectorWatchface implements Observer {

	private static final String BACKGROUND_IMAGE_PATH = "flowerlp_background"; //$NON-NLS-1$
	private static final String FOREGROUND_IMAGE_PATH = "flowerlp_center"; //$NON-NLS-1$

	/**
	 * Defines the minimal width and height the clip has to have. The value is a
	 * region divisor: minimal clip size cannot be smaller than region divided by
	 * this divisor.
	 */
	private static final int MINIMAL_CONTENT_DIV = 8;

	/**
	 * Defines the angle tolerance between 0, 90, 180 and 270 degrees the minimal
	 * clip size must be used.
	 */
	private static final int MINIMAL_CONTENT_ANGLE = 5;

	/**
	 * Watchface is updated every second.
	 */
	private static final int REFRESH_TIME_MS = 1_000;

	private static final int RIGHT_ANGLE = 90;
	private static final int FLAT_ANGLE = 180;
	private static final int ANGLE_270 = 270;
	private static final int FULL_ANGLE = 360;

	private final TimeService timeService;

	private ResourceImage backgroundImage;
	private ResourceImage foregroundImage;
	private Rectangle oldRenderingArea;

	private TimerTask updateTimerTask;

	private boolean renderFullClock;
	private int timeOffset;

	/**
	 * Constructs the flowerlp watchface.
	 */
	public FlowerLPWatchface() {
		// Services
		ServiceLoader kernelServiceLoader = KernelServiceRegistry.getServiceLoader();
		this.timeService = kernelServiceLoader.getService(TimeService.class);
	}

	@Override
	protected void renderContent(GraphicsContext g, int contentWidth, int contentHeight) {
		updateHandsAngle();
		restoreBackground(g, contentWidth, contentHeight);
		super.renderContent(g, contentWidth, contentHeight);
		renderImage(g, contentWidth, contentHeight, this.foregroundImage);
	}

	private void restoreBackground(GraphicsContext g, int contentWidth, int contentHeight) {

		// get secondsHand's area
		Rectangle newRenderingArea = setBounds(contentWidth, contentHeight, secondsHandAngle);
		Rectangle oldRenderingArea = this.oldRenderingArea;

		if (null != oldRenderingArea) {

			// merge previous clip with new clip to erase previous drawing

			int pX = oldRenderingArea.getX();
			int pY = oldRenderingArea.getY();
			int nX = newRenderingArea.getX();
			int nY = newRenderingArea.getY();

			int clipX1 = Math.min(pX, nX);
			int clipX2 = Math.max(pX + oldRenderingArea.getWidth(), nX + newRenderingArea.getWidth()) - 1;
			int clipY1 = Math.min(pY, nY);
			int clipY2 = Math.max(pY + oldRenderingArea.getHeight(), nY + newRenderingArea.getHeight()) - 1;

			// restore background
			g.setClip(clipX1, clipY1, clipX2 - clipX1 + 1, clipY2 - clipY1 + 1);
			renderImage(g, contentWidth, contentHeight, this.backgroundImage);

			// increase clip to bypass the path & matrix approximations
			g.setClip(clipX1, clipY1, clipX2 - clipX1 + 2, clipY2 - clipY1 + 2);
		}
		else {
			// clip is already reseted by MWT to full rendering area
			renderImage(g, contentWidth, contentHeight, this.backgroundImage);
		}

		// save secondsHand's area for next rendering
		this.oldRenderingArea = newRenderingArea;
	}

	private void renderImage(GraphicsContext g, int contentWidth, int contentHeight, ResourceImage image) {
		int x = Alignment.computeLeftX(image.getWidth(), 0, contentWidth, Alignment.HCENTER);
		int y = Alignment.computeTopY(image.getHeight(), 0, contentHeight, Alignment.VCENTER);
		Painter.drawImage(g, image, x, y);
	}

	private Rectangle setBounds(int contentWidth, int contentHeight, float secondsHandAngle) {

		// hand center point
		int x = contentWidth / 2;
		int y = contentHeight / 2;
		int areaWidth;
		int areaHeight;

		if ((secondsHandAngle > -MINIMAL_CONTENT_ANGLE && secondsHandAngle < MINIMAL_CONTENT_ANGLE)
				|| (secondsHandAngle > FLAT_ANGLE - MINIMAL_CONTENT_ANGLE
						&& secondsHandAngle < FLAT_ANGLE + MINIMAL_CONTENT_ANGLE)) {

			// use minimal width and maximal height
			areaWidth = contentWidth / MINIMAL_CONTENT_DIV;
			areaHeight = contentHeight / 2;

			// center clip around vertical axis
			x -= areaWidth / 2;
			y = adjustYAnchor(y, areaHeight, secondsHandAngle);
		}
		else if ((secondsHandAngle > RIGHT_ANGLE - MINIMAL_CONTENT_ANGLE
				&& secondsHandAngle < RIGHT_ANGLE + MINIMAL_CONTENT_ANGLE)
				|| (secondsHandAngle > ANGLE_270 - MINIMAL_CONTENT_ANGLE
						&& secondsHandAngle < ANGLE_270 + MINIMAL_CONTENT_ANGLE)) {

			// use minimal height and maximal width
			areaWidth = contentWidth / 2;
			areaHeight = contentHeight / MINIMAL_CONTENT_DIV;

			// center clip around horizontal axis
			y -= areaHeight / 2;
			x = adjustXAnchor(x, areaWidth, secondsHandAngle);

		} else {
			// retrieve rectangle that encompasses the seconds hand without considering the
			// hand's thickness
			areaHeight = (int) (Math.abs(Math.cos(Math.toRadians(secondsHandAngle))) * contentHeight / 2);
			areaWidth = (int) (Math.abs(Math.sin(Math.toRadians(secondsHandAngle))) * contentWidth / 2);

			x = adjustXAnchor(x, areaWidth, secondsHandAngle);
			y = adjustYAnchor(y, areaHeight, secondsHandAngle);
		}

		return new Rectangle(x, y, areaWidth, areaHeight);
	}

	private int adjustXAnchor(int x, int handWidth, float angle) {
		return (angle >= FLAT_ANGLE && angle <= FULL_ANGLE) ? x - handWidth : x;
	}

	private int adjustYAnchor(int y, int handHeight, float angle) {
		return (angle <= RIGHT_ANGLE || angle >= ANGLE_270) ? y - handHeight : y;
	}

	@Override
	protected void onAttached() {
		super.onAttached();
		PowerManagementHelper.setPerfProfile(PowerManagementHelper.PERF_PROFILE_POWER_SAVING);
		this.backgroundImage = WatchImageLoader.loadImage(BACKGROUND_IMAGE_PATH);
		this.foregroundImage = WatchImageLoader.loadImage(FOREGROUND_IMAGE_PATH);
	}

	@Override
	protected void onDetached() {
		super.onDetached();
		PowerManagementHelper.setPerfProfile(PowerManagementHelper.PERF_PROFILE_MAX_PERFS);
		ResourceImage image = this.foregroundImage;
		assert image != null;
		image.close();

	}

	@Override
	protected Hand createHourHand() {
		return new FlowerLPHourHand();
	}

	@Override
	protected Hand createMinuteHand() {
		return new FlowerLPMinuteHand();
	}

	@Override
	protected Hand createSecondHand() {
		return new FlowerLPSecondHand();
	}


	@Override
	protected void onShown() {
		// super.onShown() not called to avoid AbstractWatchface update animation
		renderFullClock = true;
		timeOffset = 0;
		startUpdateTimer();
		this.timeService.setObserver(this);
	}

	@Override
	protected void onHidden() {
		this.timeService.unsetObserver(this);
		stopUpdateTimer();
		// super.onHidden(); not call like super.onShown()
	}

	@Override
	public void update() {
		// force to render the full clock because the time has changed
		renderFullClock = true;
	}

	private void startUpdateTimer() {

		TimerTask timertask = new TimerTask() {

			@Override
			public void run() {
				requestRender();
			}
		};

		this.updateTimerTask = timertask;
		Timer timer = KernelServiceRegistry.getServiceLoader().getService(Timer.class);
		timer.schedule(timertask, REFRESH_TIME_MS, REFRESH_TIME_MS);
	}

	private void stopUpdateTimer() {

		TimerTask timertask = this.updateTimerTask;
		if (timertask != null) {
			timertask.cancel();
			this.updateTimerTask = null;
		}
	}

	protected void updateHandsAngle() {

		WatchAngleComputer angleComputer = this.angleComputer;

		boolean renderFullClock = this.renderFullClock;
		this.renderFullClock = false;

		long time = Util.currentTimeMillis() + timeOffset;
		float seconds = TimeHelper.computeSeconds(time);

		// next update will target the time + 1s
		timeOffset = REFRESH_TIME_MS;

		setSecondAngle(angleComputer.getSecondAngle(seconds));

		if (renderFullClock || seconds < 1) {
			// full rendering is forced or a new minute has started: update minute and hour
			// hands
			this.oldRenderingArea = null;

			float hours = TimeHelper.computeHour(time);
			setHourAngle(angleComputer.getHourAngle(hours));

			float minutes = TimeHelper.computeMinute(time);
			setMinuteAngle(angleComputer.getMinuteAngle(minutes));
		}
	}
}
