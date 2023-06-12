/*
 * Java
 *
 * Copyright 2020-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej;

import java.util.logging.Logger;

import com.microej.demo.watch.util.ImageLoader;
import com.microej.demo.watch.util.KernelServiceRegistry;
import com.microej.demo.watch.util.WatchImageLoader;
import com.microej.demo.watch.util.WatchfaceApp;
import com.microej.demo.watch.util.services.ActivityService;
import com.microej.demo.watch.util.services.HeartRateService;
import com.microej.demo.watch.util.services.NavigationService;
import com.microej.demo.watch.util.services.NotificationService;
import com.microej.demo.watch.util.services.PowerService;
import com.microej.demo.watch.util.services.StepsService;
import com.microej.demo.watch.util.services.TimeService;
import com.microej.demo.watch.util.services.VectorFontService;
import com.microej.demo.watch.watchface.flower.FlowerWatchfaceApp;
import com.microej.demo.watch.watchface.flowerlp.FlowerLPWatchfaceApp;
import com.microej.demo.watch.watchface.sport.SportWatchfaceApp;
import com.microej.helper.MainListPositions;
import com.microej.helper.PageResolver;
import com.microej.kernel.WatchfacesRegistry;
import com.microej.kernel.WatchfacesRegistryListener;
import com.microej.page.MainList;
import com.microej.page.WatchfacesCarouselPage;
import com.microej.service.ActivityServiceImpl;
import com.microej.service.DefaultTimeService;
import com.microej.service.HeartRateServiceImpl;
import com.microej.service.NotificationServiceImpl;
import com.microej.service.PowerServiceImpl;
import com.microej.service.StepsServiceImpl;
import com.microej.service.VectorFontServiceImpl;
import com.microej.style.StylesheetFactory;
import com.microej.widget.container.WatchTransitionContainer;
import com.nxp.rt595.util.PowerManagementHelper;

import ej.bon.Timer;
import ej.bon.Util;
import ej.microui.MicroUI;
import ej.microui.event.Event;
import ej.microui.event.generator.Command;
import ej.mwt.Desktop;
import ej.mwt.Widget;
import ej.mwt.event.EventDispatcher;
import ej.mwt.event.PointerEventDispatcher;
import ej.service.ServiceRegistry;

/**
 * Core class of the demo.
 */
public class Demo {

	private static final Logger LOGGER = Logger.getLogger(Demo.class.getName());

	private static final String TIME_SERVICE_PROPERTY = "demo.time.service.impl"; //$NON-NLS-1$

	private static final String PREVIEW = "unknown_preview"; //$NON-NLS-1$

	private static final long START_DATE = 1666109149000l;

	private static final int ANIMATION_DURATION = 200;

	private static WatchfacesRegistry watchfacesRegistry;

	/** The position of the last selected element in the main list. */
	private static int currentPosition;

	private static int currentWatchface;

	private static WatchTransitionContainer transitionContainer;

	private static Desktop kernelDesktop;

	private Demo() {
		// private constructor
	}

	/**
	 * Starts the demo using the KF watchfaces registry.
	 *
	 * @param args
	 *            the arguments of the main function.
	 */
	public static void main(String[] args) {
		// Start MicroUI
		MicroUI.start();

		// Set current time
		Util.setCurrentTimeMillis(START_DATE);

		// Create time service
		TimeService timeService = createTimeService();

		// Create the global timer
		final Timer timer = new Timer(false);
		new Thread(new Runnable() {
			@Override
			public void run() {
				timer.run();
			}
		}, "Timer").start(); //$NON-NLS-1$

		// Register kernel services
		ServiceRegistry kernelServiceRegistry = KernelServiceRegistry.getServiceRegistry();
		kernelServiceRegistry.register(Timer.class, timer);
		kernelServiceRegistry.register(TimeService.class, timeService);
		kernelServiceRegistry.register(NavigationService.class, createNavigationService());
		kernelServiceRegistry.register(VectorFontService.class, new VectorFontServiceImpl());
		kernelServiceRegistry.register(HeartRateService.class, new HeartRateServiceImpl());
		kernelServiceRegistry.register(NotificationService.class, new NotificationServiceImpl());
		kernelServiceRegistry.register(PowerService.class, new PowerServiceImpl());
		kernelServiceRegistry.register(StepsService.class, new StepsServiceImpl());
		kernelServiceRegistry.register(ActivityService.class, new ActivityServiceImpl());

		// Create watchfaces registry
		WatchfacesRegistry watchfacesRegistry = new WatchfacesRegistry();
		watchfacesRegistry.addWatchface(new FlowerWatchfaceApp());
		watchfacesRegistry.addWatchface(new SportWatchfaceApp());
		watchfacesRegistry.addWatchface(new FlowerLPWatchfaceApp());

		Demo.watchfacesRegistry = watchfacesRegistry;

		// Add watchfaces registry listener
		watchfacesRegistry.addListener(new WatchfacesRegistryListener() {
			@Override
			public void onWatchfaceInstalled(int watchfaceIndex) {
				showWatchface(watchfaceIndex);
			}

			@Override
			public void onWatchfaceUninstalled() {
				int numWatchfaces = Demo.watchfacesRegistry.getNumWatchfaces();
				currentWatchface = Math.min(currentWatchface, numWatchfaces - 1);
			}
		});

		// Initialize power manager and start everything possible
		PowerManagementHelper.initialize();

		// Initialize UI
		kernelDesktop = createDesktop();
		transitionContainer = createRootWidget();
		kernelDesktop.setWidget(transitionContainer);
		showWatchface(0);

		// Start time service
		timeService.start();
	}

	private static WatchTransitionContainer createRootWidget() {
		WatchTransitionContainer widget = new WatchTransitionContainer();
		widget.setDuration(ANIMATION_DURATION);
		return widget;
	}

	private static Desktop createDesktop() {
		Desktop desktop = new Desktop() {

			@Override
			protected EventDispatcher createEventDispatcher() {
				return new PointerEventDispatcher(this) {

					@Override
					public boolean dispatchEvent(int event) {
						if (Event.getType(event) == Command.EVENT_TYPE) {
							transitionContainer.handleEvent(event);
							return true;
						}

						return super.dispatchEvent(event);
					}
				};
			}
		};
		desktop.setStylesheet(StylesheetFactory.newStylesheet());
		return desktop;
	}

	private static WatchfacesCarouselPage createWatchfaceCarousel() {
		WatchfacesCarouselPage carousel = new WatchfacesCarouselPage() {
			@Override
			protected void onShown() {
				super.onShown();
				goTo(currentWatchface);
			}
		};

		int numWatchfaces = watchfacesRegistry.getNumWatchfaces();
		for (int i = 0; i < numWatchfaces; i++) {
			carousel.addWatchface(i, watchfacesRegistry.getWatchfacePreview(i));
		}

		if (numWatchfaces == 0) {
			ImageLoader imageLoader = new WatchImageLoader(PREVIEW);
			carousel.addWatchface(0, imageLoader);
		}

		return carousel;
	}

	/**
	 * Shows the main list page.
	 */
	public static void showMainPage() {
		showInstantly(new MainList(currentPosition));
	}

	/**
	 * Shows the Watchfaces Carousel.
	 */
	public static void showWatchfacesCarousel() {
		showInstantly(createWatchfaceCarousel());
	}

	/**
	 * Shows a page instantly.
	 *
	 * @param page
	 *            the page to show, identified by its name.
	 */
	public static void showInstantly(final String page) {
		if (page.equals(MainList.getClockMessage())) {
			showWatchface(Demo.currentWatchface);
		} else {
			showInstantly(PageResolver.resolvePage(page));
		}
	}

	/**
	 * Shows a page instantly.
	 *
	 * @param page
	 *            the page to show.
	 */
	private static void showInstantly(final Widget page) {
		MicroUI.callSerially(new Runnable() {
			@Override
			public void run() {
				transitionContainer.showInstantly(page);
			}
		});
		kernelDesktop.requestShow();
	}

	/**
	 * Shows a watchface instantly.
	 *
	 * @param watchfacePosition
	 *            the position of the watchface to show.
	 */
	public static void showWatchface(final int watchfacePosition) {
		currentPosition = MainListPositions.CLOCK;
		currentWatchface = watchfacePosition;

		callSafely(new Runnable() {
			@Override
			public void run() {
				if (!watchfacesRegistry.startWatchface(watchfacePosition)) {
					showWatchfacesCarousel();
				}
			}
		});
	}

	private static void hideCurrentWatchface(final boolean showMainPage) {
		callSafely(new Runnable() {
			@Override
			public void run() {
				watchfacesRegistry.stopCurrentWatchface();
				if (showMainPage) {
					Demo.showMainPage();
				} else {
					Demo.showWatchfacesCarousel();
				}
			}
		});
	}

	/**
	 * Shows a page with an animation, using a transition container (see {@link WatchTransitionContainer}).
	 *
	 * @param page
	 *            the page to show, identified by its name.
	 */
	public static void showWithAnimation(final String page) {
		if (page.equals(MainList.getClockMessage())) {
			showWatchface(Demo.currentWatchface);
		} else {
			MicroUI.callSerially(new Runnable() {
				@Override
				public void run() {
					transitionContainer.showWithAnimation(PageResolver.resolvePage(page), true);
				}
			});
			kernelDesktop.requestShow();
		}
	}

	/**
	 * Sets the current page's position in the main list.
	 *
	 * @param position
	 *            the position of the current page.
	 */
	public static void setCurrentPosition(int position) {
		currentPosition = position;
	}

	private static NavigationService createNavigationService() {
		return new NavigationService() {
			@Override
			public void showMainPage() {
				hideCurrentWatchface(true);
			}

			@Override
			public void showWatchfacesCarousel() {
				hideCurrentWatchface(false);
			}

			@Override
			public void registerWatchface(WatchfaceApp watchface) {
				watchfacesRegistry.addWatchface(watchface);
			}
		};
	}

	private static TimeService createTimeService() {
		String timeServiceImplName = System.getProperty(TIME_SERVICE_PROPERTY);
		if (timeServiceImplName != null) {
			LOGGER.info("Using custom time service"); //$NON-NLS-1$
			try {
				return (TimeService) Class.forName(timeServiceImplName).newInstance();
			} catch (ReflectiveOperationException e) {
				LOGGER.info("Could not create time service implementation: " + e.getMessage()); //$NON-NLS-1$
			}
		}

		LOGGER.info("Using default time service"); //$NON-NLS-1$
		return new DefaultTimeService();
	}

	/**
	 * If called from UI thread, executes the runnable in a separate UI event. Otherwise, executes the runnable
	 * immediately.
	 * <p>
	 * This method can be used to make sure that a runnable will not switch the owner of the MicroUI pump and induce a
	 * desktop hide when executed from a UI event. It is a workaround for the M0092MEJAUI-2285 issue.
	 */
	private static void callSafely(Runnable runnable) {
		if (MicroUI.isUIThread()) {
			MicroUI.callSerially(runnable);
		} else {
			runnable.run();
		}
	}
}
