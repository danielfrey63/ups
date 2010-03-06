/*
 * Herbar CD-ROM version 2
 *
 * BreadCrump.java
 *
 * Created on 15. Mai 2002, 11:51
 * Created by Daniel Frey
 */
package com.ethz.geobot.herbar.gui.lesson;

import ch.jfactory.component.ScrollerPanel;
import ch.jfactory.resource.ImageLocator;
import com.ethz.geobot.herbar.gui.tax.TaxPopup;
import com.ethz.geobot.herbar.model.Level;
import com.ethz.geobot.herbar.model.Taxon;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
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

    private BreadTaxPopup taxPopup;

    private final TaxStateModel model;

    private final ScrollerPanel panel = new ScrollerPanel();

    private Level stopperLevel;

    /**
     * Displays a series of taxa up to and excluding the given stopper level.
     *
     * @param herbarModel  the model to use
     * @param stopperLevel the level to start exclusion
     */
    public BreadCrumb( final TaxStateModel herbarModel, final Level stopperLevel )
    {
        LOG.info( toString() );
        this.model = herbarModel;
        this.stopperLevel = stopperLevel;
        this.setLayout( new BorderLayout( 0, 0 ) );
        this.add( panel, BorderLayout.CENTER );
    }

    public BreadCrumb( final TaxStateModel herbarModel )
    {
        LOG.info( toString() );
        this.model = herbarModel;
        this.setLayout( new BorderLayout( 0, 0 ) );
        this.add( panel, BorderLayout.CENTER );
    }

    public void setTaxFocus( final Taxon tax )
    {
        LOG.debug( this + " focus is " + tax );
        panel.removeAll();
        Taxon parent = tax;
        while ( parent != null && parent != parent.getParentTaxon() && parent.getLevel() != null
                && ( stopperLevel == null || parent.getLevel().isLower( stopperLevel ) ) )
        {
            LOG.debug( this + " parent is " + parent );
            final ImageIcon ii = ImageLocator.getIcon( "icon" + parent.getLevel().getName() + ".gif" );
            final JLabel label = new JLabel( parent.getName() + "  ", ii, JLabel.CENTER );
            panel.add( label );
            parent = parent.getParentTaxon();
        }
        panel.repaint();
    }

    public BreadTaxPopup getTaxPopup()
    {
        if ( taxPopup == null )
        {
            taxPopup = new BreadTaxPopup();
        }
        taxPopup.setTaxa( model.getTaxList() );
        return taxPopup;
    }

    public String toString()
    {
        return "" + hashCode();
    }

    public void setBackground( final Color background )
    {
        super.setBackground( background );
        if ( panel != null )
        {
            panel.setBackground( background );
        }
    }

    public class BreadTaxPopup extends TaxPopup
    {
        public BreadTaxPopup()
        {
            super( model.getTaxList() );
        }

        public void showPopup( final Component jb )
        {
            super.showPopup( jb, model.getFocus() );
        }

        public void itemSelected( final Object obj )
        {
            model.setFocus( (Taxon) obj );
        }
    }

}

// $Log: BreadCrumb.java,v $
// Revision 1.1  2007/09/17 11:06:56  daniel_frey
// - Version 3.0.20070401
//
// Revision 1.20  2005/06/17 06:39:59  daniel_frey
// New ActionButton icons and some corrections on documentation
//
// Revision 1.19  2004/08/31 22:10:17  daniel_frey
// Examlist loading working
//
// Revision 1.18  2004/04/25 13:56:43  daniel_frey
// Moved Dialogs from Herbars modeapi to xmatrix
//
// Revision 1.17  2003/04/29 16:58:20  daniel_frey
// - Removed upper limitation of level
//
// Revision 1.16  2003/04/11 12:28:28  daniel_frey
// - Included popup for current list in lesson panel
//
// Revision 1.15  2003/03/16 23:11:47  daniel_frey
// - New approach to GUI
//
// Revision 1.14  2003/02/14 11:12:02  daniel_frey
// - Removed second button for current taxon
//
// Revision 1.13  2003/02/13 21:45:20  daniel_frey
// - Moved ScrollerPanel to xmatrix
//
// Revision 1.12  2003/02/13 19:11:44  thomas_wegmueller
// *** empty log message ***
//
// Revision 1.8  2002/09/10 09:49:47  dirk
// change getString to getStringForObject
//
// Revision 1.7  2002/08/05 12:47:16  Dani
// - Synchronization without change
//
// Revision 1.6  2002/08/05 12:16:52  Dani
// - Changed import of Strings and ImageLocator
// - Adapted changes in ImageLocator (renamed getImageIcon to getIcon)
//
// Revision 1.5  2002/08/02 00:42:19  Dani
// Optimized import statements
//
// Revision 1.4  2002/07/23 10:11:00  dirk
// move popups to modeapi
//
// Revision 1.3  2002/05/31 20:03:54  Dani
// Refactored tax tree components
//
// Revision 1.2  2002/05/28 14:11:03  Thomas
// first shot of desired screendesign
//
// Revision 1.1  2002/05/24 00:20:44  dirk
// initial
//
// Revision 1.5  2002/05/23 17:28:17  Dani
// First shot of a morphology display panel
//
// Revision 1.4  2002/05/17 09:00:25  dani
// Added white background
//
// Revision 1.2  2002/05/15 14:44:27  Thomas
// Simplified BreadCrumb with ComponentOrientation
//
// Revision 1.1  2002/05/15 13:51:55  Thomas
// corrected name of BreadCrump to BreadCrumb
//
// Revision 1.1  2002/05/15 13:49:26  dani
// Initial
//
