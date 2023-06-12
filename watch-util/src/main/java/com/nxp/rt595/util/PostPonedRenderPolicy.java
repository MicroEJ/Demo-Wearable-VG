/*
 * Java
 *
 * Copyright 2021-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.nxp.rt595.util;

import ej.microui.MicroUI;
import ej.microui.display.Display;
import ej.microui.display.GraphicsContext;
import ej.mwt.Container;
import ej.mwt.Desktop;
import ej.mwt.Widget;
import ej.mwt.render.DefaultRenderPolicy;
import ej.mwt.render.RenderPolicy;

/**
 * Same behavior than {@link DefaultRenderPolicy} but when one widget is asked
 * to be rendered, a flush is not requested to the display. It will be requested
 * just before next widget rendering.
 * <p>
 * The aim is to run the flush action at the same time than the next frame
 * rendering. It allows to reduce the power consumption because the CPU sleeps
 * longer between two frames.
 * <p>
 * Notes:
 * <ul>
 * <li>A frame is flushed if and only if another frame is asked to be
 * rendered.</li>
 * <li>The last frame before hidding the desktop is never flushed.</li>
 * <li>Contrary to the {@link DefaultRenderPolicy}, when several widgets are
 * asked to be rendered in a row, as many flush actions are performed (except
 * the last).</li>
 * </ul>
 *
 */
public class PostPonedRenderPolicy extends RenderPolicy {

	/**
	 * Creates a postponed render policy.
	 *
	 * @param desktop
	 *            the desktop.
	 */
	public PostPonedRenderPolicy(Desktop desktop) {
		super(desktop);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * The graphics context is translated to the position of the parent of the
	 * widget. The clipping area is set to the intersection of the parent's bounds
	 * and the given bounds.
	 * <p>
	 * The desktop is immediately flushed just after its rendering and a new desktop
	 * rendering is asked. This second rendering is not flushed. The widget should
	 * adjust its model to render the frame that will be flushed at next call to
	 * {@link #requestRender(Widget, int, int, int, int)}.
	 */
	@Override
	public void renderDesktop() {
		Desktop desktop = getDesktop();
		Widget widget = desktop.getWidget();
		if (widget != null) {
			// reset translation and clip
			GraphicsContext g = Display.getDisplay().getGraphicsContext();
			g.resetTranslation();
			g.resetClip();

			// render widget
			desktop.renderWidget(g, widget);

			// flush the first frame immediately and start the next frame rendering
			widget.requestRender();
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * The previous widget rendering is flushed first. Then the new widget rendering
	 * starts. It will be flushed at next call to
	 * {@code #requestRender(Widget, int, int, int, int)}.
	 */
	@Override
	public void requestRender(Widget widget, int x, int y, int width, int height) {

		// flush previous frame (nothing is performed if frame has been already flushed)
		Display.getDisplay().flush();

		// render next frame
		while (widget.isTransparent()) {
			Container parent = widget.getParent();
			if (parent == null) {
				break;
			}
			// Render the parent.
			x += parent.getContentX() + widget.getX();
			y += parent.getContentY() + widget.getY();
			widget = parent;
		}
		asynchronousRender(widget, x, y, width, height);
	}

	private void asynchronousRender(final Widget widget, final int x, final int y, final int width, final int height) {
		MicroUI.callSerially(new Runnable() {
			@Override
			public void run() {
				executeRender(widget, x, y, width, height);
			}
		});
	}

	private void executeRender(Widget widget, int x, int y, int width, int height) {
		if (!widget.isAttached()) {
			return;
		}

		renderWidget(widget, x, y, width, height);
	}

	/**
	 * This method performs the increment render of the widget. Its implementation
	 * only renders the widget and not its siblings, but this behavior may be
	 * changed by overriding this method.
	 * <p>
	 * The given bounds are relative to the widget.
	 *
	 * @param widget
	 *            the widget to render.
	 * @param x
	 *            the x coordinate of the area to render.
	 * @param y
	 *            the y coordinate of the area to render.
	 * @param width
	 *            the width of the area to render.
	 * @param height
	 *            the height of the area to render.
	 */
	protected void renderWidget(Widget widget, int x, int y, int width, int height) {
		// reset translation and clip
		GraphicsContext g = Display.getDisplay().getGraphicsContext();
		g.resetTranslation();
		g.resetClip();

		// compute translation and clip
		setParentClip(widget, g);
		g.intersectClip(widget.getX() + x, widget.getY() + y, width, height);

		// render widget
		getDesktop().renderWidget(g, widget);
	}

	/**
	 * Clips and translates a graphics context to the absolute bounds of the parent
	 * of a widget.
	 * <p>
	 * It applies recursively a translation to each parent's coordinates, and a clip
	 * to its content size.
	 *
	 * @param widget
	 *            the widget to clip from.
	 * @param g
	 *            the graphics context to clip on.
	 */
	protected void setParentClip(Widget widget, GraphicsContext g) {
		Container parent = widget.getParent();
		if (parent != null) {
			setParentClip(parent, g);
			g.translate(parent.getX() + parent.getContentX(), parent.getY() + parent.getContentY());
			g.intersectClip(0, 0, parent.getContentWidth(), parent.getContentHeight());
		}
	}

}
