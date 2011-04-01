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
package com.ethz.geobot.herbar.gui.commands;

import ch.jfactory.action.AbstractParametrizedAction;
import com.Ostermiller.util.Browser;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.KeyStroke;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Action class to show help.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $
 */
public class ActionAppHelp extends AbstractParametrizedAction
{
    /** This class logger. */
    private static final Logger LOG = LoggerFactory.getLogger( ActionAppHelp.class );

    /**
     * Constructor
     *
     * @param frame the main frame
     */
    public ActionAppHelp( final JFrame frame )
    {
        super( "MENU.ITEM.HELP", frame );

        // overwrite default
        putValue( Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke( KeyEvent.VK_F1, 0 ) );
    }

    public void actionPerformed( final ActionEvent parm1 )
    {
        final String url = System.getProperty( "xmatrix.help.url" );
        try
        {
            Browser.init();
            Browser.displayURL( url );
        }
        catch ( IOException e )
        {
            LOG.error( "cannot display help at " + url, e );
        }
    }
}
