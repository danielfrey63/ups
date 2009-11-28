package ch.jfactory.application.view.border;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.border.Border;

/**
 * Project: $Id: SimpleBorder.java,v 1.1 2005/06/16 06:28:57 daniel_frey Exp $ $Source:
 * /repository/HerbarCD/Version2.1/xmatrix/src/com/xmatrix/gui/border/SimpleBorder.java,v $ $Revision: 1.1 $, $Author:
 * daniel_frey $
 */
public class SimpleBorder implements Border
{
    private final int top;

    private final int right;

    private final int bottom;

    private final int left;

    private final Color col;

    public SimpleBorder( final Color c, final int top, final int left, final int bottom, final int right )
    {
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.left = left;
        this.col = c;
    }

    public final Insets getBorderInsets( final Component c )
    {
        return new Insets( top, left, bottom, right );
    }

    public final boolean isBorderOpaque()
    {
        return true;
    }

    public final void paintBorder( final Component c, final Graphics g, final int x, final int y, final int width, final int height )
    {
        final Color old = g.getColor();
        g.setColor( col );
        if ( top > 0 )
        {
            //g.drawRect(x, y, width - 1, top);
            for ( int i = 0; i < top; i++ )
            {
                g.drawLine( x, y + i, x + width - 1, y + i );
            }
            //top
        }
        if ( right > 0 )
        {
            //g.drawRect(x + width - right, y, right, height - 1);
            for ( int i = 0; i < right; i++ )
            {
                g.drawLine( x + width - i - 1, y, x + width - i - 1, y + height - 1 );
            }

            //right
        }
        if ( bottom > 0 )
        {
            //g.drawRect(x, y + height - bottom, width - 1, bottom);
            for ( int i = 0; i < bottom; i++ )
            {
                g.drawLine( x, y + height - i - 1, x + width, y + height - i - 1 );
            }
            //bottom
        }
        if ( left > 0 )
        {
            //g.drawRect(x, y, left, height - 1);
            for ( int i = 0; i < left; i++ )
            {
                g.drawLine( x + i, y, x + i, y + height - 1 );
            }
            //left
        }

        g.setColor( old );
    }
}
