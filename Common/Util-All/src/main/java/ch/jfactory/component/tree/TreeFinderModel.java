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

import java.util.Observable;
import java.util.regex.Pattern;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 * Model to find easily nodes in a tree. To use this model, use code as the following:
 * <pre>
 * final TreeFinderModel myTreeFinder = new TreeFinderModel(myTreeModel);
 * myTreeFinder.find(myString);
 * final TreePath pathFound = myTreeFinder.get();
 * </pre>
 * If no node is found, <code>get()</code> returns <code>null</code>. If you want to select the corresponding node in a tree, one option is to use {@link SearchableTree} which implements the {@link TreeFinder} interface.
 * <pre>
 * final SearchableTree myTree = new SearchableTree(myTreeModel);
 * final TreeFinderModel myTreeFinder = new TreeFinderModel(myTreeModel);
 * myTreeFinder.find(myString);
 * final TreePath pathFound = myTreeFinder.get();
 * myTree.setSelection(pathFound);
 * </pre>
 * Another option is to register an observer to be notified upon found nodes.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.4 $ $Date: 2005/11/17 11:54:58 $
 */
public class TreeFinderModel extends Observable
{
    private int current;

    private TreePath[] found;

    private TreeModel model;

    private Pattern pattern;

    /**
     * Constructor for the FinderModel object
     *
     * @param model
     */
    public TreeFinderModel( final TreeModel model )
    {
        setModel( model );
        // make sure an initial call to notifyObservers will update all
        // Observers
        setChanged();
    }

    public void setModel( final TreeModel model )
    {
        this.model = model;
        current = -1;
        found = new TreePath[0];
    }

    /**
     * Gets the path attribute of the FinderModel object
     *
     * @return the TreePath object
     */
    public TreePath get()
    {
        if ( getCount() == 0 )
        {
            return null;
        }
        else
        {
            return found[current];
        }
    }

    /**
     * Gets the max attribute of the FinderModel object
     *
     * @return The max value
     */
    public int getCount()
    {
        return found.length;
    }

    /**
     * Returns the current position within the array.
     *
     * @return the current position as an int
     */
    public int getIndex()
    {
        return current;
    }

    /**
     * Starter for the recursive {@link #findIt(TreePath)} function
     *
     * @param pattern Description of the Parameter
     */
    public void find( final String pattern )
    {
        this.pattern = Pattern.compile( pattern );
        found = new TreePath[0];
        current = -1;
        if ( !pattern.equals( "" ) )
        {
            findIt( new TreePath( model.getRoot() ) );
        }
        if ( hasNext() )
        {
            // next does notify
            next();
        }
        else
        {
            setChanged();
            notifyObservers();
        }
    }

    /** @return <tt>true</tt> if the list iterator has more elements when traversing the list in the forward direction. */
    public boolean hasNext()
    {
        return current < getCount() - 1;
    }

    /**
     * The following description is taken from the interface: &quot;
     *
     * @return The down value
     */
    public boolean hasPrevious()
    {
        return current > 0;
    }

    /**
     * Advances the cursor by one if there are any elements left. Otherwise the cursor will stay on the last element.
     *
     * @return the TreePath at the position after incrementing the cursor.
     */
    public TreePath next()
    {
        final int count = Math.max( 0, getCount() - 1 );
        current = Math.min( count, current + 1 );
        setChanged();
        notifyObservers( found[current] );
        return found[current];
    }

    /**
     * Goes back one element if possible, otherwise stays at the first element.
     *
     * @return the TreePath at the position after decrementing the cursor.
     */
    public TreePath previous()
    {
        current = Math.max( 0, current - 1 );
        setChanged();
        notifyObservers();
        return found[current];
    }

    /**
     * This method returns the total count of TreePath and a comma separated list of its members in square brackets.
     *
     * @return String of members in square brackets
     */
    public String toString()
    {
        String str = "TreePath[";
        final int iL = found.length;
        for ( int i = 0; i < iL; i++ )
        {
            str = str + found[i] + ( i + 1 == iL ? "" : "," );
        }
        return "FinderModel[" + current + "," + str + "]]";
    }

    /**
     * A recursive method that loops through all children of a given TreePath and searches in each node for the specified search string.
     *
     * @param tp Description of the Parameter
     */
    private void findIt( final TreePath tp )
    {
        final Object last = tp.getLastPathComponent();
        for ( int i = 0; i < model.getChildCount( last ); i++ )
        {
            final Object o = model.getChild( last, i );
            if ( pattern.matcher( o.toString() ).matches() )
            {
                final TreePath[] tpsTemp = new TreePath[found.length + 1];
                int j = 0;
                for ( j = 0; j < found.length; j++ )
                {
                    tpsTemp[j] = found[j];
                }
                tpsTemp[j] = tp.pathByAddingChild( o );
                found = tpsTemp;
            }
            if ( !model.isLeaf( last ) )
            {
                final TreePath tpNew = tp.pathByAddingChild( o );
                if ( tpNew != null )
                {
                    findIt( tpNew );
                }
            }
        }
    }
}

// $Log: TreeFinderModel.java,v $
// Revision 1.4  2005/11/17 11:54:58  daniel_frey
// no message
//
// Revision 1.3  2005/08/14 00:37:23  daniel_frey
// Improvements on tree helpers and header panel
//
// Revision 1.2  2005/08/07 01:21:55  daniel_frey
// 2.0-20050809
//
// Revision 1.1  2005/06/16 06:28:58  daniel_frey
// Completely merged and finished for UST version 2.0-20050616
//
// Revision 1.1  2004/04/19 10:31:21  daniel_frey
// Replaced top level package com by ch
//
// Revision 1.8  2003/05/02 14:41:12  daniel_frey
// - List dialog search field now searching also for parts of strings behind the beginning
//
// Revision 1.7  2003/04/24 10:24:02  daniel_frey
// - Another try of incremental search
//
// Revision 1.6  2003/01/22 12:07:29  daniel_frey
// - Corrected javadoc
// - Removed double notification of observers in void find
//
// Revision 1.5  2002/09/17 09:15:34  Dani
// - Entry now working
//
// - Better encapsulation of layers
//
// Revision 1.4  2002/08/05 12:38:10  Dani
// - Added escape comments for CheckStyle
//
// Revision 1.3  2002/08/02 00:42:18  Dani
// Optimized import statements
//
// Revision 1.2  2002/07/11 12:12:17  Dani
// Moved from com.ethz.geobot.herbar.gui.util to ch.xmatrix.gui.component.tree
//
// Revision 1.1  2002/07/11 12:03:53  Dani
// Moved and renamed com.ethz.geobot.herbar.gui.util.TreeModel to
// ch.jfactory.component.tree.TreeFinderModel
//

