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

import ch.jfactory.application.view.builder.ActionCommandPanelBuilder;
import ch.jfactory.component.Dialogs;
import ch.jfactory.component.SimpleDocumentListener;
import ch.jfactory.event.VetoableChangeEvent;
import ch.jfactory.event.VetoableComboBoxModel;
import ch.jfactory.event.VetoableComboBoxSelectionListener;
import ch.jfactory.lang.LogicUtils;
import ch.xmatrix.ups.domain.TaxonBased;
import ch.xmatrix.ups.model.TaxonModels;
import ch.xmatrix.ups.model.TaxonTree;
import ch.xmatrix.ups.uec.master.commands.AddCommand;
import ch.xmatrix.ups.uec.master.commands.Commands;
import ch.xmatrix.ups.uec.master.commands.CopyCommand;
import ch.xmatrix.ups.uec.master.commands.DeleteCommand;
import ch.xmatrix.ups.uec.master.commands.FixCommand;
import ch.xmatrix.ups.uec.master.commands.LoadCommand;
import ch.xmatrix.ups.uec.master.commands.SaveCommand;
import com.jformdesigner.runtime.FormCreator;
import com.jformdesigner.runtime.FormLoader;
import com.jgoodies.binding.adapter.ComboBoxAdapter;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ResourceBundle;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.event.DocumentEvent;
import javax.swing.text.JTextComponent;
import org.apache.log4j.Logger;
import org.pietschy.command.ActionCommand;
import org.pietschy.command.ActionCommandInterceptor;

/**
 * Provides a choice and a taxon tree selection.
 *
 * Todo: Disable fix button if no tree is selected, or the tree is not found
 *
 * Todo: Change icon in fix button if fixed to distinguish it from disabled
 *
 * @author Daniel Frey
 * @version $Revision: 1.5 $ $Date: 2008/01/06 10:16:20 $
 */
public class MasterDetailsBuilder extends ActionCommandPanelBuilder implements DirtyListener {

    public static final String RESOURCE_BUNDLE = "ch.xmatrix.ups.uec.master.masterdetail";
    public static final String COMPONENT_COMBO_MODELS = "comboModels";
    public static final String COMPONENT_PANEL_COMBO = "panelCombo";
    public static final String COMPONENT_COMBO_TAXTREES = "comboTaxtrees";
    public static final String COMPONENT_FIELD_MODIFIED = "fieldModified";
    public static final SimpleDateFormat DATEFORMAT = new SimpleDateFormat("d.M.yyyy HH:mm:ss.SSS");

    private static final Logger LOG = Logger.getLogger(MasterDetailsBuilder.class);
    private static final String RESOURCE_FORM = "ch/xmatrix/ups/uec/master/MasterDetail.jfd";
    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle(RESOURCE_BUNDLE);

    private JPanel main;
    private JPanel master;
    private JComboBox taxa;
    private JComboBox choice;
    private DetailsBuilder details;
    private SelectionInList models;
    private MasterDetailsFactory factory;
    private ActionCommand save;
    private ActionCommand fix;
    private ActionCommand load;
    private JTextComponent modified;
    private ActionCommand copy;
    private ActionCommand delete;
    private boolean dirty;
    private boolean isAdjusting;
    private boolean taxonTreeDisabled;
    private ActionCommand add;
    private FormCreator creator;
    private static final String COMPONENT_LABEL_TAXTREE = "labelTaxtree";

    //--- Public Interface.

    /**
     * Adds the given details to the south of this panel.
     *
     * @param details the details to add
     * @param size    the size for the first column in this panel. Use this to align components propertly.
     */
    public void addDetails(final JComponent details, final int size) {
        final FormLayout layout = (FormLayout) master.getLayout();
        layout.setColumnSpec(2, new ColumnSpec("max(min;" + size + "dlu)"));
        main.add(details, BorderLayout.CENTER);
    }

    /**
     * Registers the details builder for later handling of enabled/disabled calls.
     *
     * @param detailsBuilder the builder to register
     */
    public void registerDetailsBuilder(final DetailsBuilder detailsBuilder) {
        this.details = detailsBuilder;
        detailsBuilder.registerDirtyListener(this);
    }

    public void registerModels(final SelectionInList models) {
        this.models = models;
    }

    public void registerController(final MasterDetailsFactory factory) {
        this.factory = factory;
    }

    /**
     * Disables the taxon tree combo box permanently.
     */
    public void setTaxonTreeDisabled() {
        creator.getComboBox(COMPONENT_COMBO_TAXTREES).setVisible(false);
        creator.getLabel(COMPONENT_LABEL_TAXTREE).setVisible(false);
        taxonTreeDisabled = true;
    }

    //--- ActionCommandPanelBuilder overrides

    protected JComponent createMainPanel() {
        try {
            creator = new FormCreator(FormLoader.load(RESOURCE_FORM));
            creator.createAll();
            main = new JPanel(new BorderLayout());
            master = creator.getPanel(COMPONENT_PANEL_COMBO);
            final JToolBar toolbar = getCommandManager().getGroup(Commands.GROUPID_TOOLBAR).createToolBar(Commands.TOOLBARID);
            master.add(toolbar, new CellConstraints().xy(6, 1));
            choice = creator.getComboBox(COMPONENT_COMBO_MODELS);
            choice.setModel(new ComboBoxAdapter(models));
            taxa = creator.getComboBox(COMPONENT_COMBO_TAXTREES);
            taxa.setModel(new VetoableComboBoxModel(TaxonModels.getTaxonTreesArray()));
            taxa.setSelectedIndex(-1);
            modified = creator.getTextField(COMPONENT_FIELD_MODIFIED);
            main.add(master, BorderLayout.NORTH);
        }
        catch (Exception e) {
            final String message = "could not create master panel";
            LOG.error(message, e);
            throw new IllegalStateException(message);
        }
        return main;
    }

    protected void initCommands() {
        add = initCommand(new AddCommand(getCommandManager(), models, factory), true);
        copy = initCommand(new CopyCommand(getCommandManager(), models, factory));
        delete = initCommand(new DeleteCommand(getCommandManager(), models, factory));
        load = initCommand(new LoadCommand(getCommandManager(), details));
        save = initCommand(new SaveCommand(getCommandManager(), details));
        fix = initCommand(new FixCommand(getCommandManager(), details));
    }

    protected void initModelListeners() {
        models.addPropertyChangeListener(SelectionInList.PROPERTYNAME_SELECTION, new PropertyChangeListener() {
            public void propertyChange(final PropertyChangeEvent evt) {
                final Object model = evt.getNewValue();
                if (!isAdjusting && (model instanceof TaxonBased || model == null)) {
                    isAdjusting = true;
                    final TaxonBased taxonBased = (TaxonBased) model;
                    // Make sure to first set the model in the details. All other modifications rely on it.
                    details.setModel(taxonBased);
                    final TaxonTree tree = getTaxonTree(taxonBased);
                    taxa.setSelectedItem(tree);
                    setStates(models);
                    isAdjusting = false;
                }
            }
        });
    }

    protected void initSubpanelCommands() {
        choice.addItemListener(new ItemListener() {
            public void itemStateChanged(final ItemEvent e) {
                if (!isAdjusting) {
                    isAdjusting = true;
                    setStates(choice);
                    isAdjusting = false;
                }
            }
        });
        final JTextField editor = (JTextField) choice.getEditor().getEditorComponent();
        editor.getDocument().addDocumentListener(new SimpleDocumentListener() {
            public void changedUpdate(final DocumentEvent e) {
                if (!isAdjusting) {
                    isAdjusting = true;
                    final TaxonBased model = (TaxonBased) models.getSelection();
                    if (model != null) {
                        final String newName = editor.getText().trim();
                        final String modelName = model.getName();
                        if (!newName.equals(modelName)) {
                            model.setName(newName);
                            setDirty(true, editor, true);
                        }
                    }
                    isAdjusting = false;
                }
            }
        });
        final VetoableComboBoxModel comboTaxtreeModel = (VetoableComboBoxModel) taxa.getModel();
        comboTaxtreeModel.addVetoableSelectionListener(new VetoableComboBoxSelectionListener() {
            public boolean selectionChanged(final VetoableChangeEvent e) {
                final TaxonTree newTree = (TaxonTree) e.getNewValue();
                final boolean migrate;
                if (newTree != null) {
                    final TaxonBased model = (TaxonBased) models.getSelection();
                    final String uid = newTree.getUid();
                    migrate = details.handleMigration(model, uid);
                    if (!isAdjusting) {
                        isAdjusting = true;
                        if (migrate && model != null) {
                            model.setTaxaUid(uid);
                            details.setModel(model);
                            setDirty(true, taxa, true);
                        }
                        isAdjusting = false;
                    }
                }
                else {
                    migrate = true;
                }
                return migrate;
            }
        });

        final ActionCommandInterceptor dirtyInterceptor = new ActionCommandInterceptor() {
            public boolean beforeExecute(final ActionCommand command) {
                return true;
            }

            public void afterExecute(final ActionCommand command) {
                if (!isAdjusting) {
                    isAdjusting = true;
                    setDirty(true, null, true);
                    isAdjusting = false;
                }
            }
        };
        add.addInterceptor(dirtyInterceptor);
        copy.addInterceptor(dirtyInterceptor);
        delete.addInterceptor(new ActionCommandInterceptor() {
            public boolean beforeExecute(final ActionCommand command) {
                return true;
            }

            public void afterExecute(final ActionCommand command) {
                if (!isAdjusting) {
                    isAdjusting = true;
                    setDirty(true, delete, false);
                    isAdjusting = false;
                }
            }
        });
        fix.addInterceptor(new ActionCommandInterceptor() {
            public boolean beforeExecute(final ActionCommand command) {
                return true;
            }

            public void afterExecute(final ActionCommand command) {
                if (!isAdjusting) {
                    isAdjusting = true;
                    setDirty(true, fix, false);
                    isAdjusting = false;
                }
            }
        });
        final ActionCommandInterceptor cleanInterceptor = new ActionCommandInterceptor() {
            public boolean beforeExecute(final ActionCommand command) {
                return true;
            }

            public void afterExecute(final ActionCommand command) {
                if (!isAdjusting) {
                    isAdjusting = true;
                    setDirty(false, null, true);
                    isAdjusting = false;
                }
            }
        };
        load.addInterceptor(cleanInterceptor);
        save.addInterceptor(cleanInterceptor);
    }

    //--- Dirty listener interface

    /**
     * Don't call this method from internal updates, as it sets and removes the recursion lock. Use {@link
     * #setDirty(boolean,Object,boolean)} instead.
     *
     * @param dirty the dirty state
     */
    public void setDirty(final boolean dirty) {
        if (!isAdjusting) {
            isAdjusting = true;
            setDirty(dirty, null, true);
            isAdjusting = false;
        }
    }

    //--- Utilities

    /**
     * Use this method for internal updates.
     *
     * @param dirty the dirty state
     * @param name  the origin
     * @param date  indicate whether the date has to be modified in case of a true dirty.
     */
    private void setDirty(final boolean dirty, final Object name, final boolean date) {
        if (dirty != this.dirty) {
            this.dirty = dirty;
        }
        if (dirty && date) {
            final TaxonBased model = (TaxonBased) models.getSelection();
            if (model != null) {
                model.setModified(Calendar.getInstance().getTime());
            }
        }
        setStates(name);
    }

    private TaxonTree getTaxonTree(final TaxonBased model) {
        final String taxaUid = model == null ? null : model.getTaxaUid();
        final TaxonTree tree = TaxonModels.find(taxaUid);
        if (tree == null) {
            if (taxaUid != null) {
                final String message = BUNDLE.getString("masterdetail.error.taxonTree.text");
                final String formatted = new MessageFormat(message).format(new String[]{taxaUid});
                Dialogs.showErrorMessage(master, BUNDLE.getString("masterdetail.error.taxonTree.title"), formatted);
            }
        }
        return tree;
    }

    private void setStates(final Object source) {
        setValueStates(source);
        setEnabledStates();
    }

    private void setValueStates(final Object source) {
        final TaxonBased model = (TaxonBased) models.getSelection();
        if (model != null) {
            final TaxonTree tree = model == null ? null : TaxonModels.find(model.getTaxaUid());
            if (source != modified) modified.setText(DATEFORMAT.format(model.getModified()));
            if (source != taxa) taxa.setSelectedItem(tree);
            if (source != choice) choice.setSelectedItem(model);
            choice.setEditable(!model.isFixed());
        }
        else {
            if (source != modified) modified.setText("");
            if (source != taxa) taxa.setSelectedItem(null);
            if (source != choice) choice.setSelectedItem(null);
            choice.setEditable(false);
        }
        choice.repaint();
    }

    /**
     * Input states are:
     *
     * <ul>
     *
     * <li>mo: if there is a selection in the models popup (1) or not (0).</li>
     *
     * <li>ta: if there is a taxon tree in the model (1) or not (0).</li>
     *
     * <li>fr: if the details model is free (1) or fixed (0).</li>
     *
     * <li>di: if the model has been touched (1) or is unchanged (0).</li>
     *
     * </ul>
     *
     * The components influenced by the input states are:
     *
     * <ul>
     *
     * <li>ad: the add button.</li>
     *
     * <li>co: the copy button.</li>
     *
     * <li>de: the delete button.</li>
     *
     * <li>fi: the fix button.</li>
     *
     * <li>ta: the taxon popup.</li>
     *
     * <li>pa: the details panel.</li>
     *
     * <li>lo: the load button.</li>
     *
     * <li>sa: the save button.</li>
     *
     * <li>na: the name field.</li>
     *
     * </ul>
     *
     * <pre>
     * +----+---------+--------+--------+--------+--------+--------------------+
     * | di | 0 0 0 0  0 0 0 0  1 1 1 1  1 1 1 1 | high   |                di |
     * | fr | 0 0 0 0  1 1 1 1  0 0 0 0  1 1 1 1 |        |           fr      |
     * | ta | 0 0 1 1  0 0 1 1  0 0 1 1  0 0 1 1 |        |       ta          |
     * | mo | 0 1 0 1  0 1 0 1  0 1 0 1  0 1 0 1 | low    |  mo               |
     * +----+---------+--------+--------+--------+--------+-------------------+
     * | ad | 1 1 1 1  1 1 1 1  1 1 1 1  1 1 1 1 | 0xFFFF |                   |
     * | cp | 0 1 0 1  0 1 0 1  0 1 0 1  0 1 0 1 | 0xAAAA | mo | ta | fr      |
     * | de | 0 1 0 1  0 1 0 1  0 1 0 1  0 1 0 1 | 0xAAAA | mo | ta | fr      |
     * | fi | 0 0 0 0  0 0 1 1  0 0 0 0  0 0 1 1 | 0xC0C0 |           fr      |
     * | ta | 0 0 0 0  0 1 0 1  0 0 0 0  0 1 0 1 | 0xA0A0 | mo                |
     * | pa | 0 0 0 0  0 0 0 1  0 0 0 0  0 0 0 1 | 0x8080 | mo & ta & fr      |
     * | lo | 0 0 0 0  0 0 0 0  1 1 1 1  1 1 1 1 | 0xFF00 |                di |
     * | sa | 0 0 0 0  0 0 0 0  1 1 1 1  1 1 1 1 | 0xFF00 |                di |
     * +----+---------+--------+--------+--------+--------+-------------------+
     *        low                           high
     * </pre>
     */
    private void setEnabledStates() {
        final TaxonBased model = (TaxonBased) models.getSelection();
        final String taxaUid = model == null ? "" : model.getTaxaUid();
        final TaxonTree tree = model == null ? null : TaxonModels.find(taxaUid);

        final boolean mo = model != null;
        final boolean ta = tree != null;
        final boolean fr = model != null && !model.isFixed();
        final boolean di = dirty;

        final LogicUtils.InfoBit[] bits = new LogicUtils.InfoBit[4];
        bits[0] = new LogicUtils.InfoBit(di, "data has been changed (dirty)", "data is not changed");
        bits[1] = new LogicUtils.InfoBit(fr, "model is editable (free)", "model is fixed");
        bits[2] = new LogicUtils.InfoBit(ta, "taxa tree has been defined", "taxa tree is not defined");
        bits[3] = new LogicUtils.InfoBit(mo, "model selected", "no model selected");

        final Object[] components = new Object[]{add, copy, delete, fix, taxa, details, load, save};
        LogicUtils.setEnabledStates(bits, components, new long[]{
                0xFFFF, 0xAAAA, 0xAAAA, 0xC0C0,
                0xA0A0, 0x8080, 0xFF00, 0xFF00
        }, 0);

        if (taxonTreeDisabled) {
            taxa.setEnabled(false);
        }
    }
}
