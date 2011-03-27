/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.component.tree;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeModelEvent;
import javax.swing.tree.TreeModel;

/**
 * This class solves on of the following tasks:
 *
 * <ul>
 *
 * <li>The expansion state of a tree has to be preserved.</li>
 *
 * <li>The selected nodes of a tree has to be preserved.</li>
 *
 * </ul>
 *
 * The tasks are solved during the following events in the tree:
 *
 * <ul>
 *
 * <li>A tree gets a new model.</li>
 *
 * <li>A trees model is reloaded.</li>
 *
 * </ul>
 *
 * Driven by an idea of J Jenker.
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:58 $
 */
public class TreeUpdater implements Runnable
{
    /** The count of expanded paths that is used to make the expansion asynchronously. */
    private static final int ASYNCHRON_THRESHOLD = 10000;

    /** The expansion listener. */
    private final SimpleTreeExpansionListener expansionListener;

    /** The selection listener. */
    private final SimpleTreeSelectionListener selectionListener;

    /** The tree model listener. Used to refresh the other listener upon model exchanges. */
    private final SimpleTreeModelListener treeModelListener;

    /**
     * Creates new JTreeUpdater.
     *
     * @param tree the tree to observe
     */
    public TreeUpdater( final JTree tree )
    {
        expansionListener = new SimpleTreeExpansionListener( tree );
        selectionListener = new SimpleTreeSelectionListener( tree );
        treeModelListener = new SimpleTreeModelListener();
        tree.addTreeExpansionListener( expansionListener );
        tree.addTreeSelectionListener( selectionListener );
        tree.addPropertyChangeListener( JTree.TREE_MODEL_PROPERTY, new SimplePropertyChangeListener() );
        tree.getModel().addTreeModelListener( treeModelListener );
    }

    /** The method to expand the paths. */
    public synchronized void run()
    {
        expansionListener.restore();
        selectionListener.restore();
    }

    /** Call this method to translate the expanded paths to the ones stored. A call to this method only is necessary if the underlying tree model doesn't call their model listener in a FIFO order. This is the case with the DefaultTreeModel. If the underlying model does call the listeners in the same order as they are registered, an translate should be invoked automatically by the build in tree model listener. */
    public synchronized void update()
    {
        if ( expansionListener.getNumberOfExpandedPaths() > ASYNCHRON_THRESHOLD )
        {
            SwingUtilities.invokeLater( this );
        }
        else
        {
            run();
        }
    }

    /** Listener to inform other listeners about changes in the model. */
    private class SimplePropertyChangeListener implements PropertyChangeListener
    {
        /** {@inheritDoc} */
        public void propertyChange( final PropertyChangeEvent evt )
        {
            ( (TreeModel) evt.getOldValue() ).removeTreeModelListener( treeModelListener );
            ( (TreeModel) evt.getNewValue() ).addTreeModelListener( treeModelListener );
            expansionListener.translate();
            selectionListener.translate();
            update();
        }
    }

    /** Makes sure the updates take place automatically if a tree model has been exchanged. */
    private class SimpleTreeModelListener extends TreeModelAdapter
    {
        /** {@inheritDoc} */
        public void treeStructureChanged( final TreeModelEvent e )
        {
            update();
        }
    }
}
