// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 09.06.2005 09:02:32
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) fieldsfirst nonlb

package ch.jfactory.application.view.search;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JTree;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

//
//

public class TreeSearchable extends Searchable
    implements TreeModelListener {

    private boolean _recursive;
    private transient List _treePathes;

    public TreeSearchable(final JTree jtree) {
        super(jtree);
        _recursive = false;
        if(jtree.getModel() != null)
            jtree.getModel().addTreeModelListener(this);
    }

    public boolean isRecursive() {
        return _recursive;
    }

    public void setRecursive(final boolean flag) {
        _recursive = flag;
        resetTreePathes();
    }

    public void uninstallListeners() {
        super.uninstallListeners();
        if((component instanceof JTree) && ((JTree)component).getModel() != null)
            ((JTree)component).getModel().removeTreeModelListener(this);
    }

    protected void setSelectedIndex(final int i, final boolean flag) {
        if(!isRecursive()) {
            if(flag)
                ((JTree)component).addSelectionInterval(i, i);
            else
                ((JTree)component).setSelectionRow(i);
            ((JTree)component).scrollRowToVisible(i);
        } else {
            final Object obj = getElementAt(i);
            if(obj instanceof TreePath) {
                final TreePath treepath = (TreePath)obj;
                if(treepath != null) {
                    if(flag)
                        ((JTree)component).addSelectionPath(treepath);
                    else
                        ((JTree)component).setSelectionPath(treepath);
                    ((JTree)component).scrollPathToVisible(treepath);
                }
            }
        }
    }

    protected int getSelectedIndex() {
        if(!isRecursive()) {
            final int[] ai = (int[])((JTree)component).getSelectionRows();
            return ai == null || ai.length == 0 ? -1 : ai[0];
        }
        final TreePath[] atreepath = ((JTree)component).getSelectionPaths();
        if(atreepath != null && atreepath.length > 0)
            return getTreePathes().indexOf(atreepath[0]);
        else
            return -1;
    }

    protected Object getElementAt(final int i) {
        if(i == -1)
            return null;
        if(!isRecursive())
            return ((JTree)component).getPathForRow(i);
        else
            return getTreePathes().get(i);
    }

    protected int getElementCount() {
        if(!isRecursive())
            return ((JTree)component).getRowCount();
        else
            return getTreePathes().size();
    }

    protected void populateTreePathes() {
        _treePathes = new ArrayList();
        final Object obj = ((JTree)component).getModel().getRoot();
        populateTreePathes0(obj, new TreePath(obj));
    }

    protected void resetTreePathes() {
        _treePathes = null;
    }

    private void populateTreePathes0(final Object obj, final TreePath treepath) {
        _treePathes.add(treepath);
        if(obj instanceof TreeNode) {
            for(int i = 0; i < ((TreeNode)obj).getChildCount(); i++) {
                final TreeNode treenode = ((TreeNode)obj).getChildAt(i);
                populateTreePathes0(treenode, treepath.pathByAddingChild(treenode));
            }

        }
    }

    protected List getTreePathes() {
        if(_treePathes == null)
            populateTreePathes();
        return _treePathes;
    }

    protected String convertElementToString(final Object obj) {
        if(obj instanceof TreePath) {
            final Object obj1 = ((TreePath)obj).getLastPathComponent();
            return obj1.toString();
        }
        if(obj != null)
            return obj.toString();
        else
            return "";
    }

    public void treeNodesChanged(final TreeModelEvent treemodelevent) {
        hidePopup();
        resetTreePathes();
    }

    public void treeNodesInserted(final TreeModelEvent treemodelevent) {
        hidePopup();
        resetTreePathes();
    }

    public void treeNodesRemoved(final TreeModelEvent treemodelevent) {
        hidePopup();
        resetTreePathes();
    }

    public void treeStructureChanged(final TreeModelEvent treemodelevent) {
        hidePopup();
        resetTreePathes();
    }
}