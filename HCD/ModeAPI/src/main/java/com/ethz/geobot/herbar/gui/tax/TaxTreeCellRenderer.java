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
package com.ethz.geobot.herbar.gui.tax;

import ch.jfactory.resource.ImageLocator;
import com.ethz.geobot.herbar.model.Level;
import com.ethz.geobot.herbar.model.Taxon;
import com.ethz.geobot.herbar.util.TaxonTreeNode;
import java.awt.Component;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:08 $
 */
public class TaxTreeCellRenderer extends DefaultTreeCellRenderer
{
    private static final Logger LOG = LoggerFactory.getLogger( TaxTreeCellRenderer.class );

    /** @see TreeCellRenderer#getTreeCellRendererComponent(JTree, Object, boolean, boolean, boolean, int, boolean) */
    public Component getTreeCellRendererComponent( final JTree jt, final Object objValue, final boolean bSelected, final boolean bExpanded,
                                                   final boolean bLeaf, final int iRow, final boolean bHasFocus )
    {
        super.getTreeCellRendererComponent( jt, objValue, bSelected, bExpanded, bLeaf, iRow, bHasFocus );
        Taxon taxon = null;
        if ( objValue instanceof TaxonTreeNode )
        {
            taxon = ( (TaxonTreeNode) objValue ).getTaxon();
            setText( taxon.toString() );
            final Level level = taxon.getLevel();
            setIcon( ImageLocator.getIcon( "icon" + ( level == null ? "" : level.getName() ) + ".gif" ) );
        }
        else if ( objValue instanceof DefaultMutableTreeNode )
        {
            setText( "" );
        }
        else
        {
            LOG.error( "Node " + objValue.getClass().getName() + " not of expected type TaxonTreeNode" );
        }
        setBackgroundNonSelectionColor( jt.getBackground() );
        revalidate();
        repaint();
        return this;
    }
}
