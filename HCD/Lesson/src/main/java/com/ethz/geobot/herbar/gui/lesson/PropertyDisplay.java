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
        final String subject = System.getProperty( "xmatrix.subject" );
        final ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext( "spring/lesson-" + subject + ".xml" );
        herbarModel = herbarContext.getDataModel();
        final Level stopLevel = herbarModel.getLevel( (String) context.getBean( "stopperClass" ) );
        final String morTitle = (String) context.getBean( "title1" );
        final String ecoTitle = (String) context.getBean( "title2" );
        final String medTitle = (String) context.getBean( "title3" );
        final String namTitle = (String) context.getBean( "title4" );
        display1 = new AttributeTreePanel( herbarContext, stopLevel, taxStateModel, morTitle, (VirtualGraphTreeNodeFilter) context.getBean( "filter1" ) );
        display2 = new AttributeTreePanel( herbarContext, stopLevel, taxStateModel, ecoTitle, (VirtualGraphTreeNodeFilter) context.getBean( "filter2" ) );
        display3 = new AttributeTreePanel( herbarContext, stopLevel, taxStateModel, medTitle, (VirtualGraphTreeNodeFilter) context.getBean( "filter3" ) );
        display4 = new AttributeTreePanel( herbarContext, herbarModel.getLevel( (String) context.getBean( "stopperSpecies" ) ), taxStateModel, namTitle, (VirtualGraphTreeNodeFilter) context.getBean( "filter4" ) );
        this.setTabPlacement( BOTTOM );
        this.setTabLayoutPolicy( SCROLL_TAB_LAYOUT );
        this.addTab( morTitle, getIcon( (String) context.getBean( "icon1" ) ), display1 );
        this.addTab( ecoTitle, getIcon( (String) context.getBean( "icon2" ) ), display2 );
        this.addTab( medTitle, getIcon( (String) context.getBean( "icon3" ) ), display3 );
        this.addTab( namTitle, getIcon( (String) context.getBean( "icon4" ) ), display4 );
    }

    public void setTaxFocus( final Taxon focus )
    {
        display1.setTaxonFocus( focus );
        display2.setTaxonFocus( focus );
        display3.setTaxonFocus( focus );
        display4.setTaxonFocus( focus );
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
