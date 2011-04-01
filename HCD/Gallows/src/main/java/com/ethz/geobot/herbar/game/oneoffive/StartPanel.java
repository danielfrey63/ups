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

import ch.jfactory.resource.ImageLocator;
import ch.jfactory.resource.Strings;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.ImageIcon;

/**
 * StartPanel with Help-textes.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:06:18 $
 */
public class StartPanel extends Canvas
{
    private final ImageIcon bird = ImageLocator.getIcon( "hangman_intro.jpg" );

    /** Constructor */
    public StartPanel()
    {
        this.setSize( 750, 470 );
        this.setVisible( true );
    }

    /** @see Component#paint(Graphics) */
    public void paint( final Graphics g )
    {
        /**
         Graphics2D g2 = (Graphics2D) g;
         g2.setPaint(
         new GradientPaint(
         0.5f * 72, -1.0f * 72, new Color( 10, 10, 35 ),
         10.4f * 72, -1.0f * 72, new Color( 100, 215, 230 ), false ) );
         g2.fillRect( 0, 0, 750, 470 );
         */
        g.drawImage( bird.getImage(), 0, 0, this );
        g.setColor( Color.ORANGE );
        g.setFont( new Font( "Arial", Font.PLAIN, 14 ) );
        g.drawString( Strings.getString( OneOfFive.class, "ONEOFFIVE.TITLE1" ), 280, 170 );
        g.drawString( Strings.getString( OneOfFive.class, "ONEOFFIVE.TITLE2" ), 280, 190 );
        g.drawString( Strings.getString( OneOfFive.class, "ONEOFFIVE.TITLE3" ), 280, 210 );
        g.drawString( Strings.getString( OneOfFive.class, "ONEOFFIVE.TITLE4" ), 280, 230 );
        g.drawString( Strings.getString( OneOfFive.class, "ONEOFFIVE.TITLE5" ), 280, 250 );
    }
}
