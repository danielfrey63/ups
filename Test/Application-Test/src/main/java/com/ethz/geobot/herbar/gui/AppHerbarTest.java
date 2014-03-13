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
import ch.jfactory.logging.LogUtils;
import com.jgoodies.looks.plastic.PlasticXPLookAndFeel;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:05:50 $
 */
public class AppHerbarTest extends JFrame
{
    private static Logger LOG = LoggerFactory.getLogger( AppHerbarTest.class );

    public AppHerbarTest()
    {
        final JLabel label = new JLabel( "Hallo Weld!" );
        label.setHorizontalAlignment( SwingConstants.CENTER );
        setLayout( new BorderLayout() );
        add( label, BorderLayout.CENTER );
    }

    public static void main( final String[] args ) throws UnsupportedLookAndFeelException
    {
        LogUtils.init();

        LOG.debug( "Starting main-Application" );

        try
        {
            UIManager.setLookAndFeel( new PlasticXPLookAndFeel() );
            final int selection;
            if ( args.length == 0 )
            {
                // Todo: Implements as a case of specialized resource bundles
                final EnvironmentDialog dialog = new EnvironmentDialog( null );
                dialog.setVisible( true );
                if ( dialog.ok )
                {
                    selection = dialog.systematicRadio.isSelected() ? 1 : dialog.demoRadio.isSelected() ? 3 : 2;
                }
                else
                {
                    LOG.info( "user canceled choice dialog. exiting application now." );
                    selection = -1;
                    System.exit( 1 );
                }
            }
            else
            {
                selection = Integer.parseInt( args[0] );
            }
            new AppHerbar( selection );
        } catch ( IllegalStateException e )
        {
            LOG.error( "security check failed", e );
        } catch ( Throwable e )
        {
            LOG.error( "fatal error occurred in Application: " + e.getMessage(), e );
            SystemUtil.EXIT.exit( 1 );
        }
    }
}
