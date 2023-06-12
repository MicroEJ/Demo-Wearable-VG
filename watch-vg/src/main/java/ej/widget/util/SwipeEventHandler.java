/*
 * Java
 *
 * Copyright 2017-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package ej.widget.util;

import ej.annotation.Nullable;
import ej.bon.Util;
import ej.bon.XMath;
import ej.microui.event.Event;
import ej.microui.event.EventHandler;
import ej.microui.event.generator.Pointer;
import ej.motion.Motion;
import ej.motion.MotionFactory;
import ej.motion.quart.QuartEaseOutMotion;
import ej.mwt.animation.Animation;
import ej.mwt.animation.Animator;
import ej.mwt.event.DesktopEventGenerator;
import ej.mwt.event.PointerEventDispatcher;
import ej.widget.util.swipe.SwipeListener;
import ej.widget.util.swipe.Swipeable;

/**
 * The swipe event handler responsibility is to detect pointer events (press, drag, release) and help moving over some
 * element(s).
 * <p>
 * When the pointer is pressed and dragged, the content follows the pointer.
 * <p>
 * On pointer release, the moved content stops progressively (depending on the speed of the movement).
 * <p>
 * The content may be cyclic, that means that when a bound is reached (beginning or end), the other bound is displayed.
 *
 * @since 2.3.0
 */
public class SwipeEventHandler implements EventHandler {

	/**
	 * Minimal delay between the last drag event and the release event to consider that the user stops moving before
	 * releasing: not a swipe.
	 */
	private static final int RELEASE_WITH_NO_MOVE_DELAY = 100;
	/**
	 * Minimal ratio between max speed during drag and final speed to consider that the user stops moving before
	 * releasing: not a swipe.
	 */
	private static final float RELEASE_SPEED_RATIO = 0.6f;
	/**
	 * Default animation duration.
	 */
	public static final int DEFAULT_DURATION = 800;

	private final int size;
	private final boolean cyclic;
	private final boolean horizontal;
	private final Swipeable swipeable;
	private final int itemInterval;
	@Nullable
	private final int[] itemsSize;

	@Nullable
	private SwipeListener swipeListener;

	@Nullable
	private Animator animator;
	private long duration;
	private MotionFactory motionFactory;

	private boolean pressed;
	private int initialValue;
	private int pressX;
	private int pressY;
	private int pressCoordinate;
	private long pressTime;
	private int previousCoordinate;
	private int totalShift;
	private boolean forward;
	private int currentValue;
	private boolean dragged;
	private long lastDragTime;
	private float maxSpeed;

	@Nullable
	private Animation animation;
	private boolean animationStarted;

	/**
	 * Creates a swipe event handler on an element.
	 *
	 * @param size
	 *            the available size.
	 * @param cyclic
	 *            <code>true</code> if the element to swipe is cyclic, <code>false</code> otherwise.
	 * @param horizontal
	 *            <code>true</code> if the element to swipe is horizontal, <code>false</code> otherwise.
	 * @param swipeable
	 *            the swipeable.
	 * @throws IllegalArgumentException
	 *             if the given size is lesser than or equal to 0.
	 */
	public SwipeEventHandler(int size, boolean cyclic, boolean horizontal, Swipeable swipeable) {
		this(size, cyclic, 0, null, horizontal, swipeable);
	}

	/**
	 * Creates a swipe event handler on a collection.
	 * <p>
	 * Equivalent to {@link #SwipeEventHandler(int, int, boolean, boolean, boolean, Swipeable)} without snapping to the
	 * collection items.
	 *
	 * @param itemCount
	 *            the number of items.
	 * @param itemInterval
	 *            the interval between items center.
	 * @param cyclic
	 *            <code>true</code> if the collection to swipe is cyclic, <code>false</code> otherwise.
	 * @param horizontal
	 *            <code>true</code> if the collection to swipe is horizontal, <code>false</code> otherwise.
	 * @param swipeable
	 *            the swipeable.
	 * @throws NullPointerException
	 *             if the given swipeable is <code>null</code>.
	 * @throws IllegalArgumentException
	 *             if the given item count or interval is lesser than or equal to 0.
	 */
	public SwipeEventHandler(int itemCount, int itemInterval, boolean cyclic, boolean horizontal, Swipeable swipeable) {
		this(itemCount, itemInterval, cyclic, false, horizontal, swipeable);
	}

	/**
	 * Creates a swipe event handler on a collection.
	 * <p>
	 * If the snap parameter is set, when the pointer is released, the swipeable is snapped to the closest snapping
	 * point (i.e. the closest multiple of interval). See {@link #moveTo(int, long)} for more information.
	 *
	 * @param itemCount
	 *            the number of items.
	 * @param itemInterval
	 *            the interval between items center.
	 * @param cyclic
	 *            <code>true</code> if the collection to swipe is cyclic, <code>false</code> otherwise.
	 * @param snapToItem
	 *            <code>true</code> if the items are snapped, <code>false</code> otherwise.
	 * @param horizontal
	 *            <code>true</code> if the collection to swipe is horizontal, <code>false</code> otherwise.
	 * @param swipeable
	 *            the swipeable.
	 * @throws NullPointerException
	 *             if the given swipeable is <code>null</code>.
	 * @throws IllegalArgumentException
	 *             if the given item count or interval is lesser than or equal to 0.
	 */
	public SwipeEventHandler(int itemCount, int itemInterval, boolean cyclic, boolean snapToItem, boolean horizontal,
			Swipeable swipeable) {
		this(computeSize(itemCount, itemInterval, cyclic), cyclic, snapToItem ? itemInterval : 0, null, horizontal,
				swipeable);
		if (/* itemCount <= 0 || */itemInterval <= 0) {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Creates a swipe event handler on a collection with heterogeneous sizes.
	 * <p>
	 * If the snap parameter is set, when the pointer is released, the swipeable is snapped to the closest snapping
	 * point (i.e. the closest item offset). See {@link #moveTo(int, long)} for more information.
	 *
	 * @param itemsSize
	 *            the size of the items.
	 * @param cyclic
	 *            <code>true</code> if the collection to swipe is cyclic, <code>false</code> otherwise.
	 * @param snapToItem
	 *            <code>true</code> if the items are snapped, <code>false</code> otherwise.
	 * @param horizontal
	 *            <code>true</code> if the collection to swipe is horizontal, <code>false</code> otherwise.
	 * @param swipeable
	 *            the swipeable.
	 * @throws NullPointerException
	 *             if the given swipeable is <code>null</code>.
	 * @throws IllegalArgumentException
	 *             if one of the given item size is lesser than or equal to 0.
	 */
	public SwipeEventHandler(int[] itemsSize, boolean cyclic, boolean snapToItem, boolean horizontal,
			Swipeable swipeable) {
		this(computeSize(itemsSize, cyclic), cyclic, snapToItem ? 1 : 0, itemsSize, horizontal, swipeable);
	}

	private SwipeEventHandler(int size, boolean cyclic, int itemInterval, @Nullable int[] itemsSize, boolean horizontal,
			Swipeable swipeable) {
		assert swipeable != null;
		if (size <= 0) {
			throw new IllegalArgumentException();
		}
		this.size = size;
		this.cyclic = cyclic;
		this.itemInterval = itemInterval;
		if (itemsSize != null) {
			this.itemsSize = itemsSize.clone();
		} else {
			this.itemsSize = null;
		}
		this.horizontal = horizontal;
		this.swipeable = swipeable;
		this.duration = DEFAULT_DURATION;
		this.motionFactory = new MotionFactory() {
			@Override
			public Motion createMotion(int start, int stop, long duration) {
				return new QuartEaseOutMotion(start, stop, duration);
			}
		};
	}

	/**
	 * Computes the size necessary to swipe a collection of items.
	 *
	 * @param itemCount
	 *            the number of items.
	 * @param itemInterval
	 *            the interval between items center.
	 * @param cyclic
	 *            <code>true</code> if the element to swipe is cyclic, <code>false</code> otherwise.
	 * @return the size to swipe.
	 */
	private static int computeSize(int itemCount, int itemInterval, boolean cyclic) {
		return (itemCount - (cyclic ? 0 : 1)) * itemInterval;
	}

	/**
	 * Computes the size necessary to swipe a collection of heterogeneous items.
	 *
	 * @param itemsSize
	 *            the size of the items.
	 * @param cyclic
	 *            <code>true</code> if the element to swipe is cyclic, <code>false</code> otherwise.
	 * @return the size to swipe.
	 */
	private static int computeSize(int[] itemsSize, boolean cyclic) {
		int totalSize = 0;
		for (int size : itemsSize) {
			if (size <= 0) {
				throw new IllegalArgumentException();
			}
			totalSize += size;
		}
		if (!cyclic) {
			totalSize -= itemsSize[itemsSize.length - 1];
		}
		return totalSize;
	}

	/**
	 * Sets the animator.
	 *
	 * @param animator
	 *            the animator to set.
	 */
	public void setAnimator(Animator animator) {
		this.animator = animator;
	}

	/**
	 * Sets the duration.
	 *
	 * @param duration
	 *            the duration to set.
	 */
	public void setDuration(long duration) {
		this.duration = duration;
	}

	/**
	 * Sets the motion factory.
	 *
	 * @param motionFactory
	 *            the motion factory to set.
	 */
	public void setMotionFactory(MotionFactory motionFactory) {
		this.motionFactory = motionFactory;
	}

	/**
	 * Sets the swipe listener.
	 *
	 * @param swipeListener
	 *            the swipe listener to set or <code>null</code>.
	 */
	public void setSwipeListener(@Nullable SwipeListener swipeListener) {
		this.swipeListener = swipeListener;
	}

	/**
	 * Forces the end of the animation.
	 */
	public void stop() {
		stopAnimation();
	}

	private int getCoordinate(int x, int y) {
		if (this.horizontal) {
			return x;
		} else {
			return y;
		}
	}

	private void updateCurrentStep(int currentStep) {
		if (this.cyclic) {
			currentStep = modulo(currentStep, this.size);
		}
		this.currentValue = currentStep;
		this.swipeable.onMove(currentStep);
	}

	@Override
	public boolean handleEvent(int event) {
		int type = Event.getType(event);
		if (type == Pointer.EVENT_TYPE) {
			Pointer pointer = (Pointer) Event.getGenerator(event);
			int pointerX = pointer.getX();
			int pointerY = pointer.getY();
			int pointerCoordinate = getCoordinate(pointerX, pointerY);
			int action = Pointer.getAction(event);
			switch (action) {
			case Pointer.PRESSED:
				onPointerPressed(pointerX, pointerY);
				break;
			case Pointer.DRAGGED:
				return onPointerDragged(pointerX, pointerY);
			case Pointer.RELEASED:
				return onPointerReleased(pointerCoordinate);
			}
		} else if (type == DesktopEventGenerator.EVENT_TYPE) {
			int action = DesktopEventGenerator.getAction(event);
			if (action == PointerEventDispatcher.EXITED) {
				onPointerReleased(this.previousCoordinate);
			}
		}
		return false;
	}

	/**
	 * Called when the pointer is pressed.
	 *
	 * @param pointerX
	 *            the pointer x coordinate.
	 * @param pointerY
	 *            the pointer y coordinate.
	 */
	private void onPointerPressed(int pointerX, int pointerY) {
		stopAnimation();

		long currentTimeMillis = Util.platformTimeMillis();
		this.pressed = true;
		this.pressTime = currentTimeMillis;
		this.pressX = pointerX;
		this.pressY = pointerY;
		int pressCoordinate = getCoordinate(pointerX, pointerY);
		this.pressCoordinate = pressCoordinate;
		this.previousCoordinate = pressCoordinate;
		this.initialValue = this.currentValue;
		this.totalShift = 0;
		this.dragged = false;

		this.lastDragTime = currentTimeMillis;
		this.maxSpeed = 0;
	}

	/**
	 * Called when the pointer is dragged.
	 *
	 * @param pointerX
	 *            the pointer x coordinate.
	 * @param pointerY
	 *            the pointer y coordinate.
	 * @return <code>true</code> if the event is consumed, <code>false</code> otherwise.
	 */
	private boolean onPointerDragged(int pointerX, int pointerY) {
		int pointerCoordinate = getCoordinate(pointerX, pointerY);
		if (!this.pressed) {
			onPointerPressed(pointerX, pointerY);
			return false;
		} else {
			long currentTime = Util.platformTimeMillis();
			int shift = pointerCoordinate - this.previousCoordinate;

			boolean dragged;
			if (!this.dragged) {
				// The first time, we need to check that the move is in the right direction.
				// Otherwise, a scroll included in another one (with a different direction) can prevent the other
				// one to work.
				int shiftX = Math.abs(pointerX - this.pressX);
				int shiftY = Math.abs(pointerY - this.pressY);
				dragged = this.horizontal ? (shiftX > shiftY) : (shiftY > shiftX);
			} else {
				dragged = shift != 0;
			}

			this.dragged |= dragged;
			if (dragged) {
				this.lastDragTime = currentTime;
				notifyStartAnimation();

				this.totalShift += shift;
				// Update screen.
				updateCurrentStep(this.initialValue - this.totalShift);
				this.previousCoordinate = pointerCoordinate;

				boolean currentForward = shift > 0;
				if (this.totalShift != 0 && currentForward != this.forward) {
					// Change way.
					this.pressCoordinate = pointerCoordinate;
					this.pressTime = currentTime;
					this.forward = currentForward;
					this.maxSpeed = 0;
				} else {
					float speed = getSpeed(pointerCoordinate, currentTime);
					if (this.forward) {
						this.maxSpeed = Math.min(this.maxSpeed, speed);
					} else {
						this.maxSpeed = Math.max(this.maxSpeed, speed);
					}
				}
			}
			return dragged;
		}
	}

	private float getSpeed(int pointerCoordinate, long currentTime) {
		return -(float) (pointerCoordinate - this.pressCoordinate) / (2 * (currentTime - this.pressTime));
	}

	/**
	 * Called when the pointer is released.
	 * <p>
	 * The given pointer coordinate depends on the orientation of the content:
	 * <ul>
	 * <li>x if the content is horizontal,</li>
	 * <li>y if the content is vertical.</li>
	 * </ul>
	 *
	 * @param pointerCoordinate
	 *            pointer coordinate.
	 * @return <code>true</code> if the event is consumed, <code>false</code> otherwise.
	 */
	private boolean onPointerReleased(int pointerCoordinate) {
		int currentValue = this.currentValue;
		long duration = this.duration;
		int stop;
		long delay;
		if (this.dragged) {
			long currentTime = Util.platformTimeMillis();

			int limitedStep = limit(currentValue);
			float speed = getSpeed(pointerCoordinate, currentTime);
			if (!this.cyclic && limitedStep != currentValue) {
				// Limits exceeded.
				stop = limitedStep;
				delay = duration / 2;
			} else if ((currentTime - this.lastDragTime < RELEASE_WITH_NO_MOVE_DELAY)
					&& (speed / this.maxSpeed > RELEASE_SPEED_RATIO)) {
				// Launch it!
				stop = (int) (currentValue + speed * duration);
				delay = duration;
			} else {
				// Avoid moving if the user pauses after dragging.
				stop = currentValue;
				delay = duration / 2;
			}
		} else {
			// The user has potentially interrupted an animation. The onStop() has not been triggered.
			stop = currentValue;
			delay = duration / 2;
		}
		if (!this.cyclic) {
			// Bound value.
			stop = limit(stop);
		}
		moveTo(stop, delay);
		return this.dragged;
	}

	/**
	 * Limits a position between 0 and the content size.
	 *
	 * @param position
	 *            the position to limit.
	 * @return the limited position.
	 */
	public int limit(int position) {
		return XMath.limit(position, 0, this.size);
	}

	/**
	 * Moves to a position.
	 * <p>
	 * If the snap parameter is set, the given position is snapped to the closest multiple of the interval size between
	 * elements.
	 *
	 * @param position
	 *            the position to set.
	 */
	public void moveTo(int position) {
		stopAnimation();
		updateCurrentStep(position);
	}

	/**
	 * Snaps a position to an item if the snap parameter has been set.
	 *
	 * @param position
	 *            the position to snap.
	 * @return the snapped position.
	 */
	private int snap(int position) {
		int itemInterval = this.itemInterval;
		int[] itemsSize = this.itemsSize;
		if (itemsSize != null && itemInterval != 0) {
			// Find the nearest item.
			int currentSize = (int) Math.floor((float) position / this.size) * this.size;
			for (int itemSize : itemsSize) {
				int nextSize = itemSize + currentSize;
				if (position < nextSize) {
					if (position - currentSize < nextSize - position) {
						// Nearer to the current position.
						return currentSize;
					} else {
						// Nearer to the next position.
						return nextSize;
					}
				}
				currentSize = nextSize;
			}
		} else if (itemInterval != 0) {
			// Snap the given position.
			return Math.round((float) position / itemInterval) * itemInterval;
		}
		// Not snapped.
		return position;
	}

	/**
	 * Animates the move to a position.
	 * <p>
	 * If the snap parameter is set, the given position is snapped to the closest multiple of the interval size between
	 * elements.
	 *
	 * @param stop
	 *            the expected end position.
	 * @param duration
	 *            the duration of the animation.
	 */
	public void moveTo(int stop, long duration) {
		stopAnimation();
		stop = snap(stop);

		if (stop == this.currentValue) {
			// Nothing to animate…
			notifyStopAnimation();
			return;
		}

		Animator animator = this.animator;
		if (animator == null) {
			// No animator set, move there instantly
			updateCurrentStep(stop);
			return;
		}

		final Motion moveMotion = this.motionFactory.createMotion(this.currentValue, stop, duration);
		Animation animation = new Animation() {
			@Override
			public boolean tick(long currentTimeMillis) {
				boolean finished = moveMotion.isFinished();
				int value = moveMotion.getCurrentValue();
				updateCurrentStep(value);
				if (finished) {
					notifyStopAnimation();
				}
				return !finished;
			}
		};
		this.animation = animation;
		notifyStartAnimation();
		animator.startAnimation(animation);
	}

	private void stopAnimation() {
		Animation animation = this.animation;
		if (animation != null) {
			Animator animator = this.animator;
			assert (animator != null);
			animator.stopAnimation(animation);
		}
	}

	private int modulo(int index, int length) {
		index = index % length;
		if (index < 0) {
			index += length;
		}
		return index;
	}

	private void notifyStartAnimation() {
		if (!this.animationStarted) {
			this.animationStarted = true;

			SwipeListener swipeListener = this.swipeListener;
			if (swipeListener != null) {
				swipeListener.onSwipeStarted();
			}
		}
	}

	private void notifyStopAnimation() {
		if (this.animationStarted) {
			SwipeListener swipeListener = this.swipeListener;
			if (swipeListener != null) {
				swipeListener.onSwipeStopped();
			}

			this.animationStarted = false;
		}
	}
}
