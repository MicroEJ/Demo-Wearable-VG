/*
 * Java
 *
 * Copyright 2019-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.demo.watch.util.helper;

/**
 * An utility class that provides convenient methods for manipulating colors.
 *
 */
public class ColorUtils {

	private static final int GREEN_SHIFT = 8;
	private static final int RED_SHIFT = 16;
	private static final int BLUE_MASK = 0xff;
	private static final int GREEN_MASK = 0xff00;
	private static final int ALPHA_MASK = 0xff000000;
	private static final int RED_MASK = 0xff0000;

	private static final int ALPHA_SHIFT = 24;

	private ColorUtils() {
	}

	/**
	 * Blends two colors and returns the resulting color.
	 *
	 * <p>
	 * A ratio of 0.0f will output the first color argument, while a ratio of 1.0f will output the second color
	 * argument.
	 * </p>
	 *
	 * @param startColor
	 *            the start color.
	 * @param endColor
	 *            the end color.
	 * @param ratio
	 *            the ratio to apply.
	 * @return the resulting color of the blend.
	 */
	public static int blend(final int startColor, final int endColor, final float ratio) {

		if (startColor == endColor) {
			return startColor;
		}

		if (ratio >= 1f) {
			return endColor;
		} else if (ratio <= 0f) {
			return startColor;
		}

		final float complementRatio = 1f - ratio;

		final int firstAlpha = ((startColor & ALPHA_MASK) >> ALPHA_SHIFT);
		final int firstRed = ((startColor & RED_MASK) >> RED_SHIFT);
		final int firstGreen = ((startColor & GREEN_MASK) >> GREEN_SHIFT);
		final int firstBlue = (startColor & BLUE_MASK);

		final int secondAlpha = ((endColor & ALPHA_MASK) >> ALPHA_SHIFT);
		final int secondRed = ((endColor & RED_MASK) >> RED_SHIFT);
		final int secondGreen = ((endColor & GREEN_MASK) >> GREEN_SHIFT);
		final int secondBlue = (endColor & BLUE_MASK);

		final int alpha = (int) ((firstAlpha * complementRatio) + (secondAlpha * ratio));
		final int red = (int) ((firstRed * complementRatio) + (secondRed * ratio));
		final int green = (int) ((firstGreen * complementRatio) + (secondGreen * ratio));
		final int blue = (int) ((firstBlue * complementRatio) + (secondBlue * ratio));

		return (alpha << ALPHA_SHIFT) | (red << RED_SHIFT) | (green << GREEN_SHIFT) | blue;
	}

	/**
	 * Creates a color gradient from/to the specified start color and end color with the given lentgh.
	 *
	 * @param startColor
	 *            the start color.
	 * @param endColor
	 *            the end color.
	 * @param length
	 *            the length of the gradient.
	 * @return an array containing all the colors of the resulting gradient.
	 */
	public static int[] makeGradient(final int startColor, final int endColor, final int length) {
		final int[] array = new int[length];

		final int startAlpha = ((startColor & ALPHA_MASK) >> ALPHA_SHIFT);
		final int startRed = ((startColor & RED_MASK) >> RED_SHIFT);
		final int startGreen = ((startColor & GREEN_MASK) >> GREEN_SHIFT);
		final int startBlue = (startColor & BLUE_MASK);

		final int endAlpha = ((endColor & ALPHA_MASK) >> ALPHA_SHIFT);
		final int endRed = ((endColor & RED_MASK) >> RED_SHIFT);
		final int endGreen = ((endColor & GREEN_MASK) >> GREEN_SHIFT);
		final int endBlue = (endColor & BLUE_MASK);

		for (int i = 0; i < length; i++) {
			float ratio = (float) i / length;
			float compRatio = 1 - ratio;
			int alpha = (int) (startAlpha * compRatio + endAlpha * ratio);
			int red = (int) (startRed * compRatio + endRed * ratio);
			int green = (int) (startGreen * compRatio + endGreen * ratio);
			int blue = (int) (startBlue * compRatio + endBlue * ratio);
			array[i] = (alpha << ALPHA_SHIFT) + (red << RED_SHIFT) + (green << GREEN_SHIFT) + blue;
		}

		return array;
	}

}
