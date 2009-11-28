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
package ch.jfactory.projecttime.main;

import ch.jfactory.application.view.builder.ActionCommandPanelBuilder;
import ch.jfactory.application.view.builder.DockingWindowsUtils;
import ch.jfactory.component.tree.TreeModelAdapter;
import ch.jfactory.projecttime.domain.api.IFEntry;
import ch.jfactory.projecttime.entryeditor.EntryBuilder;
import ch.jfactory.projecttime.invoice.InvoiceBuilder;
import ch.jfactory.projecttime.invoice.InvoiceModel;
import ch.jfactory.projecttime.project.ProjectBuilder;
import ch.jfactory.projecttime.project.ProjectModel;
import ch.jfactory.projecttime.stats.StatsBuilder;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JComponent;
import javax.swing.JMenuBar;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import net.infonode.docking.View;
import net.infonode.docking.util.ViewMap;
import org.pietschy.command.CommandGroup;

/**
 * TODO: document
 *
 * @author <a href="daniel.frey@xmatrix.ch">Daniel Frey</a>
 * @version $Revision: 1.3 $ $Date: 2006/11/16 13:25:17 $
 */
public class MainBuilder extends ActionCommandPanelBuilder
{
    private final MainModel model;

    private final EntryBuilder entryBuilder;

    private final ProjectBuilder projectBuilder;

    private final InvoiceBuilder invoiceBuilder;

    private final StatsBuilder statsBuilder;

    public MainBuilder( final MainModel model )
    {
        this.model = model;
        // Make sure to initialize any buffered value models first, before any other listeners are registered.
        projectBuilder = new ProjectBuilder( model.getProjectModel() );
        entryBuilder = new EntryBuilder( model.getEntryModel() );
        statsBuilder = new StatsBuilder( model.getStatsModel() );
        invoiceBuilder = new InvoiceBuilder( model.getInvoiceModel() );
    }

    protected JComponent createMainPanel()
    {
        final ViewMap views = new ViewMap();
        views.addView( 1, new View( "Erfassung", null, entryBuilder.getPanel() ) );
        views.addView( 2, new View( "Statistik", null, statsBuilder.getPanel() ) );
        views.addView( 3, new View( "Rechnungen", null, invoiceBuilder.getPanel() ) );
        return DockingWindowsUtils.createParentChildDisplay( projectBuilder.getPanel(), views );
    }

    protected void initCommands()
    {
        initCommand( new OpenCommand( getCommandManager(), model ), true );
        initCommand( new SaveCommand( getCommandManager(), model ), true );
        initCommand( new ImportCommand( getCommandManager(), model.getProjectModel() ), true );
        initCommand( new TypeMappingsCommand( getCommandManager(), model ), true );
        initCommand( new MapInvoiceCommand( Commands.COMMANDID_ADDINVOICE, getCommandManager(), model ), false );
        initCommand( new MapInvoiceCommand( Commands.COMMANDID_REMOVEINVOICE, getCommandManager(), model ), false );
    }

    protected void initSubpanelCommands()
    {
        final CommandGroup group = invoiceBuilder.getCommandManager().getGroup( ch.jfactory.projecttime.invoice.Commands.GROUPID_TOOLBAR );
        final CommandGroup invoiceCommands = getCommandManager().getGroup( Commands.GROUPID_INVOICE );
        group.addInline( invoiceCommands );
    }

    protected void initModelListeners()
    {
        final ProjectModel projectModel = model.getProjectModel();
        projectModel.addPropertyChangeListener( ProjectModel.PROPERTYNAME_RUNNING, new PropertyChangeListener()
        {
            public void propertyChange( final PropertyChangeEvent evt )
            {
                final IFEntry entry = (IFEntry) ( evt.getNewValue() == null ? evt.getOldValue() : evt.getNewValue() );
                model.getEntryModel().getModel().setBean( null );
                model.getEntryModel().getModel().setBean( entry );
            }
        } );
        // Make sure the currently selected reflectes the correct invoice or deselects it when a new entry has been selected.
        final TreeSelectionModel treeSelectionModel = (TreeSelectionModel) projectModel.getSelectionModel().getValue();
        final InvoiceModel invoiceModel = model.getInvoiceModel();
        treeSelectionModel.addTreeSelectionListener( new TreeSelectionListener()
        {
            public void valueChanged( final TreeSelectionEvent e )
            {
                adjustCommands();
            }
        } );
        invoiceModel.getTableSelectionModel().addListSelectionListener( new ListSelectionListener()
        {
            public void valueChanged( final ListSelectionEvent e )
            {
                adjustCommands();
            }
        } );
        // Update invoice panel when a invoice has been mapped or the mapping has been removed
        projectModel.addPropertyChangeListener( ProjectModel.PROPERTYNAME_INVOICEADDED, new PropertyChangeListener()
        {
            public void propertyChange( final PropertyChangeEvent evt )
            {
                invoiceBuilder.updateUI();
            }
        } );
        projectModel.addPropertyChangeListener( ProjectModel.PROPERTYNAME_INVOICEREMOVED, new PropertyChangeListener()
        {
            public void propertyChange( final PropertyChangeEvent evt )
            {
                invoiceBuilder.updateUI();
            }
        } );
        // Update invoice panel when the structure in the project panel has been changed
        final TreeModelListener treeModelListener = new TreeModelAdapter()
        {
            public void treeNodesInserted( final TreeModelEvent e )
            {
                invoiceBuilder.updateUI();
            }

            public void treeNodesRemoved( final TreeModelEvent e )
            {
                invoiceBuilder.updateUI();
            }
        };
        projectModel.getTreeModel().addTreeModelListener( treeModelListener );
        projectModel.addPropertyChangeListener( ProjectModel.PROPERTYNAME_TREEMODEL, new PropertyChangeListener()
        {
            public void propertyChange( final PropertyChangeEvent evt )
            {
                final TreeModel oldModel = (TreeModel) evt.getOldValue();
                oldModel.removeTreeModelListener( treeModelListener );
                final TreeModel newModel = (TreeModel) evt.getNewValue();
                newModel.addTreeModelListener( treeModelListener );
            }
        } );
    }

    private void adjustCommands()
    {
        final InvoiceModel invoiceModel = model.getInvoiceModel();
        final ProjectModel projectModel = model.getProjectModel();
        final TreeSelectionModel treeSelectionModel = (TreeSelectionModel) projectModel.getSelectionModel().getValue();
        final TreePath[] selectionPaths = treeSelectionModel.getSelectionPaths();
        final boolean entrySelected = selectionPaths != null && selectionPaths.length >= 0;
        final boolean invoiceSelected = invoiceModel.getTableSelectionModel().getMinSelectionIndex() >= 0;
        getCommandManager().getCommand( Commands.COMMANDID_ADDINVOICE ).setEnabled( entrySelected && invoiceSelected );
        getCommandManager().getCommand( Commands.COMMANDID_REMOVEINVOICE ).setEnabled( invoiceSelected );
    }

    public JMenuBar getMenuBar()
    {
        return getCommandManager().getGroup( Commands.GROUPID_MENU ).createMenuBar();
    }
}
