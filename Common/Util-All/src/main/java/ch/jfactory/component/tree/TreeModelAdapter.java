/* ====================================================================
 *  Copyright 2004 www.jfactory.ch
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied.
 * ====================================================================
 */
package ch.jfactory.component.tree;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;

/**
 * Adapter for dummy implementation of TreeModelListener.
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:58 $
 */
public class TreeModelAdapter implements TreeModelListener
{

    /** {@inheritDoc} */
    public void treeNodesChanged(final TreeModelEvent e)
    {
    }

    /** {@inheritDoc} */
    public void treeNodesInserted(final TreeModelEvent e)
    {
    }

    /** {@inheritDoc} */
    public void treeNodesRemoved(final TreeModelEvent e)
    {
    }

    /** {@inheritDoc} */
    public void treeStructureChanged(final TreeModelEvent e)
    {
    }
}
