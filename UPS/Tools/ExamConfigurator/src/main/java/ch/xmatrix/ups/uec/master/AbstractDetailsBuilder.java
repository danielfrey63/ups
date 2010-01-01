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
package ch.xmatrix.ups.uec.master;

import ch.jfactory.application.presentation.WindowUtils;
import ch.jfactory.application.view.builder.ActionCommandPanelBuilder;
import ch.jfactory.application.view.dialog.I15nComponentDialog;
import ch.jfactory.binding.DefaultInfoModel;
import ch.jfactory.binding.InfoModel;
import ch.jfactory.binding.SimpleNote;
import ch.jfactory.component.Dialogs;
import ch.jfactory.convert.Converter;
import ch.jfactory.model.SimpleModelList;
import ch.xmatrix.ups.controller.Loader;
import ch.xmatrix.ups.domain.TaxonBased;
import ch.xmatrix.ups.model.TaxonModels;
import ch.xmatrix.ups.model.TaxonTree;
import ch.xmatrix.ups.model.TaxonTreeModel;
import ch.xmatrix.ups.uec.main.MainModel;
import com.jformdesigner.runtime.FormCreator;
import com.jformdesigner.runtime.FormLoader;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.uif.application.Application;
import com.jgoodies.uif.application.ApplicationConfiguration;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

/**
 * General details embeded in a {@link MasterDetailsBuilder} panel. This class is useful if you use JFormsDesigner forms
 * for which you have to pass the JFD files during construction. This class takes several responsibilities:
 *
 * <ul>
 *
 * <li>It constructs a JFormCreator instance and builds the form given during construction. Use {@link #getCreator()} to
 * retrieve the JFormCreator instance and access the individual components on the form.</li>
 *
 * <li>It loads the model. Use {@link #getModels()} to access the model list.</li>
 *
 * <li>It registers the model in the factory. Use {@link #getController()} to access the factory.</li>
 *
 * <li>It registers the model with the {@link ch.xmatrix.ups.uec.main.MainModel MainModel}.</li>
 *
 * <li>And it resets the dirty flag in the dirty listener to <code>flase</code>.</li>
 *
 * </ul>
 *
 * To use this class, you need to:
 *
 * <ul>
 *
 * <li>Call the superconstructor with factory, model resource, forms resource and default space during
 * initialization.</li>
 *
 * <li>Return a correct Converter instance for the model resource in {@link #getConverter()}. As all model collections are
 * derived from {@link ch.jfactory.model.SimpleModelList}, this class provides a base converter that may be configured
 * further for the detail models (see {@link ch.jfactory.model.SimpleModelList#getConverter() getConverter}).</li>
 *
 * <li>To use the model with this class, make sure to derive it from {@link TaxonBased} or use the abstract
 * implementaiton for it {@link ch.xmatrix.ups.domain.AbstractTaxonBased AbstractTaxonBased}.</li>
 *
 * </ul>
 *
 * These three requirements already display the details build. However, any data filled in, any actions are dependent on
 * the following (optional) implementations:
 *
 * <ul>
 *
 * <li>If you want to use the creation mechanisms provided by this class, derive a type factory from {@link
 * MasterDetailsFactory} that generates the correct model types.</li>
 *
 * <li>Initialize your component variables for later manipulation and integrate the model data in {@link
 * #initComponents()}.</li>
 *
 * <li>Initialize the listeners for your components in {@link #initComponentListeners()}. Usually you commit changes in
 * your editor in these listeners. To get the model you may use {@link #getModels()} and its current selection.</li>
 *
 * </ul>
 *
 * If you plan to add a details toolbar, you may override <code>ActionCommandPanelBuilder</code>s {@link
 * ActionCommandPanelBuilder#initCommands() initCommands} to initialize commands.<p/>
 *
 * <em>Attention:</em> As this is a subclass of {@link ActionCommandPanelBuilder}, do <em>not</em> overwrite methods
 * that are spetialized here or there. Especially:
 *
 * <ul>
 *
 * <li>{@link ch.jfactory.application.view.builder.ActionCommandPanelBuilder#getPanel()}</li>
 *
 * <li>{@link ch.jfactory.application.view.builder.ActionCommandPanelBuilder#createMainPanel()}</li>
 *
 * </ul>
 *
 * If you do not plan to use JFormDesigner, either extend {@link ActionCommandPanelBuilder} directly or build another
 * concept.
 *
 * @author Daniel Frey
 * @version $Revision: 1.8 $ $Date: 2008/01/06 10:16:20 $
 */
public abstract class AbstractDetailsBuilder extends ActionCommandPanelBuilder implements DetailsBuilder
{
    private static final Logger LOG = Logger.getLogger( AbstractDetailsBuilder.class );

    protected static final Integer ZERO = new Integer( 0 );

    protected static final DefaultListModel DUMMY_MODEL = new DefaultListModel();

    protected static final ArrayList<String> DUMMY_LIST = new ArrayList<String>();

    private final String prefsPath;

    private final SelectionInList models;

    private final MasterDetailsFactory factory;

    private final String modelResource;

    private final String formResource;

    private final int space;

    private boolean loaded;

    private JComponent panel;

    private JComponent details;

    private FormCreator creator;

    private DirtyListener dirtyListener;

    private JTree tree = null;

    private TaxonTreeModel treeModel = null;

    private InfoModel infoModel = new DefaultInfoModel();

    private MasterDetailsBuilder master;

    protected AbstractDetailsBuilder( final MasterDetailsFactory factory, final String modelResource,
                                      final String resourceForm, final int space )
    {
        this.models = new SelectionInList();
        this.factory = factory;
        this.modelResource = modelResource;
        this.formResource = resourceForm;
        this.space = space;
        final ApplicationConfiguration config = Application.getConfiguration();
        prefsPath = config == null ? "ups/test" : config.getPreferencesRootName();
    }

    //--- ActionCommandPanelBulider overrides.

    /**
     * Creates the main {@link MasterDetailsBuilder} and embeds the details in it.
     *
     * @return the panel created by the MasterDetailsBuilder instance
     */
    protected JComponent createMainPanel()
    {
        try
        {
            infoModel.setNote( new SimpleNote( "Lade " + getInfoString() ) );
            InputStream in;
            in = getClass().getResourceAsStream( formResource );
            // Don't know why I have to do that: On some machines an absolute path is required, on others a relative.
            if ( in == null )
            {
                if ( !formResource.startsWith( "/" ) )
                {
                    in = getClass().getResourceAsStream( "/" + formResource );
                }
                else
                {
                    in = getClass().getResourceAsStream( formResource.substring( 1 ) );
                }
            }
            creator = new FormCreator( FormLoader.load( in ) );
            in.close();
            details = creator.createPanel();

            master = new MasterDetailsBuilder();
            master.registerController( getController() );
            master.registerModels( getModels() );
            master.registerDetailsBuilder( this );
            panel = master.getPanel();

            initComponents();

            master.addDetails( details, space );
            reload();

            return panel;
        }
        catch ( Exception e )
        {
            final String message = "failed to init components";
            LOG.error( message, e );
            throw new IllegalStateException( message );
        }
    }

    //--- DetailsBuilder implementations.

    public void lock()
    {
        final TaxonBased model = (TaxonBased) models.getSelection();
        dirtyListener.setDirty( true );
        model.setFixed();
        setEnabled( false );
    }

    /**
     * Loads the data from the model resource. First it tries to find the model resource in a user home subdirectory. If
     * not found it is loaded from the source.
     */
    public void load()
    {
        final SimpleModelList list = Loader.loadModel( modelResource, prefsPath, getConverter() );
        models.setListModel( list );
        factory.setModels( list );
        if ( details != null )
        {
            details.repaint();
        }

        MainModel.registerModels( getModelId(), list );
        if ( dirtyListener != null )
        {
            dirtyListener.setDirty( false );
        }
    }

    public void save()
    {
        Loader.saveModel( modelResource, prefsPath, getConverter(), getModels().getListModel() );
        dirtyListener.setDirty( false );
    }

    /**
     * Handles migration from one taxonomic system to another by calling if necessary:
     *
     * <ul>
     *
     * <li>{@link #shouldMigrate(String)}</li>
     *
     * <li>{@link #findMigrationErrors(String)}</li>
     *
     * <li>{@link #commitMigrate(java.util.ArrayList)}</li>
     *
     * <li>{@link #removeMigrationErrors(java.util.ArrayList)}</li>
     *
     * </ul>
     *
     * @param model the old model
     * @param uid   the new taxon tree uid
     * @return whether migration has to be done or not
     */
    public boolean handleMigration( final TaxonBased model, final String uid )
    {
        boolean migrate = true;
        if ( model != null && model.getTaxaUid() != null && !model.getTaxaUid().equals( uid ) )
        {
            migrate = shouldMigrate( uid );
            if ( migrate )
            {
                final ArrayList errors = findMigrationErrors( uid );
                if ( errors.size() > 0 )
                {
                    migrate = commitMigrate( errors );
                    if ( migrate )
                    {
                        removeMigrationErrors( errors );
                    }
                }
                else
                {
                    commitSuccessful();
                }
            }
        }
        return migrate;
    }

    public void registerDirtyListener( final DirtyListener dirtyListener )
    {
        this.dirtyListener = dirtyListener;
    }

    /**
     * Important: When overriding call <code>super.setSpecimensModel()</code> in order to handle registered trees (see
     * {@link #registerTree(javax.swing.JTree)}) correctly.
     *
     * @param taxonBased the model
     */
    public void setModel( final TaxonBased taxonBased )
    {
        if ( treeModel != null )
        {
            final TaxonTree taxa = taxonBased == null ? null : TaxonModels.find( taxonBased.getTaxaUid() );
            if ( taxa != null )
            {
                treeModel.setRoot( taxa.getRootTaxon() );
            }
            else
            {
                treeModel.setRoot( null );
            }
            treeModel.reload();
            tree.repaint();
        }
    }

    /**
     * Returns the model with the specified uid or null if none can be found.
     *
     * @param uid the uid of the model to find
     * @return the model with the given uid
     */
    public TaxonBased find( final String uid )
    {
        reload();
        for ( int i = 0; i < models.getSize(); i++ )
        {
            final TaxonBased model = (TaxonBased) models.getElementAt( i );
            if ( model.getUid().equals( uid ) )
            {
                return model;
            }
        }
        return null;
    }

    //--- Public interface.

    public FormCreator getCreator()
    {
        return creator;
    }

    public SelectionInList getModels()
    {
        reload();
        return models;
    }

    public MasterDetailsFactory getController()
    {
        return factory;
    }

    public void setInfoModel( final InfoModel infoModel )
    {
        this.infoModel = infoModel;
    }

    //--- Abstract subclass interface.

    /**
     * Use this method to initialize component variables that you may use in the subclass for later modification and
     * integrate the model into it. A convenient way is given for taxon trees: you simple may register it with {@link
     * #registerTree(javax.swing.JTree) registerTree}. To access your components of the FormCreator use {@link
     * #getCreator()} - do not initialize the FormCreator in the method itself!
     */
    protected abstract void initComponents();

    /**
     * Return an instance of the De-/Serializer for the model.
     *
     * @return the de-/serializer
     */
    protected abstract Converter getConverter();

    /**
     * Return an info string that is displayed to the user during startup.
     *
     * @return the info string
     */
    protected abstract String getInfoString();

    /**
     * Returns the model id for registering the model.
     *
     * @return the id
     */
    protected abstract String getModelId();

    //--- Adapters on subclass interfaces

    /**
     * Standard implementation that asks for configramtion and returns whether the user approves migration.
     *
     * @param uid the taxon tree uid
     * @return whether the migration should take place or not.
     */
    protected boolean shouldMigrate( final String uid )
    {
        final int result = Dialogs.showQuestionMessageCancel( panel, "Migration",
                "Sie wollen den taxonomischen Baum, auf dem diese Guppeneinstellungen basieren, ändern.\n" +
                        "Es kann sein, dass nicht alle Einstellungen in den neuen Baum übernommen werden können.\n" +
                        "Möchten Sie diese Änderungen wirklich durchführen?" );
        return result == Dialogs.OK;
    }

    /**
     * Dummy implementation that always returns an empty array list.
     *
     * @param uid the new taxon tree uid
     * @return a list of migration errors as strings
     */
    protected ArrayList findMigrationErrors( final String uid )
    {
        return new ArrayList();
    }

    /**
     * Dummy implementation that does nothing.
     *
     * @param errors the list of errors as strings
     */
    protected void removeMigrationErrors( final ArrayList errors )
    {
    }

    /**
     * Dummy implementation that does nothing. Override this if you want to feedback successful migration to the user.
     */
    protected void commitSuccessful()
    {
    }

    //--- Subclass interface

    /**
     * Marks the data as modified. As soon as the data is saved, the modified timestamp is actualized.
     */
    protected void setDirty()
    {
        dirtyListener.setDirty( true );
    }

    /**
     * Handles creation and update of a taxon tree model for you. Does not handle enabled state of the tree.
     *
     * @param tree the tree component
     */
    protected void registerTree( final JTree tree )
    {
        this.tree = tree;
        treeModel = new TaxonTreeModel( null );
        tree.setModel( treeModel );
    }

    protected TaxonTreeModel getTreeModel()
    {
        return treeModel;
    }

    //--- Utilities

    private void reload()
    {
        if ( !loaded )
        {
            load();
            loaded = true;
        }
    }

    private boolean commitMigrate( final ArrayList errors )
    {
        final JFrame parent = (JFrame) panel.getTopLevelAncestor();
        final I15nComponentDialog dialog = new I15nComponentDialog( parent, "migration" )
        {
            protected void onApply()
            {
            }

            protected void onCancel()
            {
            }

            protected JComponent createComponentPanel()
            {
                enableApply( true );
                return new JScrollPane( new JList( errors.toArray() ) );
            }
        };
        dialog.setSize( 300, 300 );
        WindowUtils.centerOnComponent( dialog, parent );
        dialog.setVisible( true );
        return dialog.isAccepted();
    }

    protected void loadStates()
    {
        final String prefix = "/" + getClass().getPackage().getName().replace( ".", "/" );
        final InputStream in = getClass().getResourceAsStream( prefix + "/states.txt" );
        if ( in != null )
        {
            final StringWriter writer = new StringWriter();
            try
            {
                IOUtils.copy( in, writer );
                final String text = writer.toString();
                final String[] lines = text.split( "\n" );
                String[] all = new String[0];
                int allIndex = 0;
                int inFrom = 0;
                int inTo = 0;
                int outFrom = 0;
                int outTo = 0;
                int colFrom = 0;
                int colTo = 0;
                int[] emptyCols = new int[0];
                for ( int i = 0; i < lines.length; i++ )
                {
                    final String line = lines[i];
                    if ( line.startsWith( "format" ) )
                    {
                        final String[] tokens = line.split( " " );
                        final String[] ins = tokens[1].split( "-" );
                        inFrom = Integer.parseInt( ins[0] );
                        inTo = Integer.parseInt( ins[1] );
                        final String[] outs = tokens[2].split( "-" );
                        outFrom = Integer.parseInt( outs[0] );
                        outTo = Integer.parseInt( outs[1] );
                        final String[] columns = tokens[3].split( "-|\r" );
                        colFrom = Integer.parseInt( columns[0] );
                        colTo = Integer.parseInt( columns[1] );
                        final String[] empties = tokens[4].split( ", *|\r" );
                        emptyCols = new int[empties.length];
                        for ( int j = 0; j < empties.length; j++ )
                        {
                            final String empty = empties[j];
                            emptyCols[j] = Integer.parseInt( empty );
                        }
                        all = new String[inTo - inFrom + 1 + outTo - outFrom + 1];
                    }
                    if ( inFrom > 0 && i >= inFrom && i <= inTo )
                    {
                        String data = line.substring( colFrom, colTo ).replace( "|", "," ).replaceAll( " , ", "  " ).
                                replaceAll( "  ", " " ).replaceAll( "  ", " 0" );
                        for ( int j = emptyCols.length - 1; j >= 0; j-- )
                        {
                            final int col = emptyCols[j];
                            data = data.substring( 0, col ).substring( col + 1 );
                        }
                        all[allIndex++] = data;
                    }
                    else if ( outFrom > 0 && i >= outFrom && i <= outTo )
                    {
                        String data = line.substring( colFrom, colTo ).replace( "|", "," ).replaceAll( " , ", "  " ).
                                replaceAll( "  ", " " ).replaceAll( "  ", " 0" );
                        for ( int j = emptyCols.length - 1; j >= 0; j-- )
                        {
                            final int col = emptyCols[j];
                            data = data.substring( 0, col ).substring( col + 1 );
                        }
                        all[allIndex++] = data;
                    }
                }
//                final String[][] matrix = new String[all.length][];
                System.out.println( all );
            }
            catch ( IOException e )
            {
                LOG.error( "problems rading states matrix", e );
            }
        }
    }

    /**
     * Returns the taxon tree associated with the current model or null.
     *
     * @return the taxon tree
     */
    protected TaxonTree getTaxonTree()
    {
        final TaxonBased taxonBased = (TaxonBased) getModels().getSelection();
        return taxonBased == null ? null : TaxonModels.find( taxonBased.getTaxaUid() );
    }

    protected void setTaxonTreeDisabled()
    {
        master.setTaxonTreeDisabled();
    }
}
