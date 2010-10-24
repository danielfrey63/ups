/*
 * Herbar CD-ROM version 2
 *
 * Created on 15. Mai 2002, 11:51
 * Created by Daniel Frey
 */
package com.ethz.geobot.herbar.gui.lesson;

import ch.jfactory.component.ScrollerPanel;
import ch.jfactory.resource.ImageLocator;
import com.ethz.geobot.herbar.model.Taxon;
import java.awt.BorderLayout;
import java.awt.Color;
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

    private final ScrollerPanel panel = new ScrollerPanel();

    /** Displays a series of taxa up to and excluding the given stopper level. */
    public BreadCrumb()
    {
        this.setLayout( new BorderLayout( 0, 0 ) );
        this.add( panel, BorderLayout.CENTER );
    }

    public void setTaxFocus( final Taxon tax )
    {
        LOG.debug( "setting focus to \"" + tax + "\"" );
        panel.removeAll();
        Taxon parent = tax;
        while ( parent != null && parent != parent.getParentTaxon() && parent.getLevel() != null )
        {
            LOG.trace( "iterating up to " + parent );
            final ImageIcon ii = ImageLocator.getIcon( "icon" + parent.getLevel().getName() + ".gif" );
            final JLabel label = new JLabel( parent.getName() + "  ", ii, JLabel.CENTER );
            panel.add( label );
            parent = parent.getParentTaxon();
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
