package ch.jfactory.application.view.border;

import java.awt.Component;
import java.awt.Graphics;
import javax.swing.border.LineBorder;

public class TransparentBorder extends LineBorder
{

    public TransparentBorder()
    {
        super(null);
    }

    public TransparentBorder(final int i)
    {
        super(null, i);
    }

    public void paintBorder(final Component c, final Graphics g, final int x, final int y, final int width, final int height)
    {
        lineColor = c.getBackground();
        super.paintBorder(c, g, x, y, width, height);
    }
}