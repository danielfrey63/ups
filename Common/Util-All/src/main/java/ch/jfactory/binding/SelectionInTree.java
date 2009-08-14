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
package ch.jfactory.binding;

import ch.jfactory.component.tree.TreeUtils;
import com.jgoodies.binding.value.ValueModel;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**
 * Original from Thomas Wegmüller.
 *
 * @author Daniel Frey
 * @version $Revision: 1.3 $ $Date: 2006/03/14 21:27:55 $
 */
public class SelectionInTree extends DefaultTreeSelectionModel implements TreeSelectionModel
{

    public static final String PROPERTYNAME_TREEMODEL = "treeModel";

    private TreeModel treeModel;

    /** Refers to the underlying ValueModel that stores the state. */
    private ValueModel valueModel;

    private TreeSelectionListener selectionListener = new TreeSelectionHandler();

    private PropertyChangeListener subjectChangeListener = new SelectionChangeHandler();

    public SelectionInTree(final ValueModel valueModel, final TreeModel treeModel)
    {
        setTreeModel(treeModel);
        setValueModel(valueModel);
        super.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        addTreeSelectionListener(selectionListener);
        valueModel.addValueChangeListener(subjectChangeListener);
    }

    public void setValueModel(final ValueModel valueModel)
    {
        this.valueModel = valueModel;
    }

    public void setSelectionMode(final int mode)
    {
    }

    private void updateSubjectSilenty(final TreePath path)
    {
        valueModel.removeValueChangeListener(subjectChangeListener);
        valueModel.setValue(path == null ? null : path.getLastPathComponent());
        valueModel.addValueChangeListener(subjectChangeListener);
    }

    private void changePathSilenty()
    {
        removeTreeSelectionListener(selectionListener);
        setSelectionPath(TreeUtils.findPathInTreeModel(getTreeModel(), valueModel.getValue()));
        addTreeSelectionListener(selectionListener);
    }

    public TreeModel getTreeModel()
    {
        return treeModel;
    }

    public void setTreeModel(final TreeModel treeModel)
    {
        this.treeModel = treeModel;
    }

    private class TreeSelectionHandler implements TreeSelectionListener
    {

        public void valueChanged(final TreeSelectionEvent e)
        {
            final TreePath[] path = getSelectionPaths();
            if (path == null)
            {
                updateSubjectSilenty(null);
            }
            else
            {
                final TreePath value = path[path.length - 1];
                updateSubjectSilenty(value);
            }
        }
    }

    private class SelectionChangeHandler implements PropertyChangeListener
    {

        public void propertyChange(final PropertyChangeEvent evt)
        {
            changePathSilenty();
        }
    }
}
