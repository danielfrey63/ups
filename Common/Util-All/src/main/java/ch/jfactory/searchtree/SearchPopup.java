/* ====================================================================
 *  Copyright 2004 www.jfactory.ch
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied.
 * ====================================================================
 */
package ch.jfactory.searchtree;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import javax.swing.JApplet;
import javax.swing.JInternalFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;

/**
 * Popup Window for Searchtree
 *
 * @author <a href="mail@wegmueller.com">Thomas Wegmueller</a>
 * @version $Revision: 1.2 $ $Date: 2006/03/14 21:27:55 $
 */
class SearchPopup
{
    private JPanel panel;

    private JTree owner;

    private final int x;

    private final int y;

    protected SearchPopup( final JTree owner, final Component contents, final int x, final int y )
    {
        if ( contents == null )
        {
            throw new IllegalArgumentException( "Contents must be non-null" );
        }
        if ( panel == null )
        {
            panel = new JPanel( new BorderLayout(), true );
            panel.setOpaque( true );
        }
        this.owner = owner;
        this.x = x;
        this.y = y;
        panel.add( contents, BorderLayout.CENTER );
        contents.invalidate();
        pack();
    }

    public void pack()
    {
        if ( panel != null )
        {
            panel.setSize( panel.getPreferredSize() );
        }
    }

    public void hide()
    {
        if ( panel != null )
        {
            final Container parent = panel.getParent();

            if ( parent != null )
            {
                final Rectangle bounds = panel.getBounds();

                parent.remove( panel );
                parent.repaint( bounds.x, bounds.y, bounds.width,
                        bounds.height );
            }
        }
        owner = null;

        panel.removeAll();
    }

    public void show()
    {
        Container parent = null;

        if ( owner != null )
        {
            parent = ( owner instanceof Container ? owner : owner.getParent() );
        }

        // Try to find a JLayeredPane and Window to add
        for ( Container p = parent; p != null; p = p.getParent() )
        {
            if ( p instanceof JRootPane )
            {
                if ( p.getParent() instanceof JInternalFrame )
                {
                    continue;
                }
                parent = ( (JRootPane) p ).getLayeredPane();
                // Continue, so that if there is a higher JRootPane, we'll
                // pick it up.
            }
            else if ( p instanceof Window )
            {
                if ( parent == null )
                {
                    parent = p;
                }
                break;
            }
            else if ( p instanceof JApplet )
            {
                // Painting code stops at Applets, we don't want
                // to add to a Component above an Applet otherwise
                // you'll never see it painted.
                break;
            }
        }

        final Point p = convertScreenLocationToParent( parent, x, y );
        panel.setLocation( p.x, p.y );
        if ( parent instanceof JLayeredPane )
        {
            parent.add( panel, JLayeredPane.POPUP_LAYER, 0 );
        }
        else
        {
            parent.add( panel );
        }
    }

    static Point convertScreenLocationToParent( final Container parent, final int x, final int y )
    {
        for ( Container p = parent; p != null; p = p.getParent() )
        {
            if ( p instanceof Window )
            {
                final Point point = new Point( x, y );
                SwingUtilities.convertPointFromScreen( point, parent );
                return point;
            }
        }
        throw new Error( "convertScreenLocationToParent: no window ancestor" );
    }

}
