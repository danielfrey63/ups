package ch.jfactory.image;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import javax.swing.Icon;

/**
 * @author Nobuo Tamemasa, $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2006/04/15 23:03:21 $
 */
public class BlankIcon implements Icon
{
    private Color fillColor;

    private int size;

    public BlankIcon()
    {
        this(null, 11);
    }

    public BlankIcon(final Color color, final int size)
    {
        //UIManager.getColor("control")
        //UIManager.getColor("controlShadow")
        fillColor = color;

        this.size = size;
    }

    public void paintIcon(final Component c, final Graphics g, final int x, final int y)
    {
        if (fillColor != null)
        {
            g.setColor(fillColor);
            g.drawRect(x, y, size - 1, size - 1);
        }
    }

    public int getIconWidth()
    {
        return size;
    }

    public int getIconHeight()
    {
        return size;
    }
}
