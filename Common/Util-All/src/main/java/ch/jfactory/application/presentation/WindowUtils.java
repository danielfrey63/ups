/*
 * Copyright 2002 by x-matrix Switzerland
 *
 * WindowUtils.java
 *
 * Created on 14. Mai 2002, 23:45
 * Created by Daniel Frey
 */
package ch.jfactory.application.presentation;

import ch.jfactory.resource.OperatingSystem;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.IllegalComponentStateException;
import java.awt.Point;
import javax.swing.Box.Filler;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JSplitPane;
import javax.swing.plaf.SplitPaneUI;

/**
 * Utility methods for GUI components.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.2 $ $Date: 2005/08/07 01:21:55 $
 */
public class WindowUtils
{
    /**
     * Centers the given Component in the center of the given second Component.
     *
     * @param c the Component to center
     * @param p the Component to be centered on
     */
    public static void centerOnComponent( final Component c, final Component p )
    {
        final Point pnt = getCenterOnComponent( c, p );
        c.setLocation( pnt.x, pnt.y );
    }

    /**
     * Returns the center point (top-left) of the component centered on the parent component.
     *
     * @param c the component to center
     * @param p the parent component
     * @return a Point
     */
    public static Point getCenterOnComponent( final Component c, final Component p )
    {
        final Point pnt = getCenterLocation( c, p.getSize() );
        final Point pntP = p.getLocation();
        pnt.translate( pntP.x, pntP.y );
        return pnt;
    }

    /**
     * Centers the given Component approximatly on the screen. In the vertical the Component is placed in the upper
     * third.
     *
     * @param c the Component to place
     */
    public static void centerOnScreen( final Component c )
    {
        final Point p = getCenterLocation( c, OperatingSystem.getScreenBounds().getSize() );
        c.setLocation( p.x, p.y );
    }

    /**
     * Adds space between the components. Make sure to place them in the wished component before calling this method, in
     * order to get the right layout. The components have to be in the same parent component for this method to work
     * properly. The components have to be added to the parent component subsequently for this method to work. By
     * default a space of {@link Constants#GAP_WITHIN_GROUP} is used.
     *
     * @param components an array of JButtons layout
     */
    public static void spaceComponents( final JComponent[] components )
    {
        spaceComponents( components, Constants.GAP_WITHIN_GROUP );
    }

    /**
     * Adds space between the components of a FlowLayout. Make sure to place them in the wished component before calling
     * this method, in order to get the right layout. The components have to be in the same parent component for this
     * method to work properly. The components have to be added to the parent component subsequently for this method to
     * work.
     *
     * @param components an array of JButtons layout
     * @param space      the space in pixels to add between the components
     */

    public static void spaceComponents( final JComponent[] components, final int space )
    {
        final int len = components.length;

        // A single component has not to be manipulated
        if ( len < 1 )
        {
            return;
        }

        final JComponent component = components[0];

        // Find out whether the components are added subsequently to the parent component and get correct start position
        // of the first button.
        final Container parent = component.getParent();

        if ( !( parent.getLayout() instanceof FlowLayout ) )
        {
            throw new IllegalComponentStateException( "spaceComponents only supports FlowLayout containers." );
        }

        int start = -1;
        int end = -1;
        // If this method is called a second time, we need to keep track of fillers already filled in by a previous
        // pass.
        int fillers = 0;
        for ( int i = 0; i < parent.getComponentCount(); i++ )
        {
            final Component comp = parent.getComponent( i );
            if ( comp.getParent() != parent )
            {
                throw new IllegalComponentStateException( "All components have to be in the same component" );
            }
            if ( comp instanceof Filler )
            {
                fillers++;
            }
            if ( comp instanceof JComponent )
            {
                if ( start == -1 )
                {
                    start = i;
                }
                end = i;
            }
        }
        if ( len - 1 + fillers != end - start )
        {
            throw new IllegalComponentStateException( "Components must be added subsequently" );
        }

        // Add spacer between buttons if not already there
        if ( fillers == 0 )
        {
            final Dimension dim = new Dimension( space, space );
            for ( int i = end; i > start; i-- )
            {
                final Filler filler = new Filler( dim, dim, dim );
                filler.setBackground( Color.blue );
                parent.add( filler, i );
            }
        }
    }

    /**
     * Resizes the given JButtons to the largest one. Height is not affected.
     *
     * @param buttons an array of JButtons layout
     */
    public static void equalizeButtons( final JButton[] buttons )
    {
        final int len = buttons.length;
        if ( len < 1 )
        {
            return;
        }
        // Ajust size of each button to the wides one.
        double w = 0;
        final double h = buttons[0].getPreferredSize().getHeight();
        for ( int i = 0; i < len; i++ )
        {
            final JButton button = buttons[i];
            button.setPreferredSize( null );
            w = Math.max( w, button.getPreferredSize().getWidth() );
        }
        final Dimension dim = new Dimension( (int) w, (int) h );
        for ( int i = 0; i < len; i++ )
        {
            buttons[i].setPreferredSize( dim );
        }
    }

    /**
     * Returns the point calculated by centering the given Component in the middle of the given Dimension.
     *
     * @param c the Component to center
     * @param p the Dimension the Component is centered on
     * @return the Point for the new location relative to the given dimension
     */
    private static Point getCenterLocation( final Component c, final Dimension p )
    {
        final Dimension dimC = c.getSize();
        final int iNewX = ( p.width - dimC.width ) / 2;
        final int iNewY = ( p.height - dimC.height ) / 3;
        return new Point( iNewX, iNewY );
    }

    /**
     * Makes sure both sides of the split pane are at least of their preferred size. If not the sides are expanded.
     *
     * @param pane
     */
    public static void ensureSplitComponentsVisible( final JSplitPane pane )
    {
        final SplitPaneUI ui = pane.getUI();
        final int loc = ui.getDividerLocation( pane );
        if ( loc < ui.getMinimumDividerLocation( pane ) || loc > ui.getMaximumDividerLocation( pane ) )
        {
            pane.resetToPreferredSizes();
        }
    }
}
