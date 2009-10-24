/*
 * Herbar CD-ROM version 2
 *
 * GalgenSchlinge.java
 *
 * Created on 26. April 2002, 11:51
 * Created by lilo
 */
package com.ethz.geobot.herbar.game.oneoffive;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Animation of the schlinge as sprite.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $  $Date: 2007/09/17 11:06:18 $
 */
public class GalgenSchlinge extends GalgenPolygon {

    /**
     * Constructor for the GalgenSchlinge object
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
    public GalgenSchlinge(double[] baseX, double[] baseY, double[] topX,
                          double[] topY, Color right, Color left, Color top, Color line,
                          boolean topVisible) {
        super(baseX, baseY, topX, topY, right, left, top, line, topVisible);
    }

    /**
     * @see com.ethz.geobot.herbar.game.oneoffive.GalgenPolygon#paint(Graphics)
     */
    public void paint(Graphics g) {
        super.paint(g);
        if (this.isVisible()) {
            if (getCounter() == getInterval() + 1) {
                g.setColor(Color.black);
                g.drawArc(360, 175, 38, 70, 0, 360);
                g.drawArc(359, 175, 38, 70, 0, 360);
                g.setColor(getTop());
                g.drawArc(358, 175, 38, 70, 0, 360);
                g.drawArc(358, 174, 38, 70, 0, 360);
                g.drawArc(357, 174, 38, 70, 0, 360);
                g.setColor(getLine());
                g.drawArc(357, 173, 38, 70, 0, 360);
            }
        }
    }
}
