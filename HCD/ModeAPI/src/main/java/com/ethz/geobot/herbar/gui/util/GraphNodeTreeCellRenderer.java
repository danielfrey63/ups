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
package com.ethz.geobot.herbar.gui.util;

import ch.jfactory.component.tree.GraphTreeNode;
import ch.jfactory.model.graph.GraphNode;
import ch.jfactory.model.graph.GraphNodeList;
import ch.jfactory.resource.ImageLocator;
import com.ethz.geobot.herbar.model.Level;
import com.ethz.geobot.herbar.model.Taxon;
import com.ethz.geobot.herbar.model.trait.EcologyAttribute;
import com.ethz.geobot.herbar.model.trait.EcologySubject;
import com.ethz.geobot.herbar.model.trait.EcologyText;
import com.ethz.geobot.herbar.model.trait.EcologyValue;
import com.ethz.geobot.herbar.model.trait.MedicineAttribute;
import com.ethz.geobot.herbar.model.trait.MedicineSubject;
import com.ethz.geobot.herbar.model.trait.MedicineText;
import com.ethz.geobot.herbar.model.trait.MedicineValue;
import com.ethz.geobot.herbar.model.trait.MorphologyAttribute;
import com.ethz.geobot.herbar.model.trait.MorphologySubject;
import com.ethz.geobot.herbar.model.trait.MorphologyText;
import com.ethz.geobot.herbar.model.trait.MorphologyValue;
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
        iconMapping.put( MorphologySubject.class, "MORSUBJECT" );
        iconMapping.put( MorphologyAttribute.class, "MORATTRIBUTE" );
        iconMapping.put( MorphologyValue.class, "MORVALUE" );
        iconMapping.put( MorphologyText.class, "MORTEXT" );

        iconMapping.put( MedicineSubject.class, "ADDSUBJECT" );
        iconMapping.put( MedicineAttribute.class, "ADDATTRIBUTE" );
        iconMapping.put( MedicineValue.class, "ADDVALUE" );
        iconMapping.put( MedicineText.class, "ADDTEXT" );

        iconMapping.put( EcologySubject.class, "ECOSUBJECT" );
        iconMapping.put( EcologyAttribute.class, "ECOATTRIBUTE" );
        iconMapping.put( EcologyValue.class, "ECOVALUE" );
        iconMapping.put( EcologyText.class, "ECOTEXT" );
    }

    /** @see TreeCellRenderer #getTreeCellRendererComponent(JTree, Object, boolean, boolean, boolean, int, boolean) */
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
