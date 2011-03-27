/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.searchtree;

import javax.swing.JTree;
import javax.swing.tree.TreePath;

/**
 * Implementation for searching the tree
 *
 * @author <a href="mail@wegmueller.com">Thomas Wegmueller</a>
 * @version $Revision: 1.2 $ $Date: 2006/03/14 21:27:55 $
 */
class TreeSearcher implements ITreeSearcher
{
    private TreePath treePath;

    private String searchString;

    private final JTree tree;

    private final boolean starting;

    private final boolean ignoreCase;

    public TreeSearcher( final JTree tree, final boolean staring, final boolean ignoreCase )
    {
        this.tree = tree;
        this.starting = staring;
        this.ignoreCase = ignoreCase;
    }

    class MyTreePath extends TreePath
    {
        private MyTreePath( final TreePath p, final Object o )
        {
            super( p, o );
        }
    }

    public TreePath next()
    {
        TreePath last = null;
        if ( treePath != null && found() )
        {
            last = treePath;
        }
        if ( treePath == null )
        {
            gotoFirst();
            if ( found() )
            {
                return treePath;
            }
        }
        while ( gotoNext() && !found() )
        {
        }
        if ( treePath == null )
        {
            treePath = last;
        }
        return treePath;
    }

    public TreePath prev()
    {
        TreePath last = null;
        if ( treePath != null && found() )
        {
            last = treePath;
        }
        if ( treePath == null )
        {
            gotoLast();
            if ( found() )
            {
                return treePath;
            }
        }
        while ( gotoPrev() && !found() )
        {
        }
        if ( treePath == null )
        {
            treePath = last;
        }
        return treePath;
    }

    private void gotoLast()
    {
        gotoFirst();
        Object object = treePath.getLastPathComponent();
        while ( tree.getModel().getChildCount( object ) > 0 )
        {
            final int index = tree.getModel().getChildCount( object );
            treePath = new MyTreePath( treePath, tree.getModel().getChild( object, index - 1 ) );
            object = treePath.getLastPathComponent();
        }
    }

    private TreePath findLast( TreePath path )
    {
        Object object = path.getLastPathComponent();
        while ( tree.getModel().getChildCount( object ) > 0 )
        {
            final int index = tree.getModel().getChildCount( object );
            path = new MyTreePath( path, tree.getModel().getChild( object, index - 1 ) );
            object = path.getLastPathComponent();
        }
        return path;
    }

    private void gotoFirst()
    {
        treePath = new TreePath( tree.getModel().getRoot() );
    }

    private boolean found()
    {
        String treeItem = treePath.getLastPathComponent().toString();
        String searchItem = searchString;
        if ( ignoreCase )
        {
            treeItem = treeItem.toUpperCase();
            searchItem = searchItem.toUpperCase();
        }
        if ( starting )
        {
            return treeItem.startsWith( searchItem );
        }
        else
        {
            return treeItem.indexOf( searchItem ) >= 0;
        }
    }

    private boolean gotoNext()
    {
        treePath = searchNext( treePath, null );
        return treePath != null;
    }

    private boolean gotoPrev()
    {
        treePath = searchPrev( treePath );
        return treePath != null;
    }

    private TreePath searchNext( final TreePath path, final Object child )
    {
        final Object object = path.getLastPathComponent();
        int startingChildIndex = 0;
        if ( child != null )
        {
            startingChildIndex = tree.getModel().getIndexOfChild( object, child ) + 1;
        }
        if ( tree.getModel().getChildCount( object ) > startingChildIndex )
        {
            return new MyTreePath( path, tree.getModel().getChild( object, 0 ) );
        }
        else
        {
            final TreePath parentPath = path.getParentPath();
            if ( parentPath != null )
            {
                final Object parent = parentPath.getLastPathComponent();
                final int count = tree.getModel().getChildCount( parent );
                final int index = tree.getModel().getIndexOfChild( parent, object );
                if ( count > ( index + 1 ) )
                {
                    return new MyTreePath( parentPath, tree.getModel().getChild( parent, index + 1 ) );
                }
                else
                {
                    return searchNext( parentPath, object );
                }
            }
        }
        return null;
    }

    private TreePath searchPrev( final TreePath path )
    {
        final Object object = path.getLastPathComponent();
        final TreePath parentPath = path.getParentPath();
        if ( parentPath != null )
        {
            final Object parent = parentPath.getLastPathComponent();
            final int index = tree.getModel().getIndexOfChild( parent, object );
            if ( index > 0 )
            {
                return findLast( new MyTreePath( parentPath, tree.getModel().getChild( parent, index - 1 ) ) );
            }
            else
            {
                return parentPath;
            }
        }
        return null;
    }

    public TreePath getTreePath()
    {
        return treePath;
    }

    public String getSearchString()
    {
        return searchString;
    }

    public TreePath setSearchString( final String searchString )
    {
        this.searchString = searchString;
        if ( treePath == null || !found() )
        {
            gotoFirst();
        }
        if ( !found() )
        {
            return next();
        }
        return treePath;
    }

}