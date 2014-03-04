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
package com.ethz.geobot.herbar.gui;

import ch.jfactory.application.SystemUtil;
import ch.jfactory.application.presentation.WindowUtils;
import ch.jfactory.logging.LogUtils;
import ch.jfactory.resource.ImageLocator;
import ch.xmatrix.ups.pmb.exam.Main;
import com.ethz.geobot.herbar.Application;
import com.ethz.geobot.herbar.gui.about.Splash;
import com.ethz.geobot.herbar.modeapi.HerbarContext;
import com.jgoodies.looks.plastic.PlasticXPLookAndFeel;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:05:50 $
 */
public class AppHerbar
{
    private static Logger LOG = LoggerFactory.getLogger( AppHerbar.class );

    private static final String DIR_SC = "sc";

    private static final String DIR_DE = "de";

    private static final String homeDir = System.getProperty( "user.home" ).replace( '\\', '/' ) + "/.hcd2/";

    private static Splash splash;

    /**
     * reference to the one and only mainframe
     */
    private static MainFrame mainFrame = null;

    public AppHerbar( final int selection )
    {
        setupDirectories( selection );
        initSplash();
        decompressDatabase( selection );
        Application.getInstance().getModel();
        System.setProperty( ImageLocator.PROPERTY_IMAGE_LOCATION, System.getProperty( "xmatrix.picture.path" ) );

        SwingUtilities.invokeLater( new Runnable()
        {
            public void run()
            {
                mainFrame = new MainFrame();
                mainFrame.setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );

                // set frame position
                final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                mainFrame.setSize( (int) ( screenSize.width / 1.2 ), (int) ( screenSize.height / 1.2 ) );
                WindowUtils.centerOnScreen( mainFrame );

                // load old user settings
                mainFrame.loadSettings();
                mainFrame.setVisible( true );

                splash.finish();
            }
        } );
    }

    public static MainFrame getMainFrame()
    {
        return mainFrame;
    }

    private void setupDirectories( final int selection )
    {
        switch ( selection )
        {
            case 1:
                System.setProperty( "xmatrix.input.db", System.getProperty( "xmatrix.input.db" ).replace( "${hcd.mode}", DIR_SC ) );
                System.setProperty( "herbar.filter.location", System.getProperty( "herbar.filter.location" ).replace( "${hcd.mode}", DIR_SC ) );
                System.setProperty( "herbar.exam.default_list", System.getProperty( "herbar.exam.default_list.sc" ) );
                System.setProperty( "xmatrix.subject", HerbarContext.ENV_SCIENTIFIC );
                System.setProperty( "xmatrix.cache.net.path", System.getProperty( "xmatrix.cache.net.path" ) + "/systematic/" );
                System.setProperty( "xmatrix.cache.local.path", System.getProperty( "xmatrix.cache.local.path" ).replace( "${hcd.mode}", DIR_SC ) );
                break;
            default: // case 2
                System.setProperty( "xmatrix.input.db", System.getProperty( "xmatrix.input.db" ).replace( "${hcd.mode}", DIR_DE ) );
                System.setProperty( "herbar.filter.location", System.getProperty( "herbar.filter.location" ).replace( "${hcd.mode}", DIR_DE ) );
                System.setProperty( "herbar.exam.default_list", System.getProperty( "herbar.exam.default_list.de" ) );
                System.setProperty( "xmatrix.subject", HerbarContext.ENV_DENDROLOGY );
                System.setProperty( "xmatrix.cache.net.path", System.getProperty( "xmatrix.cache.net.path" ) + "/dendro/" );
                System.setProperty( "xmatrix.cache.local.path", System.getProperty( "xmatrix.cache.local.path" ).replace( "${hcd.mode}", DIR_DE ) );
                break;
        }
        LOG.info( "setting database to (xmatrix.input.db): " + System.getProperty( "xmatrix.input.db" ) );
        LOG.info( "setting filter directory to (herbar.filter.location): " + System.getProperty( "herbar.filter.location" ) );
        LOG.info( "setting exam list to (herbar.exam.default_list): " + System.getProperty( "herbar.exam.default_list" ) );
    }

    private void decompressDatabase( int selection )
    {
        if ( !new File( homeDir ).mkdirs() )
        {
            LOG.info( "didn't create destination directory at \"" + homeDir + "\"" );
        }
        final String[] files;
        switch ( selection )
        {
            case 1:
                files = new String[]{"sc/data/2.2.9.properties", "sc/data/2.2.9.data", "sc/data/2.2.9.script"};
                break;
            default: // case 2
                files = new String[]{"de/data/2.2.9.properties", "de/data/2.2.9.data", "de/data/2.2.9.script"};
        }
        try
        {
            for ( final String file : files )
            {
                LOG.info( "decompressing DB from " + AppHerbar.class.getResource( "/" + file ) );
                final InputStream is = AppHerbar.class.getResourceAsStream( "/" + file );
                File destinationFile = new File( homeDir + file );
                destinationFile.getParentFile().mkdirs();
                final OutputStream os = new FileOutputStream( destinationFile );
                IOUtils.copy( is, os );
                os.close();
            }
        } catch ( IOException e )
        {
            e.printStackTrace();
        }
    }

    public static void main( final String[] args )
    {
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

    private void initSplash()
    {
        final ImageIcon imageIcon = ImageLocator.getIcon( "splash.jpg" );
        SwingUtilities.invokeLater( new Runnable()
        {
            public void run()
            {
                splash = new Splash( imageIcon );
            }
        } );
    }

    static
    {
        try
        {
            LogUtils.init();
            LOG = LoggerFactory.getLogger( AppHerbar.class );
        } catch ( Exception e )
        {
            e.printStackTrace();
        }
    }
}
