package com.ethz.geobot.herbar.gui.util;

import ch.jfactory.component.tree.GraphTreeNode;
import ch.jfactory.model.graph.GraphNode;
import ch.jfactory.model.graph.GraphNodeList;
import ch.jfactory.resource.ImageLocator;
import com.ethz.geobot.herbar.model.EcoAttribute;
import com.ethz.geobot.herbar.model.EcoSubject;
import com.ethz.geobot.herbar.model.EcoText;
import com.ethz.geobot.herbar.model.EcoValue;
import com.ethz.geobot.herbar.model.Level;
import com.ethz.geobot.herbar.model.MedAttribute;
import com.ethz.geobot.herbar.model.MedSubject;
import com.ethz.geobot.herbar.model.MedText;
import com.ethz.geobot.herbar.model.MedValue;
import com.ethz.geobot.herbar.model.MorAttribute;
import com.ethz.geobot.herbar.model.MorSubject;
import com.ethz.geobot.herbar.model.MorText;
import com.ethz.geobot.herbar.model.MorValue;
import com.ethz.geobot.herbar.model.Taxon;
import java.awt.Component;
import java.util.HashMap;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:08 $
 */
public class GraphNodeTreeCellRenderer extends DefaultTreeCellRenderer
{
    private static final HashMap iconMapping = new HashMap();

    static
    {
        iconMapping.put( MorSubject.class, "MORSUBJECT" );
        iconMapping.put( MorAttribute.class, "MORATTRIBUTE" );
        iconMapping.put( MorValue.class, "MORVALUE" );
        iconMapping.put( MorText.class, "MORTEXT" );

        iconMapping.put( MedSubject.class, "ADDSUBJECT" );
        iconMapping.put( MedAttribute.class, "ADDATTRIBUTE" );
        iconMapping.put( MedValue.class, "ADDVALUE" );
        iconMapping.put( MedText.class, "ADDTEXT" );

        iconMapping.put( EcoSubject.class, "ECOSUBJECT" );
        iconMapping.put( EcoAttribute.class, "ECOATTRIBUTE" );
        iconMapping.put( EcoValue.class, "ECOVALUE" );
        iconMapping.put( EcoText.class, "ECOTEXT" );
    }

    /**
     * @see TreeCellRenderer #getTreeCellRendererComponent(JTree, Object, boolean, boolean, boolean,
     *      int, boolean)
     */
    public Component getTreeCellRendererComponent( final JTree tree, final Object value, final boolean selected, final boolean expanded,
                                                   final boolean leaf, final int row, final boolean hasFocus )
    {
        super.getTreeCellRendererComponent( tree, value, selected, expanded, leaf, row, hasFocus );
        if ( !( value instanceof GraphTreeNode ) )
        {
            throw new IllegalStateException( "Renderer only for GraphTreeNodes, not for " + value.getClass().getName() );
        }
        final GraphTreeNode tNode = (GraphTreeNode) value;
        final GraphNode node = tNode.getDependent();
        setText( value.toString() );
        String baseName = "";
        if ( tNode.isType( Taxon.class ) )
        {
            final GraphNodeList levels = node.getChildren( Level.class );
            if ( levels.size() == 1 )
            {
                baseName = levels.get( 0 ).getName();
            }
        }
        else
        {
            baseName = getIconBase( tNode ).toString();
        }
        setIcon( ImageLocator.getIcon( "icon" + baseName + ".gif" ) );
        setBackgroundNonSelectionColor( tree.getBackground() );
        revalidate();
        repaint();
        return this;
    }

    private String getIconBase( final GraphTreeNode node )
    {
        for ( final Object o : iconMapping.keySet() )
        {
            final Class clazz = (Class) o;
            if ( node.isType( clazz ) )
            {
                return (String) iconMapping.get( clazz );
            }
        }
        return "";
    }
}
