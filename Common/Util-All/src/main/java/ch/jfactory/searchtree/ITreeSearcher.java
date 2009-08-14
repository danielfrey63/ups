/* ====================================================================
 *  Copyright 2004 www.jfactory.ch
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied.
 * ====================================================================
 */
package ch.jfactory.searchtree;

import javax.swing.tree.TreePath;

/**
 * Generic interface for a Object searching the tree
 *
 * @author <a href="mail@wegmueller.com">Thomas Wegmueller</a>
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:58 $
 */
public interface ITreeSearcher
{

    /**
     * Get the next TreePath matching the searchCriteria
     *
     * @return javax.swing.tree.TreePath
     */
    TreePath next();

    /**
     * Get the previous TreePath matching the searchCriteria
     *
     * @return javax.swing.tree.TreePath
     */
    TreePath prev();

    /**
     * Get the actually selected Treepath
     *
     * @return javax.swing.tree.TreePath
     */
    TreePath getTreePath();

    /**
     * Get the search Criteria
     *
     * @return the Search Criteria
     */
    String getSearchString();

    /**
     * Set the Search criteria
     *
     * @return javax.swing.tree.TreePath representing the first entry matching (can be null)
     */
    TreePath setSearchString(String searchString);
}
