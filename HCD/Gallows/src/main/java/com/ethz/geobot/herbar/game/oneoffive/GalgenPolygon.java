/*
 * Herbar CD-ROM version 2
 *
 * GalgenPolygon.java
 *
 * Created on 26. April 2002, 11:51
 * Created by lilo
 */
package com.ethz.geobot.herbar.game.oneoffive;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Animated polygons.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:06:18 $
 */
public class GalgenPolygon
{
    private double[] baseX;

    private double[] baseY;

    private double[] topX;

    private double[] topY;

    private Color right;

    private Color left;

    private Color top;

    private Color line;

    private boolean topVisible;

    private boolean growing;

    private boolean isVisible;

    private int interval = 20;

    private double diffX0, diffX1, diffX2, diffX3, diffY0, diffY1, diffY2,
            diffY3;

    private final int[] iRightX = new int[4];

    private final int[] iRightY = new int[4];

    private final int[] iLeftX = new int[4];

    private final int[] iLeftY = new int[4];

    private final int[] iTopX = new int[4];

    private final int[] iTopY = new int[4];

    private final double[] dRightX = new double[4];

    private final double[] dRightY = new double[4];

    private final double[] dLeftX = new double[4];

    private final double[] dLeftY = new double[4];

    private final double[] dTopX = new double[4];

    private final double[] dTopY = new double[4];

    private int counter;

    /**
     * Constructor for the GalgenPolygon object
     *
     * @param baseX      array of x-coordinates of base-polygon
     * @param baseY      array of y-coordinates of base-polygon
     * @param topX       array of x-coordinates of top-polygon
     * @param topY       array of y-coordinates of top-polygon
     * @param right      color of right polygon
     * @param left       color of left polygon
     * @param top        color of top polygon
     * @param line       color of outline polygon
     * @param topVisible draw top-polygon
     */
    public GalgenPolygon( final double[] baseX, final double[] baseY, final double[] topX,
                          final double[] topY, final Color right, final Color left, final Color top, final Color line,
                          final boolean topVisible )
    {
        this.baseX = baseX;
        this.baseY = baseY;
        this.topX = topX;
        this.topY = topY;
        this.right = right;
        this.left = left;
        this.top = top;
        this.line = line;
        this.topVisible = topVisible;
        init();
    }

    /**
     * Constructor
     */
    public GalgenPolygon()
    {
    }

    /**
     * Sets the growing attribute of the GalgenPolygon object
     *
     * @param growing The new growing value
     */
    public void setGrowing( final boolean growing )
    {
        this.growing = growing;
    }

    /**
     * Sets the isVisible attribute of the GalgenPolygon object
     *
     * @param isVisible The new isVisible value
     */
    public void setIsVisible( final boolean isVisible )
    {
        this.isVisible = isVisible;
    }

    /**
     * construct the 3 polygons to one balken
     */
    public void init()
    {
        this.counter = 0;

        diffX0 = ( baseX[0] - topX[0] ) / interval;
        diffX1 = ( baseX[1] - topX[1] ) / interval;
        diffX2 = ( baseX[2] - topX[2] ) / interval;
        diffX3 = ( baseX[3] - topX[3] ) / interval;
        diffY0 = ( baseY[0] - topY[0] ) / interval;
        diffY1 = ( baseY[1] - topY[1] ) / interval;
        diffY2 = ( baseY[2] - topY[2] ) / interval;
        diffY3 = ( baseY[3] - topY[3] ) / interval;

        // All arrays start at the bottom right corner
        // Array of coordinates of the right polygon
        dRightX[0] = baseX[0];
        dRightX[1] = baseX[1];
        dRightX[2] = baseX[1];
        dRightX[3] = baseX[0];
        dRightY[0] = baseY[0];
        dRightY[1] = baseY[1];
        dRightY[2] = baseY[1];
        dRightY[3] = baseY[0];
        // Array of coordinates of the left polygon
        dLeftX[0] = baseX[1];
        dLeftX[1] = baseX[2];
        dLeftX[2] = baseX[2];
        dLeftX[3] = baseX[1];
        dLeftY[0] = baseY[1];
        dLeftY[1] = baseY[2];
        dLeftY[2] = baseY[2];
        dLeftY[3] = baseY[1];
        // Array of coordinates of the top polygon
        dTopX[0] = baseX[0];
        dTopX[1] = baseX[1];
        dTopX[2] = baseX[2];
        dTopX[3] = baseX[3];
        dTopY[0] = baseY[0];
        dTopY[1] = baseY[1];
        dTopY[2] = baseY[2];
        dTopY[3] = baseY[3];

        iRightX[0] = (int) dRightX[0];
        iRightX[1] = (int) dRightX[1];
        iRightX[2] = (int) dRightX[1];
        iRightX[3] = (int) dRightX[0];
        iRightY[0] = (int) dRightY[0];
        iRightY[1] = (int) dRightY[1];
        iRightY[2] = (int) dRightY[1];
        iRightY[3] = (int) dRightY[0];
        iLeftX[0] = (int) dLeftX[0];
        iLeftX[1] = (int) dLeftX[1];
        iLeftX[2] = (int) dLeftX[1];
        iLeftX[3] = (int) dLeftX[0];
        iLeftY[0] = (int) dLeftY[0];
        iLeftY[1] = (int) dLeftY[1];
        iLeftY[2] = (int) dLeftY[1];
        iLeftY[3] = (int) dLeftY[0];
        iTopX[0] = (int) baseX[0];
        iTopX[1] = (int) baseX[1];
        iTopX[2] = (int) baseX[2];
        iTopX[3] = (int) baseX[3];
        iTopY[0] = (int) baseY[0];
        iTopY[1] = (int) baseY[1];
        iTopY[2] = (int) baseY[2];
        iTopY[3] = (int) baseY[3];
    }

    /**
     * Method update.
     *
     * @param g Graphics-object
     */
    public void update( final Graphics g )
    {
        if ( counter == interval + 1 )
        {
            this.setGrowing( false );
        }

        if ( growing )
        {
            dRightX[3] = baseX[0] - counter * diffX0;
            dRightY[3] = baseY[0] - counter * diffY0;
            dRightX[2] = baseX[1] - counter * diffX1;
            dRightY[2] = baseY[1] - counter * diffY1;
            dLeftX[2] = baseX[2] - counter * diffX2;
            dLeftY[2] = baseY[2] - counter * diffY2;
            dLeftX[3] = baseX[1] - counter * diffX1;
            dLeftY[3] = baseY[1] - counter * diffY1;
            dTopX[0] = baseX[0] - counter * diffX0;
            dTopX[1] = baseX[1] - counter * diffX1;
            dTopX[2] = baseX[2] - counter * diffX2;
            dTopX[3] = baseX[3] - counter * diffX3;
            dTopY[0] = baseY[0] - counter * diffY0;
            dTopY[1] = baseY[1] - counter * diffY1;
            dTopY[2] = baseY[2] - counter * diffY2;
            dTopY[3] = baseY[3] - counter * diffY3;

            counter++;

            iRightX[2] = (int) dRightX[2];
            iRightY[2] = (int) dRightY[2];
            iRightX[3] = (int) dRightX[3];
            iRightY[3] = (int) dRightY[3];
            iLeftX[2] = (int) dLeftX[2];
            iLeftY[2] = (int) dLeftY[2];
            iLeftX[3] = (int) dLeftX[3];
            iLeftY[3] = (int) dLeftY[3];
            iTopX[0] = (int) dTopX[0];
            iTopY[0] = (int) dTopY[0];
            iTopX[1] = (int) dTopX[1];
            iTopY[1] = (int) dTopY[1];
            iTopX[2] = (int) dTopX[2];
            iTopY[2] = (int) dTopY[2];
            iTopX[3] = (int) dTopX[3];
            iTopY[3] = (int) dTopY[3];
        }
    }

    /**
     * Method paint.
     *
     * @param g Graphics-object
     */
    public void paint( final Graphics g )
    {
        if ( this.isVisible )
        {
            if ( topVisible )
            {
                g.setColor( top );
                g.fillPolygon( iTopX, iTopY, 4 );
            }
            g.setColor( right );
            g.fillPolygon( iRightX, iRightY, 4 );
            g.setColor( line );
            g.drawPolygon( iRightX, iRightY, 4 );
            g.setColor( left );
            g.fillPolygon( iLeftX, iLeftY, 4 );
        }
    }

    /**
     * Returns the isVisible.
     *
     * @return boolean
     */
    public boolean isVisible()
    {
        return isVisible;
    }

    /**
     * Returns the growing.
     *
     * @return boolean
     */
    public boolean isGrowing()
    {
        return growing;
    }

    /**
     * Returns the counter.
     *
     * @return int
     */
    public int getCounter()
    {
        return counter;
    }

    /**
     * Returns the interval.
     *
     * @return int
     */
    public int getInterval()
    {
        return interval;
    }

    /**
     * Returns the line.
     *
     * @return Color
     */
    public Color getLine()
    {
        return line;
    }

    /**
     * Returns the top.
     *
     * @return Color
     */
    public Color getTop()
    {
        return top;
    }

    /**
     * Sets the counter.
     *
     * @param counter The counter to set
     */
    public void setCounter( final int counter )
    {
        this.counter = counter;
    }

    /**
     * Sets the interval.
     *
     * @param interval The interval to set
     */
    public void setInterval( final int interval )
    {
        this.interval = interval;
    }

    /**
     * Sets the line.
     *
     * @param line The line to set
     */
    public void setLine( final Color line )
    {
        this.line = line;
    }

    /**
     * Sets the top.
     *
     * @param top The top to set
     */
    public void setTop( final Color top )
    {
        this.top = top;
    }

}
