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
import static com.ethz.geobot.herbar.gui.lesson.PictureCache.RESUME;
import static com.ethz.geobot.herbar.gui.lesson.PictureCache.WAITING;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.SubMode.LERNEN;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.TaxState.FOCUS;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.TaxState.SUB_MODUS;
import com.ethz.geobot.herbar.gui.picture.PictureModel;
import com.ethz.geobot.herbar.modeapi.HerbarContext;
import static com.ethz.geobot.herbar.modeapi.HerbarContext.ENV_SCIENTIFIC;
import com.ethz.geobot.herbar.modeapi.ModeActivationPanel;
import com.ethz.geobot.herbar.model.CommentedPicture;
import com.ethz.geobot.herbar.model.HerbarModel;
import com.ethz.geobot.herbar.model.PictureTheme;
import com.ethz.geobot.herbar.model.Taxon;
import java.awt.BorderLayout;
import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.NORTH;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
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

    private final TaxStateModel taxStateModel;

    private final PropertyDisplay propertyDisplay;

    private final PicturePanel picturePanel;

    private final PictureModel pictureModel;

    private final PictureCache cache;

    private final HerbarContext context;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool( 1 );

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

            final HerbarModel herbarModel = context.getDataModel();
            taxStateModel = new TaxStateModel( context, herbarModel );

            pictureModel = new PictureModel( herbarModel.getPictureThemes() );
            cache = new PictureCache( "Main-Image-Thread", new LessonCachingExceptionHandler( this ), ImageLocator.PICT_LOCATOR );

            final JSplitPane splitPane = new NiceSplitPane();
            splitPane.add( picturePanel = new PicturePanel( herbarModel.getPictureThemes(), pictureModel, cache ), LEFT );
            splitPane.add( propertyDisplay = new PropertyDisplay( context, taxStateModel ), RIGHT );

            final JSplitPane navigationEtAll = new NiceSplitPane();
            navigationEtAll.add( new NavigationBuilder( context, taxStateModel ).getPanel(), LEFT );
            navigationEtAll.add( splitPane, RIGHT );
            navigationEtAll.setDividerLocation( 300 );

            final LessonBar bar = new LessonBar( context, taxStateModel );
            setLayout( new BorderLayout() );
            add( bar, NORTH );
            add( navigationEtAll, CENTER );

            splitPane.setDividerLocation( 760 );

            initListeners();

            if ( System.getProperty( "xmatrix.subject", "" ).equals( ENV_SCIENTIFIC ) )
            {
                taxStateModel.setModel( context.getModel( System.getProperty( "herbar.model.default", "" ) ) );
            }
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

    private void initListeners()
    {
        taxStateModel.addPropertyChangeListener( FOCUS.name(), new PropertyChangeListener()
        {
            @Override
            public void propertyChange( PropertyChangeEvent e )
            {
                final Taxon focus = (Taxon) e.getNewValue();
                propertyDisplay.setTaxFocus( focus );
                picturePanel.setTaxon( focus, taxStateModel.getNext(), taxStateModel.getPrev() );
            }
        } );
        taxStateModel.addPropertyChangeListener( SUB_MODUS.name(), new PropertyChangeListener()
        {
            @Override
            public void propertyChange( PropertyChangeEvent e )
            {
                final TaxStateModel.SubMode subMode = taxStateModel.getGlobalSubMode();
                propertyDisplay.setSubMode( subMode );
                picturePanel.setShowText( subMode == LERNEN );
            }
        } );
    }

    public void activate()
    {
        super.activate();
        context.getHerbarGUIManager().getStatusBar().addStatusComponent( downloadStatus );
        ensureBackgroundThread();
        for ( final String name : collectAllPictures() )
        {
            backgroundCache.cacheImage( name, false, false );
        }
        updateFromCache();
    }

    private void ensureBackgroundThread()
    {
        if ( backgroundCache == null )
        {
            backgroundCache = new PictureCache(
                    "Background-Image-Thread", new LessonCachingExceptionHandler( this ),
                    new NestedImageCache( new ImageCache[0],
                            new FileImageCache( ImageLocator.getPicturePath(), "jpg" ),
                            new UrlImageCache( ImageLocator.getImageURL(), "jpg" ) ) );
            backgroundCache.suspend();
            cache.addPropertyChangeListener( WAITING, new PropertyChangeListener()
            {
                @Override
                public void propertyChange( PropertyChangeEvent e )
                {
                    LOG.info( "switching to background image loader" );
                    backgroundCache.resume();
                }
            } );
            cache.addPropertyChangeListener( RESUME, new PropertyChangeListener()
            {
                @Override
                public void propertyChange( PropertyChangeEvent e )
                {
                    LOG.info( "switching to main image loader" );
                    backgroundCache.suspend();
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
                downloadStatus.setText( "Überprüfe Offlinestatus: " +
                        (errors ? (done ? "Es gab Fehler" : percentage + " (Fehler)") :
                                (done ? "Alles offline verfügbar" : percentage)) );
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
                pictureNames.add( picture.getPicture().getName() );
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
