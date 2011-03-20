/*
 * Herbar CD-ROM version 2
 *
 * PropertyDisplay.java
 *
 * Created on 17. Mai 2002, 14:33
 * Created by dani
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
import static ch.jfactory.resource.Strings.getString;

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

    private final AttributeTreePanel display3;

    private final AttributeTreePanel display2;

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
        final Level stopLevel = herbarModel.getLevel( (String) context.getBean( "stopper" ) );
        final String morTitle = getString( "PROPERTY.MORPHOLOGY.TEXT" );
        final String ecoTitle = getString( "PROPERTY.ECOLOGY.TEXT" );
        final String medTitle = getString( "PROPERTY.MEDICINE.TEXT" );
        display1 = new AttributeTreePanel( herbarContext, stopLevel, taxStateModel, morTitle, (VirtualGraphTreeNodeFilter) context.getBean( "filter1" ) );
        display2 = new AttributeTreePanel( herbarContext, stopLevel, taxStateModel, ecoTitle, (VirtualGraphTreeNodeFilter) context.getBean( "filter2" ) );
        display3 = new AttributeTreePanel( herbarContext, stopLevel, taxStateModel, medTitle, (VirtualGraphTreeNodeFilter) context.getBean( "filter3" ) );
        this.setTabPlacement( BOTTOM );
        this.setTabLayoutPolicy( SCROLL_TAB_LAYOUT );
        this.addTab( morTitle, getIcon( getString( "PROPERTY.MORPHOLOGY.ICON" ) ), display1 );
        this.addTab( ecoTitle, getIcon( getString( "PROPERTY.ECOLOGY.ICON" ) ), display2 );
        this.addTab( medTitle, getIcon( getString( "PROPERTY.MEDICINE.ICON" ) ), display3 );
    }

    public void setTaxFocus( final Taxon focus )
    {
        display1.setTaxonFocus( focus );
        display2.setTaxonFocus( focus );
        display3.setTaxonFocus( focus );
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
