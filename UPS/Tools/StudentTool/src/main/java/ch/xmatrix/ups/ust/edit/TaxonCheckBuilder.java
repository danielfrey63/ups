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

package ch.xmatrix.ups.ust.edit;

import ch.jfactory.application.AbstractMainModel;
import ch.jfactory.application.view.builder.ActionCommandPanelBuilder;
import ch.jfactory.command.AddFilterToAndedFilterable;
import ch.jfactory.command.CollapseAllTreeNodes;
import ch.jfactory.command.ExpandAllTreeNodes;
import ch.jfactory.command.RemoveFilterFromAndedFilterable;
import ch.jfactory.component.tree.SearchableTree;
import ch.jfactory.component.tree.TreeExpandedRestorer;
import ch.jfactory.component.tree.filtered.ModelBasedFilteredTreeModel;
import ch.jfactory.filter.Filter;
import ch.jfactory.filter.MultiAndFilter;
import ch.xmatrix.ups.controller.TreeCheckboxController;
import ch.xmatrix.ups.domain.Constraint;
import ch.xmatrix.ups.domain.Constraints;
import ch.xmatrix.ups.domain.SimpleTaxon;
import ch.xmatrix.ups.model.TaxonModels;
import ch.xmatrix.ups.model.TaxonTree;
import ch.xmatrix.ups.model.TaxonTreeModel;
import ch.xmatrix.ups.ust.main.UserModel;
import ch.xmatrix.ups.view.renderer.ConstraintsTreeRenderer;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.TreePath;
import org.apache.log4j.Logger;
import org.pietschy.command.CommandManager;

/**
 * Builds the panel from which taxa can be selected and deselected.
 */
public class TaxonCheckBuilder extends ActionCommandPanelBuilder {

    private static final Logger LOG = Logger.getLogger(TaxonCheckBuilder.class);
    private static final boolean DEBUG = LOG.isDebugEnabled();

    private SearchableTree tree;
    private ModelBasedFilteredTreeModel treeModel;
    private TaxonTreeModel taxonTreeModel;
    private ConstraintsTreeRenderer treeRenderer;
    private UserModel userModel;
    private Constraints constraints;
    private ArrayList<JComponent> componentsToRepaint = new ArrayList<JComponent>();
    private ArrayList<SpeciesSelectionListener> listeners = new ArrayList<SpeciesSelectionListener>();
    private final HideSingleGenus singleGenusFilter = new HideSingleGenus();

    public void setModel(final UserModel userModel) {
        this.userModel = userModel;
        constraints = (Constraints) AbstractMainModel.findModel(userModel.getConstraintsUid());
        final TaxonTree taxonTree = TaxonModels.find(constraints.getTaxaUid());
        taxonTreeModel.setRoot(taxonTree.getRootTaxon());
        treeRenderer.setConstraints(constraints);
        treeRenderer.setTaxa(userModel.getTaxa());
        taxonTreeModel.reload();
    }

    public void setSelection(final SimpleTaxon taxon) {
        if (taxon != null) {
            final TreePath path = taxonTreeModel.getPathToRoot(taxon);
            if (path != null) {
                final Object[] pathElements = path.getPath();
                TreePath finalPath = new TreePath(pathElements[0]);
                for (int i = 1; i < pathElements.length; i++) {
                    final Object element = pathElements[i];
                    if (singleGenusFilter.matches(element)) {
                        finalPath = finalPath.pathByAddingChild(element);
                    }
                }
                tree.setSelection(finalPath);
            }
            else {
                tree.setSelection(null);
            }
        }
        else {
            tree.setSelection(null);
        }
    }

    public void addComponentToRepaint(final JComponent componentToRepaint) {
        componentsToRepaint.add(componentToRepaint);
    }

    public void addComponentsToRepaint(final ArrayList<JComponent> componentsToRepaint) {
        this.componentsToRepaint.addAll(componentsToRepaint);
    }

    /**
     * Creates the Taxon tree panel.
     *
     * @return the panel
     */
    public JComponent createMainPanel() {
        final JPanel taxTreePanel = new JPanel(new BorderLayout());
        tree = getTaxTree();
        taxTreePanel.add(new JScrollPane(tree), BorderLayout.CENTER);
        taxTreePanel.add(getCommandManager().getGroup(Commands.GROUPID_TAXTREE_TOOLBAR).createToolBar(Commands.FACENAME_TOOLBAR), BorderLayout.NORTH);
        return taxTreePanel;
    }

    public void addSpeciesSelectionListener(final SpeciesSelectionListener listener) {
        listeners.add(listener);
    }

    protected void initCommands() {

        taxonTreeModel = new TaxonTreeModel(SimpleTaxon.DUMMY);
        treeModel = new ModelBasedFilteredTreeModel(taxonTreeModel);
        treeModel.setFilter(new MultiAndFilter(new Filter[]{singleGenusFilter}));

        final SearchableTree tree = getTaxTree();

        final CommandManager manager = getCommandManager();
        initCommand(new ExpandAllTreeNodes(manager, tree), true);
        initCommand(new CollapseAllTreeNodes(manager, tree), true);

        final TreeExpandedRestorer saver = new TreeExpandedRestorer(tree);
        final Filter hide = new HideConstraintlessTaxon();
        initToggleCommand(new RemoveFilterFromAndedFilterable(manager, Commands.COMMANDID_TAXTREE_DEEP, treeModel, hide, saver), true);
        initToggleCommand(new AddFilterToAndedFilterable(manager, Commands.COMMANDID_TAXTREE_FLAT, treeModel, hide, saver), false);

        final Filter non = new HideNonSelectedSpecies();
        initToggleCommand(new RemoveFilterFromAndedFilterable(manager, Commands.COMMANDID_TAXTREE_ALLSPECIES, treeModel, non, saver), true);
        initToggleCommand(new AddFilterToAndedFilterable(manager, Commands.COMMANDID_TAXTREE_SELECTEDONLY, treeModel, non, saver), false);
    }

    /**
     * Creates a tree component with the given tree model.
     *
     * @return the tree component
     */
    private SearchableTree getTaxTree() {
        if (tree == null) {
            treeRenderer = new ConstraintsTreeRenderer();
            treeRenderer.setEnabled(true);
            tree = new SearchableTree(treeModel);
            final TreeCheckboxController checkBoxHandler = new CheckboxController();
            tree.addMouseListener(checkBoxHandler);
            tree.addKeyListener(checkBoxHandler);
            tree.setBorder(new EmptyBorder(3, 3, 3, 3));
            tree.setCellRenderer(treeRenderer);
        }
        return tree;
    }

    public interface SpeciesSelectionListener {

        public void speciesSelectionChanged();
    }

    private class CheckboxController extends TreeCheckboxController {

        public CheckboxController() {
            super(TaxonCheckBuilder.this.tree);
        }

        protected void handleSelection(final TreePath path) {
            if (path != null) {
                final SimpleTaxon taxon = (SimpleTaxon) path.getLastPathComponent();
                if (SimpleTaxon.isSpecies(taxon)) {
                    final String name = taxon.getName();
                    final Constraint constraint = constraints.findConstraint(name);
                    if (constraint != null) {
                        final List<String> children = constraint.getTaxa();
                        if (children.size() == 1) {
                            final String constraintTaxonName = children.get(0);
                            final TaxonTree taxonTree = TaxonModels.find(constraints.getTaxaUid());
                            final SimpleTaxon constraintTaxon = taxonTree.findTaxonByName(constraintTaxonName);
                            if (constraintTaxon != null && SimpleTaxon.isSpecies(constraintTaxon)) {
                                return;
                            }
                        }
                    }
                    final ArrayList<String> taxa = userModel.getTaxa();
                    final boolean toAdd = (taxa == null || !taxa.contains(name));
                    if (toAdd) {
                        taxa.add(name);
                    }
                    else {
                        taxa.remove(name);
                    }
                    tree.repaint();
                    for (int i = 0; i < componentsToRepaint.size(); i++) {
                        final JComponent component = componentsToRepaint.get(i);
                        component.repaint();
                    }
                    final ArrayList<SpeciesSelectionListener> copy = new ArrayList<SpeciesSelectionListener>(listeners);
                    for (int i = 0; i < copy.size(); i++) {
                        final SpeciesSelectionListener listener = copy.get(i);
                        listener.speciesSelectionChanged();
                    }
                }
            }
        }
    }

    private class HideConstraintlessTaxon implements Filter {

        public boolean matches(final Object object) {
            final SimpleTaxon taxon = (SimpleTaxon) object;
            final boolean isSpecies = SimpleTaxon.isSpecies(taxon);
            final boolean hasConstraint = constraints.findConstraint(taxon.getName()) != null;
            final boolean isShown = isSpecies || hasConstraint;
            if (DEBUG) LOG.debug((isShown ? "hiding  " : "showing ") + object);
            return isShown;
        }
    }

    private class HideNonSelectedSpecies implements Filter {

        public boolean matches(final Object object) {
            final SimpleTaxon taxon = (SimpleTaxon) object;
            final boolean isSpecies = SimpleTaxon.isSpecies(taxon);
            return !isSpecies || userModel.getTaxa().contains(taxon.getName());
        }
    }
}
