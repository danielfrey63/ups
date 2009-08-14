/* ====================================================================
 *  Copyright 2004-2005 www.xmatrix.ch
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 * ====================================================================
 */
package ch.jfactory.color;

import ch.jfactory.reflection.ReflectionUtils;
import java.awt.Color;
import net.infonode.util.ImageUtils;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.4 $ $Date: 2006/03/14 21:27:55 $
 */
public class ColorUtils {

    /**
     * Fades the given color by the given percentage towards white. The higher the percentage, the more it is faded
     * faded towards white.
     *
     * @param color      the original color
     * @param percentage the percentage to fade
     * @return the faded color
     */
    public static Color fade(final Color color, final double percentage) {

        final int red = color.getRed();
        final int green = color.getGreen();
        final int blue = color.getBlue();

        return new Color(getFaded(red, percentage), getFaded(green, percentage), getFaded(blue, percentage));
    }

    public static Color darken(final Color color, final double percentage) {

        final int red = color.getRed();
        final int green = color.getGreen();
        final int blue = color.getBlue();

        return new Color(getDarkened(red, percentage), getDarkened(green, percentage), getDarkened(blue, percentage));
    }

    /**
     * Adds the amount of alpha to the color. The more alpha you add, the more transparent the color becomes.
     *
     * @param color the color to add alpha
     * @param alpha the amount of alpha to add
     * @return a new color that is partly transparent
     */
    public static Color alpha(final Color color, final double alpha) {

        final int red = color.getRed();
        final int green = color.getGreen();
        final int blue = color.getBlue();

        return new Color(red, green, blue, (int) (255 * alpha));
    }

    private static int getFaded(final int channel, final double percentage) {
        return channel + (int) ((255 - channel) * percentage);
    }

    private static int getDarkened(final int chanel, final double percentage) {
        return chanel - (int) (chanel * percentage);
    }

    /**
     * Returns the Color object assigned by the name. The name might be a hexadecimal number preceeded by a "#" or a
     * color name as defined in the java.awt.Color class. Passing in a null or non-existing Color does return the
     * default color.
     *
     * @param name of the color
     * @return Color object
     */
    public static Color parseColor(final String name) {
        return parseColor(name, Color.black);
    }

    /**
     * Returns the Color object assigned by the name. The name might be a hexadecimal number preceeded by a "#" or a
     * color name as defined in the java.awt.Color class. Passing in a null or non-existing Color does return the
     * default color.
     *
     * @param name         of the color
     * @param defaultColor the default color in case no matching color is found
     * @return Color object
     */
    public static Color parseColor(final String name, final Color defaultColor) {
        if (name == null) {
            return defaultColor;
        }
        else if (name.startsWith("#")) {
            try {
                return Color.decode(name);
            }
            catch (NumberFormatException nf) {
                return defaultColor;
            }
        }
        else {
            ReflectionUtils.getField(name, defaultColor, Color.class, Color.class);
        }
        return defaultColor;
    }

    public static boolean isGray(final Color color) {
        return isGray(color.getRGB());
    }

    public static boolean isGray(final int pixel) {
        final int r = ImageUtils.getRed(pixel);
        final int g = ImageUtils.getGreen(pixel);
        final int b = ImageUtils.getBlue(pixel);
        return (r == g) && (g == b);
    }

    public static int getBrightness(final int pixel) {
        final int grayPixel = getGray(pixel);
        return ImageUtils.getRed(grayPixel);
    }

    /**
     * Returns the average of all three rgb channels. Alpha is preserved.
     *
     * @param pixel the value of the color
     * @return the value of the gray color
     */
    public static int getGray(final int pixel) {
        final int r = ImageUtils.getRed(pixel);
        final int g = ImageUtils.getGreen(pixel);
        final int b = ImageUtils.getBlue(pixel);
        final int a = ImageUtils.getAlpha(pixel);
        final int avg = (r + g + b) / 3;
        return ((a & 0xFF) << 24) | ((avg & 0xFF) << 16) | ((avg & 0xFF) << 8) | ((avg & 0xFF));
    }
}
