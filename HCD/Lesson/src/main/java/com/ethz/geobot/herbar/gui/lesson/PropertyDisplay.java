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

import ch.jfactory.model.graph.tree.VirtualGraphTreeNodeFilter;
import static ch.jfactory.resource.ImageLocator.getIcon;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.SubMode.ABFRAGEN;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.SubMode.LERNEN;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.TaxState.FOCUS;
import static com.ethz.geobot.herbar.gui.lesson.TaxStateModel.TaxState.SUB_MODUS;
import com.ethz.geobot.herbar.modeapi.HerbarContext;
import com.ethz.geobot.herbar.model.HerbarModel;
import com.ethz.geobot.herbar.model.Level;
import com.ethz.geobot.herbar.model.Taxon;
import java.awt.CardLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import static javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT;
import static javax.swing.SwingConstants.TOP;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Displays two tabs. First tab presents the data to learn. The second one tests the user.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:06:56 $
 */
public class PropertyDisplay extends JPanel
{
    protected final static Logger LOG = LoggerFactory.getLogger( PropertyDisplay.class );

    private final ArrayList<AttributeTreePanel> displayPanels = new ArrayList<AttributeTreePanel>();

    private final ArrayList<AttributeTreePanel> guessPanels = new ArrayList<AttributeTreePanel>();
    private final TaxStateModel taxStateModel;

    private CardLayout cardLayout;

    protected HerbarModel herbarModel;

    /**
     * Creates a new instance of PropertyDisplay.
     *
     * @param herbarContext the main context
     * @param taxStateModel the model
     */
    public PropertyDisplay( final HerbarContext herbarContext, final TaxStateModel taxStateModel )
    {
        this.taxStateModel = taxStateModel;
        initGui( herbarContext, taxStateModel );
        initListeners();
        setFocus( taxStateModel.getFocus() );
    }

    private void initGui( HerbarContext herbarContext, TaxStateModel taxStateModel )
    {
        cardLayout = new CardLayout();
        setLayout( cardLayout );

        final String subject = System.getProperty( "xmatrix.subject" );
        final ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext( "spring/lesson-" + subject + ".xml" );

        final JTabbedPane displayTabs = new JTabbedPane( TOP );
        final JTabbedPane guessTabs = new JTabbedPane( TOP );

        displayTabs.setTabPlacement( TOP );
        displayTabs.setTabLayoutPolicy( SCROLL_TAB_LAYOUT );

        displayPanels.add( getTab( displayTabs, herbarContext, taxStateModel, context, "stopper", "title1", "filter1a", "icon1" ) );
        displayPanels.add( getTab( displayTabs, herbarContext, taxStateModel, context, "stopper", "title2", "filter2a", "icon2" ) );
        displayPanels.add( getTab( displayTabs, herbarContext, taxStateModel, context, "stopper", "title3", "filter3a", "icon3" ) );
        displayPanels.add( getTab( displayTabs, herbarContext, taxStateModel, context, "stopperSyn", "title4", "filter4a", "icon4" ) );

        add( displayTabs, LERNEN.name() );

        guessTabs.setTabPlacement( TOP );
        guessTabs.setTabLayoutPolicy( SCROLL_TAB_LAYOUT );

        guessPanels.add( getTab( guessTabs, herbarContext, taxStateModel, context, "stopper", "title1", "filter1b", "icon1" ) );
        guessPanels.add( getTab( guessTabs, herbarContext, taxStateModel, context, "stopper", "title2", "filter2b", "icon2" ) );
        guessPanels.add( getTab( guessTabs, herbarContext, taxStateModel, context, "stopper", "title3", "filter3b", "icon3" ) );
        guessPanels.add( getTab( guessTabs, herbarContext, taxStateModel, context, "stopperSyn", "title4", "filter4b", "icon4" ) );

        add( guessTabs, ABFRAGEN.name() );

        displayTabs.addChangeListener( new TabSyncListener( guessTabs ) );
        guessTabs.addChangeListener( new TabSyncListener( displayTabs ) );
    }

    private AttributeTreePanel getTab( JTabbedPane tabbedPane, final HerbarContext herbarContext, final TaxStateModel taxStateModel, final ClassPathXmlApplicationContext context,
                                       final String stopperLevel, final String title, final String filter, final String icon )
    {
        if ( context.containsBean( title ) )
        {
            herbarModel = herbarContext.getDataModel();
            final Level stopLevel = herbarModel.getLevel( (String) context.getBean( stopperLevel ) );
            final String titleString = (String) context.getBean( title );
            final AttributeTreePanel display = new AttributeTreePanel( herbarContext, stopLevel, taxStateModel, titleString, (VirtualGraphTreeNodeFilter) context.getBean( filter ) );
            tabbedPane.addTab( titleString, getIcon( (String) context.getBean( icon ) ), display );
            return display;
        }
        else
        {
            return null;
        }
    }

    public void initListeners()
    {
        taxStateModel.addPropertyChangeListener( FOCUS.name(), new PropertyChangeListener()
        {
            @Override
            public void propertyChange( PropertyChangeEvent e )
            {
                final Taxon focus = (Taxon) e.getNewValue();
                setFocus( focus );
            }
        } );
        taxStateModel.addPropertyChangeListener( SUB_MODUS.name(), new PropertyChangeListener()
        {
            @Override
            public void propertyChange( PropertyChangeEvent e )
            {
                final TaxStateModel.SubMode subMode = taxStateModel.getGlobalSubMode();
                cardLayout.show( PropertyDisplay.this, subMode.name() );
            }
        } );

    }

    private void setFocus( Taxon focus )
    {
        for ( final AttributeTreePanel display : displayPanels )
        {
            if ( display != null )
            {
                display.setTaxonFocus( focus );
            }
        }
        for ( final AttributeTreePanel display : guessPanels )
        {
            if ( display != null )
            {
                display.setTaxonFocus( focus );
            }
        }
    }

    public String toString()
    {
        return "" + hashCode();
    }

    private static class TabSyncListener implements ChangeListener
    {
        private final JTabbedPane tabs;

        public TabSyncListener( JTabbedPane tabs )
        {
            this.tabs = tabs;
        }

        @Override
        public void stateChanged( ChangeEvent e )
        {
            tabs.setSelectedIndex( ((JTabbedPane) e.getSource()).getSelectedIndex() );
        }
    }
}
