/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.component;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;
import javax.swing.tree.TreePath;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2005/08/16 10:20:18 $
 */
public class OverlayListener extends MouseInputAdapter
{
    JTree tree;

    Component oldGlassPane;

    TreePath path;

    int row;

    Rectangle bounds;

    public OverlayListener( final JTree tree )
    {
        this.tree = tree;
        tree.addMouseListener( this );
        tree.addMouseMotionListener( this );
    }

    JComponent c = new JComponent()
    {
        public void paint( final Graphics g )
        {
            final boolean selected = tree.isRowSelected( row );
            final Component renderer = tree.getCellRenderer().getTreeCellRendererComponent( tree, path.getLastPathComponent(),
                    tree.isRowSelected( row ), tree.isExpanded( row ), tree.getModel().isLeaf( path.getLastPathComponent() ), row,
                    selected );
            c.setFont( tree.getFont() );
            final Rectangle paintBounds = SwingUtilities.convertRectangle( tree, bounds, this );
            SwingUtilities.paintComponent( g, renderer, this, paintBounds );
            if ( selected )
            {
                return;
            }

            g.setColor( Color.blue );
            ( (Graphics2D) g ).draw( paintBounds );
        }
    };

    public void mouseExited( final MouseEvent e )
    {
        resetGlassPane();
    }

    private void resetGlassPane()
    {
        if ( oldGlassPane != null )
        {
            c.setVisible( false );
            tree.getRootPane().setGlassPane( oldGlassPane );
            oldGlassPane = null;
        }
    }

    public void mouseMoved( final MouseEvent me )
    {
        path = tree.getPathForLocation( me.getX(), me.getY() );
        if ( path == null )
        {
            resetGlassPane();
            return;
        }
        row = tree.getRowForPath( path );
        bounds = tree.getPathBounds( path );
        if ( !tree.getVisibleRect().contains( bounds ) )
        {
            if ( oldGlassPane == null )
            {
                oldGlassPane = tree.getRootPane().getGlassPane();
                c.setOpaque( false );
                tree.getRootPane().setGlassPane( c );
                c.setVisible( true );
            }
            else
            {
                tree.getRootPane().repaint();
            }
        }
        else
        {
            resetGlassPane();
        }
    }
}