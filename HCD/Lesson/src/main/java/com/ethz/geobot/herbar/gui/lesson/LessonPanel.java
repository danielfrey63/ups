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
import com.ethz.geobot.herbar.gui.picture.PicturePanel;
import com.ethz.geobot.herbar.modeapi.HerbarContext;
import com.ethz.geobot.herbar.modeapi.HerbarGUIManager;
import com.ethz.geobot.herbar.modeapi.ModeActivationPanel;
import com.ethz.geobot.herbar.model.HerbarModel;
import com.ethz.geobot.herbar.model.Taxon;
import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ResourceBundle;
import javax.swing.JFrame;
import javax.swing.JSplitPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.SubMode.Lernen;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.TaxState.Focus;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.TaxState.SubModus;
import static com.ethz.geobot.herbar.modeapi.HerbarContext.ENV_SCIENTIFIC;
import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.NORTH;
import static javax.swing.JSplitPane.LEFT;
import static javax.swing.JSplitPane.RIGHT;

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

    private final PropertyDisplay lessonPanel;

    private final PicturePanel picturePanel;

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
            final HerbarContext context = mode.getHerbarContext();

            final HerbarModel herbarModel = context.getDataModel();
            taxStateModel = new TaxStateModel( herbarModel );

            final JSplitPane splitPane = new NiceSplitPane();
            splitPane.add( picturePanel = new PicturePanel( herbarModel ), LEFT );
            splitPane.add( lessonPanel = new PropertyDisplay( context, taxStateModel ), RIGHT );

            final JSplitPane navigationEtAll = new NiceSplitPane();
            navigationEtAll.add( new NavigationBuilder( context, taxStateModel ).getPanel(), LEFT );
            navigationEtAll.add( splitPane, RIGHT );
            navigationEtAll.setDividerLocation( 300 );

            final LessonBar bar = new LessonBar( context, taxStateModel );
            setLayout( new BorderLayout() );
            add( bar, NORTH );
            add( navigationEtAll, CENTER );

            taxStateModel.addPropertyChangeListener( Focus.name(), new PropertyChangeListener()
            {
                @Override
                public void propertyChange( PropertyChangeEvent e )
                {
                    final Taxon focus = (Taxon) e.getNewValue();
                    lessonPanel.setTaxFocus( focus );
                    picturePanel.setTaxon( focus, taxStateModel.getNext(), taxStateModel.getPrev() );
                }
            } );
            taxStateModel.addPropertyChangeListener( SubModus.name(), new PropertyChangeListener()
            {
                @Override
                public void propertyChange( PropertyChangeEvent e )
                {
                    final TaxStateModel.SubMode subMode = taxStateModel.getGlobalSubMode();
                    lessonPanel.setSubMode( subMode );
                    picturePanel.setShowText( subMode == Lernen );
                }
            } );

            splitPane.setDividerLocation( 760 );

            if ( System.getProperty( "xmatrix.subject", "" ).equals( ENV_SCIENTIFIC ) )
            {
                taxStateModel.setModel( context.getModel( "4 Liste 600" ) );
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

    public void activate()
    {
        super.activate();
    }

    public void deactivate()
    {
        super.deactivate();
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
