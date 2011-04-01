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
package com.ethz.geobot.herbar.game.catcher;

import ch.jfactory.resource.ImageLocator;
import ch.jfactory.resource.Strings;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.ImageIcon;

/**
 * At the begin of the first game this panel is showed. it contains the roles.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:05:58 $
 */
public class StartPanel extends Canvas
{
    private final ImageIcon blume = ImageLocator.getIcon( "catcher_intro.jpg" );

    private final Color green;

    /**
     * Constructor
     *
     * @param green Color for the background
     */
    public StartPanel( final Color green )
    {
        this.setSize( 750, 470 );
        this.green = green;
        this.setVisible( true );
    }

    /** @see Component#paint(Graphics) */
    public void paint( final Graphics g )
    {
        //*
        final Graphics2D g2 = (Graphics2D) g;
        g2.setPaint( new GradientPaint( 0.5f * 72, -1.0f * 72, green,
                10.4f * 72, -1.0f * 72, new Color( 220, 225, 80 ), false ) );
        g2.fillRect( 0, 0, 750, 470 );
        //
        g.drawImage( blume.getImage(), 0, 0, this );
        g.setColor( Color.yellow );
        g.setFont( new Font( "Arial", Font.PLAIN, 14 ) );
        g.drawString( Strings.getString( Catcher.class, "CATCHER.TITLE.TEXT1" ), 40, 40 );
        g.drawString( Strings.getString( Catcher.class, "CATCHER.TITLE.TEXT2" ), 40, 60 );
        g.drawString( Strings.getString( Catcher.class, "CATCHER.TITLE.TEXT3" ), 40, 80 );
        g.drawString( Strings.getString( Catcher.class, "CATCHER.TITLE.TEXT4" ), 40, 100 );
        g.drawString( Strings.getString( Catcher.class, "CATCHER.TITLE.TEXT5" ), 40, 120 );
        g.drawString( Strings.getString( Catcher.class, "CATCHER.TITLE.TEXT6" ), 40, 140 );
    }
}
