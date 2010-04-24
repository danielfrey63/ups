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
package ch.xmatrix.ups.uec.specimens;

import ch.jfactory.component.SimpleDocumentListener;
import ch.jfactory.component.tree.TreeExpandedRestorer;
import ch.jfactory.convert.Converter;
import ch.jfactory.model.SimpleModelList;
import ch.jfactory.xstream.XStreamConverter;
import ch.xmatrix.ups.domain.SimpleTaxon;
import ch.xmatrix.ups.domain.TaxonBased;
import ch.xmatrix.ups.model.SpecimenModel;
import ch.xmatrix.ups.model.SpecimensModel;
import ch.xmatrix.ups.uec.main.MainModel;
import ch.xmatrix.ups.uec.master.AbstractDetailsBuilder;
import ch.xmatrix.ups.uec.specimens.commands.Commands;
import ch.xmatrix.ups.uec.specimens.commands.DeleteSpecimen;
import ch.xmatrix.ups.uec.specimens.commands.NewSpecimen;
import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.Sizes;
import com.jgoodies.looks.plastic.Plastic3DLookAndFeel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.AbstractListModel;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import org.pietschy.command.ActionCommand;
import org.pietschy.command.ActionCommandInterceptor;

/**
 * @author Daniel Frey
 * @version $Revision: 1.6 $ $Date: 2008/01/23 22:19:08 $
 */
public class SpecimensBuilder extends AbstractDetailsBuilder
{
    private static final String RESOURCE_FORM = "/ch/xmatrix/ups/uec/specimens/SpecimensPanel.jfd";

    private static final String RESOURCE_MODEL = "/data/specimens.xml";

    private final SpecimensTreeRenderer renderer = new SpecimensTreeRenderer();

    private final TreeSelectionModel selectionModel = new DefaultTreeSelectionModel();

    private JTree tree;

    private JTextField id;

    private JSpinner count;

    private JSpinner known;

    private JSpinner unknown;

    private JCheckBox knownDisabled;

    private JCheckBox unknownDisabled;

    private Converter converter;

    private boolean enabledByMaster;

    private boolean isAdjusting;

    private TreeExpandedRestorer saver;

    private JPanel edit;

    private ActionCommand deleteCommand;

    private ActionCommand newCommand;

    private JCheckBox backup;

    public SpecimensBuilder()
    {
        super( new SpecimensFactory(), RESOURCE_MODEL, RESOURCE_FORM, 30 );
    }

    //--- ActionCommandPanelBuilder overrides

    protected void initComponentListeners()
    {
        tree.addTreeSelectionListener( new TreeSelectionListener()
        {
            public void valueChanged( final TreeSelectionEvent e )
            {
                if ( !isAdjusting )
                {
                    isAdjusting = true;
                    final TreePath path = e.getPath();
                    final SpecimensModel models = getSpecimenModels();
                    if ( path != null && models != null )
                    {
                        final SimpleTaxon taxon = (SimpleTaxon) path.getLastPathComponent();
                        final SpecimenModel model = models.find( taxon.getName() );
                        models.setCurrent( model );
                        setStates( tree );
                    }
                    isAdjusting = false;
                }
            }
        } );
        id.getDocument().addDocumentListener( new SimpleDocumentListener()
        {
            public void changedUpdate( final DocumentEvent e )
            {
                if ( !isAdjusting )
                {
                    isAdjusting = true;
                    final SpecimenModel model = getSpecimenModel();
                    final String text = id.getText().trim();
                    if ( model != null && !"".equals( text ) && !text.equals( model.getId() ) )
                    {
                        model.setId( text );
                        setDirty();
                        setStates( id );
                    }
                    isAdjusting = false;
                }
            }
        } );
        count.addChangeListener( new ChangeListener()
        {
            public void stateChanged( final ChangeEvent e )
            {
                if ( !isAdjusting )
                {
                    isAdjusting = true;
                    final SpecimenModel model = getSpecimenModel();
                    if ( model != null )
                    {
                        final int c = ( (Integer) count.getValue() ).intValue();
                        model.setNumberOfSpecimens( c );
                        if ( c == 0 )
                        {
                            model.setDeactivatedIfKnown( true );
                            model.setDeactivatedIfUnknown( true );
                        }
                        setDirty();
                        setStates( count );
                    }
                    isAdjusting = false;
                }
            }
        } );
        backup.addChangeListener( new ChangeListener()
        {
            public void stateChanged( final ChangeEvent e )
            {
                if ( !isAdjusting )
                {
                    isAdjusting = true;
                    final SpecimenModel model = getSpecimenModel();
                    if ( model != null && backup.isSelected() != model.isBackup() )
                    {
                        setDirty();
                        model.setBackup( backup.isSelected() );
                    }
                    isAdjusting = false;
                }
            }
        } );
        known.addChangeListener( new ChangeListener()
        {
            public void stateChanged( final ChangeEvent e )
            {
                if ( !isAdjusting )
                {
                    isAdjusting = true;
                    final SpecimenModel model = getSpecimenModel();
                    if ( model != null )
                    {
                        model.setWeightIfKnown( ( (Integer) known.getValue() ).intValue() );
                        setDirty();
                        setStates( known );
                    }
                    isAdjusting = false;
                }
            }
        } );
        unknown.addChangeListener( new ChangeListener()
        {
            public void stateChanged( final ChangeEvent e )
            {
                if ( !isAdjusting )
                {
                    isAdjusting = true;
                    final SpecimenModel model = getSpecimenModel();
                    if ( model != null )
                    {
                        model.setWeightIfUnknown( ( (Integer) unknown.getValue() ).intValue() );
                        setDirty();
                        setStates( unknown );
                    }
                    isAdjusting = false;
                }
            }
        } );
        knownDisabled.addActionListener( new ActionListener()
        {
            public void actionPerformed( final ActionEvent e )
            {
                if ( !isAdjusting )
                {
                    isAdjusting = true;
                    final SpecimenModel model = getSpecimenModel();
                    if ( model != null )
                    {
                        model.setDeactivatedIfKnown( knownDisabled.isSelected() );
                        setDirty();
                        setStates( knownDisabled );
                    }
                    isAdjusting = false;
                }
            }
        } );
        unknownDisabled.addActionListener( new ActionListener()
        {
            public void actionPerformed( final ActionEvent e )
            {
                if ( !isAdjusting )
                {
                    isAdjusting = true;
                    final SpecimenModel model = getSpecimenModel();
                    if ( model != null )
                    {
                        model.setDeactivatedIfUnknown( unknownDisabled.isSelected() );
                        setDirty();
                        setStates( unknownDisabled );
                    }
                    isAdjusting = false;
                }
            }
        } );
        final ActionCommandInterceptor interceptor = new ActionCommandInterceptor()
        {
            public boolean beforeExecute( final ActionCommand command )
            {
                return true;
            }

            public void afterExecute( final ActionCommand command )
            {
                setDirty();
                setStates( null );
            }
        };
        newCommand.addInterceptor( interceptor );
        deleteCommand.addInterceptor( interceptor );
    }

    private SpecimenModel getSpecimenModel()
    {
        final SpecimensModel models = getSpecimenModels();
        return models == null ? null : models.getCurrent();
    }

    //--- DetailsBulider implementations.

    public void setModel( final TaxonBased taxonBased )
    {
        super.setModel( taxonBased );
        if ( !isAdjusting )
        {
            isAdjusting = true;
            final SpecimensModel models = (SpecimensModel) taxonBased;
            renderer.setSpecimensModel( models );
            setStates( null );
            isAdjusting = false;
        }
    }

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

    //-- AbstractDetailsBuilder implementations.

    protected void initComponents()
    {
        newCommand = initCommand( new NewSpecimen( getCommandManager(), getModels(), selectionModel ) );
        deleteCommand = initCommand( new DeleteSpecimen( getCommandManager(), getModels() ) );

        tree = getCreator().getTree( "tree" );
        registerTree( tree );
        tree.setCellRenderer( renderer );
        tree.setSelectionModel( selectionModel );
        saver = new TreeExpandedRestorer( tree );
        id = getCreator().getTextField( "fieldId" );
        count = getCreator().getSpinner( "spinnerCount" );
        count.setModel( new SpinnerNumberModel( 0, 0, null, 1 ) );
        known = getCreator().getSpinner( "spinnerKnown" );
        known.setModel( new SpinnerNumberModel( 0, 0, null, 1 ) );
        unknown = getCreator().getSpinner( "spinnerUnknown" );
        unknown.setModel( new SpinnerNumberModel( 0, 0, null, 1 ) );
        knownDisabled = getCreator().getCheckBox( "checkKnown" );
        unknownDisabled = getCreator().getCheckBox( "checkUnknown" );
        backup = getCreator().getCheckBox( "checkBackup" );
        edit = getCreator().getPanel( "panelEdit" );
        final JToolBar bar = getCommandManager().getGroup( Commands.GROUP_ID_TOOLBAR ).createToolBar( Commands.GROUP_ID_TOOLBAR );
        getCreator().getPanel( "panelToolbar" ).add( bar, new CellConstraints().xy( 3, 1 ) );
    }

    protected Converter getConverter()
    {
        if ( converter == null )
        {
            final Map<String, Class> aliases = new HashMap<String, Class>();
            aliases.put( "specimensModels", SimpleModelList.class );
            aliases.put( "specimensModel", SpecimensModel.class );
            aliases.put( "specimenModel", SpecimenModel.class );

            final Map<Class, String> implicitCollections = new HashMap<Class, String>();
            implicitCollections.put( SimpleModelList.class, "models" );

            final Map<Class, String> omits = new HashMap<Class, String>();
            omits.put( AbstractListModel.class, "listenerList" );

            converter = new XStreamConverter( aliases, implicitCollections, omits);
        }
        return converter;
    }

    protected String getInfoString()
    {
        return "Herbarbeleg-Editor";
    }

    protected String getModelId()
    {
        return MainModel.MODELID_SPECIMENS;
    }

    //--- Utilities

    private void setStates( final Object caller )
    {
        setValueStates( caller );
        setEnabledStates();
    }

    private void setValueStates( final Object caller )
    {
        edit.setVisible( enabledByMaster );
        final SpecimenModel model = getSpecimenModel();
        if ( model != null )
        {
            if ( caller != id )
            {
                id.setText( model.getId() );
            }
            if ( caller != count )
            {
                count.setValue( new Integer( model.getNumberOfSpecimens() ) );
            }
            if ( caller != known )
            {
                known.setValue( new Integer( model.getWeightIfKnown() ) );
            }
            if ( caller != unknown )
            {
                unknown.setValue( new Integer( model.getWeightIfUnknown() ) );
            }
            if ( caller != knownDisabled )
            {
                knownDisabled.setSelected( model.isDeactivatedIfKnown() );
            }
            if ( caller != unknownDisabled )
            {
                unknownDisabled.setSelected( model.isDeactivatedIfUnknown() );
            }
            if ( caller != backup )
            {
                backup.setSelected( model.isBackup() );
            }
        }
        else
        {
            if ( caller != id )
            {
                id.setText( "" );
            }
            if ( caller != count )
            {
                count.setValue( ZERO );
            }
            if ( caller != known )
            {
                known.setValue( ZERO );
            }
            if ( caller != unknown )
            {
                unknown.setValue( ZERO );
            }
            if ( caller != knownDisabled )
            {
                knownDisabled.setSelected( false );
            }
            if ( caller != unknownDisabled )
            {
                unknownDisabled.setSelected( false );
            }
            if ( caller != backup )
            {
                backup.setSelected( false );
            }
        }
        if ( caller != tree )
        {
            updateTree();
        }
    }

    private void setEnabledStates()
    {
        final SpecimenModel model = getSpecimenModel();
        final boolean hasModel = model != null;
        final boolean modelThere = hasModel && enabledByMaster;
        final boolean oneSelected = tree.getSelectionPaths() != null && tree.getSelectionPaths().length == 1;
        final SimpleTaxon taxon = oneSelected ? (SimpleTaxon) tree.getSelectionPath().getLastPathComponent() : null;
        final boolean speciesSelected = SimpleTaxon.isSpecies( taxon );
        final boolean hasSpecimens = model != null && model.getNumberOfSpecimens() > 0;
        id.setEnabled( modelThere );
        count.setEnabled( modelThere );
        backup.setEnabled( modelThere );
        known.setEnabled( modelThere );
        unknown.setEnabled( modelThere );
        knownDisabled.setEnabled( modelThere && hasSpecimens );
        unknownDisabled.setEnabled( modelThere && hasSpecimens );
        newCommand.setEnabled( enabledByMaster && !hasModel && speciesSelected );
        deleteCommand.setEnabled( enabledByMaster && hasModel && speciesSelected );
        renderer.setEnabled( enabledByMaster );
    }

    private SpecimensModel getSpecimenModels()
    {
        return (SpecimensModel) getModels().getSelection();
    }

    private void updateTree()
    {
        saver.save();
        getTreeModel().reload();
        if ( getSpecimenModels() != null && getSpecimenModels().getTaxaUid() != null )
        {
            saver.restore();
        }
    }

    public static void main( final String[] args ) throws UnsupportedLookAndFeelException
    {
        UIManager.setLookAndFeel( new Plastic3DLookAndFeel() );
        UIManager.put( "ToolBar.border", new EmptyBorder( 0, 0, 0, 0 ) );
        System.setProperty( "jfactory.resource.path", "/icon" );
        System.setProperty( "jfactory.strings.resource", "ch.xmatrix.ups.uec.Strings" );
        final JFrame f = new JFrame();
        f.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        final JComponent panel = new SpecimensBuilder().getPanel();
        panel.setBorder( Borders.createEmptyBorder( Sizes.DLUX8, Sizes.DLUX8, Sizes.DLUX8, Sizes.DLUX8 ) );
        f.getContentPane().add( panel );
        f.setSize( 650, 500 );
        f.setVisible( true );
    }
}
