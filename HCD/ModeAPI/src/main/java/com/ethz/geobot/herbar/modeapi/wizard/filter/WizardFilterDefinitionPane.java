package com.ethz.geobot.herbar.modeapi.wizard.filter;

import ch.jfactory.application.presentation.Constants;
import ch.jfactory.application.view.dialog.ListDialog;
import ch.jfactory.component.tree.AbstractMutableTreeNode;
import ch.jfactory.component.tree.TreeExpandedRestorer;
import ch.jfactory.component.tree.TreeExpander;
import ch.jfactory.lang.ArrayUtils;
import ch.jfactory.lang.ToStringComparator;
import ch.jfactory.resource.ImageLocator;
import ch.jfactory.resource.Strings;
import com.ethz.geobot.herbar.gui.tax.TaxTreeDialog;
import com.ethz.geobot.herbar.modeapi.wizard.WizardModel;
import com.ethz.geobot.herbar.modeapi.wizard.WizardPane;
import com.ethz.geobot.herbar.model.Level;
import com.ethz.geobot.herbar.model.LevelComparator;
import com.ethz.geobot.herbar.model.Taxon;
import com.ethz.geobot.herbar.model.filter.FilterDefinitionDetail;
import com.ethz.geobot.herbar.model.filter.FilterModel;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import org.apache.log4j.Category;

/**
 * WizardPane to display Order selection
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:13 $
 */
public class WizardFilterDefinitionPane extends WizardPane {

    private static final Category cat = Category.getInstance(WizardFilterDefinitionPane.class);

    /**
     * name of the pane
     */
    public static final String NAME = "filter.define";

    private String filterPropertyName;
    private DefaultTreeModel model;
    private JTree tree;
    private JScrollPane scrollPane;

    public WizardFilterDefinitionPane(String filterPropertyName) {
        super(NAME, new String[]{filterPropertyName});
        this.filterPropertyName = filterPropertyName;
    }

    public void activate() {
        FilterModel filterModel = (FilterModel) getProperty(filterPropertyName);
        if (model == null) {
            filterModel.addFilterDetail();
        }
        model = new DefaultTreeModel(new FilterTreeNode(filterModel));
        tree.setModel(model);
        new TreeExpander(tree, 3);
        checkLevels((FilterTreeNode) model.getRoot());
    }

    protected JPanel createDisplayPanel(String prefix) {
        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                TreeExpandedRestorer ter = new TreeExpandedRestorer(tree);
                ter.save();
                model.reload();
                ter.restore();
            }
        });

        JPanel text = createTextPanel(prefix);
        JPanel title = createDefaultTitlePanel(prefix);
        JPanel taxonText = createTextPanel(prefix + ".TAXON");
        JPanel levelText = createTextPanel(prefix + ".LEVEL");

        tree = createTree();

        JButton addTaxon = createAddTaxonButton(prefix);
        JButton deleteTaxon = createDeleteTaxaButton(prefix);
        JButton editTaxon = createEditTaxonButton(prefix);
        JButton addLevel = createAddLevelsButton(prefix);
        JButton deleteLevel = createDeleteLevelsButton(prefix);

        int gap = Constants.GAP_WITHIN_TOGGLES;

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.gridwidth = 7;
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(text, gbc);
        gbc.gridy += 1;
        panel.add(title, gbc);

        gbc.gridwidth = 4;
        gbc.gridy += 1;
        panel.add(taxonText, gbc);
        gbc.gridwidth = 3;
        gbc.gridx += 4;
        panel.add(levelText, gbc);

        gbc.gridy += 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(gap, 0, gap, gap);

        gbc.weightx = 0.0;
        gbc.gridx = 0;
        panel.add(addTaxon, gbc);
        gbc.gridx += 1;
        panel.add(deleteTaxon, gbc);
        gbc.gridx += 1;
        panel.add(editTaxon, gbc);
        gbc.weightx = 0.5;
        gbc.gridx += 1;
        panel.add(new JPanel(), gbc);

        gbc.weightx = 0.0;
        gbc.gridx += 1;
        panel.add(addLevel, gbc);
        gbc.gridx += 1;
        panel.add(deleteLevel, gbc);
        gbc.weightx = 0.5;
        gbc.gridx += 1;
        panel.add(new JPanel(), gbc);

        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.weightx = 0.0;
        gbc.gridwidth = 7;
        gbc.weighty = 1.0;
        gbc.gridx = 0;
        gbc.gridy += 1;
        panel.add(scrollPane, gbc);

        return panel;
    }

    private JTree createTree() {
        JTree tree = new JTree();
        scrollPane = new JScrollPane(tree);
        scrollPane.setPreferredSize(new Dimension(400, 10));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        tree.setRootVisible(false);
        tree.setShowsRootHandles(true);
        tree.setCellRenderer(new FilterTreeCellRenderer(this));
        tree.setAutoscrolls(true);

        return tree;
    }

    private JButton createDeleteLevelsButton(String prefix) {
        ActionListener action = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!isVisible()) {
                    return;
                }
                TreePath[] paths = tree.getSelectionPaths();
                if (paths == null) {
                    return;
                }
                TreeExpandedRestorer ter = new TreeExpandedRestorer(tree);
                ter.save();
                for (int i = 0; i < paths.length; i++) {
                    TreePath path = paths[ i ];
                    FilterTreeNode node = (FilterTreeNode) path.getLastPathComponent();
                    node.removeFromParent();
                    ter.remove(path);
                }
                model.reload();
                ter.restore();
            }
        };
        final JButton button = createListEditButton(prefix + ".LEVEL.DELETE", action);
        button.setEnabled(false);
        // make sure not all level would be deleted for a scope, and that all selected nodes are levels.
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                JTree tree = (JTree) e.getSource();
                TreePath[] paths = tree.getSelectionPaths();
                if (paths == null) {
                    button.setEnabled(false);
                }
                else {
                    boolean allSelectionsAreLevels = true;
                    for (int i = 0; i < paths.length; i++) {
                        allSelectionsAreLevels &= isLeaf(paths[ i ]);
                    }
                    boolean wouldRemoveAllLevels = false;
                    TreeNode root = (TreeNode) tree.getModel().getRoot();
                    for (int i = 0; i < root.getChildCount(); i++) {
                        TreeNode taxon = root.getChildAt(i);
                        int children = taxon.getChildCount();
                        for (int j = 0; j < paths.length; j++) {
                            TreeNode node = (TreeNode) paths[ j ].getParentPath().getLastPathComponent();
                            if (node == taxon) {
                                children--;
                            }
                        }
                        wouldRemoveAllLevels |= (children == 0);
                    }
                    button.setEnabled(allSelectionsAreLevels && !wouldRemoveAllLevels);
                }
            }
        });
        return button;
    }

    private boolean isLeaf(TreePath path) {
        TreeNode node = (TreeNode) path.getLastPathComponent();
        boolean isLeaf = node.isLeaf();
        return isLeaf;
    }

    private JButton createAddLevelsButton(String prefix) {
        ActionListener action = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!isVisible()) {
                    return;
                }
                Level[] levels = getValidNewLevels(tree.getSelectionPath());
                Arrays.sort(levels, new ToStringComparator());
                JDialog parent = (JDialog) WizardFilterDefinitionPane.this.getTopLevelAncestor();
                ListDialog dialog = new ListDialog(parent, "DIALOG.LEVELS", levels);
                dialog.setSize(300, 300);
                dialog.setLocationRelativeTo(WizardFilterDefinitionPane.this);
                dialog.setVisible(true);
                if (dialog.isAccepted()) {
                    Level[] newLevels = (Level[]) dialog.getSelectedData(new Level[0]);
                    insertNewLevels(newLevels);
                }
            }
        };
        final JButton button = createListEditButton(prefix + ".LEVEL.ADD", action);
        button.setEnabled(false);
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            // button is enabled if there is exactly one non-leaf node selected - a scope - and the assigned levels
            // are not already complete, or any levels of the same scope (and the scope itself) are selected.
            public void valueChanged(TreeSelectionEvent e) {
                JTree tree = (JTree) e.getSource();
                TreePath[] paths = tree.getSelectionPaths();
                if (paths == null || paths.length == 0) {
                    button.setEnabled(false);
                    return;
                }
                // make sure that all selected nodes are from the same parent. the parent may be selected as well.
                Object theParent = null;
                boolean ok = true;
                TreePath parentPath = null;
                for (int i = 0; i < paths.length; i++) {
                    TreePath path = paths[ i ];
                    TreeNode treeNode = (TreeNode) (path.getLastPathComponent());
                    if (treeNode.isLeaf()) {
                        Object parent = path.getParentPath().getLastPathComponent();
                        if (theParent == null) {
                            theParent = parent;
                            parentPath = path.getParentPath();
                        }
                        ok &= theParent == parent;
                    }
                    else {
                        if (theParent == null) {
                            theParent = treeNode;
                            parentPath = path;
                        }
                        ok &= theParent == treeNode;
                    }
                }
                // look for new valid levels
                if (ok) {
                    Level[] validNewLevels = getValidNewLevels(parentPath);
                    ok &= (validNewLevels.length > 0);
                }
                button.setEnabled(ok);
            }
        });
        return button;
    }

    private void insertNewLevels(Level[] newLevels) {
        TreeExpandedRestorer ter = new TreeExpandedRestorer(tree);
        ter.save();
        for (int i = 0; i < newLevels.length; i++) {
            FilterTreeNode newNode = new FilterTreeNode(newLevels[ i ]);
            FilterTreeNode filterTreeNode = getDetailsNode(tree.getSelectionPath());
            FilterDefinitionDetail detail = (FilterDefinitionDetail) filterTreeNode.getUserObject();
            filterTreeNode.insert(newNode, getIndex(detail, newLevels[ i ]));
            ter.addSelection(new TreePath(model.getPathToRoot(newNode)));
        }
        model.reload();
        ter.restore();
    }

    private Level[] getValidNewLevels(TreePath selectionPath) {
        FilterTreeNode parentNode = getFilterModelNode(selectionPath);
        FilterModel filterModel = (FilterModel) parentNode.getUserObject();
        FilterTreeNode filterTreeNode = getDetailsNode(selectionPath);
        FilterDefinitionDetail detail = (FilterDefinitionDetail) filterTreeNode.getUserObject();
        Level[] levels = getValidNewLevels(filterModel, detail);
        return levels;
    }

    private Level[] getValidNewLevels(FilterModel filterModel, FilterDefinitionDetail detail) {
        // make sure to display only valid levels. valid levels are all levels of the base list which occur
        // also in the scopes sublevels, but are not already chosen.
        Taxon scope = detail.getScope();
        Level[] proto = new Level[0];
        Level[] levels = scope.getSubLevels();
        boolean top1 = scope.equals(filterModel.getRootTaxon());
        boolean top2 = filterModel.getRootTaxon().equals(scope);
        if (top1 || top2) {
            levels = (Level[]) ArrayUtils.unite(levels, filterModel.getDependantModel().getLevels(), proto, new LevelComparator());
            levels = (Level[]) ArrayUtils.removeAll(levels, detail.getLevels(), proto);
        }
        else {
            levels = (Level[]) ArrayUtils.removeAll(levels, detail.getLevels(), proto);
            levels = (Level[]) ArrayUtils.intersect(levels, filterModel.getDependantModel().getLevels(), proto);
        }
        Arrays.sort(levels, new LevelComparator());

        return levels;
    }

    private FilterTreeNode getFilterModelNode(TreePath selectionPath) {
        FilterTreeNode filterTreeNode = getDetailsNode(selectionPath);
        return (FilterTreeNode) filterTreeNode.getParent();
    }

    private FilterTreeNode getDetailsNode(TreePath selectionPath) {
        FilterTreeNode filterTreeNode = (FilterTreeNode) selectionPath.getLastPathComponent();
        if (filterTreeNode.isLeaf()) {
            filterTreeNode = (FilterTreeNode) filterTreeNode.getParent();
        }
        return filterTreeNode;
    }

    private JButton createDeleteTaxaButton(String prefix) {
        ActionListener action = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!isVisible()) {
                    return;
                }
                TreePath[] paths = tree.getSelectionPaths();
                TreeExpandedRestorer ter = new TreeExpandedRestorer(tree);
                ter.save();
                for (int i = 0; i < paths.length; i++) {
                    TreePath path = paths[ i ];
                    FilterTreeNode node = (FilterTreeNode) path.getLastPathComponent();
                    FilterTreeNode root = (FilterTreeNode) model.getRoot();
                    root.remove(node);
                    ter.remove(path);

                }
                model.reload();
                ter.restore();
            }
        };
        final JButton button = createListEditButton(prefix + ".TAXON.DELETE", action);
        button.setEnabled(false);
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            // button gets activated when only scopes are selected
            public void valueChanged(TreeSelectionEvent e) {
                JTree tree = (JTree) e.getSource();
                TreePath[] paths = tree.getSelectionPaths();
                if (paths == null || paths.length == 0) {
                    button.setEnabled(false);
                }
                else {
                    boolean allTaxa = true;
                    for (int i = 0; i < paths.length; i++) {
                        TreePath path = paths[ i ];
                        TreeNode node = (TreeNode) path.getLastPathComponent();
                        allTaxa &= !node.isLeaf();
                    }
                    TreeNode root = (TreeNode) tree.getModel().getRoot();
                    button.setEnabled(allTaxa && root.getChildCount() != paths.length);
                }
            }
        });
        return button;
    }

    private boolean checkLevels(FilterTreeNode root) {
        TreeExpandedRestorer ter = new TreeExpandedRestorer(tree);
        ter.save();
        boolean ok;
        if (!(ok = cleanupLevels(root))) {
            JOptionPane.showMessageDialog(WizardFilterDefinitionPane.this,
                    Strings.getString(prefix + ".MESSAGE.CLEAN.TEXT"),
                    Strings.getString(prefix + ".MESSAGE.CLEAN.TITLE"),
                    JOptionPane.INFORMATION_MESSAGE);
        }
        model.reload();
        ter.restore();
        return ok;
    }

    private JButton createEditTaxonButton(final String prefix) {
        ActionListener action = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!isVisible()) {
                    return;
                }
                FilterModel filterModel = (FilterModel) getProperty(filterPropertyName);
                Taxon rootTaxon = filterModel.getDependantModel().getRootTaxon();
                TaxTreeDialog dialog = new TaxTreeDialog((JDialog) getTopLevelAncestor(), rootTaxon);
                dialog.setSize(400, 500);
                dialog.setLocationRelativeTo(WizardFilterDefinitionPane.this);
                FilterTreeNode node = (FilterTreeNode) tree.getSelectionPath().getLastPathComponent();
                FilterDefinitionDetail detail = (FilterDefinitionDetail) node.getUserObject();
                Taxon scope = detail.getScope();
                dialog.setSelectedTaxon(scope);
                dialog.setVisible(true);
                if (dialog.isAccepted()) {
                    TreeExpandedRestorer ter = new TreeExpandedRestorer(tree);
                    ter.save();
                    scope = dialog.getSelectedTaxon();
                    detail.setScope(scope);
                    checkLevels(node);
                    model.reload();
                    ter.restore();
                }
            }
        };
        final JButton button = createListEditButton(prefix + ".TAXON.EDIT", action);
        button.setEnabled(false);
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                JTree tree = (JTree) e.getSource();
                TreePath[] paths = tree.getSelectionPaths();
                if (paths == null || paths.length != 1) {
                    button.setEnabled(false);
                }
                else {
                    TreePath path = paths[ 0 ];
                    TreeNode node = (TreeNode) path.getLastPathComponent();
                    button.setEnabled(!node.isLeaf());
                }
            }
        });
        return button;
    }

    private JButton createAddTaxonButton(String prefix) {
        ActionListener action = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!isVisible()) {
                    return;
                }
                FilterModel filterModel = (FilterModel) getProperty(filterPropertyName);
                Taxon rootTaxon = filterModel.getDependantModel().getRootTaxon();
                TaxTreeDialog dialog = new TaxTreeDialog((JDialog) getTopLevelAncestor(), rootTaxon);
                dialog.setSize(400, 500);
                dialog.setLocationRelativeTo(WizardFilterDefinitionPane.this);
                dialog.setVisible(true);
                if (dialog.isAccepted()) {
                    Taxon scope = dialog.getSelectedTaxon();
                    Level[] levels = scope.getSubLevels();
                    FilterDefinitionDetail detail = filterModel.createFilterDetail(scope, levels);
                    FilterTreeNode root = (FilterTreeNode) model.getRoot();
                    root.insert(new FilterTreeNode(detail), root.getChildCount());

                    TreeExpandedRestorer ter = new TreeExpandedRestorer(tree);
                    ter.save();
                    model.reload();
                    ter.restore();
                }
            }
        };
        JButton button = createListEditButton(prefix + ".TAXON.ADD", action);
        return button;
    }

    private int getIndex(FilterDefinitionDetail detail, Level newLevel) {
        int index = 0;
        if (detail.getLevels().length == 0) {
            return 0;
        }
        Level tempLevel;
        Level[] assigned;
        do {
            assigned = detail.getLevels();
            tempLevel = assigned[ index++ ];
        }
        while (tempLevel != null && tempLevel.isHigher(newLevel) && index < assigned.length);
        return index - (tempLevel != null && tempLevel.isHigher(newLevel) ? 0 : 1);
    }

    /**
     * Checks for levels not consistent with the scope and removes them. Makes sure that at least one level is in the
     * scope.
     *
     * @param node the FilterTreeNode containing the FilterDefinitionDetail to check
     * @return <code>true</code> if no changes were made
     */
    private boolean cleanupLevels(FilterTreeNode node) {
        Object userObject = node.getUserObject();
        boolean ok = true;
        if (userObject instanceof FilterDefinitionDetail) {
            // collect levels to remove
            FilterDefinitionDetail detail = (FilterDefinitionDetail) userObject;
            List toRemove = new ArrayList();
            for (int i = 0; i < node.getChildCount(); i++) {
                FilterTreeNode child = (FilterTreeNode) node.getChildAt(i);
                Level level = (Level) child.getUserObject();
                if (!isConsistent(detail, level)) {
                    toRemove.add(child);
                    ok = false;
                }
            }
            // remove them
            for (Iterator iterator = toRemove.iterator(); iterator.hasNext();) {
                FilterTreeNode child = (FilterTreeNode) iterator.next();
                node.remove(child);
            }
            // make sure at least one detail is there
            if (node.getChildCount() == 0) {
                Level level = detail.getScope().getLevel();
                node.insert(new FilterTreeNode(level), getIndex(detail, level));
                ok = false;
            }
            return ok;
        }
        else { // is a filter model
            for (int i = 0; i < node.getChildCount(); i++) {
                ok &= cleanupLevels((FilterTreeNode) node.getChildAt(i));
            }
        }
        return ok;
    }

    /**
     * Checks whether the level given is being contained in the sublevels of the scope.
     *
     * @param detail       the FilterDefinitionDetail of the level to evaluate
     * @param levelToCheck the level to validate
     * @return <code>true</code> if the level is being contained in the scopes levels
     */
    protected boolean isConsistent(FilterDefinitionDetail detail, Level levelToCheck) {
        Taxon scope = detail.getScope();
        Level[] scopesLevel = scope.getSubLevels();
        return ArrayUtils.contains(scopesLevel, levelToCheck) || levelToCheck == scope.getLevel();
    }

    /**
     * Checks whether the given scope is consistent with the other scopes in the model. Expecially it is checked whether
     * the given scope is: <ul> <li>not the same any other one in the model</li> <li>is not a child of any scope in the
     * model</li> <li>is not a parent of any scope in the model</li> </ul>
     *
     * @param detailToCheck the FilterDefinitionDetail containing the scope under investigation
     * @return <code>true</code> if all conditions evaluate to <code>true</code>
     */
    protected boolean isConsistent(FilterDefinitionDetail detailToCheck) {
        FilterModel filterModel = (FilterModel) getProperty(filterPropertyName);
        FilterDefinitionDetail[] details = filterModel.getFilterDetails();
        // make sure two same scopes are properly recognized
        details = (FilterDefinitionDetail[]) ArrayUtils.remove(details, detailToCheck, new FilterDefinitionDetail[0]);
        Taxon checkScope = detailToCheck.getScope();
        Level checkLevel = checkScope.getLevel();
        boolean consistent = true;
        for (int i = 0; i < details.length; i++) {
            FilterDefinitionDetail detail = details[ i ];
            Taxon scope = detail.getScope();
            Level level = scope.getLevel();
            consistent &=
                    (level == checkLevel && scope != checkScope) ||
                            (level != null && level.isHigher(checkLevel) && !containsTaxon(checkScope, scope)) ||
                            (checkLevel != null && checkLevel.isHigher(level) && !containsTaxon(scope, checkScope)) ||
                            (containsTaxon(checkScope, scope) && !ArrayUtils.haveCommon(detailToCheck.getLevels(), detail.getLevels())) ||
                            (containsTaxon(scope, checkScope) && !ArrayUtils.haveCommon(detailToCheck.getLevels(), detail.getLevels())) ||
                            (scope == checkScope && !ArrayUtils.haveCommon(detailToCheck.getLevels(), detail.getLevels()));
        }
        return consistent;
    }

    public void registerPropertyChangeListener(WizardModel wizardModel) {
        wizardModel.addPropertyChangeListener(filterPropertyName, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent event) {
                FilterModel fModel = (FilterModel) event.getNewValue();
                if (fModel != null) {
                    if (cat.isDebugEnabled()) {
                        cat.debug("model change to: " + fModel.getName());
                    }
                    model = new DefaultTreeModel(new FilterTreeNode(fModel));
                    tree.setModel(model);
                    model.reload();
                }
                else {
                    cat.warn("change filter model to null.");
                }
            }
        });
    }

    private boolean containsTaxon(Taxon toBeSearchedFor, Taxon possibleParent) {
        return ArrayUtils.contains(possibleParent.getAllChildTaxa(toBeSearchedFor.getLevel()), toBeSearchedFor);
    }

    public static class FilterTreeNode extends AbstractMutableTreeNode {

        private FilterTreeNode[] children;
        private FilterTreeNode parent;
        private Object userObject;

        public FilterTreeNode(Object userObject) {
            setUserObject(userObject);
        }

        public void insert(MutableTreeNode child, int index) {
            FilterTreeNode childNode = (FilterTreeNode) child;
            // update model
            if (userObject instanceof FilterModel) {
                if (!(childNode.getUserObject() instanceof FilterDefinitionDetail)) {
                    throw new IllegalArgumentException("Child of a FilterModel node must be a FilterDefinitionDetail");
                }
                FilterModel filterModel = (FilterModel) userObject;
                FilterDefinitionDetail detail = (FilterDefinitionDetail) childNode.getUserObject();
                filterModel.addFilterDetail(detail);
            }
            else if (userObject instanceof FilterDefinitionDetail) {
                if (!(childNode.getUserObject() instanceof Level)) {
                    throw new IllegalArgumentException("Child of a FilterDefinitionDetail node must be a Level");
                }
                FilterDefinitionDetail detail = (FilterDefinitionDetail) userObject;
                Level level = (Level) childNode.getUserObject();
                List levels = new ArrayList(Arrays.asList(detail.getLevels()));
                levels.add(index, level);
                detail.setLevels((Level[]) levels.toArray(new Level[0]));
            }
            // update cache
            List childrenList = new ArrayList(Arrays.asList(children));
            childrenList.add(index, childNode);
            childNode.parent = this;
            childNode.children = new FilterTreeNode[childNode.getChildCount()];
            children = (FilterTreeNode[]) childrenList.toArray(new FilterTreeNode[0]);
        }

        public void remove(MutableTreeNode child) {
            FilterTreeNode childNode = (FilterTreeNode) child;
            // update model
            if (userObject instanceof FilterModel) {
                FilterModel filterModel = (FilterModel) userObject;
                FilterDefinitionDetail detail = (FilterDefinitionDetail) childNode.getUserObject();
                filterModel.removeFilterDetail(detail);
            }
            else if (userObject instanceof FilterDefinitionDetail) {
                FilterDefinitionDetail detail = (FilterDefinitionDetail) userObject;
                Level[] levels = detail.getLevels();
                levels = (Level[]) ArrayUtils.remove(levels, childNode.getUserObject(), new Level[0]);
                detail.setLevels(levels);
            }
            // update cache
            children = (FilterTreeNode[]) ArrayUtils.remove(children, child, new FilterTreeNode[0]);
        }

        public void setParent(MutableTreeNode newParent) {
            throw new NoSuchMethodError("setParent not implemented");
        }

        public void setUserObject(Object userObject) {
            this.userObject = userObject;
        }

        public boolean getAllowsChildren() {
            return userObject instanceof FilterModel || userObject instanceof FilterDefinitionDetail;
        }

        public TreeNode getChildAt(int childIndex) {
            if (children == null) {
                children = new FilterTreeNode[getChildCount()];
            }
            if (children.length < childIndex || children[ childIndex ] == null) {
                FilterTreeNode result = null;
                if (userObject instanceof FilterModel) {
                    FilterModel filterModel = (FilterModel) userObject;
                    FilterDefinitionDetail filterDetail = filterModel.getFilterDetails()[ childIndex ];
                    result = new FilterTreeNode(filterDetail);
                }
                else if (userObject instanceof FilterDefinitionDetail) {
                    FilterDefinitionDetail detail = (FilterDefinitionDetail) userObject;
                    Level level = detail.getLevels()[ childIndex ];
                    result = new FilterTreeNode(level);
                }
                result.parent = this;
                children[ childIndex ] = result;
                return result;
            }
            else {
                return children[ childIndex ];
            }
        }

        public int getChildCount() {
            if (userObject instanceof FilterModel) {
                return ((FilterModel) userObject).getFilterDetails().length;
            }
            else if (userObject instanceof FilterDefinitionDetail) {
                return ((FilterDefinitionDetail) userObject).getLevels().length;
            }
            else if (userObject instanceof Level) {
                return 0;
            }
            return 0;
        }

        public TreeNode getParent() {
            return parent;
        }

        public Object getUserObject() {
            return userObject;
        }

        public String toString() {
            if (userObject instanceof FilterModel) {
                return "FilterModel root";
            }
            else if (userObject instanceof FilterDefinitionDetail) {
                FilterDefinitionDetail detail = (FilterDefinitionDetail) userObject;
                return detail.getScope() + " " + Arrays.asList(detail.getLevels());
            }
            else if (userObject instanceof Level) {
                return userObject.toString();
            }
            return "";
        }
    }

    public static class FilterTreeCellRenderer extends DefaultTreeCellRenderer {

        private WizardFilterDefinitionPane pane;

        public FilterTreeCellRenderer(WizardFilterDefinitionPane pane) {
            this.pane = pane;
        }

        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
                                                      boolean leaf, int row, boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
            if (value instanceof FilterTreeNode) {
                FilterTreeNode filterTreeNode = (FilterTreeNode) value;
                Object newValue = filterTreeNode.getUserObject();
                if (newValue instanceof FilterModel) {
                    setText(((FilterModel) newValue).getName());
                }
                else if (newValue instanceof FilterDefinitionDetail) {
                    FilterDefinitionDetail detail = (FilterDefinitionDetail) newValue;
                    Taxon scope = detail.getScope();
                    setText(scope.getName());
                    Level level = scope.getLevel();
                    if (level != null) {
                        setIcon(ImageLocator.getIcon("icon" + level.getName() + ".gif"));
                    }
                    if (!pane.isConsistent(detail)) {
                        setForeground(Color.red);
                    }
                }
                else if (newValue instanceof Level) {
                    Level level = (Level) newValue;
                    String name = level.getName();
                    setText(name);
                    ImageIcon icon = ImageLocator.getIcon("icon" + name + ".gif");
                    int diff = pane.scrollPane.getViewport().getWidth() / 2 - 30;
                    setIcon(new ImageIcon(new PrefixedIcon(icon, diff)));
                    FilterTreeNode parent = (FilterTreeNode) filterTreeNode.getParent();
                    FilterDefinitionDetail detail = (FilterDefinitionDetail) parent.getUserObject();
                    if (!pane.isConsistent(detail, level)) {
                        setForeground(Color.red);
                    }
                }
                else {
                    setText("unknown value of class " + newValue.getClass());
                }
                if (selected) {
                    setBackgroundSelectionColor(UIManager.getColor("Tree.selectionBackground"));
                }
                else {
                    setBackgroundNonSelectionColor(UIManager.getColor("Tree.background"));
                }
            }

            return this;
        }
    }

    public static class PrefixedIcon extends BufferedImage {

        public PrefixedIcon(ImageIcon icon, int sizeOfPrefix) {
            super(32 + sizeOfPrefix, 16, TYPE_INT_ARGB_PRE);

            Map map = new HashMap();
            map.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            map.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            RenderingHints hints = new RenderingHints(map);
            Graphics2D g2 = this.createGraphics();
            g2.setRenderingHints(hints);

            int w = 32;
            int h = 16;
            g2.setColor(UIManager.getColor("Tree.hash"));
            drawDashedLine(g2, 0, h / 2, sizeOfPrefix - 5, h / 2, 8, 4);
            g2.drawImage(icon.getImage(), sizeOfPrefix, 0, w, h, null);
        }

        private void drawDashedLine(Graphics2D g, int x1, int y1, int x2, int y2, int dashLength, int dashSpace) {
            while (x1 < x2) {
                g.drawLine(x1, y1, Math.min(x1 + dashLength, x2), y2);
                x1 += dashLength + dashSpace;
            }
        }
    }
}
