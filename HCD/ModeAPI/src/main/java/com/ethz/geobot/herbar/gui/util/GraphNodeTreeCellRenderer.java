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
 * nichtkommerzielle Nutzung einen einmaligen Beitrag von Fr. 20.? zu bezahlen.
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
import com.ethz.geobot.herbar.model.trait.NameText;
import java.awt.BorderLayout;
import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.WEST;
import java.awt.Component;
import java.awt.Dimension;
import static java.lang.Boolean.TRUE;
import static java.lang.Integer.MAX_VALUE;
import java.util.HashMap;
import javax.swing.JEditorPane;
import static javax.swing.JEditorPane.HONOR_DISPLAY_PROPERTIES;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.TreeCellRenderer;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:08 $
 */
public class GraphNodeTreeCellRenderer extends JPanel implements TreeCellRenderer
{
    private static final HashMap<Class, String> iconMapping = new HashMap<Class, String>();

    final JPanel dummyPanel = new JPanel( new BorderLayout() );
    final JLabel dummyIcon = new JLabel();
    final JEditorPane dummyText = new JEditorPane( "text/html", "" );

    final JLabel thisIcon = new JLabel();
    final JEditorPane thisText = new JEditorPane( "text/html", "" );

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

        iconMapping.put( NameText.class, "SYNTEXT" );
    }

    private final JTree tree;

    public GraphNodeTreeCellRenderer( final JTree tree )
    {
        this.tree = tree;

        dummyText.setEditable( false );
        dummyText.putClientProperty( HONOR_DISPLAY_PROPERTIES, TRUE );
        dummyText.setFont( UIManager.getFont( "Label.font" ) );
        dummyPanel.setOpaque( false );
        dummyPanel.add( dummyIcon, WEST );
        dummyPanel.add( dummyText, CENTER );

        thisText.setEditable( false );
        thisText.putClientProperty( HONOR_DISPLAY_PROPERTIES, TRUE );
        thisText.setFont( UIManager.getFont( "Label.font" ) );
        setOpaque( false );
        setLayout( new BorderLayout() );
        add( thisIcon, WEST );
        add( thisText, CENTER );
    }

    /**
     * @see javax.swing.tree.TreeCellRenderer #getTreeCellRendererComponent(JTree, Object, boolean, boolean, boolean, int, boolean)
     */
    public Component getTreeCellRendererComponent( final JTree tree, final Object value, final boolean selected, final boolean expanded,
                                                   final boolean leaf, final int row, final boolean hasFocus )
    {
        if ( !(value instanceof GraphTreeNode) )
        {
            throw new IllegalStateException( "Renderer only for GraphTreeNodes, not for " + value.getClass().getName() );
        }
        thisText.setText( value.toString() );
        thisIcon.setIcon( ImageLocator.getIcon( "icon" + getBaseName( (GraphTreeNode) value ) + ".gif" ) );
        revalidate();
        repaint();
        return this;
    }

    public Dimension getPreferredSize()
    {
        final int width = tree.getSize().width - 40;
        dummyText.setText( thisText.getText() );
        dummyText.setSize( width, MAX_VALUE );
        final int height = dummyText.getPreferredScrollableViewportSize().height;
        return new Dimension( width, height );
    }

    private String getBaseName( final GraphTreeNode value )
    {
        final GraphNode node = value.getDependent();
        String baseName = "";
        if ( value.isType( Taxon.class ) )
        {
            final GraphNodeList levels = node.getChildren( Level.class );
            if ( levels.size() == 1 )
            {
                baseName = levels.get( 0 ).getName();
            }
        }
        else if ( value.isType( Level.class ) )
        {
            baseName = value.toString();
        }
        else
        {
            baseName = getIconBase( value );
        }
        return baseName;
    }

    private String getIconBase( final GraphTreeNode node )
    {
        for ( final Object o : iconMapping.keySet() )
        {
            final Class clazz = (Class) o;
            if ( node.isType( clazz ) )
            {
                return iconMapping.get( clazz );
            }
        }
        return "";
    }
}
