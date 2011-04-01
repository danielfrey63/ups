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

import com.ethz.geobot.herbar.game.util.CountScore;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import javax.swing.event.EventListenerList;

/**
 * Energybar which represents the score of the game.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:05:58 $
 */
public class EnergyBar extends Canvas
{
    private int barHeight = 100;

    private final int barSteps = 20;

    private final int barInterval = barHeight / barSteps;

    private int red;

    private int green;

    private int blue;

    private final EventListenerList eventListeners = new EventListenerList();

    private CountScore countScore;

    /** Constructor, which initializes the class */
    public EnergyBar()
    {
        init();
    }

    /**
     * adjust the RGB-values of the color and the height of the energybar according score changes
     *
     * @param total new barhight
     */
    public void setScore( final int total )
    {
        if ( total <= 100 )
        {
            this.barHeight = total;
            if ( !( barHeight > 100 || barHeight < 0 ) )
            {
                if ( barHeight / barInterval > ( barSteps / 2 ) - 1 )
                {
                    red = 0 + ( ( barSteps - barHeight / barInterval )
                            * 255 / ( barSteps / 2 ) );
                }
                if ( barHeight / barInterval <= barSteps / 2 )
                {
                    green = 255 - ( ( barSteps / 2 - barHeight /
                            barInterval ) * 255 / ( barSteps / 2 ) );
                }
            }
            else
            {
                barHeight = total;
            }
        }
    }

    /** reset the color values to defaults when starting the game */
    public void init()
    {
        red = 0;
        green = 255;
        blue = 0;
    }

    /** @see Component#paint(Graphics) */
    public void paint( final Graphics g )
    {
        g.setColor( new Color( 144, 144, 144 ) );
        g.fillRect( 5, 5, 10, 100 );
        g.setColor( new Color( red, green, blue ) );
        g.fillRect( 5, 5 + 100 - barHeight, 10, barHeight );
        g.setColor( new Color( 29, 64, 13 ) );
        g.drawRect( 5, 5, 10, 100 );
    }
}
