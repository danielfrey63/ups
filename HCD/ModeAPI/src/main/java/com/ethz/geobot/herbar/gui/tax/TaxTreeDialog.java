/*
 * Herbar CD-ROM version 2
 *
 * TaxTreeDialog.java
 *
 * Created on 14. Mai 2002, 17:13
 * Created by Daniel Frey
 */
package com.ethz.geobot.herbar.gui.tax;

import ch.jfactory.application.presentation.Constants;
import ch.jfactory.application.view.dialog.I15nComponentDialog;
import ch.jfactory.application.view.search.SearchableUtils;
import ch.jfactory.application.view.search.TreeSearchable;
import com.ethz.geobot.herbar.model.Taxon;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import org.apache.log4j.Logger;

/**
 * Dialog to display a tree of the taxonomie and a search field. After disposing the dialog, the actual value may be
 * gotten by calling {@link #getSelectedTaxon()}.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:08 $
 */
public class TaxTreeDialog extends I15nComponentDialog implements TreeSelectionListener
{
    protected final static Logger LOG = Logger.getLogger( TaxTreeDialog.class );

    private TaxTree taxTree;

    /**
     * Creates a new instance of TaxTreeDialog
     *
     * @param parent    JFrame to be centered on and modal to
     * @param rootTaxon the root taxon for the tree
     */
    public TaxTreeDialog( final JFrame parent, final Taxon rootTaxon )
    {
        super( parent, "DIALOG.TAX" );
        taxTree.setRootTaxon( rootTaxon );
        enableApply( taxTree.getSelectionPath() != null );
    }

    /**
     * Creates a new instance of TaxTreeDialog
     *
     * @param parent    JDialog to be centered on and modal to
     * @param rootTaxon the root taxon for the tree
     */
    public TaxTreeDialog( final JDialog parent, final Taxon rootTaxon )
    {
        super( parent, "DIALOG.TAX" );
        if ( LOG.isDebugEnabled() )
        {
            LOG.debug( "create tax tree dialog with root taxon: " + rootTaxon );
        }
        taxTree.setRootTaxon( rootTaxon );
        enableApply( taxTree.getSelectionPath() != null );
    }

    /**
     * set the selected taxon
     *
     * @param taxon selected taxon
     */
    public void setSelectedTaxon( final Taxon taxon )
    {
        if ( LOG.isDebugEnabled() )
        {
            LOG.debug( "set selected taxon: " + taxon );
        }
        taxTree.setSelectedTaxon( taxon );
    }

    /**
     * get the selected taxon.
     *
     * @return selected taxon
     */
    public Taxon getSelectedTaxon()
    {
        return taxTree.getSelectedTaxon();
    }

    public void valueChanged( final TreeSelectionEvent tse )
    {
        enableApply( tse.getNewLeadSelectionPath() != null );
    }

    protected void onApply() throws ComponentDialogException
    {
        if ( LOG.isDebugEnabled() )
        {
            LOG.debug( "tax tree dialog return taxon: " + getSelectedTaxon() );
        }
    }

    protected void onCancel()
    {
        if ( LOG.isDebugEnabled() )
        {
            LOG.debug( "no taxon selected; cancel pressed" );
        }
        taxTree.setSelectedTaxon( null );
    }

    protected JComponent createComponentPanel()
    {
        // Add tree in center.
        taxTree = new TaxTree();
        taxTree.addTreeSelectionListener( this );

        // Add TreeFindPanel at north.
        final TreeSearchable treeSearchable = SearchableUtils.installSearchable( taxTree );
        treeSearchable.setRecursive( true );

        final JPanel treeFinderPanel = new JPanel( new FlowLayout( FlowLayout.LEFT, 0, 0 ) );
        treeFinderPanel.setBorder( new EmptyBorder( 0, 0, Constants.GAP_BETWEEN_GROUP, 0 ) );

        final JPanel panel = new JPanel( new BorderLayout() );
        panel.add( new JScrollPane( taxTree ), BorderLayout.CENTER );
        panel.add( treeFinderPanel, BorderLayout.NORTH );

        return new JScrollPane( taxTree );
    }
}

// $Log: TaxTreeDialog.java,v $
// Revision 1.1  2007/09/17 11:07:08  daniel_frey
// - Version 3.0.20070401
//
// Revision 1.28  2005/08/31 17:42:58  daniel_frey
// *** empty log message ***
//
// Revision 1.27  2005/06/17 06:39:58  daniel_frey
// New ActionButton icons and some corrections on documentation
//
// Revision 1.26  2004/08/31 22:10:16  daniel_frey
// Examlist loading working
//
// Revision 1.25  2004/04/25 13:56:42  daniel_frey
// Moved Dialogs from Herbars modeapi to xmatrix
//
// Revision 1.24  2004/03/04 23:39:28  daniel_frey
// - Build with News on Splash
//
// Revision 1.23  2003/05/25 21:38:46  daniel_frey
// - Optimized imports
// - Replaced static access by proper class access instead of object access
//
// Revision 1.22  2003/04/02 14:49:03  daniel_frey
// - Revised wizards
//
// Revision 1.21  2003/02/13 23:08:28  dirk_hoffmann
// fix wizard model problem
//
// Revision 1.20  2003/02/12 19:13:26  dirk_hoffmann
// delete FilterDefinition
//
// Revision 1.19  2003/02/09 11:59:14  daniel_frey
// - Externalized binding of ESC key to cancel
//
// Revision 1.18  2003/01/23 10:54:27  daniel_frey
// - Optimized imports
//
// Revision 1.17  2003/01/22 13:11:12  daniel_frey
// - Added ESC key, RETURN key for simplier dialog handling.
//
// Revision 1.16  2003/01/22 12:02:34  daniel_frey
// - Added correct TreeFinder implementation
//
// Revision 1.15  2002/09/20 14:04:21  Dani
// - Simplified use of tree finder
//
// Revision 1.14  2002/09/17 09:15:41  Dani
// - Entry now working
// - Better encapsulation of layers
//
// Revision 1.13  2002/09/12 12:17:12  dirk
// add support for empty TaxTree
//
// Revision 1.12  2002/08/05 11:27:12  Dani
// - Moved to ch.xmatrix
//
// Revision 1.11  2002/08/02 00:42:19  Dani
// Optimized import statements
//
// Revision 1.10  2002/07/11 18:51:03  Dani
// Added commented main
//
// Revision 1.9  2002/07/11 13:56:29  dirk
// add setSelectedTaxon method
//
// Revision 1.8  2002/07/11 12:13:58  Dani
// Adapted import to move of TreeFinder
//
// Revision 1.7  2002/07/10 15:08:51  Dani
// Adapte import to move of TreeFinder to xmatrix package
//
// Revision 1.6  2002/07/10 14:28:11  dirk
// move getSelectedTaxon to TaxTree (only delegation in TaxTreeDialog)
//
// Revision 1.5  2002/07/10 14:21:35  dirk
// add getSelectedTaxon
//
// Revision 1.4  2002/07/05 08:04:33  Dani
// Refactored for clearer variable naming
//
// Revision 1.4  2002/07/05 07:20:15  Dani
// Added devdoc target for private javadoc generation
//
// Revision 1.3  2002/05/31 20:01:10  Dani
// Refactored tax tree components
//
// Revision 1.2  2002/05/28 10:01:21  Dani
// Adapted headers and footers
//
// Revision 1.1  2002/05/23 23:51:29  dirk
// initial checkin
//
// Revision 1.5  2002/05/15 13:04:02  Dani
// BreadCrump added
//
// Revision 1.4  2002/05/15 09:26:08  Dani
// Added proper button updates in TaxTreeDialog
//
// Revision 1.3  2002/05/15 09:24:56  Dani
// Corrected header
//
// Revision 1.2  2002/05/15 09:20:44  Dani
// Added proper button updates in TaxTreeDialog
//
// Revision 1.1  2002/05/15 07:50:31  Dani
// Added TaxTreeDialog to LessonBar
//

