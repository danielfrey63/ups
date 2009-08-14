/* ====================================================================
 *  Copyright 2004 www.jfactory.ch
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied.
 * ====================================================================
 */

package ch.jfactory.component.tree.filtered;

/**
 * Filter interface to be used together with FilteredTreeModel.
 *
 * @author Daniel Frey
 * @version $Revision: 1.2 $ $Date: 2005/08/07 01:21:55 $
 */
public interface ViewFilter
{

    /**
     * Method to evaluate whether a specific node is shown or not. Implement this method to remove / show nodes in the
     * tree.
     *
     * @param node the node to determine whether it is shown or not
     * @return whether the node is shown or not
     */
    boolean isShown(Object node);
}
