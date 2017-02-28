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
import ch.jfactory.cache.FileImageCache;
import ch.jfactory.cache.ImageCache;
import ch.jfactory.cache.NestedImageCache;
import ch.jfactory.cache.UrlImageCache;
import ch.jfactory.file.FileUtils;
import ch.jfactory.logging.LogUtils;
import ch.jfactory.resource.ImageLocator;
import ch.xmatrix.ups.pmb.exam.Main;
import com.ethz.geobot.herbar.Application;
import com.ethz.geobot.herbar.gui.about.Splash;
import com.ethz.geobot.herbar.gui.mode.ModeManager;
import com.ethz.geobot.herbar.gui.mode.ModeNotFoundException;
import com.ethz.geobot.herbar.gui.picture.CachingExceptionHandler;
import com.ethz.geobot.herbar.gui.picture.ImageCachingExceptionHandler;
import com.ethz.geobot.herbar.gui.picture.PictureCache;
import static com.ethz.geobot.herbar.gui.picture.PictureCache.FINISHED;
import static com.ethz.geobot.herbar.gui.picture.PictureCache.RESUME;
import static com.ethz.geobot.herbar.gui.picture.PictureCache.WAITING;
import com.ethz.geobot.herbar.modeapi.HerbarContext;
import com.ethz.geobot.herbar.model.CommentedPicture;
import com.ethz.geobot.herbar.model.HerbarModel;
import com.ethz.geobot.herbar.model.Picture;
import com.ethz.geobot.herbar.model.PictureTheme;
import com.ethz.geobot.herbar.model.Taxon;
import com.jgoodies.looks.plastic.PlasticXPLookAndFeel;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Set;
import java.util.TreeSet;
import static java.util.jar.Attributes.Name.IMPLEMENTATION_VERSION;
import java.util.jar.Manifest;
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
    private static final String PROPERTYNAME_VERSION_FULL = "herbar.version";

    private static Logger LOG = LoggerFactory.getLogger( AppHerbar.class );

    private static final String DIR_SC = "sc";

    private static final String DIR_DE = "de";

    private static final String homeDir = System.getProperty( "user.home" ).replace( '\\', '/' ) + "/.hcd2/";

    private static Splash splash;

    private PictureCache cache;

    private PictureCache backgroundCache;

    private NestedImageCache imageCache;

    /**
     * reference to the one and only mainframe
     */
    private static MainFrame mainFrame = null;

    public AppHerbar( final int selection ) throws IOException
    {
        setVersion( AppHerbar.class );
        setupDirectories( selection );
        initSplash();
        decompressDatabase( selection );
        installDownloaders();
        System.setProperty( ImageLocator.PROPERTY_IMAGE_LOCATION, System.getProperty( "xmatrix.picture.path" ) );

        mainFrame = new MainFrame();
        mainFrame.setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );
        // load old user settings
        try
        {
            ModeManager.getInstance().registerCaches( cache, backgroundCache );
            mainFrame.loadState();
            mainFrame.setVisible( true );
        }
        catch ( ModeNotFoundException e )
        {
            LOG.error( "couldn't load state", e );
        }

        splash.finish();
    }

    private void installDownloaders() throws IOException
    {
        CachingExceptionHandler exceptionHandler = new ImageCachingExceptionHandler();
        cache = new PictureCache( "Main-Image-Thread", exceptionHandler, ImageLocator.PICT_LOCATOR );
        ensureBackgroundThread( exceptionHandler );
    }

    private void ensureBackgroundThread( final CachingExceptionHandler exceptionHandler ) throws IOException
    {
        final String fileName = ImageLocator.getPicturePath() + "/_PictureCacheComplete.xml";
        final String property = "version";
        final String version = "6.0.3089";
        if ( backgroundCache == null && !version.equals( FileUtils.readPropertyFromXML( fileName, property ) ) )
        {
            imageCache = new NestedImageCache( new ImageCache[0], new FileImageCache( ImageLocator.getPicturePath(), "jpg" ), new UrlImageCache( ImageLocator.getImageURL(), "jpg" ) );
            backgroundCache = new PictureCache( "Background-Image-Thread", exceptionHandler, imageCache );
            final PropertyChangeListener waitingListener = new PropertyChangeListener()
            {
                @Override
                public void propertyChange( PropertyChangeEvent e )
                {
                    LOG.info( "resuming " + backgroundCache.getName() + " by WAITING of " + cache.getName() );
                    backgroundCache.resume();
                }
            };
            final PropertyChangeListener resumingListener = new PropertyChangeListener()
            {
                @Override
                public void propertyChange( PropertyChangeEvent e )
                {
                    LOG.info( "suspending " + backgroundCache.getName() + " by RESUME of " + cache.getName() );
                    backgroundCache.suspend();
                }
            };
            cache.addPropertyChangeListener( WAITING, waitingListener );
            cache.addPropertyChangeListener( RESUME, resumingListener );
            backgroundCache.addPropertyChangeListener( FINISHED, new PropertyChangeListener()
            {
                @Override
                public void propertyChange( PropertyChangeEvent evt )
                {
                    LOG.info( "finishing " + backgroundCache.getName() );
                    backgroundCache.stop();
                    cache.removePropertyChangeListener( WAITING, waitingListener );
                    cache.removePropertyChangeListener( RESUME, resumingListener );
                    if ( !backgroundCache.hadError() )
                    {
                        try
                        {
                            org.apache.commons.io.FileUtils.touch( new File( fileName ) );
                            FileUtils.writePropertyToXML( fileName, property, version );
                        }
                        catch ( IOException e )
                        {
                            e.printStackTrace();
                        }
                    }

                    LOG.info( "resuming " + cache.getName() );
                }
            } );

            final Set<String> images = collectAllPictures();
            backgroundCache.queueImages( images.toArray( new String[images.size()] ), false, false, cache.getStatus() == 0 );
        }
    }

    private Set<String> collectAllPictures()
    {
        LOG.info( "collecting all pictures" );
        final HerbarModel dataModel = Application.getInstance().getModel();
        final Taxon taxon = dataModel.getRootTaxon();
        final PictureTheme[] themes = dataModel.getPictureThemes();
        final Set<String> pictureNames = new TreeSet<String>();
        collectPictures( taxon, themes, pictureNames );
        return pictureNames;
    }

    private void collectPictures( final Taxon taxon, final PictureTheme[] themes, final Set<String> pictureNames )
    {
        for ( final PictureTheme pictureTheme : themes )
        {
            final CommentedPicture[] pictures = taxon.getCommentedPictures( pictureTheme );
            for ( final CommentedPicture picture : pictures )
            {
                final Picture pic = picture.getPicture();
                if ( pic != null )
                {
                    pictureNames.add( pic.getName() );
                }
                else
                {
                    LOG.error( "picture for \"" + picture + "\" is null" );
                }
            }
        }
        for ( final Taxon child : taxon.getChildTaxa() )
        {
            collectPictures( child, themes, pictureNames );
        }
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
                boolean b = destinationFile.getParentFile().mkdirs();
                if ( !b )
                {
                    LOG.debug( "parent directory for DB exists, no need to create one" );
                }
                final OutputStream os = new FileOutputStream( destinationFile );
                IOUtils.copy( is, os );
                os.close();
            }
            Application.getInstance().getModel().getRootTaxon();
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
            final int selection;
            if ( args.length == 0 )
            {
                // Todo: Implements as a case of specialized resource bundles
                final EnvironmentDialog dialog = new EnvironmentDialog( null );
                dialog.setVisible( true );
                if ( dialog.ok )
                {
                    selection = dialog.systematicRadio.isSelected() ? 1 : 2;
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
            if ( selection == 3 )
            {
                System.setProperty( "password", "" );
                Main.main( null );
            }
            else
            {
                new AppHerbar( selection );
            }
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

    private void initSplash()
    {
        final ImageIcon imageIcon = ImageLocator.getIcon( "splash.png" );
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
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }

    public static void setVersion( final Class clazz )
    {
        try
        {
            final String classContainer = clazz.getProtectionDomain().getCodeSource().getLocation().toString();
            final URL manifestUrl = new URL( "jar:" + classContainer + "!/META-INF/MANIFEST.MF" );
            final Manifest manifest = new Manifest( manifestUrl.openStream() );
            final String version = manifest.getMainAttributes().getValue( IMPLEMENTATION_VERSION );
            System.setProperty( PROPERTYNAME_VERSION_FULL, version );
            LOG.info( "eBot version is " + version );
        }
        catch ( IOException e )
        {
            System.setProperty( PROPERTYNAME_VERSION_FULL, "6.0.SNAPSHOT" );
        }
    }

}
