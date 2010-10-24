/*
 * Herbar CD-ROM version 2
 *
 * PropertyDisplayer.java
 *
 * Created on 17. Mai 2002, 14:33
 * Created by dani
 */
package com.ethz.geobot.herbar.gui.lesson;

import ch.jfactory.resource.ImageLocator;
import ch.jfactory.resource.Strings;
import com.ethz.geobot.herbar.model.HerbarModel;
import com.ethz.geobot.herbar.model.Level;
import com.ethz.geobot.herbar.model.Taxon;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTabbedPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Displays two tabs. First tab presents the data to learn. The second one tests the user.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:06:56 $
 */
public class PropertyDisplay extends JTabbedPane
{
    private final static String STOP = Strings.getString( "LEVEL.STOPPER" );

    private final static int THEME_MORPHOLOGY = 0;

    protected final static Logger LOG = LoggerFactory.getLogger( PropertyDisplay.class );

    protected final static DefaultMutableTreeNode ROOT_NODE = new DefaultMutableTreeNode();

    private final AttributeDisplayModel model;

    private final MorphologyDisplay morDisplay;

    private final MedicineDisplay medDisplay;

    private final EcologyDisplay ecoDisplay;

    protected HerbarModel herbarModel;

    /**
     * Creates a new instance of PropertyDisplay.
     *
     * @param herbarModel the main model
     */
    public PropertyDisplay( final HerbarModel herbarModel )
    {
        this.herbarModel = herbarModel;
        this.setTabPlacement( JTabbedPane.BOTTOM );
        model = new AttributeDisplayModel( herbarModel.getLevel( STOP ) );
        morDisplay = new MorphologyDisplay( herbarModel, herbarModel.getLevel( STOP ) );
        ecoDisplay = new EcologyDisplay( herbarModel, herbarModel.getLevel( STOP ) );
        medDisplay = new MedicineDisplay( herbarModel, herbarModel.getLevel( STOP ) );
        this.setTabLayoutPolicy( JTabbedPane.SCROLL_TAB_LAYOUT );
        this.addTab( Strings.getString( "PROPERTY.MORTEXT.TEXT" ),
                ImageLocator.getIcon( Strings.getString( "PROPERTY.MORTEXT.ICON" ) ), morDisplay );
        this.addTab( Strings.getString( "PROPERTY.ECOTEXT.TEXT" ),
                ImageLocator.getIcon( Strings.getString( "PROPERTY.ECOTEXT.ICON" ) ), ecoDisplay );
        this.addTab( Strings.getString( "PROPERTY.MEDTEXT.TEXT" ),
                ImageLocator.getIcon( Strings.getString( "PROPERTY.MEDTEXT.ICON" ) ), medDisplay );
    }

    public void setTaxFocus( final Taxon focus )
    {
        model.setTaxFocus( focus );
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

    class AttributeDisplayModel extends DefaultTreeModel
    {
        private int theme;

        private List<Level> vLevels = new ArrayList<Level>();

        private final Level stopper;

        public AttributeDisplayModel( final Level stopper )
        {
            super( ROOT_NODE );
            if ( stopper == null )
            {
                throw new RuntimeException( "stopper level object " + STOP + " not found." );
            }
            this.stopper = stopper;
        }

        public void setTaxFocus( final Taxon focus )
        {
            LOG.info( "setting focus to \"" + focus + "\"" );
            if ( theme == THEME_MORPHOLOGY )
            {
                Taxon taxon = focus;
                Level level = focus.getLevel();
                vLevels = new ArrayList<Level>();
                final Level root = herbarModel.getRootLevel();
                if ( stopper != null && stopper.isHigher( level ) )
                {
                    while ( level != stopper && stopper != root && stopper.isHigher( level ) )
                    {
                        vLevels.add( 0, level );
                        taxon = taxon.getParentTaxon();
                        level = taxon.getLevel();
                    }
                }
                else
                {
                    vLevels.add( level );
                }
            }
            fireTreeStructureChanged( this, getPathToRoot( ROOT_NODE ), null, null );
        }

        public Object getChild( final Object parent, final int index )
        {
            if ( theme == THEME_MORPHOLOGY )
            {
                if ( parent instanceof Level )
                {
                    final Level level = (Level) parent;
                }
                else if ( parent == ROOT_NODE )
                {
                    return vLevels.get( index );
                }
            }
            return null;
        }

        public int getChildCount( final Object obj )
        {
            if ( LOG.isDebugEnabled() )
            {
                LOG.debug( "counting children of " + obj );
            }
            int ret = 0;
            if ( theme == THEME_MORPHOLOGY )
            {
                if ( obj == ROOT_NODE )
                {
                    ret = vLevels.size();
                }
                else if ( obj instanceof Level )
                {
                    ret = 0;
                }
            }
            LOG.debug( this + " return " + ret );
            return ret;
        }

        public int getIndexOfChild( final Object parent, final Object child )
        {
            LOG.debug( "getting index of " + child + " within " + parent );
            int ret = 0;
            if ( theme == THEME_MORPHOLOGY )
            {
                if ( parent == ROOT_NODE )
                {
                    ret = vLevels.lastIndexOf( child );
                }
                throw new RuntimeException( "child " + child + " does not exist in parent " + parent );
            }
            LOG.debug( "index of " + child + " within " + parent + " is " + ret );
            return ret;
        }

        public Object getRoot()
        {
            LOG.debug( this + " getRoot()" );
            Object ret = null;
            if ( theme == THEME_MORPHOLOGY )
            {
                ret = ROOT_NODE;
            }
            return ret;
        }

        public boolean isLeaf( final Object obj )
        {
            final boolean leaf = ( getChildCount( obj ) == 0 );
            if ( LOG.isDebugEnabled() )
            {
                LOG.debug( this + " isLeaf(" + obj + "): " + leaf );
            }
            return leaf;
        }
    }
}
