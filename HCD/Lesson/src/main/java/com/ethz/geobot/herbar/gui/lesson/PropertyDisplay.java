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
import com.ethz.geobot.herbar.modeapi.HerbarContext;
import com.ethz.geobot.herbar.model.HerbarModel;
import com.ethz.geobot.herbar.model.Level;
import com.ethz.geobot.herbar.model.Taxon;
import javax.swing.JTabbedPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import static ch.jfactory.resource.ImageLocator.getIcon;

/**
 * Displays two tabs. First tab presents the data to learn. The second one tests the user.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:06:56 $
 */
public class PropertyDisplay extends JTabbedPane
{
    protected final static Logger LOG = LoggerFactory.getLogger( PropertyDisplay.class );

    private final AttributeTreePanel display1;

    private final AttributeTreePanel display2;

    private final AttributeTreePanel display3;

    private final AttributeTreePanel display4;

    protected HerbarModel herbarModel;

    /**
     * Creates a new instance of PropertyDisplay.
     *
     * @param herbarContext the main context
     * @param taxStateModel the model
     */
    public PropertyDisplay( final HerbarContext herbarContext, final TaxStateModel taxStateModel )
    {
        this.setTabPlacement( BOTTOM );
        this.setTabLayoutPolicy( SCROLL_TAB_LAYOUT );

        final String subject = System.getProperty( "xmatrix.subject" );
        final ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext( "spring/lesson-" + subject + ".xml" );
        display1 = getTab( herbarContext, taxStateModel, context, "stopper", "title1", "filter1", "icon1" );
        display2 = getTab( herbarContext, taxStateModel, context, "stopper", "title2", "filter2", "icon2" );
        display3 = getTab( herbarContext, taxStateModel, context, "stopper", "title3", "filter3", "icon3" );
        display4 = getTab( herbarContext, taxStateModel, context, "stopperSyn", "title4", "filter4", "icon4" );
    }

    private AttributeTreePanel getTab( final HerbarContext herbarContext, final TaxStateModel taxStateModel, final ClassPathXmlApplicationContext context,
                                       final String stopperLevel, final String title, final String filter, final String icon )
    {
        if ( context.containsBean( title ) )
        {
            herbarModel = herbarContext.getDataModel();
            final Level stopLevel = herbarModel.getLevel( (String) context.getBean( stopperLevel ) );
            final String titleString = (String) context.getBean( title );
            final AttributeTreePanel display = new AttributeTreePanel( herbarContext, stopLevel, taxStateModel, titleString,
                    (VirtualGraphTreeNodeFilter) context.getBean( filter ) );
            this.addTab( titleString, getIcon( (String) context.getBean( icon ) ), display );
            return display;
        }
        else
        {
            return null;
        }
    }

    public void setTaxFocus( final Taxon focus )
    {
        display1.setTaxonFocus( focus );
        display2.setTaxonFocus( focus );
        display3.setTaxonFocus( focus );
        if ( display4 != null )
        {
            display4.setTaxonFocus( focus );
        }
    }

    public String toString()
    {
        return "" + hashCode();
    }

    public void synchronizeTabs( final JTabbedPane otherTab )
    {
        this.setSelectedIndex( otherTab.getSelectedIndex() );
    }
}
