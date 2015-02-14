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
package com.ethz.geobot.herbar.gui.lesson;

import ch.jfactory.cache.FileImageCache;
import ch.jfactory.cache.ImageCache;
import ch.jfactory.cache.NestedImageCache;
import ch.jfactory.cache.UrlImageCache;
import ch.jfactory.component.split.NiceSplitPane;
import ch.jfactory.logging.LogUtils;
import ch.jfactory.resource.ImageLocator;
import ch.jfactory.resource.Strings;
import static com.ethz.geobot.herbar.gui.lesson.PictureCache.FINISHED;
import static com.ethz.geobot.herbar.gui.lesson.PictureCache.RESUME;
import static com.ethz.geobot.herbar.gui.lesson.PictureCache.WAITING;
import com.ethz.geobot.herbar.gui.picture.PictureModel;
import com.ethz.geobot.herbar.modeapi.HerbarContext;
import com.ethz.geobot.herbar.modeapi.ModeActivationPanel;
import com.ethz.geobot.herbar.model.CommentedPicture;
import com.ethz.geobot.herbar.model.HerbarModel;
import com.ethz.geobot.herbar.model.Picture;
import com.ethz.geobot.herbar.model.PictureTheme;
import com.ethz.geobot.herbar.model.Taxon;
import java.awt.BorderLayout;
import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.NORTH;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.prefs.Preferences;
import javax.swing.JLabel;
import javax.swing.JSplitPane;
import static javax.swing.JSplitPane.LEFT;
import static javax.swing.JSplitPane.RIGHT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Will be added to the BaseFrame as an own mode.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:06:56 $
 */
public class LessonPanel extends ModeActivationPanel
{
    private final static Logger LOG;

    private final static String SPLIT_LOCATION_1 = "split.location.1";
    private final static String SPLIT_LOCATION_2 = "split.location.2";

    private final PictureCache cache;

    private final HerbarContext context;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool( 1 );

    private final NestedImageCache imageCache = new NestedImageCache( new ImageCache[0],
            new FileImageCache( ImageLocator.getPicturePath(), "jpg" ),
            new UrlImageCache( ImageLocator.getImageURL(), "jpg" ) );

    private LessonCachingExceptionHandler exceptionHandler;

    private JLabel downloadStatus = new JLabel();

    private PictureCache backgroundCache;

    /**
     * Creates new form LessonPanel. This panel will be instantiated by reflection.
     *
     * @param mode the mode of this panel
     */
    public LessonPanel( final LessonMode mode )
    {
        LOG.info( "initiate mode panel" );

        /*
         * The constructor is called by reflection, so any exceptions occurring
         * during construction would be visible as a simple InvocationException.
         * To keep the relevant exception, make sure to catch and re-throw
         * the exceptions in the constructor.
         */
        try
        {
            context = mode.getHerbarContext();
            context.getModels();

            final HerbarModel herbarModel = context.getDataModel();
            final TaxStateModel taxStateModel = new TaxStateModel( context );
            final PictureModel pictureModel = new PictureModel( herbarModel.getPictureThemes() );

            exceptionHandler = new LessonCachingExceptionHandler( context.getHerbarGUIManager().getParentFrame() );

            cache = new PictureCache( "Main-Image-Thread", exceptionHandler, ImageLocator.PICT_LOCATOR );

            final JSplitPane splitPane = new NiceSplitPane();
            splitPane.add( new PicturePanel( taxStateModel, herbarModel.getPictureThemes(), pictureModel, cache ), LEFT );
            splitPane.add( new PropertyDisplay( context, taxStateModel ), RIGHT );

            final JSplitPane navigationEtAll = new NiceSplitPane();
            navigationEtAll.add( new NavigationBuilder( context, taxStateModel ).getPanel(), LEFT );
            navigationEtAll.add( splitPane, RIGHT );
            navigationEtAll.setDividerLocation( 300 );

            final LessonBar bar = new LessonBar( context, taxStateModel );
            setLayout( new BorderLayout() );
            add( bar, NORTH );
            add( navigationEtAll, CENTER );

            addListeners( splitPane, navigationEtAll );
            loadState( splitPane, navigationEtAll );
        }
        catch ( RuntimeException e )
        {
            LOG.error( "LessonPanel(...)", e );
            throw e;
        }
        catch ( Error e )
        {
            LOG.error( "LessonPanel(...)", e );
            throw e;
        }
    }

    private void addListeners( final JSplitPane splitPane, final JSplitPane navigationEtAll )
    {
        splitPane.addPropertyChangeListener( JSplitPane.DIVIDER_LOCATION_PROPERTY, new PropertyChangeListener()
        {
            @Override
            public void propertyChange( PropertyChangeEvent evt )
            {
                context.getPreferencesNode().putInt( SPLIT_LOCATION_1, splitPane.getDividerLocation() );
            }
        } );
        navigationEtAll.addPropertyChangeListener( JSplitPane.DIVIDER_LOCATION_PROPERTY, new PropertyChangeListener()
        {
            @Override
            public void propertyChange( PropertyChangeEvent evt )
            {
                context.getPreferencesNode().putInt( SPLIT_LOCATION_2, navigationEtAll.getDividerLocation() );
            }
        } );
    }

    private void loadState( final JSplitPane splitPane, final JSplitPane navigationEtAll )
    {
        final Preferences prefsNode = context.getPreferencesNode();
        splitPane.setDividerLocation( prefsNode.getInt( SPLIT_LOCATION_1, 760 ) );
        navigationEtAll.setDividerLocation( prefsNode.getInt( SPLIT_LOCATION_2, 280 ) );
    }

    public void activate()
    {
        super.activate();
        context.getHerbarGUIManager().getStatusBar().addStatusComponent( downloadStatus );
        downloadStatus.addMouseListener( new MouseAdapter()
        {
            @Override
            public void mouseClicked( MouseEvent e )
            {
                exceptionHandler.displayDialog( imageCache );
            }
        } );
        ensureBackgroundThread();
        final Set<String> images = collectAllPictures();
        backgroundCache.queueImages( images.toArray( new String[images.size()] ), false, false, cache.getStatus() == 0 );
        updateFromCache();
    }

    private void ensureBackgroundThread()
    {
        if ( backgroundCache == null )
        {
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
                    LOG.info( "resuming " + cache.getName() );
                }
            } );
        }
    }

    private void updateFromCache()
    {
        scheduler.scheduleAtFixedRate( new Runnable()
        {
            @Override
            public void run()
            {
                final double status = backgroundCache.getStatus();
                final boolean errors = backgroundCache.hadError();
                final boolean done = status == 0.0;
                final String percentage = ((int) ((1 - status) * 100)) + "%";
                downloadStatus.setText( "Status: " +
                        (errors ? (done ? "Nicht alles offline verfügbar" : "Überprüfung " + percentage + " fertig (Fehler)") :
                                (done ? "Alles offline verfügbar" : "Überprüfung " + percentage + " fertig")) );
            }
        }, 0, 1, TimeUnit.SECONDS );
    }

    private Set<String> collectAllPictures()
    {
        LOG.info( "collecting all pictures" );
        final HerbarModel dataModel = context.getDataModel();
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

    public void deactivate()
    {
        super.deactivate();
        context.getHerbarGUIManager().getStatusBar().remove( downloadStatus );
    }

    static
    {
        LogUtils.init();
        try
        {
            Strings.addResourceBundle( LessonMode.class, ResourceBundle.getBundle( LessonPanel.class.getName() ) );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        LOG = LoggerFactory.getLogger( LessonPanel.class );
    }
}
