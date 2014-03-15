/*
 * Copyright (c) 2011.
 *
 * Nutzung und Rechte
 *
 * Die Applikation eBot wurde f�r Studierende der ETH Z�rich entwickelt. Sie  steht
 * allen   an   Hochschulen  oder   Fachhochschulen   eingeschriebenen Studierenden
 * (auch  ausserhalb  der  ETH  Z�rich)  f�r  nichtkommerzielle  Zwecke  im Studium
 * kostenlos zur Verf�gung. Nichtstudierende Privatpersonen, die die Applikation zu
 * ihrer  pers�nlichen  Weiterbildung  nutzen  m�chten,  werden  gebeten,  f�r  die
 * nichtkommerzielle Nutzung einen einmaligen Beitrag von Fr. 20.� zu bezahlen.
 *
 * Postkonto
 *
 * Unterricht, 85-761469-0, Vermerk "eBot"
 * IBAN 59 0900 0000 8576  1469 0; BIC POFICHBEXXX
 *
 * Jede andere Nutzung der Applikation  ist vorher mit dem Projektleiter  (Matthias
 * Baltisberger, Email:  balti@ethz.ch) abzusprechen  und mit  einer entsprechenden
 * Vereinbarung zu regeln. Die  Applikation wird ohne jegliche  Garantien bez�glich
 * Nutzungsanspr�chen zur Verf�gung gestellt.
 */
package com.ethz.geobot.herbar.gui;

import ch.jfactory.application.SystemUtil;
import ch.jfactory.resource.ImageLocator;
import com.jgoodies.looks.plastic.PlasticXPLookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:05:50 $
 */
public class AppHerbarTest
{
    private static Logger LOG = LoggerFactory.getLogger( AppHerbarTest.class );

    private static MainFrame mainFrame = null;

    public AppHerbarTest()
    {
        SwingUtilities.invokeLater( new Runnable()
        {
            public void run()
            {
                mainFrame = new MainFrame();
                mainFrame.setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );
                mainFrame.setSize( 600, 400 );
                mainFrame.setLocationRelativeTo( null );
                mainFrame.setVisible( true );
            }
        } );
    }

    public static void main( final String[] args )
    {
        LOG.debug( "Starting main-Application" );
        try
        {
            UIManager.setLookAndFeel( new PlasticXPLookAndFeel() );
            new AppHerbarTest();
        }
        catch ( IllegalStateException e )
        {
            LOG.error( "security check failed", e );
        }
        catch ( Throwable e )
        {
            LOG.error( "fatal error occurred in Application: " + e.getMessage(), e );
            SystemUtil.EXIT.exit( 1 );
        }
    }
}
