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
package ch.jfactory.image;

import ch.jfactory.color.ColorUtils;
import java.awt.Color;
import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;
import org.pietschy.command.AbstractReflectionIconFactory;

/**
 * Creates some new icon faces from a given icon.
 *
 * @author Daniel Frey
 * @version $Revision: 1.2 $ $Date: 2007/09/27 10:41:22 $
 */
public class SimpleIconFactory extends AbstractReflectionIconFactory
{

    private String pathPrefix = "";

    public SimpleIconFactory(final String pathPrefix)
    {
        this.pathPrefix = pathPrefix;
    }

    // Images

    public Image createDropShadowImage(final String iconString)
    {
        final Image image = getImage(iconString);
        return new DropShadowImage(image);
    }

    public Image createDropShadowImage(final String iconString, final String colorString)
    {
        final Image colorized = createColorizedImage(iconString, colorString);
        return new DropShadowImage(colorized);
    }

    public Image createDropShadowImage(final String iconString, final String colorString, final float factor)
    {
        final Image colorized = createColorizedImage(iconString, colorString, factor);
        return new DropShadowImage(colorized);
    }

    public Image createPressedImage(final String iconString)
    {
        final Image image = getImage(iconString);
        return new DropShadowImage(image, DropShadowImage.DEFAULT_SHADOW_SIZE, -1, -1);
    }

    public Image createPressedImage(final String iconString, final String colorString)
    {
        final Image colorized = createColorizedImage(iconString, colorString);
        return new DropShadowImage(colorized, DropShadowImage.DEFAULT_SHADOW_SIZE, -1, -1);
    }

    public Image createPressedImage(final String iconString, final String colorString, final float factor)
    {
        final Image colorized = createColorizedImage(iconString, colorString, factor);
        return new DropShadowImage(colorized, DropShadowImage.DEFAULT_SHADOW_SIZE, -1, -1);
    }

    public Image createColorizedImage(final String iconString, final String colorString)
    {
        final Image image = getImage(iconString);
        final Color color = ColorUtils.parseColor(colorString);
        return ImageUtils.createColorizedImage(image, color);
    }

    public Image createColorizedImage(final String iconString, final String colorString, final float factor)
    {
        final Image image = getImage(iconString);
        final Color color = ColorUtils.parseColor(colorString);
        return ImageUtils.createColorizedImage(image, color, factor);
    }

    public Image createColorizedImage(final String colorString, final int width, final int height)
    {
        final Color color = ColorUtils.parseColor(colorString);
        return ImageUtils.createColorizedImage(color, width, height);
    }

    // Icons

    public ImageIcon createDropShadowIcon(final String iconString)
    {
        final Image drop = createDropShadowImage(iconString);
        return new ImageIcon(drop);
    }

    public ImageIcon createDropShadowIcon(final String iconString, final String colorString)
    {
        final Image drop = createDropShadowImage(iconString, colorString);
        return new ImageIcon(drop);
    }

    public ImageIcon createDropShadowIcon(final String iconString, final String colorString, final String factor)
    {
        final float f = Float.parseFloat(factor);
        final Image drop = createDropShadowImage(iconString, colorString, f);
        return new ImageIcon(drop);
    }

    public ImageIcon createPressedIcon(final String iconString)
    {
        final Image drop = createPressedImage(iconString);
        return new ImageIcon(drop);
    }

    public ImageIcon createPressedIcon(final String iconString, final String colorString)
    {
        final Image drop = createPressedImage(iconString, colorString);
        return new ImageIcon(drop);
    }

    public ImageIcon createPressedIcon(final String iconString, final String colorString, final String factor)
    {
        final float f = Float.parseFloat(factor);
        final Image drop = createPressedImage(iconString, colorString, f);
        return new ImageIcon(drop);
    }

    public ImageIcon createColorizedIcon(final String iconString, final String colorString)
    {
        final Image colorized = createColorizedImage(iconString, colorString);
        return new ImageIcon(colorized);
    }

    public ImageIcon getIcon(final String iconString)
    {
        return new ImageIcon(getImage(iconString));
    }

    public Image getImage(final String iconString)
    {
        final String resourcePath;
        if (iconString.startsWith("/"))
        {
            resourcePath = iconString;
        }
        else
        {
            resourcePath = pathPrefix + "/" + iconString;
        }
        final URL resource = SimpleIconFactory.class.getResource(resourcePath);
        if (resource == null)
        {
            throw new NullPointerException("icon could not be found at \"" + resourcePath + "\"");
        }
        final ImageIcon icon = new ImageIcon(resource);
        return icon.getImage();
    }
}
