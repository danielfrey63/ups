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
package ch.xmatrix.ups.uec.constraints;

import ch.jfactory.application.view.search.SearchableUtils;
import ch.jfactory.component.tree.TreeExpandedRestorer;
import ch.jfactory.convert.Converter;
import ch.jfactory.lang.LogicUtils;
import ch.jfactory.lang.ToStringComparator;
import ch.jfactory.model.SimpleModelList;
import ch.jfactory.xstream.XStreamConverter;
import ch.jfactory.component.TreeCheckboxController;
import ch.xmatrix.ups.domain.Constraint;
import ch.xmatrix.ups.domain.Constraints;
import ch.xmatrix.ups.domain.SimpleTaxon;
import ch.xmatrix.ups.domain.TaxonBased;
import ch.xmatrix.ups.model.TaxonModels;
import ch.xmatrix.ups.model.TaxonTree;
import ch.xmatrix.ups.uec.constraints.commands.AddTaxa;
import ch.xmatrix.ups.uec.constraints.commands.Commands;
import ch.xmatrix.ups.uec.constraints.commands.DeleteConstraints;
import ch.xmatrix.ups.uec.constraints.commands.NewConstraints;
import ch.xmatrix.ups.uec.constraints.commands.RemoveTaxa;
import ch.xmatrix.ups.uec.main.MainModel;
import ch.xmatrix.ups.uec.master.AbstractDetailsBuilder;
import ch.xmatrix.ups.view.renderer.ConstraintsTreeRenderer;
import com.jgoodies.binding.adapter.SingleListSelectionAdapter;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.Sizes;
import com.jgoodies.looks.plastic.Plastic3DLookAndFeel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.AbstractListModel;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.JTextComponent;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import org.apache.commons.lang.ArrayUtils;
import org.pietschy.command.ActionCommand;
import org.pietschy.command.ActionCommandInterceptor;

/**
 * @author Daniel Frey
 * @version $Revision: 1.9 $ $Date: 2007/05/16 17:00:15 $
 */
public class ConstraintsBuilder extends AbstractDetailsBuilder
{
    public static final String COMPONENT_FIELD_DESCRIPTION = "fieldDescription";

    public static final String COMPONENT_LIST_CONSTRAINTS = "listConstraints";

    public static final String COMPONENT_LIST_TAXA = "listTaxa";

    public static final String COMPONENT_FIELD_NAME = "fieldConstraintName";

    public static final String COMPONENT_SPINNER_COUNT = "spinnerCount";

    public static final String COMPONENT_TREE_TAXA = "treeTaxa";

    protected static final String RESOURCE_FORM = "/ch/xmatrix/ups/uec/constraints/ConstraintsPanel.jfd";

    protected static final String RESOURCE_MODEL = "/data/constraints.xml";

    private final SelectionInList constraintsList = new SelectionInList();

    private final SelectionInList taxaList = new SelectionInList();

    private final TreeSelectionModel treeSelection = new DefaultTreeSelectionModel();

    private final TaxaListRenderer taxaRenderer = new TaxaListRenderer();

    private final ConstraintsTreeRenderer treeRenderer = new ConstraintsTreeRenderer();

    private final ConstraintsListRenderer constraintsRenderer = new ConstraintsListRenderer();

    private JList listConstraints;

    private JList taxa;

    private JTextComponent name;

    private JTextComponent description;

    private JSpinner count;

    private JTree tree;

    private RemoveTaxa remove;

    private ActionCommand add;

    private ActionCommand nev;

    private DeleteConstraints delete;

    private TreeExpandedRestorer restorer;

    private Converter<SimpleModelList> converter;

    private boolean enabledByMaster;

    private boolean isAdjusting;

    public ConstraintsBuilder()
    {
        super( new ConstraintsFactory(), RESOURCE_MODEL, RESOURCE_FORM, 30 );
    }

    //--- ActionCommandPanelBuilder overrides

    protected void initComponentListeners()
    {
        listConstraints.addListSelectionListener( new ListSelectionListener()
        {
            public void valueChanged( final ListSelectionEvent e )
            {
                if ( !e.getValueIsAdjusting() && !isAdjusting )
                {
                    isAdjusting = true;
                    final Constraints constraints = getConstraints();
                    final Constraint constraint = (Constraint) listConstraints.getSelectedValue();
                    constraints.setCurrent( constraint );
                    setStates( listConstraints );
                    isAdjusting = false;
                }
            }
        } );
        SearchableUtils.installSearchable( listConstraints );
        taxa.addListSelectionListener( new ListSelectionListener()
        {
            public void valueChanged( final ListSelectionEvent e )
            {
                if ( !isAdjusting )
                {
                    isAdjusting = true;
                    setStates( taxa );
                    isAdjusting = false;
                }
            }
        } );
        taxa.addMouseListener( new MouseAdapter()
        {
            public void mouseClicked( final MouseEvent e )
            {
                if ( e.getClickCount() == 2 )
                {
                    final String taxonName = (String) taxa.getSelectedValue();
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
        SearchableUtils.installSearchable( taxa );
        final TreeCheckboxController handler = new TreeCheckboxController( tree )
        {
            protected void handleSelection( final TreePath path )
            {
                if ( !isAdjusting )
                {
                    isAdjusting = true;
                    final Constraints constraints = getConstraints();
                    if ( constraints != null && path != null && !constraints.isFixed() )
                    {
                        final SimpleTaxon taxon = (SimpleTaxon) path.getLastPathComponent();
                        final boolean isSpecies = SimpleTaxon.isSpecies( taxon );
                        if ( isSpecies )
                        {
                            final String[] defaultTaxa = constraints.getDefaultTaxa();
                            final String name = taxon.getName();
                            final Constraint constraint = constraints.findConstraint( name );
                            final boolean isObligateConstraint = constraint != null && constraint.getTaxa().size() == 1 && isSpecies;
                            if ( !isObligateConstraint )
                            {
                                String[] newDefaults = null;
                                if ( ArrayUtils.contains( defaultTaxa, name ) )
                                {
                                    newDefaults = (String[]) ArrayUtils.remove( defaultTaxa, ArrayUtils.indexOf( defaultTaxa, name ) );
                                }
                                else
                                {
                                    newDefaults = (String[]) ArrayUtils.add( defaultTaxa, name );
                                }
                                constraints.setDefaultTaxa( newDefaults );
                                setDirty();
                            }
                        }
                        setStates( getTree() );
                    }
                    isAdjusting = false;
                }
            }
        };
        tree.addMouseListener( handler );
        tree.addKeyListener( handler );
        tree.addTreeSelectionListener( new TreeSelectionListener()
        {
            public void valueChanged( final TreeSelectionEvent e )
            {
                if ( !isAdjusting )
                {
                    isAdjusting = true;
                    setStates( tree );
                    isAdjusting = false;
                }
            }
        } );
        SearchableUtils.installSearchable( tree ).setRecursive( true );
        name.addCaretListener( new CaretListener()
        {
            public void caretUpdate( final CaretEvent e )
            {
                if ( !isAdjusting )
                {
                    isAdjusting = true;
                    final Constraint constraint = getCurrentConstraint();
                    if ( constraint != null )
                    {
                        final String old = constraint.getName();
                        final String text = name.getText();
                        if ( ( old == null && text != null ) || !old.equals( text ) )
                        {
                            constraint.setName( text );
                            setDirty();
                            setStates( name );
                        }
                    }
                    isAdjusting = false;
                }
            }
        } );
        description.addCaretListener( new CaretListener()
        {
            public void caretUpdate( final CaretEvent e )
            {
                final String text = description.getText();
                final Constraints constraints = getConstraints();
                if ( constraints != null )
                {
                    final String old = constraints.getDescription();
                    if ( !isAdjusting )
                    {
                        isAdjusting = true;
                        if ( ( ( old == null && !"".equals( text ) ) || ( old != null && !old.equals( text ) ) ) )
                        {
                            constraints.setDescription( text );
                            setDirty();
                            setStates( description );
                        }
                        isAdjusting = false;
                    }
                }
            }
        } );
        count.addChangeListener( new ChangeListener()
        {
            public void stateChanged( final ChangeEvent e )
            {
                final Integer value = (Integer) count.getValue();
                final Constraint constraint = getCurrentConstraint();
                if ( constraint != null )
                {
                    final int old = constraint.getMinimalCount();
                    final int newValue = value.intValue();
                    if ( old != newValue )
                    {
                        constraint.setMinimalCount( newValue );
                        setDirty();
                        setStates( count );
                    }
                }
            }
        } );
        final ActionCommandInterceptor addRemoveInterceptor = new ActionCommandInterceptor()
        {
            public boolean beforeExecute( final ActionCommand command )
            {
                return true;
            }

            public void afterExecute( final ActionCommand command )
            {
                final Constraint constraint = getCurrentConstraint();
                final List<String> taxa = constraint.getTaxa();
                if ( taxa != null && taxa.size() == 1 )
                {
                    final String taxon = taxa.get( 0 );
                    constraint.setName( taxon );
                    final TaxonTree tree = getTaxonTree();
                    if ( SimpleTaxon.isSpecies( tree.findTaxonByName( taxon ) ) )
                    {
                        constraint.setMinimalCount( 1 );
                        setDirty();
                    }
                }
            }
        };
        add.addInterceptor( addRemoveInterceptor );
        add.addInterceptor( new ActionCommandInterceptor()
        {
            public boolean beforeExecute( final ActionCommand command )
            {
                return true;
            }

            public void afterExecute( final ActionCommand command )
            {
                tree.clearSelection();
                setDirty();
                setStates( add );
            }
        } );
        remove.addInterceptor( addRemoveInterceptor );
        remove.addInterceptor( new ActionCommandInterceptor()
        {
            public boolean beforeExecute( final ActionCommand command )
            {
                return true;
            }

            public void afterExecute( final ActionCommand command )
            {
                setDirty();
                setStates( remove );
            }
        } );
        nev.addInterceptor( new ActionCommandInterceptor()
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
                    setStates( nev );
                    isAdjusting = false;
                }
            }
        } );
        delete.addInterceptor( new ActionCommandInterceptor()
        {
            public boolean beforeExecute( final ActionCommand command )
            {
                return true;
            }

            public void afterExecute( final ActionCommand command )
            {
                final Constraints constraints = getConstraints();
                taxaList.setList( null );
                constraintsList.setList( null );
                constraintsList.setList( constraints.getConstraints() );
                setDirty();
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
            setStates( null );
            isAdjusting = false;
        }
    }

    //--- AbstractDetailsBuilder overrides

    public void setModel( final TaxonBased model )
    {
        super.setModel( model );
        if ( !isAdjusting )
        {
            isAdjusting = true;
            if ( model != null )
            {
                final Constraints constraintsModel = (Constraints) model;
                final ArrayList<Constraint> constraintModels = constraintsModel.getConstraints();
                if ( constraintModels != null )
                {
                    Collections.sort( constraintModels, new ToStringComparator() );
                }
                constraintsList.setList( constraintModels );
            }
            setStates( null );
            isAdjusting = false;
        }
    }

    //--- AbstractDetailsBuilder implementations

    protected ArrayList findMigrationErrors( final String uid )
    {
        final TaxonTree tree = TaxonModels.find( uid );
        final Constraints constraints = getConstraints();
        final ArrayList<Constraint> errors = new ArrayList<Constraint>();
        final ArrayList<Constraint> constraintList = constraints.getConstraints();
        for ( final Constraint constraint : constraintList )
        {
            final ConstraintError constraintError = new ConstraintError( constraint );
            final List<String> taxa = constraintError.getTaxa();
            for ( final Iterator<String> iterator = taxa.iterator(); iterator.hasNext(); )
            {
                final String taxon = iterator.next();
                if ( tree.findTaxonByName( taxon ) != null )
                {
                    iterator.remove();
                }
            }
            if ( taxa.size() > 0 )
            {
                errors.add( constraintError );
            }
        }
        return errors;
    }

    protected void removeMigrationErrors( final ArrayList errors )
    {
        final ArrayList<ConstraintError> constraints = (ArrayList<ConstraintError>) errors;
        for ( final ConstraintError constraintError : constraints )
        {
            final List<String> unknownTaxa = constraintError.getTaxa();
            final Constraint constraint = constraintError.getOriginal();
            final List<String> taxa = constraint.getTaxa();
            for ( final String taxon : unknownTaxa )
            {
                taxa.remove( taxon );
            }
        }
    }

    protected void initComponents()
    {
        nev = initCommand( new NewConstraints( getCommandManager(), getModels() ), false );
        delete = (DeleteConstraints) initCommand( new DeleteConstraints( getCommandManager(), getModels() ), false );
        add = initCommand( new AddTaxa( getCommandManager(), getModels(), treeSelection ), false );
        remove = (RemoveTaxa) initCommand( new RemoveTaxa( getCommandManager(), getModels() ), false );

        final JToolBar bar = getCommandManager().getGroup( Commands.GROUP_ID_TOOLBAR ).createToolBar();
        final JPanel separator = getCreator().getPanel( "panelSeparator" );
        separator.add( bar, new CellConstraints().xy( 3, 1 ) );

        description = getCreator().getTextArea( COMPONENT_FIELD_DESCRIPTION );

        listConstraints = getCreator().getList( COMPONENT_LIST_CONSTRAINTS );
        listConstraints.setModel( constraintsList );
        listConstraints.setCellRenderer( constraintsRenderer );
        listConstraints.setSelectionModel( new SingleListSelectionAdapter( constraintsList.getSelectionIndexHolder() ) );
        delete.setList( listConstraints );

        taxa = getCreator().getList( COMPONENT_LIST_TAXA );
        taxa.setSelectionModel( new SingleListSelectionAdapter( taxaList.getSelectionIndexHolder() ) );
        taxa.setModel( taxaList );
        taxa.setCellRenderer( taxaRenderer );

        remove.setList( taxa );
        name = getCreator().getTextField( COMPONENT_FIELD_NAME );
        count = getCreator().getSpinner( COMPONENT_SPINNER_COUNT );
        count.setModel( new SpinnerNumberModel( 0, 0, null, 1 ) );

        tree = getCreator().getTree( COMPONENT_TREE_TAXA );
        registerTree( tree );
        tree.setCellRenderer( treeRenderer );
        tree.setSelectionModel( treeSelection );
        treeSelection.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
        restorer = new TreeExpandedRestorer( tree );
    }

    protected Converter<SimpleModelList> getConverter()
    {
        if ( converter == null )
        {
            final Map<String, Class> aliases = new HashMap<String, Class>();
            aliases.put( "constraintsModels", SimpleModelList.class );
            aliases.put( "constraintsModel", Constraints.class );
            aliases.put( "constraintModel", Constraint.class );

            final Map<Class, String> implicitCollections = new HashMap<Class, String>();
            implicitCollections.put( SimpleModelList.class, "models" );
            implicitCollections.put( Constraints.class, "constraints" );
            implicitCollections.put( Constraint.class, "taxa" );

            final Map<Class, String> omits = new HashMap<Class, String>();
            omits.put( AbstractListModel.class, "listenerList" );

            final Map<String, XStreamConverter.NamedAlias> namedAliases = new HashMap<String, XStreamConverter.NamedAlias>();
            namedAliases.put( "index", new XStreamConverter.NamedAlias( Constraints.class, "indexToConstraints" ) );

            converter = new XStreamConverter<SimpleModelList>( aliases, implicitCollections, new HashMap<Class, String>(), namedAliases );
        }
        return converter;
    }

    protected String getInfoString()
    {
        return "Vorgaben-Editor";
    }

    protected String getModelId()
    {
        return MainModel.MODELID_CONSTRAINTS;
    }

    //--- Utilities

    private void setStates( final Object source )
    {
        setValueStates( source );
        setEnabledStates();
    }

    /**
     * Details enabled states. The list of input states used to determine the component states is:
     *
     * <ul>
     *
     * <li>eb: the enabled-by-master flag is set (1) or not (0).</li>
     *
     * <li>fr: the fixed flag ist set (0) or not (1).</li>
     *
     * <li>co: a constraint is selected (1) or none is selected (0).</li>
     *
     * <li>hc: there are constraints (1) or not (0).</li>
     *
     * <li>si: more than one constraint is selected in the constraints list (0) or just one (1).</li>
     *
     * <li>se: selected taxon in tree is involved in a constraint (1) or not (0).</li>
     *
     * <li>mu: constraint has more than one taxon (1) or just one (0).</li>
     *
     * <li>ta: at least one taxon is selected in the taxon list (1) or non is selected (0).</li>
     *
     * <li>tr: at least one taxon is selected in the taxon tree (1) or non is selected (0).</li>
     *
     * <li>hi: the single taxon of the currently selected constraint is of higer taxonomic level than a species (1) or not (0).</li>
     *
     * </ul>
     *
     * The list of components affected by the input states are:
     *
     * <ul>
     *
     * <li>ds: the description field is enabled (1) or disabled (0).</li>
     *
     * <li>co: the constraints list is enabled (1) or disabled (0).</li>
     *
     * <li>ta: the taxon list for a constraint is enabled (1) or disabled (0).</li>
     *
     * <li>tr: the taxon renderer is enabled (1) or disabled (0).</li>
     *
     * <li>na: the name field is enabled (1) or disabled (0).</li>
     *
     * <li>ct: the count spinner is enabled (1) or disabled (0).</li>
     *
     * <li>ne: the new-constraint button is enabled (1) or disabled (0).</li>
     *
     * <li>de: the delete-constraints button is enabled (1) or disabled (0).</li>
     *
     * <li>ad: the add-taxon button is enabled (1) or disabled (0).</li>
     *
     * <li>re: the remove-taxa button is enabled (1) or disabled (0).</li>
     *
     * </ul>
     *
     * States matrix for enabled states of components:
     *
     * <pre>
     * +----+--- -------------------------------------------+--------------+
     * | se |       0000 0000 0000 0000 0000 1111 0000 1111 | high         |
     * | ta |       0000 0000 1111 1111 0000 0000 1111 1111 |              |
     * | tr |       0000 1111 0000 1111 0000 1111 0000 1111 |              |
     * | mu |       0011 0011 0011 0011 0011 0011 0011 0011 |              |
     * | hi |       0101 0101 0101 0101 0101 0101 0101 0101 |              |
     * | si |     0 1111 1111 1111 1111 1111 1111 1111 1111 |              |
     * | co |    01 1111 1111 1111 1111 1111 1111 1111 1111 |              |
     * | hc |   011 1111 1111 1111 1111 1111 1111 1111 1111 |              |
     * | fr |  0111 1111 1111 1111 1111 1111 1111 1111 1111 |              |
     * | eb | 01111 1111 1111 1111 1111 1111 1111 1111 1111 | low          |
     * +----+-----------------------------------------------+--------------+
     * | ds | 00111 1111 1111 1111 1111 1111 1111 1111 1111 | 0x1FFFFFFFFC |
     * | tr | 00111 1111 1111 1111 1111 1111 1111 1111 1111 | 0x1FFFFFFFFC |
     * | ne | 00111 1111 1111 1111 1111 1111 1111 1111 1111 | 0x1FFFFFFFFC |
     * | de | 00001 1111 1111 1111 1111 1111 1111 1111 1111 | 0x1FFFFFFFF0 |
     * | na | 00000 0011 0011 0011 0011 0011 0011 0011 0011 | 0x1999999980 |
     * | ct | 00000 0111 0111 0111 0111 0111 0111 0111 0111 | 0x1DDDDDDDC0 |
     * | ad | 00000 0000 1111 0000 1111 0000 0000 0000 0000 | 0x00001E1E00 |
     * | re | 00000 0000 0000 1111 1111 0000 0000 1111 1111 | 0x1FE01FE000 |
     * +----+-----------------------------------------------+--------------+
     *        &lt; low                                  high &gt;
     * </pre>
     */
    private void setEnabledStates()
    {
        final Constraints constraints = getConstraints();
        final Constraint constraint = getCurrentConstraint();
        final List<String> taxons = ( constraint != null ? constraint.getTaxa() : null );
        final TaxonTree taxonTree = getTaxonTree();
        final TreePath path = tree.getSelectionPath();
        final SimpleTaxon selected = (SimpleTaxon) ( path == null ? null : path.getLastPathComponent() );

        final boolean se = !( selected == null || constraints == null ) && constraints.findConstraint( selected.getName() ) != null;
        final boolean ta = taxa.getSelectedIndices() != null && taxa.getSelectedIndices().length > 0;
        final boolean tr = tree.getSelectionPaths() != null && tree.getSelectionPaths().length > 0;
        final boolean mu = constraint != null && constraint.getTaxa() != null && constraint.getTaxa().size() > 1;
        final boolean hi = taxons != null && taxons.size() == 1 && !SimpleTaxon.isSpecies( taxonTree.findTaxonByName( taxons.get( 0 ) ) );
        final boolean si = listConstraints.getSelectedIndices().length == 1;
        final boolean co = listConstraints.getSelectedValues().length >= 1;
        final boolean hc = constraints != null && constraints.getConstraints() != null && constraints.getConstraints().size() > 0;
        final boolean fr = constraints != null && !constraints.isFixed();
        final boolean eb = enabledByMaster;

        final LogicUtils.InfoBit[] bits = new LogicUtils.InfoBit[10];
        bits[0] = new LogicUtils.InfoBit( se, "taxon selected in tree is used in constraints", "taxon selected in tree is not used in constraints" );
        bits[1] = new LogicUtils.InfoBit( ta, "current constraint with one or more taxa", "current constraint without taxa" );
        bits[2] = new LogicUtils.InfoBit( tr, "tree has a selection", "tree has no selection" );
        bits[3] = new LogicUtils.InfoBit( mu, "current constraint has more than one taxon", "current constraint has exacly one taxon" );
        bits[4] = new LogicUtils.InfoBit( hi, "current constraints single taxon is of higher level than species", "current constraint has more than one taxon or the only on is not a species" );
        bits[5] = new LogicUtils.InfoBit( si, "one single constraint is selected in the constraints list", "more than one constraint is selected in the constraints list" );
        bits[6] = new LogicUtils.InfoBit( co, "constraints list with one selected constraint", "constraints list with several or no selected constraints" );
        bits[7] = new LogicUtils.InfoBit( hc, "constraints has at least one constraint", "constraints has no constraints" );
        bits[8] = new LogicUtils.InfoBit( fr, "the configuration is editable (free)", "the configuration is fixed" );
        bits[9] = new LogicUtils.InfoBit( eb, "the details are enabled by master", "the details are disabled by master" );

        final Object[] components = {description, treeRenderer, nev, delete, name, count, add, remove};
        final long[] componentMapps = {
                0x1FFFFFFFFCL, 0x1FFFFFFFFCL, 0x1FFFFFFFFCL, 0x1FFFFFFFF0L,
                0x1999999980L, 0x1DDDDDDDC0L, 0x00001E1E00L, 0x1FE01FE000L};
        final int split = 5;
        LogicUtils.setEnabledStates( bits, components, componentMapps, split );
    }

    private void setValueStates( final Object source )
    {
        constraintsRenderer.setEnabled( enabledByMaster );
        taxaRenderer.setEnabled( enabledByMaster );
        final Constraints constraints = getConstraints();
        final Constraint constraint = getCurrentConstraint();
        final String[] defaultTaxa = constraints == null ? null : constraints.getDefaultTaxa();
        if ( source != constraintsRenderer )
        {
            constraintsRenderer.setConstraints( constraints );
        }
        if ( source != listConstraints )
        {
            constraintsList.setSelection( constraint );
        }
        if ( source != listConstraints )
        {
            listConstraints.setModel( DUMMY_MODEL );
        }
        if ( source != listConstraints )
        {
            listConstraints.setModel( constraintsList );
        }
        if ( source != treeRenderer )
        {
            treeRenderer.setConstraints( constraints );
        }
        if ( source != treeRenderer )
        {
            treeRenderer.setTaxa( defaultTaxa == null ? DUMMY_LIST : new ArrayList<String>( Arrays.asList( defaultTaxa ) ) );
        }
        if ( source != taxaRenderer )
        {
            taxaRenderer.setConstraints( constraints );
        }
        if ( constraints != null )
        {
            restorer.save();
            final Object selection = taxaList.getSelection();
            if ( source != description )
            {
                description.setText( constraints.getDescription() );
            }
            if ( source != taxa )
            {
                taxaList.setList( DUMMY_LIST );
            }
            if ( constraint != null )
            {
                final List<String> taxaArray = constraint.getTaxa();
                if ( source != name )
                {
                    name.setText( constraint.getName() );
                }
                if ( source != count )
                {
                    count.setValue( constraint.getMinimalCount() );
                }
                listConstraints.setSelectedValue( constraint, true );
                taxaList.setList( taxaArray );
                taxa.setModel( DUMMY_MODEL );
                taxa.setModel( taxaList );
            }
            else
            {
                if ( source != name )
                {
                    name.setText( "" );
                }
                if ( source != count )
                {
                    count.setValue( ZERO );
                }
                if ( source != tree )
                {
                    tree.clearSelection();
                }
            }
            taxa.setSelectedValue( selection, true );
            getTreeModel().reload();
            restorer.restore();
        }
        else
        {
            if ( source != description )
            {
                description.setText( "" );
            }
            if ( source != name )
            {
                name.setText( "" );
            }
            if ( source != count )
            {
                count.setValue( ZERO );
            }
            if ( source != taxa )
            {
                taxa.setModel( DUMMY_MODEL );
            }
            if ( source != listConstraints )
            {
                listConstraints.setModel( DUMMY_MODEL );
            }
            if ( source != tree )
            {
                tree.clearSelection();
            }
        }
        tree.repaint();
        taxa.repaint();
        listConstraints.repaint();
    }

    private Constraints getConstraints()
    {
        return (Constraints) getModels().getSelection();
    }

    private Constraint getCurrentConstraint()
    {
        final Constraints constraints = getConstraints();
        return constraints == null ? null : constraints.getCurrent();
    }

    private static class ConstraintError extends Constraint
    {
        private final Constraint original;

        public ConstraintError( final Constraint original )
        {
            super( original );
            this.original = original;
        }

        public Constraint getOriginal()
        {
            return original;
        }
    }

    public static void main( final String[] args ) throws UnsupportedLookAndFeelException
    {
        UIManager.setLookAndFeel( new Plastic3DLookAndFeel() );
        UIManager.put( "ToolBar.border", new EmptyBorder( 0, 0, 0, 0 ) );
        System.setProperty( "log4j.configuration", "/log4j.properties" );
        System.setProperty( "xmatrix.resource.path", "/icon" );
        System.setProperty( "jfactory.strings.resource", "ch.xmatrix.ups.uec.Strings" );
        final JFrame f = new JFrame();
        f.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        final JComponent panel = new ConstraintsBuilder().getPanel();
        panel.setBorder( Borders.createEmptyBorder( Sizes.DLUX8, Sizes.DLUX8, Sizes.DLUX8, Sizes.DLUX8 ) );
        f.getContentPane().add( panel );
        f.setSize( 650, 500 );
        f.setVisible( true );
    }
}
