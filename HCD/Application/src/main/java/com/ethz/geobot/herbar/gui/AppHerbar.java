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
import com.ethz.geobot.herbar.gui.util.HerbarTheme;
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
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.metal.MetalLookAndFeel;
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

    public AppHerbar()
    {
        switchDatabase();
        initSplash();
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
            }
        } );
    }

    public static MainFrame getMainFrame()
    {
        return mainFrame;
    }

    private void switchDatabase()
    {
        // Todo: Implement this as a case of specialized resource bundles
        final String[] options = new String[]{"Deutsch", "Wissenschaftlich", "Dendro"};
        final String message = Strings.getString( "SWITCH.MESSAGE" );
        final String title = Strings.getString( "SWITCH.TITLE" );
        final int selection = Dialogs.showOptionsQuestion( null, title, message, options, options[1] );
        switch ( selection )
        {
            case 0:
                System.setProperty( "xmatrix.input.db", System.getProperty( "xmatrix.input.db" ) + EXT_GE );
                System.setProperty( "herbar.filter.location", System.getProperty( "herbar.filter.location" ) + DIR_GE );
                System.setProperty( "herbar.exam.defaultlist", System.getProperty( "herbar.exam.defaultlist.ge" ) );
                break;
            case 1:
                System.setProperty( "xmatrix.input.db", System.getProperty( "xmatrix.input.db" ) + EXT_SC );
                System.setProperty( "herbar.filter.location", System.getProperty( "herbar.filter.location" ) + DIR_SC );
                System.setProperty( "herbar.exam.defaultlist", System.getProperty( "herbar.exam.defaultlist.sc" ) );
                break;
            case 2:
                System.setProperty( "xmatrix.input.db", System.getProperty( "xmatrix.input.db" ) + EXT_DE );
                System.setProperty( "herbar.filter.location", System.getProperty( "herbar.filter.location" ) + DIR_DE );
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
//        RepaintManager.setCurrentManager(new CheckThreadViolationRepaintManager());

        LOG.debug( "Starting main-Application" );

        try
        {
            MetalLookAndFeel.setCurrentTheme( HerbarTheme.THEME );
            UIManager.setLookAndFeel( UIManager.getLookAndFeel() );
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
        finally
        {
            if ( splash != null )
            {
                splash.finish();
            }
        }

    }

    private void initSplash()
    {
        ImageLocator.registerErrorHandler( new ImageLocator.ErrorHandler()
        {
            public void handleError( final Throwable e )
            {
                JOptionPane.showMessageDialog( mainFrame, e.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE );
            }
        } );
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
