/* ====================================================================
 *  Copyright 2004-2005 www.xmatrix.ch
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 * ====================================================================
 */
package ch.xmatrix.ups.uec.groups;

import ch.jfactory.component.Dialogs;
import ch.jfactory.component.SimpleDocumentListener;
import ch.jfactory.component.tree.TreeExpandedRestorer;
import ch.jfactory.convert.Converter;
import ch.jfactory.model.SimpleModelList;
import ch.jfactory.resource.Strings;
import ch.jfactory.xstream.XStreamConverter;
import ch.xmatrix.ups.controller.TreeCheckboxController;
import ch.xmatrix.ups.domain.SimpleTaxon;
import ch.xmatrix.ups.domain.TaxonBased;
import ch.xmatrix.ups.model.TaxonModels;
import ch.xmatrix.ups.model.TaxonTree;
import ch.xmatrix.ups.uec.groups.commands.Commands;
import ch.xmatrix.ups.uec.groups.commands.DeleteGroup;
import ch.xmatrix.ups.uec.groups.commands.NewGroup;
import ch.xmatrix.ups.uec.main.MainModel;
import ch.xmatrix.ups.uec.master.AbstractDetailsBuilder;
import com.jgoodies.binding.list.ArrayListModel;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.Sizes;
import com.jgoodies.looks.plastic.Plastic3DLookAndFeel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import javax.swing.AbstractListModel;
import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.tree.TreePath;
import org.pietschy.command.ActionCommand;
import org.pietschy.command.ActionCommandInterceptor;

/**
 * @author Daniel Frey
 * @version $Revision: 1.6 $ $Date: 2007/09/27 10:48:07 $
 */
public class GroupsBuilder extends AbstractDetailsBuilder
{
    public static final String COMPONENT_TREE = "tree";

    public static final String COMPONENT_LISTGROUPS = "listGroups";

    public static final String COMPONENT_LISTTAXA = "listTaxa";

    public static final String COMPONENT_NAME = "fieldName";

    public static final String COMPONENT_MINIMUM = "spinnerMinimum";

    public static final String COMPONENT_MAXIMUM = "spinnerMaximum";

    private static final String RESOURCE_MODEL = "/data/groups.xml";

    private static final String RESOURCE_FORM = "/ch/xmatrix/ups/uec/groups/GroupsPanel.jfd";

    private final DefaultListModel listModel = new DefaultListModel();

    private final SelectionInList groupModels = new SelectionInList( listModel );

    private final DefaultListSelectionModel groupsSelection = new DefaultListSelectionModel();

    private final DefaultListSelectionModel taxaSelection = new DefaultListSelectionModel();

    private final GroupsTreeRenderer groupsTreeRenderer = new GroupsTreeRenderer();

    private final GroupsListRenderer groupsListRenderer = new GroupsListRenderer();

    private JTree tree;

    private JList listGroups;

    private JList listTaxa;

    private JTextField name;

    private JSpinner minimum;

    private JSpinner maximum;

    private ActionCommand delete;

    private ActionCommand add;

    private Converter converter;

    private TreeExpandedRestorer restorer;

    private TaxaListRenderer taxaListRenderer;

    private boolean enabledByMaster;

    private boolean isAdjusting;

    public GroupsBuilder()
    {
        super( new GroupsFactory(), RESOURCE_MODEL, RESOURCE_FORM, 30 );
    }

    //--- ActionCommandPanelBuilder overrides

    protected void initComponentListeners()
    {
        listGroups.addListSelectionListener( new ListSelectionListener()
        {
            public void valueChanged( final ListSelectionEvent e )
            {
                if ( !isAdjusting )
                {
                    isAdjusting = true;
                    if ( !e.getValueIsAdjusting() )
                    {
                        final GroupsModel model = getGroupsModel();
                        if ( model != null )
                        {
                            model.setCurrentGroup( (GroupModel) listGroups.getSelectedValue() );
                            setStates( listGroups );
                        }
                    }
                    isAdjusting = false;
                }
            }
        } );
        listTaxa.addMouseListener( new MouseAdapter()
        {
            public void mousePressed( final MouseEvent e )
            {
                if ( e.getClickCount() == 2 )
                {
                    final String taxonName = (String) listTaxa.getSelectedValue();
                    final TaxonTree taxonTree = getTaxonTree();
                    final ArrayList<Object> pathElements = new ArrayList<Object>();
                    SimpleTaxon taxon = taxonTree.findTaxonByName( taxonName );
                    while ( taxon != null )
                    {
                        pathElements.add( 0, taxon );
                        taxon = taxon.getParentTaxon();
                    }
                    final TreePath path = new TreePath( pathElements.toArray() );
                    tree.makeVisible( path );
                    tree.scrollPathToVisible( path );
                    tree.setSelectionPath( path );
                    tree.requestFocus();
                }
            }
        } );
        tree.addMouseListener( new MouseAdapter()
        {
            public void mouseClicked( final MouseEvent e )
            {
                setStates( tree );
            }
        } );
        tree.addKeyListener( new KeyAdapter()
        {
            public void keyPressed( final KeyEvent e )
            {
                setStates( tree );
            }
        } );
        final ActionCommandInterceptor dirtyInterceptor = new ActionCommandInterceptor()
        {
            public boolean beforeExecute( final ActionCommand command )
            {
                return true;
            }

            public void afterExecute( final ActionCommand command )
            {
                if ( !isAdjusting )
                {
                    isAdjusting = true;
                    setDirty();
                    setStates( null );
                    isAdjusting = false;
                }
            }
        };
        add.addInterceptor( dirtyInterceptor );
        delete.addInterceptor( dirtyInterceptor );
        name.getDocument().addDocumentListener( new SimpleDocumentListener()
        {
            public void changedUpdate( final DocumentEvent e )
            {
                if ( !isAdjusting )
                {
                    isAdjusting = true;
                    final GroupModel model = getCurrentGroupModel();
                    final String text = name.getText().trim();
                    if ( model != null && text != null && !text.equals( model.getName() ) )
                    {
                        model.setName( text );
                        setDirty();
                        setStates( name );
                    }
                    isAdjusting = false;
                }
            }
        } );
        minimum.addChangeListener( new ChangeListener()
        {
            public void stateChanged( final ChangeEvent e )
            {
                if ( !isAdjusting )
                {
                    isAdjusting = true;
                    final GroupModel model = getCurrentGroupModel();
                    if ( model != null )
                    {
                        final Integer value = (Integer) minimum.getValue();
                        model.setMinimum( value.intValue() );
                        setDirty();
                        setStates( minimum );
                    }
                    isAdjusting = false;
                }
            }
        } );
        maximum.addChangeListener( new ChangeListener()
        {
            public void stateChanged( final ChangeEvent e )
            {
                if ( !isAdjusting )
                {
                    isAdjusting = true;
                    final GroupModel model = getCurrentGroupModel();
                    if ( model != null )
                    {
                        final Integer value = (Integer) maximum.getValue();
                        model.setMaximum( value.intValue() );
                        setDirty();
                        setStates( maximum );
                    }
                    isAdjusting = false;
                }
            }
        } );
    }

    //--- DetailsBuilder implementations

    public void setEnabled( final boolean enabled )
    {
        if ( !isAdjusting )
        {
            isAdjusting = true;
            enabledByMaster = enabled;
            final GroupsModel model = getGroupsModel();
            if ( model != null )
            {
                model.setCurrentGroup( null );
            }
            setStates( null );
            isAdjusting = false;
        }
    }

    //--- AbstractDetailsBuilder overrides

    public void setModel( final TaxonBased taxonBased )
    {
        if ( !isAdjusting )
        {
            isAdjusting = true;
            super.setModel( taxonBased );
            final GroupsModel models = (GroupsModel) taxonBased;
            if ( models != null )
            {
                models.setCurrentGroup( null );
                listModel.clear();
                final ArrayList<GroupModel> groups = getGroupModels();
                for ( int i = 0; groups != null && i < groups.size(); i++ )
                {
                    final GroupModel model = groups.get( i );
                    listModel.addElement( model );
                }
            }
            else
            {
                listModel.clear();
            }
            setStates( null );
            isAdjusting = false;
        }
    }

    protected boolean shouldMigrate( final String uid )
    {
        final int result = Dialogs.showQuestionMessageCancel( name, "Migration",
                "Sie wollen den taxonomischen Baum, auf dem diese Guppeneinstellungen basieren, ändern.\n" +
                        "Es kann sein, dass nicht alle Einstellungen in den neuen Baum übernommen werden können.\n" +
                        "Möchten Sie diese Änderungen wirklich durchführen?" );
        return result == Dialogs.OK;
    }

    protected ArrayList findMigrationErrors( final String uid )
    {
        final ArrayList<String> errors = new ArrayList<String>();
        final TaxonTree tree = TaxonModels.find( uid );
        final ArrayList<GroupModel> groups = getGroupModels();
        for ( int i = 0; groups != null && i < groups.size(); i++ )
        {
            final GroupModel model = groups.get( i );
            final ArrayList taxa = model.getTaxa();
            for ( final Object aTaxa : taxa )
            {
                final String taxon = (String) aTaxa;
                if ( tree.findTaxonByName( taxon ) == null )
                {
                    errors.add( taxon );
                }
            }
        }
        setEnabled( tree != null );
        return errors;
    }

    protected void removeMigrationErrors( final ArrayList errors )
    {
        final GroupsModel models = getGroupsModel();
        for ( final Object error : errors )
        {
            final String taxon = (String) error;
            final GroupModel model = models.find( taxon );
            model.removeTaxon( taxon );
        }
    }

    protected void commitSuccessful()
    {
        Dialogs.showInfoMessage( name, "Migration", "Die Migration war ohne problemantische Fälle erfolgreich." );
    }

    //--- AbstractDetailsBuilder implementations

    protected void initComponents()
    {
        final JToolBar bar = getCommandManager().getGroup( Commands.GROUP_ID_TOOLBAR ).createToolBar();
        getCreator().getPanel( "panelToolbarSeparator" ).add( bar, new CellConstraints().xy( 3, 1 ) );

        initCommand( new DeleteGroup( getCommandManager(), getModels(), listModel ), false );
        initCommand( new NewGroup( getCommandManager(), getModels(), listModel ), false );

        listGroups = getCreator().getList( COMPONENT_LISTGROUPS );
        listGroups.setModel( groupModels );
        listGroups.setSelectionModel( groupsSelection );
        listGroups.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
        listGroups.setCellRenderer( groupsListRenderer );

        taxaListRenderer = new TaxaListRenderer( getModels() );
        listTaxa = getCreator().getList( COMPONENT_LISTTAXA );
        listTaxa.setSelectionModel( taxaSelection );
        listTaxa.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
        listTaxa.setCellRenderer( taxaListRenderer );

        tree = getCreator().getTree( COMPONENT_TREE );
        registerTree( tree );
        tree.setCellRenderer( groupsTreeRenderer );
        restorer = new TreeExpandedRestorer( tree );

        final TreeCheckboxController treeHandler = new TreeCheckboxController( tree )
        {
            protected void handleSelection( final TreePath path )
            {
                final GroupModel selected = (GroupModel) listGroups.getSelectedValue();
                if ( selected != null && path != null && enabledByMaster )
                {
                    final GroupsModel model = getGroupsModel();
                    final SimpleTaxon taxon = (SimpleTaxon) path.getLastPathComponent();
                    if ( SimpleTaxon.isSpecies( taxon ) )
                    {
                        final String name = taxon.getName();
                        final GroupModel taxonsGroup = model.find( name );
                        if ( taxonsGroup == null )
                        {
                            model.addTaxon( name );
                            final GroupModel current = getCurrentGroupModel();
                            final ArrayList<String> taxa = current.getTaxa();
                            final TaxonTree tree = getTaxonTree();
                            final ArrayList<String> sorted = tree.sortTaxaStrings( taxa );
                            current.setTaxa( sorted );
                        }
                        else if ( taxonsGroup == selected )
                        {
                            model.removeTaxon( name );
                        }
                        getTree().repaint();
                        restorer.save();
                        getTreeModel().reload();
                        restorer.restore();
                    }
                }
            }
        };
        tree.addMouseListener( treeHandler );
        tree.addKeyListener( treeHandler );

        name = getCreator().getTextField( COMPONENT_NAME );
        minimum = getCreator().getSpinner( COMPONENT_MINIMUM );
        minimum.setModel( new SpinnerNumberModel( 0, 0, null, 1 ) );
        maximum = getCreator().getSpinner( COMPONENT_MAXIMUM );
        maximum.setModel( new SpinnerNumberModel( 0, 0, null, 1 ) );

        delete = getCommandManager().getCommand( Commands.COMMAND_ID_DELETE_GROUP );
        add = getCommandManager().getCommand( Commands.COMMAND_ID_NEW_GROUP );
    }

    protected Converter getConverter()
    {
        if ( converter == null )
        {
            final HashMap<String, Class> aliases = new HashMap<String, Class>();
            aliases.put( "groupsModels", SimpleModelList.class );
            aliases.put( "groupsModel", GroupsModel.class );
            aliases.put( "groupModel", GroupModel.class );
            aliases.put( "taxon", String.class );

            final HashMap<Class, String> implicitCollections = new HashMap<Class, String>();
            implicitCollections.put( SimpleModelList.class, "models" );
            implicitCollections.put( GroupsModel.class, "groups" );

            final HashMap<Class, String> omits = new HashMap<Class, String>();
            omits.put( AbstractListModel.class, "listenerList" );

            converter = new XStreamConverter<SimpleModelList>( aliases, implicitCollections, omits);
        }
        return converter;
    }

    protected String getInfoString()
    {
        return "Gruppen-Editor";
    }

    protected String getModelId()
    {
        return MainModel.MODELID_GROUPS;
    }

    //--- Utilities

    private void setStates( final Object source )
    {
        setValueStates( source );
        setEnabledStates();
    }

    private void setEnabledStates()
    {
        final boolean isGroupModelSelected = enabledByMaster && listGroups.getSelectedValue() != null;
        groupsTreeRenderer.setEnabled( isGroupModelSelected );
        groupsListRenderer.setEnabled( enabledByMaster );
        taxaListRenderer.setEnabled( enabledByMaster );
        name.setEnabled( isGroupModelSelected );
        minimum.setEnabled( isGroupModelSelected );
        maximum.setEnabled( isGroupModelSelected );
        delete.setEnabled( isGroupModelSelected );
        add.setEnabled( enabledByMaster );
    }

    private void setValueStates( final Object source )
    {
        final GroupModel groupModel = getCurrentGroupModel();
        final ArrayList<String> taxa = groupModel == null ? DUMMY_LIST : groupModel.getTaxa();
        if ( source != listGroups )
        {
            groupsTreeRenderer.setGroupsModel( getGroupsModel() );
        }
        if ( source != listGroups )
        {
            listGroups.getSelectionModel().clearSelection();
        }
        if ( source != listGroups )
        {
            listGroups.setSelectedValue( groupModel, true );
        }
        if ( source != listTaxa )
        {
            listTaxa.setModel( new ArrayListModel( taxa ) );
        }
        if ( groupModel != null )
        {
            if ( source != name )
            {
                name.setText( groupModel.getName() );
            }
            if ( source != minimum )
            {
                minimum.setValue( new Integer( groupModel.getMinimum() ) );
            }
            if ( source != maximum )
            {
                maximum.setValue( new Integer( groupModel.getMaximum() ) );
            }
        }
        else
        {
            name.setText( null );
            minimum.setValue( ZERO );
            maximum.setValue( ZERO );
        }
        listGroups.repaint();
        listTaxa.repaint();
        tree.repaint();
    }

    private GroupsModel getGroupsModel()
    {
        return (GroupsModel) getModels().getSelection();
    }

    private GroupModel getCurrentGroupModel()
    {
        final GroupsModel models = getGroupsModel();
        return models == null ? null : models.getCurrentGroup();
    }

    private ArrayList<GroupModel> getGroupModels()
    {
        final GroupsModel models = getGroupsModel();
        return models == null ? null : models.getGroups();
    }

    public static void main( final String[] args ) throws UnsupportedLookAndFeelException
    {
        UIManager.setLookAndFeel( new Plastic3DLookAndFeel() );
        UIManager.put( "ToolBar.border", new EmptyBorder( 0, 0, 0, 0 ) );
        System.setProperty( "jfactory.resource.path", "/icon" );
        System.setProperty( "log4j.configuration", "log4j.properties" );
        Strings.setResourceBundle( ResourceBundle.getBundle( "ch.xmatrix.ups.uec.Strings" ) );
        final JFrame f = new JFrame();
        f.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        final JComponent panel = new GroupsBuilder().getPanel();
        panel.setBorder( Borders.createEmptyBorder( Sizes.DLUX8, Sizes.DLUX8, Sizes.DLUX8, Sizes.DLUX8 ) );
        f.getContentPane().add( panel );
        f.setSize( 600, 500 );
        f.setVisible( true );
    }
}
