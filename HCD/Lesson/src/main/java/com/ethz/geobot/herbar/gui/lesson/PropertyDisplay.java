/*
 * Herbar CD-ROM version 2
 *
 * PropertyDisplay.java
 *
 * Created on 17. Mai 2002, 14:33
 * Created by dani
 */
package com.ethz.geobot.herbar.gui.lesson;

import ch.jfactory.model.graph.GraphNode;
import ch.jfactory.model.graph.tree.VirtualGraphTreeNodeFilter;
import com.ethz.geobot.herbar.modeapi.HerbarContext;
import com.ethz.geobot.herbar.model.HerbarModel;
import com.ethz.geobot.herbar.model.Level;
import com.ethz.geobot.herbar.model.Taxon;
import com.ethz.geobot.herbar.model.trait.EcologyText;
import com.ethz.geobot.herbar.model.trait.MedicineAttribute;
import com.ethz.geobot.herbar.model.trait.MedicineSubject;
import com.ethz.geobot.herbar.model.trait.MedicineText;
import com.ethz.geobot.herbar.model.trait.MedicineValue;
import com.ethz.geobot.herbar.model.trait.MorphologyText;
import javax.swing.JTabbedPane;
import javax.swing.tree.DefaultMutableTreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ch.jfactory.model.graph.tree.VirtualGraphTreeNodeFilter.Constraint.BOUND;
import static ch.jfactory.model.graph.tree.VirtualGraphTreeNodeFilter.Constraint.FREE;
import static ch.jfactory.model.graph.tree.VirtualGraphTreeNodeFilter.Lineage.ANCESTOR;
import static ch.jfactory.model.graph.tree.VirtualGraphTreeNodeFilter.Lineage.DESCENDANT;
import static ch.jfactory.model.graph.tree.VirtualGraphTreeNodeFilter.Lineage.RELATED;
import static ch.jfactory.model.graph.tree.VirtualGraphTreeNodeFilter.Self.FLAT;
import static ch.jfactory.model.graph.tree.VirtualGraphTreeNodeFilter.Visibility.HIDDEN;
import static ch.jfactory.model.graph.tree.VirtualGraphTreeNodeFilter.Visibility.VISIBLE;
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
    private final static String STOP = getString( "LEVEL.STOPPER" );

    private final static int THEME_MORPHOLOGY = 0;

    protected final static Logger LOG = LoggerFactory.getLogger( PropertyDisplay.class );

    protected final static DefaultMutableTreeNode ROOT_NODE = new DefaultMutableTreeNode();

//    private final AttributeDisplayModel model;

    private final AttributeTreePanel morDisplay;

    private final AttributeTreePanel medDisplay;

    private final AttributeTreePanel ecoDisplay;

    protected HerbarModel herbarModel;

    /**
     * Creates a new instance of PropertyDisplay.
     *
     * @param herbarContext the main context
     * @param taxStateModel the model
     */
    public PropertyDisplay( final HerbarContext herbarContext, final TaxStateModel taxStateModel )
    {
        herbarModel = herbarContext.getDataModel();
        final Level stopLevel = herbarModel.getLevel( STOP );
        final String morTitle = getString( "PROPERTY.MORPHOLOGY.TEXT" );
        final String ecoTitle = getString( "PROPERTY.ECOLOGY.TEXT" );
        final String medTitle = getString( "PROPERTY.MEDICINE.TEXT" );
//        model = new AttributeDisplayModel( stopLevel );
        morDisplay = new AttributeTreePanel( herbarContext, stopLevel, taxStateModel, morTitle, getMorphologyFilter() );
        ecoDisplay = new AttributeTreePanel( herbarContext, stopLevel, taxStateModel, ecoTitle, getEcologyFilter() );
        medDisplay = new AttributeTreePanel( herbarContext, stopLevel, taxStateModel, medTitle, getMedicineFilter() );
        this.setTabPlacement( BOTTOM );
        this.setTabLayoutPolicy( SCROLL_TAB_LAYOUT );
        this.addTab( morTitle, getIcon( getString( "PROPERTY.MORPHOLOGY.ICON" ) ), morDisplay );
        this.addTab( ecoTitle, getIcon( getString( "PROPERTY.ECOLOGY.ICON" ) ), ecoDisplay );
        this.addTab( medTitle, getIcon( getString( "PROPERTY.MEDICINE.ICON" ) ), medDisplay );
    }

    public void setTaxFocus( final Taxon focus )
    {
//        model.setTaxFocus( focus );
        morDisplay.setTaxonFocus( focus );
        ecoDisplay.setTaxonFocus( focus );
        medDisplay.setTaxonFocus( focus );
    }

    public String toString()
    {
        return "" + hashCode();
    }

    public void synchronizeTabs( final JTabbedPane otherTab )
    {
        this.setSelectedIndex( otherTab.getSelectedIndex() );
    }

    public VirtualGraphTreeNodeFilter getMorphologyFilter()
    {
        VirtualGraphTreeNodeFilter filter;
        filter = new VirtualGraphTreeNodeFilter( MorphologyText.class, VISIBLE, FLAT, FREE, DESCENDANT, null );
        filter = new VirtualGraphTreeNodeFilter( Taxon.class, VISIBLE, FLAT, FREE, DESCENDANT, filter );
        filter = new VirtualGraphTreeNodeFilter( GraphNode.class, VISIBLE, FLAT, FREE, DESCENDANT, filter );
        return filter;
    }

    public VirtualGraphTreeNodeFilter getEcologyFilter()
    {
        VirtualGraphTreeNodeFilter filter;
        filter = new VirtualGraphTreeNodeFilter( EcologyText.class, VISIBLE, FLAT, FREE, RELATED, null );
        filter = new VirtualGraphTreeNodeFilter( Taxon.class, VISIBLE, FLAT, FREE, DESCENDANT, filter );
        filter = new VirtualGraphTreeNodeFilter( GraphNode.class, VISIBLE, FLAT, FREE, DESCENDANT, filter );
        return filter;
    }

    public VirtualGraphTreeNodeFilter getMedicineFilter()
    {
        VirtualGraphTreeNodeFilter filter;
        filter = new VirtualGraphTreeNodeFilter( MedicineText.class, VISIBLE, FLAT, BOUND, DESCENDANT, null );
        filter = new VirtualGraphTreeNodeFilter( MedicineValue.class, HIDDEN, FLAT, BOUND, DESCENDANT, filter );
        filter = new VirtualGraphTreeNodeFilter( MedicineAttribute.class, VISIBLE, FLAT, BOUND, DESCENDANT, filter );
        filter = new VirtualGraphTreeNodeFilter( MedicineSubject.class, VISIBLE, FLAT, FREE, ANCESTOR, filter );
        filter = new VirtualGraphTreeNodeFilter( MedicineAttribute.class, HIDDEN, FLAT, FREE, ANCESTOR, filter );
        filter = new VirtualGraphTreeNodeFilter( MedicineValue.class, HIDDEN, FLAT, FREE, ANCESTOR, filter );
        filter = new VirtualGraphTreeNodeFilter( MedicineText.class, HIDDEN, FLAT, FREE, DESCENDANT, filter );
        filter = new VirtualGraphTreeNodeFilter( Taxon.class, VISIBLE, FLAT, FREE, DESCENDANT, filter );
        filter = new VirtualGraphTreeNodeFilter( GraphNode.class, VISIBLE, FLAT, FREE, DESCENDANT, filter );
        return filter;
    }
}
