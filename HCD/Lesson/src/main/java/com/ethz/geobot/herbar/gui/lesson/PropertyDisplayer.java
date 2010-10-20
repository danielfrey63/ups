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
public class PropertyDisplayer extends JTabbedPane
{
    private final static String STOP = Strings.getString( "LEVEL.STOPPER" );

    private final static int THEME_MORPHOLOGY = 0;

    protected final static Logger LOG = LoggerFactory.getLogger( PropertyDisplayer.class );

    protected final static DefaultMutableTreeNode ROOT_NODE = new DefaultMutableTreeNode();

    private final AttributeDisplayerModel model;

    private final MorphologyDisplayer morDisplayer;

    private final MedicineDisplayer medDisplayer;

    private final EcologyDisplayer ecoDisplayer;

    protected HerbarModel herbarModel;

    /** Creates a new instance of PropertyDisplayer */
    public PropertyDisplayer( final HerbarModel herbarModel )
    {
        this.herbarModel = herbarModel;
        this.setTabPlacement( JTabbedPane.BOTTOM );
        model = new AttributeDisplayerModel( herbarModel.getLevel( STOP ) );
        morDisplayer = new MorphologyDisplayer( herbarModel, herbarModel.getLevel( STOP ) );
        ecoDisplayer = new EcologyDisplayer( herbarModel, herbarModel.getLevel( STOP ) );
        medDisplayer = new MedicineDisplayer( herbarModel, herbarModel.getLevel( STOP ) );
        this.setTabLayoutPolicy( JTabbedPane.SCROLL_TAB_LAYOUT );
        this.addTab( Strings.getString( "PROPERTY.MORTEXT.TEXT" ),
                ImageLocator.getIcon( Strings.getString( "PROPERTY.MORTEXT.ICON" ) ), morDisplayer );
        this.addTab( Strings.getString( "PROPERTY.ECOTEXT.TEXT" ),
                ImageLocator.getIcon( Strings.getString( "PROPERTY.ECOTEXT.ICON" ) ), ecoDisplayer );
        this.addTab( Strings.getString( "PROPERTY.MEDTEXT.TEXT" ),
                ImageLocator.getIcon( Strings.getString( "PROPERTY.MEDTEXT.ICON" ) ), medDisplayer );
    }

    public void setTaxFocus( final Taxon focus )
    {
        model.setTaxFocus( focus );
        morDisplayer.setTaxonFocus( focus );
        ecoDisplayer.setTaxonFocus( focus );
        medDisplayer.setTaxonFocus( focus );
    }

    public String toString()
    {
        return "" + hashCode();
    }

    public void synchronizeTabs( final JTabbedPane othertab )
    {
        this.setSelectedIndex( othertab.getSelectedIndex() );
    }

    class AttributeDisplayerModel extends DefaultTreeModel
    {
        private int theme;

        private List vLevels = new ArrayList();

        private final Level stopper;

        public AttributeDisplayerModel( final Level stopper )
        {
            super( ROOT_NODE );
            if ( stopper == null )
            {
                throw new RuntimeException( "Stopper Level object " + STOP + " not found." );
            }
            this.stopper = stopper;
        }

        public void setAttributeTheme( final int index )
        {
            this.theme = index;
        }

        public void setTaxFocus( final Taxon focus )
        {
            LOG.info( this + " setTaxFocus(" + focus + ")" );
            if ( theme == THEME_MORPHOLOGY )
            {
                Taxon taxon = focus;
                Level level = focus.getLevel();
                vLevels = new ArrayList();
                final Level root = herbarModel.getRootLevel();
                if ( stopper != null && stopper.isHigher( level ) )
                {
                    while ( level != stopper && stopper != root && stopper != null && stopper.isHigher( level ) )
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
                LOG.debug( this + " getChildCound(" + obj + ")" );
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
            if ( LOG.isDebugEnabled() )
            {
                LOG.debug( this + " getIndexOfChild(" + parent + "," + child + ")" );
            }
            int ret = 0;
            if ( theme == THEME_MORPHOLOGY )
            {
                if ( parent == ROOT_NODE )
                {
                    ret = vLevels.lastIndexOf( child );
                }
                throw new RuntimeException( "Child " + child + " does not exist in parent " + parent );
            }
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

        public String toString()
        {
            return "" + this.hashCode();
        }
    }
}

// $Log: PropertyDisplayer.java,v $
// Revision 1.1  2007/09/17 11:06:56  daniel_frey
// - Version 3.0.20070401
//
// Revision 1.14  2005/06/17 06:39:59  daniel_frey
// New ActionButton icons and some corrections on documentation
//
// Revision 1.13  2004/08/31 22:10:17  daniel_frey
// Examlist loading working
//
// Revision 1.12  2004/04/25 13:56:43  daniel_frey
// Moved Dialogs from Herbars modeapi to xmatrix
//
// Revision 1.11  2003/05/01 14:34:55  daniel_frey
// - Adapted to elimination of level for root taxon
//
// Revision 1.10  2003/04/21 18:00:32  daniel_frey
// - Abstracted stopper level
//
// Revision 1.9  2003/04/15 19:34:45  daniel_frey
// - Initial version of a finder window
//
// Revision 1.8  2003/04/02 14:49:04  daniel_frey
// - Revised wizards
//
// Revision 1.7  2003/03/14 09:51:14  thomas_wegmueller
// *** empty log message ***
//
// Revision 1.6  2003/03/13 17:23:26  thomas_wegmueller
// *** empty log message ***
//
// Revision 1.5  2003/03/05 15:44:17  daniel_frey
// - Common result model now working for lesson and exam
//
// Revision 1.4  2003/02/27 12:21:56  daniel_frey
// - Removed bug where activate was called twice during initalization of mode
// - Moved some components common to lesson and exam to modeapi
// - Added additional functions to exam
//
// Revision 1.3  2003/02/21 15:51:05  daniel_frey
// - Removed dependency to modelimpl
//
// Revision 1.2  2003/02/13 14:00:11  daniel_frey
// - Removed here
//
// Revision 1.1  2003/02/13 13:47:05  daniel_frey
// - Renamed AttributeDisplayer to PropertyDisplayer
//
// Revision 1.16  2003/02/13 13:44:30  daniel_frey
// - Splittet each class into an own file
//
// Revision 1.15  2003/02/12 19:11:23  daniel_frey
// - Transfered TaxFocusListener from com.ethz.geobot.herbar.gui.lesson to com.ethz.geobot.herbar.modeapi.state
//
// Revision 1.14  2003/02/05 21:53:55  daniel_frey
// - Simplified construction of a virtual tree with a factory method
// - Removed some unused code
//
// Revision 1.13  2003/01/24 23:35:04  daniel_frey
// - Some small changes
//
// Revision 1.12  2003/01/24 13:47:40  daniel_frey
// - Added synchronisation of the selected one of the three tabs between Lehrgang and Abfrage
//
// Revision 1.11  2003/01/23 13:46:11  daniel_frey
// *** empty log message ***
//
// Revision 1.10  2003/01/23 10:53:47  daniel_frey
// - Optimized imports
//
// Revision 1.9  2003/01/22 12:05:24  daniel_frey
// - Removed console printouts.
//
// Revision 1.8  2002/11/05 11:21:58  daniel_frey
// - Level with tree from GraphNode
//
// Revision 1.7  2002/09/10 09:49:47  dirk
// change getString to getStringForObject
//
// Revision 1.6  2002/08/05 12:47:16  Dani
// - Synchronization without change
//
// Revision 1.5  2002/08/05 12:16:27  Dani
// - Changed import of Strings and ImageLocator
// - Adapted changes in ImageLocator (renamed getImageIcon to getIcon)
//
// Revision 1.4  2002/08/02 00:42:19  Dani
// Optimized import statements
//
// Revision 1.3  2002/08/01 16:11:52  Dani
// Removed ambigous references claimed by jikes
//
// Revision 1.2  2002/06/21 13:39:43  lilo
// mode-eigenes string-property-file, ergänzt um reiterbezeichnungen morphologie, ökologie und aspekte
//
// Revision 1.1  2002/05/24 00:20:44  dirk
// initial
//
// Revision 1.2  2002/05/23 19:19:42  dirk
// fix ExamPanel update problems
//
// Revision 1.1  2002/05/23 17:29:57  Dani
// First shot of a morphology display panel
//
