/*
 * Java
 *
 * Copyright 2017-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package ej.widget.container.transition;

import ej.annotation.Nullable;
import ej.microui.MicroUI;
import ej.motion.Motion;
import ej.motion.MotionFactory;
import ej.motion.quart.QuartEaseOutMotion;
import ej.mwt.Container;
import ej.mwt.Widget;
import ej.mwt.animation.Animation;
import ej.mwt.util.Size;

/**
 * A transition container holds only one widget at a time. When a new widget is shown (replacing the current one), an
 * animated transition is done.
 *
 * @since 2.3.0
 */
public abstract class TransitionContainer extends Container implements Animation {

	/**
	 * Default transition duration.
	 */
	public static final int DURATION = 300;

	private MotionFactory motionFactory;
	private int duration;

	@Nullable
	private Motion motion;

	/**
	 * Creates a transition container with a motion factory that creates {@link QuartEaseOutMotion} and a default
	 * duration of {@value #DURATION}.
	 */
	public TransitionContainer() {
		this.motionFactory = new MotionFactory() {
			@Override
			public Motion createMotion(int start, int stop, long duration) {
				return new QuartEaseOutMotion(start, stop, duration);
			}
		};
		this.duration = DURATION;
	}

	/**
	 * Sets the motion factory to use for the animations.
	 *
	 * @param motionFactory
	 *            the motion factory to set.
	 */
	public void setMotionFactory(MotionFactory motionFactory) {
		assert (motionFactory != null);
		this.motionFactory = motionFactory;
	}

	/**
	 * Gets the motion factory.
	 *
	 * @return the motion factory.
	 */
	public MotionFactory getMotionFactory() {
		return this.motionFactory;
	}

	/**
	 * Sets the animation duration.
	 *
	 * @param duration
	 *            the duration to set.
	 */
	public void setDuration(int duration) {
		this.duration = duration;
	}

	/**
	 * Gets the animation duration.
	 *
	 * @return the duration.
	 */
	public int getDuration() {
		return this.duration;
	}

	/**
	 * Creates a motion based on the registered motion factory and duration.
	 *
	 * @param start
	 *            the start value.
	 * @param stop
	 *            the stop value.
	 * @return the created motion.
	 *
	 * @see #setMotionFactory(MotionFactory)
	 * @see #setDuration(int)
	 */
	protected Motion createMotion(int start, int stop) {
		Motion motion = this.motionFactory.createMotion(start, stop, this.duration);
		assert (motion != null);
		return motion;
	}

	/**
	 * Shows a new widget.
	 *
	 * @param widget
	 *            the new widget to show.
	 * @param forward
	 *            <code>true</code> if going forward, <code>false</code> otherwise.
	 * @throws IllegalArgumentException
	 *             if the specified widget is already in a hierarchy (already contained in a container or desktop).
	 */
	public abstract void show(Widget widget, boolean forward);

	@Override
	protected void onHidden() {
		super.onHidden();
		stopAnimation();
	}

	/**
	 * Creates a motion and starts an animation.
	 * <p>
	 * The registered animation listeners will be notified.
	 * <p>
	 * The callers of this method must be careful to start the animation if and only if the widget is actually visible.
	 *
	 * @param widget
	 *            the new widget.
	 * @param previousWidget
	 *            the previous widget.
	 * @param forward
	 *            <code>true</code> if going forward, <code>false</code> otherwise.
	 * @param start
	 *            the start value of the motion.
	 * @param stop
	 *            the stop value of the motion.
	 */
	protected void startAnimation(Widget widget, Widget previousWidget, boolean forward, int start, int stop) {
		// if this is invisible, the animation is not started and the context is reset
		if (isShown()) {
			this.motion = createMotion(start, stop);
			getDesktop().getAnimator().startAnimation(this);
		} else {
			resetContext();
		}
	}

	/**
	 * Stops the animation.
	 * <p>
	 * The registered animation listeners will be notified.
	 * <p>
	 * If no animation is running, nothing changes.
	 */
	protected void stopAnimation() {
		if (this.motion != null) {
			getDesktop().getAnimator().stopAnimation(this);
			this.motion = null;
		}
	}

	/**
	 * Cleans up the context after the animation. The animation may not have been started.
	 */
	protected void resetContext() {
		// nothing to be done at this level, subclasses may add their own context cleanup.
	}

	/**
	 * Cancels the animation (stops it and resets the context).
	 */
	protected void cancelAnimation() {
		stopAnimation();
		resetContext();
	}

	@Override
	public boolean tick(long currentTimeMillis) {
		Motion motion = this.motion;
		if (motion == null) {
			return false;
		}
		boolean finished = motion.isFinished();
		if (finished) {
			// Do the last step of the animation before finalizing the animation (the steps are supposed to be fast, the
			// finalization may be long).
			updateStep(motion.getStopValue());
			MicroUI.callSerially(new Runnable() {
				@Override
				public void run() {
					resetContext();
					TransitionContainer.this.motion = null;
				}
			});
			return false;
		} else {
			updateStep(motion.getCurrentValue());
			return true;
		}
	}

	/**
	 * Updates the current step.
	 * <p>
	 * Called during the animation using the current value of the motion.
	 *
	 * @param step
	 *            the current step.
	 */
	protected abstract void updateStep(int step);

	@Override
	protected void computeContentOptimalSize(Size size) {
		int width = 0;
		int height = 0;

		// compute child optimal size
		if (getChildrenCount() == 1) {
			Widget child = getChild(0);
			computeChildOptimalSize(child, size.getWidth(), size.getHeight());
			width = child.getWidth();
			height = child.getHeight();
		}

		// set container optimal size
		size.setSize(width, height);
	}

	@Override
	protected void layOutChildren(int contentWidth, int contentHeight) {
		// lay out child
		if (getChildrenCount() == 1) {
			Widget widget = getChild(0);
			setBounds(widget, 0, 0, contentWidth, contentHeight);
		}
	}

	/**
	 * Sets the bounds of its child if one and only one.
	 */
	protected void setChildBounds() {
		if (getChildrenCount() == 1) {
			Widget widget = getChild(0);
			setBounds(widget, getContentX(), getContentY(), getContentWidth(), getContentHeight());
		}
	}

	private void setBounds(Widget widget, int contentX, int contentY, int contentWidth, int contentHeight) {
		computeChildOptimalSize(widget, contentWidth, contentHeight);
		layOutChild(widget, contentX, contentY, contentWidth, contentHeight);
	}

}
