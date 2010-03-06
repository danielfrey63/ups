package ch.jfactory.layout;

/**
 * Project:
 * $Id: ScrollerLayout.java,v 1.2 2006/03/14 21:27:55 daniel_frey Exp $
 * $Source: /repository/HerbarCD/Version2.1/xmatrix/src/java/ch/jfactory/layout/ScrollerLayout.java,v $
 * $Revision: 1.2 $, $Author: daniel_frey $
 */

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.io.Serializable;
import javax.swing.JComponent;
import javax.swing.SizeRequirements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScrollerLayout implements LayoutManager2, Serializable
{
    private static final Logger LOGGER = LoggerFactory.getLogger( ScrollerLayout.class );

    private final Container target;

    public static final Object NEXTSCROLLER = new Object();

    public static final Object PREVSCROLLER = new Object();

    private JComponent nextscroller, prevscroller;

    private transient SizeRequirements[] xChildren;

    private transient SizeRequirements[] yChildren;

    private transient SizeRequirements xTotal;

    private transient SizeRequirements yTotal;

    private int start = 0;

    public ScrollerLayout( final Container target )
    {
        this.target = target;
    }

    public void incStart()
    {
        start++;
        layoutContainer( target );
    }

    public void decStart()
    {
        if ( start > 0 )
        {
            start--;
        }
        layoutContainer( target );
    }

    public int getStart()
    {
        return start;
    }

    /**
     * Indicates that a child has changed its layout related information, and thus any cached calculations should be
     * flushed. <p/> This method is called by AWT when the invalidate method is called on the Container.  Since the
     * invalidate method may be called asynchronously to the event thread, this method may be called asynchronously.
     *
     * @param target the affected container
     * @throws AWTError if the target isn't the container specified to the BoxLayout constructor
     */
    public synchronized void invalidateLayout( final Container target )
    {
        xChildren = null;
        yChildren = null;
        xTotal = null;
        yTotal = null;
    }

    /**
     * Not used by this class.
     *
     * @param name the name of the component
     * @param comp the component
     */
    public void addLayoutComponent( final String name, final Component comp )
    {
        addLayoutComponent( comp, name );
    }

    /**
     * Not used by this class.
     *
     * @param comp the component
     */
    public void removeLayoutComponent( final Component comp )
    {
        start = 0;
        if ( comp == NEXTSCROLLER )
        {
            nextscroller = null;
        }
        if ( comp == PREVSCROLLER )
        {
            prevscroller = null;
        }
    }

    /**
     * Not used by this class.
     *
     * @param comp        the component
     * @param constraints constraints
     */
    public void addLayoutComponent( final Component comp, final Object constraints )
    {
        start = 0;
        if ( constraints == NEXTSCROLLER )
        {
            nextscroller = (JComponent) comp;
        }
        if ( constraints == PREVSCROLLER )
        {
            prevscroller = (JComponent) comp;
        }
    }

    /**
     * Returns the preferred dimensions for this layout, given the components in the specified target container.
     *
     * @param target the container that needs to be laid out
     * @return the dimensions >= 0 && <= Integer.MAX_VALUE
     * @throws AWTError if the target isn't the container specified to the BoxLayout constructor
     * @see Container
     * @see #minimumLayoutSize
     * @see #maximumLayoutSize
     */
    public Dimension preferredLayoutSize( final Container target )
    {
        final Dimension size;
        synchronized ( this )
        {
            checkRequests();
            size = new Dimension( xTotal.preferred, yTotal.preferred );
        }
        final Insets insets = target.getInsets();
        size.width = (int) Math.min( (long) size.width + (long) insets.left + (long) insets.right, Integer.MAX_VALUE );
        size.height = (int) Math.min( (long) size.height + (long) insets.top + (long) insets.bottom, Integer.MAX_VALUE );
        return size;
    }

    /**
     * Returns the minimum dimensions needed to lay out the components contained in the specified target container.
     *
     * @param target the container that needs to be laid out
     * @return the dimensions >= 0 && <= Integer.MAX_VALUE
     * @throws AWTError if the target isn't the container specified to the BoxLayout constructor
     * @see #preferredLayoutSize
     * @see #maximumLayoutSize
     */
    public Dimension minimumLayoutSize( final Container target )
    {
        final Dimension size;
        synchronized ( this )
        {
            checkRequests();
            size = new Dimension( xTotal.minimum, yTotal.minimum );
        }

        final Insets insets = target.getInsets();
        size.width = (int) Math.min( (long) size.width + (long) insets.left + (long) insets.right, Integer.MAX_VALUE );
        size.height = (int) Math.min( (long) size.height + (long) insets.top + (long) insets.bottom, Integer.MAX_VALUE );
        return size;
    }

    /**
     * Returns the maximum dimensions the target container can use to lay out the components it contains.
     *
     * @param target the container that needs to be laid out
     * @return the dimenions >= 0 && <= Integer.MAX_VALUE
     * @throws AWTError if the target isn't the container specified to the BoxLayout constructor
     * @see #preferredLayoutSize
     * @see #minimumLayoutSize
     */
    public Dimension maximumLayoutSize( final Container target )
    {
        final Dimension size;
        synchronized ( this )
        {
            checkRequests();
            size = new Dimension( xTotal.maximum, yTotal.maximum );
        }

        final Insets insets = target.getInsets();
        size.width = (int) Math.min( (long) size.width + (long) insets.left + (long) insets.right, Integer.MAX_VALUE );
        size.height = (int) Math.min( (long) size.height + (long) insets.top + (long) insets.bottom, Integer.MAX_VALUE );
        return size;
    }

    /**
     * Returns the alignment along the X axis for the container. If the box is horizontal, the default alignment will be
     * returned. Otherwise, the alignment needed to place the children along the X axis will be returned.
     *
     * @param target the container
     * @return the alignment >= 0.0f && <= 1.0f
     * @throws AWTError if the target isn't the container specified to the BoxLayout constructor
     */
    public synchronized float getLayoutAlignmentX( final Container target )
    {
        checkRequests();
        return xTotal.alignment;
    }

    /**
     * Returns the alignment along the currentY axis for the container. If the box is vertical, the default alignment
     * will be returned. Otherwise, the alignment needed to place the children along the currentY axis will be
     * returned.
     *
     * @param target the container
     * @return the alignment >= 0.0f && <= 1.0f
     * @throws AWTError if the target isn't the container specified to the BoxLayout constructor
     */
    public synchronized float getLayoutAlignmentY( final Container target )
    {
        checkRequests();
        return yTotal.alignment;
    }

    private int getLayoutComponentsCount()
    {
        int nChildren = target.getComponentCount();
        if ( prevscroller != null )
        {
            nChildren--;
        }
        if ( nextscroller != null )
        {
            nChildren--;
        }
        return nChildren;
    }

    /**
     * Called by the AWT <!-- XXX CHECK! --> when the specified container needs to be laid out.
     *
     * @param target the container to lay out
     * @throws AWTError if the target isn't the container specified to the BoxLayout constructor
     */
    public void layoutContainer( final Container target )
    {
        try
        {
            synchronized ( this )
            {
                layoutContainer();
            }
        }
        catch ( RuntimeException e )
        {
            LOGGER.warn( "Error layouting", e );
        }
    }

    public void layoutContainer() throws RuntimeException
    {
        final int nChildren = target.getComponentCount();
        final int nLayoutChildren = getLayoutComponentsCount();
        final int[] xOffsets = new int[nLayoutChildren];
        final int[] xSpans = new int[nLayoutChildren];
        //int[] yOffsets = new int[nLayoutChildren];
        xChildren = yChildren = null;

        final Dimension alloc = target.getSize();
        final Insets in = target.getInsets();
        alloc.width -= in.left + in.right;
        alloc.height -= in.top + in.bottom;
        int psw = 0;

        // determine the child placements
        checkRequests();
        SizeRequirements.calculateTiledPositions( alloc.width, xTotal, xChildren, xOffsets, xSpans, true );
        psw = xTotal.preferred;

        // flush changes to the container
        int xofs = 0;
        int layoutCount = 0;
        final int wi = target.getWidth();
        final boolean hasScroller = ( nextscroller != null );
        boolean needsScroller = hasScroller && ( ( psw > wi ) );

        int scw = 0;
        if ( needsScroller )
        {
            scw += nextscroller.getPreferredSize().width;
        }
        if ( start > 0 )
        {
            scw += prevscroller.getPreferredSize().width;
        }

        for ( int i = 0; i < nChildren; i++ )
        {
            final Component c = target.getComponent( i );
            if ( c == nextscroller )
            {
                continue;
            }
            if ( c == prevscroller )
            {
                continue;
            }
            if ( layoutCount >= start )
            {
                int x = (int) Math.min( (long) in.left + (long) xOffsets[layoutCount], Integer.MAX_VALUE ) - xofs;
                int w = xSpans[layoutCount];
                if ( scw > 0 )
                {
                    if ( x + w > wi - scw )
                    {
                        w = wi - scw - x;
                        if ( w <= 0 )
                        {
                            x = w = 0;
                        }
                        positionComponent( c, x, w );
                        if ( i > 0 )
                        {
                            i -= 1;
                        }
                        for ( int k = i + 1; k < nChildren; k++ )
                        {
                            target.getComponent( k ).setBounds( 0, 0, 0, 0 );
                        }
                        break;
                    }
                }
                positionComponent( c, x, w );
            }
            else
            {
                xofs += xSpans[layoutCount];
                c.setBounds( 0, 0, 0, 0 );
                needsScroller = hasScroller && ( ( psw - xofs > wi ) );
            }
            layoutCount++;
        }
        if ( ( start > 0 ) && ( prevscroller != null ) )
        {
            int pos = wi - prevscroller.getPreferredSize().width;
            if ( needsScroller )
            {
                pos -= nextscroller.getPreferredSize().width;
            }
            positionScroller( prevscroller, pos );
            psw += prevscroller.getWidth();
        }
        else
        {
            prevscroller.setBounds( 0, 0, 0, 0 );
        }
        if ( needsScroller )
        {
            final int pos = wi - nextscroller.getPreferredSize().width;
            positionScroller( nextscroller, pos );
        }
        else
        {
            nextscroller.setBounds( 0, 0, 0, 0 );
        }
    }

    private void positionComponent( final Component c, final int x, final int w )
    {
        final int hei = target.getHeight();
        int y = 0;
        final int h = c.getPreferredSize().height;
        if ( h < hei )
        {
            y = ( hei - h ) / 2;
        }
        c.setBounds( x, y, w, h );
    }

    private void positionScroller( final JComponent scroller, final int x )
    {
        final int h = scroller.getPreferredSize().height;
        int th = ( h + scroller.getInsets().top + scroller.getInsets().bottom );
        if ( th > target.getHeight() )
        {
            th = target.getHeight();
        }
        final int y = ( target.getHeight() - th ) / 2;
        final int w = scroller.getPreferredSize().width;
        scroller.setBounds( x, y, w, th );
    }

    void checkRequests()
    {
        if ( xChildren == null || yChildren == null )
        {
            final int n = target.getComponentCount();
            final int nLay = getLayoutComponentsCount();
            xChildren = new SizeRequirements[nLay];
            yChildren = new SizeRequirements[nLay];
            int layoutCounter = 0;
            for ( int i = 0; i < n; i++ )
            {
                final Component c = target.getComponent( i );
                if ( c == nextscroller )
                {
                    continue;
                }
                if ( c == prevscroller )
                {
                    continue;
                }
                if ( !c.isVisible() )
                {
                    xChildren[layoutCounter] = new SizeRequirements( 0, 0, 0, c.getAlignmentX() );
                    yChildren[layoutCounter] = new SizeRequirements( 0, 0, 0, c.getAlignmentY() );
                    layoutCounter++;
                    continue;
                }
                final Dimension min = c.getMinimumSize();
                final Dimension typ = c.getPreferredSize();
                final Dimension max = c.getMaximumSize();
                xChildren[layoutCounter] = new SizeRequirements( min.width, typ.width, max.width, c.getAlignmentX() );
                yChildren[layoutCounter] = new SizeRequirements( min.height, typ.height, max.height, c.getAlignmentY() );
                layoutCounter++;
            }

            xTotal = SizeRequirements.getTiledSizeRequirements( xChildren );
            yTotal = SizeRequirements.getAlignedSizeRequirements( yChildren );
        }
    }

}

