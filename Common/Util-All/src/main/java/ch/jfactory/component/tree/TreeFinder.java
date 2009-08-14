/*
 * TreeFinder.java
 *
 * Created on 31. Mai 2002, 15:22
 * Created by Daniel Frey
 */
package ch.jfactory.component.tree;

import javax.swing.tree.TreePath;

/**
 * Interface which allows to influence the selected path of a component that contains a tree. Only one single selection
 * is supported.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:58 $
 */
public interface TreeFinder
{

    /**
     * Sets the path found for the node found. The path may be null.
     *
     * @param tp TreePath found
     */
    public void setSelection(TreePath tp);

}
