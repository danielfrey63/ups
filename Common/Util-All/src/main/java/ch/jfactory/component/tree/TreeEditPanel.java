/*
 * Herbar CD-ROM version 2
 *
 * TreeAwarePanel.java
 *
 * Created on 8.7.2002 17:19
 * Created by Daniel Frey
 */
package ch.jfactory.component.tree;

import ch.jfactory.application.presentation.Constants;
import ch.jfactory.application.presentation.WindowUtils;
import ch.jfactory.component.EnabablePopup;
import ch.jfactory.resource.ImageLocator;
import ch.jfactory.resource.Strings;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;

/**
 * A panel that implements the TreeSelectionListener to be able to pass it to the {@link
 * com.ethz.geobot.herbar.entry.common.TreeMutator}. This panel allows insertion and deletion of nodes into a tree.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.4 $ $Date: 2006/03/22 15:05:10 $
 */
public abstract class TreeEditPanel extends JPanel implements TreeSelectionListener, DocumentListener, ActionListener
{
    private static final boolean enableDebugNode = false;

    private JButton debugButton;

    private JButton deleteButton;

    private EnabablePopup enababelPopup;

    private JButton newButton;

    private JTextField newField;

    private JTree tree;

    /**
     * Instantiates the panels for the editor
     *
     * @param prefix the prefix used to load string resources
     */
    public TreeEditPanel( final String prefix )
    {
        // Initialize
        initComponents();

        // Layout
        final GridBagLayout gbl = new GridBagLayout();
        this.setLayout( gbl );
        // It's a top panel, so space only at the bottom
        this.setBorder( BorderFactory.createEmptyBorder( 0, 0, Constants.GAP_BETWEEN_REGIONS, 0 ) );
        final GridBagConstraints gbc = new GridBagConstraints();

        final Insets insLeftWithin = new Insets( 0, 0, Constants.GAP_BORDER_RIGHT_BOTTOM, Constants.GAP_BORDER_LEFT_TOP );
        final Insets insRightWithin = new Insets( 0, 0, Constants.GAP_BORDER_RIGHT_BOTTOM, 0 );

        gbc.insets = new Insets( 0, 0, Constants.GAP_BETWEEN_GROUP, Constants.GAP_BETWEEN_GROUP );
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.0;
        gbc.insets = insLeftWithin;
        this.add( new JLabel( Strings.getString( prefix + ".NEWLABEL.NAME" ) ), gbc );

        gbc.gridx = 1; // 1
        gbc.gridy = 0; // 0
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.insets = insRightWithin;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        this.add( newField, gbc );

        if ( enababelPopup != null )
        {
            gbc.gridx = 0; // 0
            gbc.gridy += 1; // 1
            gbc.weightx = 0.0;
            gbc.insets = insLeftWithin;
            gbc.fill = GridBagConstraints.NONE;
            this.add( new JLabel( Strings.getString( prefix + ".NODETYPES.NAME" ) ), gbc );

            gbc.gridx += 1; // 1
            gbc.gridy += 0; // 1
            gbc.gridwidth = 2;
            gbc.weightx = 1.0;
            gbc.insets = insRightWithin;
            this.add( enababelPopup, gbc );
        }

        final JPanel buttonsPanel = new JPanel();
        final FlowLayout fl = new FlowLayout( FlowLayout.RIGHT, 0, 0 );
        buttonsPanel.setLayout( fl );
        // Debug
        if ( enableDebugNode )
        {
            buttonsPanel.add( debugButton );
        }
        buttonsPanel.add( newButton );
        buttonsPanel.add( deleteButton );
        final JButton[] buttons;
        if ( enableDebugNode )
        {
            buttons = new JButton[]{debugButton, newButton, deleteButton};
        }
        else
        {
            buttons = new JButton[]{newButton, deleteButton};
        }
        WindowUtils.equalizeButtons( buttons );
        WindowUtils.spaceComponents( buttons );

        gbc.gridx = 1; // 1
        gbc.gridy += 1; // 2
        gbc.insets = insRightWithin;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        this.add( buttonsPanel, gbc );

        gbc.gridx = 0;
        gbc.gridy += 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 1.0;
        this.add( new JPanel(), gbc );

        // Register for events
        newField.getDocument().addDocumentListener( this );
        newButton.addActionListener( this );
        deleteButton.addActionListener( this );
        if ( enableDebugNode )
        {
            debugButton.addActionListener( new ActionListener()
            {
                public void actionPerformed( final ActionEvent ae )
                {
                    final TreePath tp = getTree().getSelectionPath();
                    if ( tp == null )
                    {
                        return;
                    }
                    System.out.println();
                    final MutableTreeNode node = (MutableTreeNode)
                            tp.getLastPathComponent();
                    debugNode( node );
                }
            } );
        }
    }

    /**
     * Returns the tree.
     *
     * @return JTree
     */
    public JTree getTree()
    {
        return tree;
    }

    /**
     * Sets the tree
     *
     * @param tree the <code>JTree</code> to set
     */
    public void setTree( final JTree tree )
    {
        this.tree = tree;
    }

    /**
     * Returns the newField.
     *
     * @return JTextField
     */
    public JTextField getNewField()
    {
        return newField;
    }

    /**
     * Returns the enababelPopup.
     *
     * @return EnabablePopup
     */
    public EnabablePopup getEnababelPopup()
    {
        return enababelPopup;
    }

    /**
     * @see TreeSelectionListener #valueChanged(TreeSelectionEvent)
     */
    public void valueChanged( final TreeSelectionEvent tse )
    {
        updateControls();
    }

    /**
     * @see DocumentListener#changedUpdate(DocumentEvent)
     */
    public void changedUpdate( final DocumentEvent e )
    {
        updateControls();
    }

    /**
     * @see DocumentListener#insertUpdate(DocumentEvent)
     */
    public void insertUpdate( final DocumentEvent e )
    {
        changedUpdate( e );
    }

    /**
     * @see DocumentListener#removeUpdate(DocumentEvent)
     */
    public void removeUpdate( final DocumentEvent e )
    {
        changedUpdate( e );
    }

    /**
     * @see ActionListener#actionPerformed(ActionEvent)
     */
    public void actionPerformed( final ActionEvent ae )
    {
        // Keep state of expanded nodes in the tree
        final TreeExpandedRestorer ter = new TreeExpandedRestorer( tree );
        ter.save();

        // Dispatch button event
        final Object source = ae.getSource();
        if ( source == newButton )
        {
            insertNewItem();
        }
        else if ( source == deleteButton )
        {
            final TreePath[] paths = deleteItems();
            for ( final TreePath path : paths )
            {
                path.getParentPath();
                ter.remove( path );
            }
        }

        // Delete contents of edit field
        newField.setSelectionStart( 0 );
        newField.setSelectionEnd( newField.getText().length() );

        // Reload tree and restore expanded tree nodes
        ( (DefaultTreeModel) tree.getModel() ).reload();
        ter.restore();
    }

    /**
     * Default behaviour of the delete button is to be enabled as soon as any item is selected in the tree. Overwrite
     * this method if you wish to have another behaviour for the delete button.
     */
    protected boolean isDeletionAllowed()
    {
        return getTree().getSelectionCount() > 0;
    }

    /**
     * Default behaviour of the new button is to be enabled as soon as any valid item is selected from the popup, the
     * edit field is not empty, and any item is selected in the tree. Overwrite this method if you wish to have another
     * behaviour for the new button.
     */
    protected boolean isNewAllowed( final boolean isPopupValid, final String newText,
                                    final TreePath[] selections )
    {
        boolean result = isPopupValid;
        result &= ( selections == null || selections.length > 0 );
        result &= newText.length() > 0;
        return result;
    }

    /**
     * Returns whether the root node is selected. May be used in addition to isNewAllowed and isDeletionAllowed to
     * determine state of buttons.
     */
    protected boolean isRootSelected()
    {
        final JTree tree = getTree();
        final int[] rows = tree.getSelectionRows();
        return tree.isRootVisible() && rows != null && rows.length > 0 && rows[0] == 0;
    }

    /**
     * The <code>TreeEditPanel</code> uses a popup to represent different types of nodes that may be constructed. This
     * method should return which types are enabled at a given state. <p/> To determine the valid node types, the usual
     * method is to access the tree and get the selected node. Dependent on this node, the valid ones may be
     * determined.<p> <p/> Note: Reference identity is used to enable/disable the popup entries. So make sure to deliver
     * the same objects as delivered by {@link #getNodeTypes()}.
     *
     * @return the valid Objects
     */
    protected abstract Object[] getValidNodeTypes();

    /**
     * The <code>TreeEditPanel</code> uses a popup to represent different types of nodes that may be constructed. All
     * node types that are available should be returned by this method. A new node will be of one of these types.
     *
     * @return all Objects used as a option
     */
    protected abstract Object[] getNodeTypes();

    /**
     * If this method is called, the tree has in any case a selected path. The purpose of this method is to update the
     * data model (model side). The tree side of the task is acomplished by the superclass (i.e. restoring of the
     * expanded nodes).
     *
     * @return the TreePath for the new inserted node
     */
    protected abstract TreePath insertNewItem();

    /**
     * This method should delete all items selected in the tree from the data model and return <code>TreePath</code>s of
     * the deleted nodes.
     *
     * @return an array of <code>TreePath</code>s representing the deleted nodes
     */
    protected abstract TreePath[] deleteItems();

    /**
     * Used to debug a given node. Override this method to debug node information.
     *
     * @param node the <code>MutableTreeNode</code> to debug
     */
    protected void debugNode( final MutableTreeNode node )
    {
    }

    private void initComponents()
    {
        newButton = new JButton( Strings.getString( "ADD.NAME" ) );
        newButton.setEnabled( false );
        deleteButton = new JButton( Strings.getString( "DELETE.NAME" ) );
        deleteButton.setEnabled( false );
        // Debug
        if ( enableDebugNode )
        {
            debugButton = new JButton( Strings.getString( "DEBUG.NAME" ) );
        }
        newField = new JTextField();
        // Popup
        final Icon icon = ImageLocator.getIcon( "scrollDown.gif" );
        final Object[] nodeTypes = getNodeTypes();
        if ( nodeTypes != null && nodeTypes.length > 0 )
        {
            enababelPopup = new EnabablePopup( nodeTypes, icon );
        }
    }

    private void updateControls()
    {
        // ajust delete button
        deleteButton.setEnabled( isDeletionAllowed() );

        // ajust popup
        boolean isValid = true;
        if ( enababelPopup != null )
        {
            final Object[] valids = getValidNodeTypes();
            enababelPopup.setEnabled( valids );
            isValid = valids.length > 0;
        }

        // ajust new button
        final String text = newField.getText();
        final TreePath[] selections = getTree().getSelectionPaths();
        newButton.setEnabled( isNewAllowed( isValid, text, selections ) );
    }
}

// $Log: TreeEditPanel.java,v $
// Revision 1.4  2006/03/22 15:05:10  daniel_frey
// *** empty log message ***
//
// Revision 1.3  2005/09/01 14:53:49  daniel_frey
// TreeExpandedRestorer now working with paths, rows are eliminated
//
// Revision 1.2  2005/08/16 10:20:17  daniel_frey
// - Component dialog may stay open upon specific ComponentDialogException
// - TreeExpandedRestorer simplified
//
// Revision 1.1  2005/06/16 06:28:58  daniel_frey
// Completely merged and finished for UST version 2.0-20050616
//
// Revision 1.3  2004/08/30 21:46:58  daniel_frey
// Lot of refactorings
//
// Revision 1.2  2004/06/16 21:18:32  daniel_frey
// Extracted package from xmatrix to jfactory
//
// Revision 1.1  2004/04/19 10:31:21  daniel_frey
// Replaced top level package com by ch
//
// Revision 1.14  2004/03/04 23:39:28  daniel_frey
// - Build with News on Splash
//
// Revision 1.13  2003/05/25 21:38:47  daniel_frey
// - Optimized imports
// - Replaced static access by proper class access instead of object access
//
// Revision 1.12  2003/04/02 14:49:04  daniel_frey
// - Revised wizards
//
// Revision 1.11  2003/02/05 22:01:18  daniel_frey
// - Added wrong java doc links
// - Removed unused code
//
// Revision 1.10  2002/11/05 11:21:58  daniel_frey
// - Level with tree from GraphNode
//
// Revision 1.9  2002/10/04 14:42:03  matthias_baltisberger
// - Added semicolon...
//
// Revision 1.8  2002/10/04 14:30:22  matthias_baltisberger
// - Removed debug mode
//
// Revision 1.7  2002/09/25 14:41:35  daniel_frey
// - Introduced dynamic relevance object model
// - Replaced roles with relevances  by class types for each comination
// - Removed some caching issues
//
// Revision 1.6  2002/09/20 14:01:28  Dani
// - Simplified interface tree-graph node
//
// Revision 1.5  2002/09/17 09:15:34  Dani
// - Entry now working
// - Better encapsulation of layers
//
// Revision 1.4  2002/09/12 15:48:29  Dani
// - Types are determined by class types
// - Filters work with threes
// - Generic GraphNode model used
//
// Revision 1.3  2002/09/10 09:45:04  Dani
// - Better documentation
//
// Revision 1.2  2002/08/05 19:21:32  Dani
// - Mor dnd working but not saving
//
// Revision 1.1  2002/08/05 12:48:50  Dani
// - Moved to tree package
//
// Revision 1.2  2002/08/01 15:48:27  Dani
// - Removed unnecessary DB accesses.
// - Update, deletion, addition of Taxon objects work now.
//
// Revision 1.1  2002/07/11 18:40:40  Dani
// - Version before short transactions
//
