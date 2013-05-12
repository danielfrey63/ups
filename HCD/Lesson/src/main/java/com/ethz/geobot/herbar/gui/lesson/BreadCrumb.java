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

import ch.jfactory.component.ScrollPanel;
import ch.jfactory.resource.ImageLocator;
import com.ethz.geobot.herbar.model.Taxon;
import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Displays the whole path of the current Taxon object in focus.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:06:56 $
 */
public class BreadCrumb extends JPanel
{
    private final static Logger LOG = LoggerFactory.getLogger( BreadCrumb.class );

    private final ScrollPanel panel = new ScrollPanel();

    /** Displays a series of taxa up to and excluding the given stopper level. */
    public BreadCrumb()
    {
        this.setLayout( new BorderLayout( 0, 0 ) );
        this.add( panel, BorderLayout.CENTER );
    }

    public void setTaxFocus( final Taxon tax )
    {
        LOG.debug( "adjusting bread crumb to new focus \"" + tax + "\"" );
        panel.removeAll();
        final List<JLabel> list = new ArrayList<JLabel>();
        Taxon parent = tax;
        while ( parent != null && parent != parent.getParentTaxon() && parent.getLevel() != null )
        {
            LOG.trace( "iterating up to " + parent );
            final ImageIcon ii = ImageLocator.getIcon( "icon" + parent.getLevel().getName() + ".gif" );
            final JLabel label = new JLabel( parent.getName() + "  ", ii, JLabel.CENTER );
            list.add( 0, label );
            parent = parent.getParentTaxon();
        }
        for ( final JLabel label : list )
        {
            panel.add( label );
        }
        panel.repaint();
    }

    public void setBackground( final Color background )
    {
        super.setBackground( background );
        if ( panel != null )
        {
            panel.setBackground( background );
        }
    }
}
