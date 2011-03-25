/*
 * Herbar CD-ROM version 2
 *
 * AppHerbar.java
 *
 * Created on 2. April 2002
 */
package com.ethz.geobot.herbar.gui;

import ch.jfactory.animation.AnimationQueue;
import ch.jfactory.animation.Paintable;
import ch.jfactory.animation.fading.FadingPaintable;
import ch.jfactory.animation.scrolltext.ScrollingTextPaintable;
import ch.jfactory.application.SystemUtil;
import ch.jfactory.application.presentation.WindowUtils;
import ch.jfactory.component.Dialogs;
import ch.jfactory.logging.LogUtils;
import ch.jfactory.resource.ImageLocator;
import ch.jfactory.resource.Strings;
import com.ethz.geobot.herbar.Application;
import com.ethz.geobot.herbar.gui.about.Splash;
import com.jgoodies.looks.plastic.PlasticXPLookAndFeel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
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

    private static final String DIR_GE = "/ge/";

    private static final String DIR_SC = "/sc/";

    private static final String DIR_DE = "/de/";

    private static final String EXT_GE = "ge";

    private static final String EXT_SC = "sc";

    private static final String EXT_DE = "de";

    private static final String homeDir = System.getProperty( "user.home" ).replace( '\\', '/' ) + "/.hcd2/";

    private static Splash splash;

    /** reference to the one and only mainframe */
    private static MainFrame mainFrame = null;

    /** Started in the "german" environment. */
    public static final String ENV_GERMAN = "german";

    /** Started in the "scientific" environment. */
    public static final String ENV_SCIENTIFIC = "scientific";

    /** Started in the "dendrology" environment. */
    public static final String ENV_DENDRO = "dendro";

    public AppHerbar()
    {
        initSplash();
        switchDatabase();
        decompressDatabase();
        Application.getInstance().getModel();
        System.setProperty( ImageLocator.PROPERTY_IMAGE_LOCATION, System.getProperty( "xmatrix.picture.path" ) );

        SwingUtilities.invokeLater( new Runnable()
        {
            public void run()
            {
                mainFrame = new MainFrame();
                mainFrame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

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

    private void switchDatabase()
    {
        // Todo: Implements as a case of specialized resource bundles
        final EnvironmentDialog dialog = new EnvironmentDialog( (JFrame) null );
        dialog.setVisible( true );
        int selection = -1;
        if ( dialog.ok )
        {
            selection = dialog.systematicRadio.isSelected() ? dialog.scientificRadio.isSelected() ? 0 : 1 : 2;
        }
        else
        {
            LOG.info( "user canceled choice dialog. exiting application now." );
            System.exit( 1 );
        }
        switch ( selection )
        {
            case 0:
                System.setProperty( "xmatrix.input.db", System.getProperty( "xmatrix.input.db" ) + EXT_SC );
                System.setProperty( "herbar.filter.location", System.getProperty( "herbar.filter.location" ) + DIR_SC );
                System.setProperty( "herbar.exam.defaultlist", System.getProperty( "herbar.exam.defaultlist.sc" ) );
                System.setProperty( "xmatrix.subject", ENV_SCIENTIFIC );
                break;
            case 1:
                System.setProperty( "xmatrix.input.db", System.getProperty( "xmatrix.input.db" ) + EXT_GE );
                System.setProperty( "herbar.filter.location", System.getProperty( "herbar.filter.location" ) + DIR_GE );
                System.setProperty( "herbar.exam.defaultlist", System.getProperty( "herbar.exam.defaultlist.ge" ) );
                System.setProperty( "xmatrix.subject", ENV_GERMAN );
                break;
            case 2:
                System.setProperty( "xmatrix.input.db", System.getProperty( "xmatrix.input.db" ) + EXT_DE );
                System.setProperty( "herbar.filter.location", System.getProperty( "herbar.filter.location" ) + DIR_DE );
                System.setProperty( "xmatrix.subject", ENV_DENDRO );
                break;
            default:
                break;
        }
        LOG.info( "setting database to (xmatrix.input.db): " + System.getProperty( "xmatrix.input.db" ) );
        LOG.info( "setting filter directory to (herbar.filter.location): " + System.getProperty( "herbar.filter.location" ) );
        LOG.info( "setting exam list to (herbar.exam.defaultlist): " + System.getProperty( "herbar.exam.defaultlist" ) );
    }

    private void decompressDatabase()
    {
        final String destinationDir = homeDir + "data/";
        new File( destinationDir ).mkdirs();
        final String[] files = new String[]{
                "hcdsqlsc.properties", "hcdsqlsc.backup", "hcdsqlsc.data", "hcdsqlsc.script",
                "hcdsqlge.properties", "hcdsqlge.backup", "hcdsqlge.data", "hcdsqlge.script",
                "hcdsqlde.properties", "hcdsqlde.script"
        };
        try
        {
            for ( final String file : files )
            {
                LOG.info( "decompressing DB from " + AppHerbar.class.getResource( "/" + file ) );
                final InputStream is = AppHerbar.class.getResourceAsStream( "/" + file );
                final OutputStream os = new FileOutputStream( destinationDir + file );
                IOUtils.copy( is, os );
                os.close();
            }
        }
        catch ( IOException e )
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
            new AppHerbar();
        }
        catch ( IllegalStateException e )
        {
            LOG.error( "security check failed", e );
            showPicturesNotFound();
        }
        catch ( Throwable e )
        {
            LOG.error( "fatal error occured in Application: " + e.getMessage(), e );
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

    private static void showPicturesNotFound()
    {
        final JComponent pane = ( mainFrame == null ? null : mainFrame.getRootPane() );
        Dialogs.showErrorMessage( pane, "Fehler", Strings.getString( "ERROR.IMAGES_NOT_FOUND" ) );
        SystemUtil.EXIT.exit( 1 );
    }

    private static AnimationQueue getScroller()
    {
        final AnimationQueue scrollingComponent = new AnimationQueue();
        scrollingComponent.setBounds( 100, 68, 200, 167 );
        final Insets insets = new Insets( 0, 10, 0, 10 );
        scrollingComponent.setInsets( insets );

        final Color fadeColor = new Color( 255, 255, 255, 150 );
        final Paintable fader = new FadingPaintable( fadeColor );
        scrollingComponent.addPaintable( fader );

        final int printSpaceWidth = scrollingComponent.getSize().width - insets.left - insets.right;
        final InputStream textFileInputStream = AppHerbar.class.getResourceAsStream( "/News.txt" );
        final ScrollingTextPaintable scroller = new ScrollingTextPaintable( textFileInputStream, printSpaceWidth, true );
        scroller.setBackgroundColor( fadeColor );
        scroller.setScrollDelay( 5 );
        scroller.setParagraphDelay( 12000 );
        scrollingComponent.addPaintable( scroller );

        return scrollingComponent;
    }

    static
    {
        try
        {
            LogUtils.init();
            LOG = LoggerFactory.getLogger( AppHerbar.class );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }
}
