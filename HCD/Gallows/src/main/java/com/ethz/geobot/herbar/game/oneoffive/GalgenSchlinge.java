/*
 * Copyright (c) 2011.
 *
 * Nutzung und Rechte
 *
 * Die Applikation eBot wurde für Studierende der ETH Zürich entwickelt. Sie  steht
 * allen   an   Hochschulen  oder   Fachhochschulen   eingeschriebenen Studierenden
 * (auch  ausserhalb  der  ETH  Zürich)  für  nichtkommerzielle  Zwecke  im Studium
 * kostenlos zur Verfügung. Nichtstudierende Privatpersonen, die die Applikation zu
 * ihrer  persönlichen  Weiterbildung  nutzen  möchten,  werden  gebeten,  für  die
 * nichtkommerzielle Nutzung einen einmaligen Beitrag von Fr. 20.– zu bezahlen.
 *
 * Postkonto
 *
 * Unterricht, 85-761469-0, Vermerk "eBot"
 * IBAN 59 0900 0000 8576  1469 0; BIC POFICHBEXXX
 *
 * Jede andere Nutzung der Applikation  ist vorher mit dem Projektleiter  (Matthias
 * Baltisberger, Email:  balti@ethz.ch) abzusprechen  und mit  einer entsprechenden
 * Vereinbarung zu regeln. Die  Applikation wird ohne jegliche  Garantien bezüglich
 * Nutzungsansprüchen zur Verfügung gestellt.
 */

/*
 * Copyright (c) 2011.
 *
 * Nutzung und Rechte
 *
 * Die Applikation eBot wurde für Studierende der ETH Zürich entwickelt. Sie  steht
 * allen   an   Hochschulen  oder   Fachhochschulen   eingeschriebenen Studierenden
 * (auch  ausserhalb  der  ETH  Zürich)  für  nichtkommerzielle  Zwecke  im Studium
 * kostenlos zur Verfügung. Nichtstudierende Privatpersonen, die die Applikation zu
 * ihrer  persönlichen  Weiterbildung  nutzen  möchten,  werden  gebeten,  für  die
 * nichtkommerzielle Nutzung einen einmaligen Beitrag von Fr. 20.– zu bezahlen.
 *
 * Postkonto
 *
 * Unterricht, 85-761469-0, Vermerk "eBot"
 * IBAN 59 0900 0000 8576  1469 0; BIC POFICHBEXXX
 *
 * Jede andere Nutzung der Applikation  ist vorher mit dem Projektleiter  (Matthias
 * Baltisberger, Email:  balti@ethz.ch) abzusprechen  und mit  einer entsprechenden
 * Vereinbarung zu regeln. Die  Applikation wird ohne jegliche  Garantien bezüglich
 * Nutzungsansprüchen zur Verfügung gestellt.
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
public class GalgenSchlinge extends GalgenPolygon
{
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
    public GalgenSchlinge( final double[] baseX, final double[] baseY, final double[] topX,
                           final double[] topY, final Color right, final Color left, final Color top, final Color line,
                           final boolean topVisible )
    {
        super( baseX, baseY, topX, topY, right, left, top, line, topVisible );
    }

    /** @see GalgenPolygon#paint(Graphics) */
    public void paint( final Graphics g )
    {
        super.paint( g );
        if ( this.isVisible() )
        {
            if ( getCounter() == getInterval() + 1 )
            {
                g.setColor( Color.black );
                g.drawArc( 360, 175, 38, 70, 0, 360 );
                g.drawArc( 359, 175, 38, 70, 0, 360 );
                g.setColor( getTop() );
                g.drawArc( 358, 175, 38, 70, 0, 360 );
                g.drawArc( 358, 174, 38, 70, 0, 360 );
                g.drawArc( 357, 174, 38, 70, 0, 360 );
                g.setColor( getLine() );
                g.drawArc( 357, 173, 38, 70, 0, 360 );
            }
        }
    }
}
