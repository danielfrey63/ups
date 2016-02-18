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
package com.ethz.geobot.herbar.gui;

import ch.jfactory.action.ComponentFocusListener;
import ch.jfactory.application.presentation.Constants;
import ch.jfactory.component.ComponentFactory;
import ch.jfactory.component.TrackableJSplitPane;
import ch.jfactory.component.tree.GraphTreeNode;
import ch.jfactory.component.tree.SearchableTree;
import ch.jfactory.model.graph.GraphNode;
import ch.jfactory.model.graph.tree.VirtualGraphTreeNodeFilter;
import ch.jfactory.resource.Strings;
import com.ethz.geobot.herbar.model.Taxon;
import com.jidesoft.swing.SearchableUtils;
import com.jidesoft.swing.TreeSearchable;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 * Each choice for attributes (i.e. medicine, morphology, ecology) is displayed in one of these panels. It has an upper and a lower region. Upper displays the results chosen by the student, lower offers the possible choices to select from.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:08 $
 */
public class PropertyInterrogatorPanel extends JPanel
{
    /** Used to keep track of all panels which need to have synchronized split panes. */
    private static PropertyInterrogatorPanel[] members = new PropertyInterrogatorPanel[0];

    /** A split pane which sends events upon change of the splitter position. Has to member as one instance will access the other ones to adjust position. */
    private final JSplitPane split;

    private JPanel cardsPanel;

    private List<ChooserTree> treeList;

    private CardLayout cards;

    private final ResultPanel resultPanel;

    private final DetailResultModel resultModel;

    private JComboBox complexityChooser;

    private JButton addButton;

    private static final double INITIAL_DIVIDER_LOCATION = 0.8;

    public PropertyInterrogatorPanel( final DetailResultModel resultModel )
    {
        this.resultModel = resultModel;

        registerInstance();

        resultPanel = new ResultPanel( resultModel );
        split = createSplit( createChooserPanel(), resultPanel );

        setLayout( new BorderLayout() );
        add( split, BorderLayout.CENTER );

        // Dirty trick to get the divider location right although hidden behind the second tab.
        addHierarchyListener( new HierarchyListener()
        {
            public void hierarchyChanged( final HierarchyEvent e )
            {
                final Container top = PropertyInterrogatorPanel.this.getTopLevelAncestor();
                if ( top instanceof JFrame )
                {
                    if ( isVisible() )
                    {
                        split.setDividerLocation( INITIAL_DIVIDER_LOCATION );
                        split.setResizeWeight( 1.0 );
                    }
                }
                if ( split.getDividerLocation() == INITIAL_DIVIDER_LOCATION )
                {
                    removeHierarchyListener( this );
                }
            }
        } );
    }

    /** Register this new panel to make its split pane synchronizeable with the other instances' one. */
    private void registerInstance()
    {
        final int len = members.length;
        final PropertyInterrogatorPanel[] newMembers = new PropertyInterrogatorPanel[len + 1];
        System.arraycopy( members, 0, newMembers, 0, len );
        newMembers[len] = this;
        members = newMembers;
    }

    private JSplitPane createSplit( final Component topLeft, final JPanel bottomRight )
    {
        final TrackableJSplitPane split = new TrackableJSplitPane( JSplitPane.VERTICAL_SPLIT, topLeft, bottomRight );

        // Synchronise dividers
        split.setBorder( new EmptyBorder( 0, 0, 0, 0 ) );
        split.addDividerListener( new TrackableJSplitPane.DividerListener()
        {
            public void dividerMoved( final TrackableJSplitPane.DividerChangeEvent e )
            {
                int location = 0;
                for ( final PropertyInterrogatorPanel member : members )
                {
                    if ( member.isShowing() )
                    {
                        location = member.split.getDividerLocation();
                    }
                }
                for ( final PropertyInterrogatorPanel member : members )
                {
                    member.split.setDividerLocation( location );
                }
            }
        } );
        return split;
    }

    public void setTaxFocus( final Taxon focus )
    {
        // Inform upper part (result panel)
        resultPanel.setTaxFocus( focus );
        // Inform lower part (choice panels)
        for ( final ChooserTree chooserTree : treeList )
        {
            chooserTree.setTaxFocus();
        }
        cardsPanel.validate();
    }

    public String getBase()
    {
        return resultModel.getTypeToDisplay();
    }

    /**
     * Adds a chooser panel for each sub model.
     *
     * @return the card panel
     */
    private JPanel createTreeCardsPanel()
    {
        cards = new CardLayout();
        cardsPanel = new JPanel( cards );
        treeList = new ArrayList<ChooserTree>();
        final Enumeration e = resultModel.subStateModels();
        while ( e.hasMoreElements() )
        {
            final InterrogatorComplexityFactory.Type type = (InterrogatorComplexityFactory.Type) e.nextElement();
            final ChooserTree chooserTree = new ChooserTree( type );
            ComponentFocusListener.registerComponentFocusListener( chooserTree.getTree(), addButton );
            treeList.add( chooserTree );
            cardsPanel.add( chooserTree, type.toString() );
        }
        return cardsPanel;
    }

    private JPanel createChooserPanel()
    {
        // Be aware that the combo box and the cards are expected to have synchonized indexes.
        final JPanel chooserPanel = new JPanel( new BorderLayout() );
        chooserPanel.add( createSelectionPanel(), BorderLayout.NORTH );
        chooserPanel.add( createTreeCardsPanel(), BorderLayout.CENTER );

        relinkTreeFinder();
        createComponentListener();
        createTreeListeners();

        return chooserPanel;
    }

    private JComboBox createComplexityChooser()
    {
        final JComboBox combo = new JComboBox( resultModel.getSubModels() );
        combo.setToolTipText( Strings.getString( "PROPERTY.CHOOSER.HINT" ) );
        combo.addActionListener( new ActionListener()
        {
            public void actionPerformed( final ActionEvent e )
            {
                final Object selectedItem = complexityChooser.getSelectedItem();
                cards.show( cardsPanel, selectedItem.toString() );
                relinkTreeFinder();
            }
        } );

        // Make sure the add button is only enabled when a leaf is selected in the choice tree for the new tree now
        // chosen by the complexity type.
        combo.addItemListener( new ItemListener()
        {
            public void itemStateChanged( final ItemEvent e )
            {
                if ( e.getStateChange() == ItemEvent.DESELECTED )
                {
                    // Finding out the index of the deselected item is not obvious as the item has already been
                    // switched. The utility method getIndex() in Type helps here.
                    final int selectedIndex = combo.getSelectedIndex();
                    final JTree theTree = ( treeList.get( selectedIndex ) ).getTree();
                    final TreeSelectionListener[] listeners = theTree.getTreeSelectionListeners();
                    for ( final TreeSelectionListener listener : listeners )
                    {
                        theTree.removeTreeSelectionListener( listener );
                    }
                }
                else if ( e.getStateChange() == ItemEvent.SELECTED )
                {
                    // Register new tree selection listener.
                    final int selectedIndex = combo.getSelectedIndex();
                    final JTree jTree = ( treeList.get( selectedIndex ) ).getTree();
                    jTree.addTreeSelectionListener( new TreeSelectionListener()
                    {
                        public void valueChanged( final TreeSelectionEvent e )
                        {
                            adaptButtonStateToTreeSelection( selectedIndex );
                        }
                    } );
                }
                adaptButtonStateToTreeSelection( complexityChooser.getSelectedIndex() );
            }
        } );
        return combo;
    }

    private void createTreeListeners()
    {
        // Make sure that the button is also initially adapted to the trees selection
        final JTree theTree = ( treeList.get( complexityChooser.getSelectedIndex() ) ).getTree();
        theTree.addTreeSelectionListener( new TreeSelectionListener()
        {
            public void valueChanged( final TreeSelectionEvent e )
            {
                adaptButtonStateToTreeSelection( complexityChooser.getSelectedIndex() );
            }
        } );
    }

    private void createComponentListener()
    {
        addComponentListener( new ComponentAdapter()
        {
            public void componentShown( final ComponentEvent e )
            {
                final JRootPane rootPane = getRootPane();
                rootPane.setDefaultButton( addButton );
                addButton.setEnabled( false );
            }
        } );
    }

    private JPanel createSelectionPanel()
    {
        complexityChooser = createComplexityChooser();
        addButton = createAddButton();

        final int gapTL = Constants.GAP_BORDER_LEFT_TOP;
        final int gapBR = Constants.GAP_BORDER_RIGHT_BOTTOM;
        final JPanel midPanel = new JPanel( new GridBagLayout() );
        midPanel.setBorder( BorderFactory.createEmptyBorder( gapTL, gapTL, gapBR, gapBR ) );

        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1.0;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.WEST;
        midPanel.add( complexityChooser, gbc );
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.EAST;
        midPanel.add( addButton, gbc );

        return midPanel;
    }

    private JButton createAddButton()
    {
        final ActionListener action = new ActionListener()
        {
            public void actionPerformed( final ActionEvent e )
            {
                final ChooserTree chooserTree = treeList.get( complexityChooser.getSelectedIndex() );
                final JTree tree = chooserTree.getTree();
                final GraphTreeNode lastPathComponent = (GraphTreeNode) tree.getSelectionPath().getLastPathComponent();
                resultPanel.addGuess( new DefaultMutableTreeNode( lastPathComponent.getNode().getDependent() ) );
                addButton.setEnabled( !resultPanel.isComplete() );
                if ( split.getDividerLocation() == 0 )
                {
                    split.setDividerLocation( 0.25 );
                }
            }
        };
        return ComponentFactory.createButton( "PROPERTY.ADD", action );
    }

    private void relinkTreeFinder()
    {
        final ChooserTree chooserTree = treeList.get( complexityChooser.getSelectedIndex() );
        final SearchableTree tree = (SearchableTree) chooserTree.getTree();
        final TreeSearchable treeSearchable = SearchableUtils.installSearchable( tree );
        treeSearchable.setRecursive( true );
    }

    private void adaptButtonStateToTreeSelection( final int selectionIndex )
    {
        final JTree theTree = ( treeList.get( selectionIndex ) ).getTree();
        final TreePath path = theTree.getSelectionPath();
        if ( path != null )
        {
            final TreeNode node = (TreeNode) path.getLastPathComponent();
            addButton.setEnabled( node.isLeaf() );
        }
        else
        {
            addButton.setEnabled( false );
        }
    }

    public DetailResultModel getResultModel()
    {
        return resultModel;
    }

    static class ChooserTree extends JPanel
    {
        private final InterrogatorComplexityFactory.Type type;

        private GraphNode root;

        private final JTree tree;

        private final VirtualGraphTreeNodeFilter filter;

        public ChooserTree( final InterrogatorComplexityFactory.Type type )
        {
            this.type = type;
            setLayout( new BorderLayout() );
            filter = type.getFilter();
            root = type.getRoot();
            tree = VirtualGraphTreeFactory.getVirtualTree( root, filter );
            tree.setShowsRootHandles( true );
            add( new JScrollPane( tree ), BorderLayout.CENTER );
        }

        public void setTaxFocus()
        {
            // Keep state of the whole tree, so user expansions/collapsions are not discared
            root = type.getRoot();
            VirtualGraphTreeFactory.updateModel( tree, filter, root );
        }

        public JTree getTree()
        {
            return tree;
        }
    }
}
