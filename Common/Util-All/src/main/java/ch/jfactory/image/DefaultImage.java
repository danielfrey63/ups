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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import javax.swing.UIManager;

/** An image that has the background of a panel and displays a configurable string. */
public class DefaultImage extends BufferedImage
{

    public DefaultImage(final Dimension dim, final String string)
    {
        super(dim.width, dim.height, TYPE_INT_ARGB_PRE);
        final Graphics2D g = this.createGraphics();
        g.setFont(new Font("Arial", Font.BOLD, 20));
        final FontMetrics metrics = g.getFontMetrics();
        final Rectangle2D bounds = metrics.getStringBounds(string, g);
        final double f = Math.min(getWidth() / bounds.getWidth() / 2, getHeight() / bounds.getHeight() / 2);
        g.setFont(new Font("Arial", Font.BOLD, (int) (12 * f)));
        final FontMetrics metrics2 = g.getFontMetrics();
        final Rectangle2D bounds2 = metrics2.getStringBounds(string, g);
        final Color background = UIManager.getColor("Panel.background");
        final int x = (int) ((getWidth() - bounds2.getWidth()) / 2);
        final int y = (int) ((getHeight() - bounds2.getHeight()) / 2 + bounds2.getHeight());
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(ColorUtils.darken(background, 0.3d));
        g.drawString(string, x, y);
        g.dispose();
    }
}
