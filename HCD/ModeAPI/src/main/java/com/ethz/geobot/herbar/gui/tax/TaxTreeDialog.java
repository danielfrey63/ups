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

import ch.jfactory.application.presentation.Constants;
import ch.jfactory.application.view.dialog.I15nComponentDialog;
import com.ethz.geobot.herbar.model.Taxon;
import com.jidesoft.swing.SearchableUtils;
import com.jidesoft.swing.TreeSearchable;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Dialog to display a tree of the taxonomie and a search field. After disposing the dialog, the actual value may be gotten by calling {@link #getSelectedTaxon()}.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:08 $
 */
public class TaxTreeDialog extends I15nComponentDialog implements TreeSelectionListener
{
    protected final static Logger LOG = LoggerFactory.getLogger( TaxTreeDialog.class );

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
     * set the selected taxon
     *
     * @param taxon selected taxon
     */
    public void setSelectedTaxon( final Taxon taxon )
    {
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
    }

    protected void onCancel()
    {
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
