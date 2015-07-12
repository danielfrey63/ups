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

import ch.jfactory.component.split.NiceSplitPane;
import ch.jfactory.logging.LogUtils;
import ch.jfactory.resource.Strings;
import com.ethz.geobot.herbar.gui.picture.PictureModel;
import com.ethz.geobot.herbar.modeapi.HerbarContext;
import com.ethz.geobot.herbar.modeapi.ModeActivationPanel;
import com.ethz.geobot.herbar.model.HerbarModel;
import java.awt.BorderLayout;
import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.NORTH;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ResourceBundle;
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

    private final HerbarContext context;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool( 1 );

    private JLabel downloadStatus = new JLabel();

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

            final JSplitPane splitPane = new NiceSplitPane();
            splitPane.add( new PicturePanel( taxStateModel, herbarModel.getPictureThemes(), pictureModel, context.getMainCache() ), LEFT );
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
        updateFromCache();
    }

    private void updateFromCache()
    {
        scheduler.scheduleAtFixedRate( new Runnable()
        {
            @Override
            public void run()
            {
                if ( context.getBackgroundCache() != null )
                {
                    final double status = context.getBackgroundCache().getStatus();
                    final boolean errors = context.getBackgroundCache().hadError();
                    final boolean done = status == 0.0;
                    final String percentage = ((int) ((1 - status) * 100)) + "%";
                    downloadStatus.setText( "Status: " +
                            (errors ? (done ? "Nicht alles offline verfügbar" : "Überprüfung " + percentage + " fertig (Fehler)") :
                                    (done ? "Alles offline verfügbar" : "Überprüfung " + percentage + " fertig")) );
                    if ( done )
                    {
                        LOG.info( "stopping updates from caching threads to status bar" );
                        scheduler.shutdownNow();
                    }
                }
                else
                {
                    downloadStatus.setText( "Status: Alles offline verfügbar" );
                }
            }
        }, 0, 1, TimeUnit.SECONDS );
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
