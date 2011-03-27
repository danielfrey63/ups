/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.component.tree.filtered;

import ch.jfactory.component.tree.AbstractTreeModel;
import ch.jfactory.component.tree.NotifiableTreeModel;
import ch.jfactory.component.tree.TreeExpandedRestorer;
import ch.jfactory.filter.Filter;
import ch.jfactory.filter.Filterable;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 * Wrapper around a tree model that implements the {@link Filterable} interface. If the wrapped tree model itself implements {@link NotifiableTreeModel}, removal and insertion of nodes are supported. Otherwise an IllegalStateException is thrown.
 *
 * <p/>After setting or removing the filter, the model is automatically reloaded. As a side-effect, nodes are collapsed. If you want to keep the expanded states of the nodes, consider to use {@link TreeExpandedRestorer}.
 *
 * @author Daniel Frey
 * @version $Revision: 1.4 $ $Date: 2008/01/06 10:16:23 $
 */
public class ModelBasedFilteredTreeModel extends AbstractTreeModel implements Filterable
{
    /** Propertyname for tree model exchanges. */
    public static final String PROPERTYNAME_MODEL = "model";

    private final TreeModelListener listener = new TreeModelListener()
    {
        public void treeNodesChanged( final TreeModelEvent e )
        {
            fireTreeNodesChanged( e );
        }

        public void treeNodesInserted( final TreeModelEvent e )
        {
            fireTreeNodesChanged( e );
        }

        public void treeNodesRemoved( final TreeModelEvent e )
        {
            fireTreeNodesRemoved( e );
        }

        public void treeStructureChanged( final TreeModelEvent e )
        {
            if ( model.getRoot() != root )
            {
                ModelBasedFilteredTreeModel.super.setRoot( model.getRoot() );
            }
            fireTreeStructureChanged( e );
        }
    };

    private TreeModel model;

    private Filter filter = Filter.TRUEFILTER;

    public ModelBasedFilteredTreeModel()
    {
        this( null );
    }

    public ModelBasedFilteredTreeModel( final TreeModel model )
    {
        super( model == null ? null : model.getRoot() );
        this.setModel( model );
    }

    // AbstractTreeModel

    public void setRoot( final Object root )
    {
        super.setRoot( root );
        fireTreeStructureChanged( new TreeModelEvent( this, new TreePath( root ) ) );
    }

    protected void remove( final Object child, final TreePath parentPath )
    {
        if ( getModel() instanceof NotifiableTreeModel )
        {
            ( (NotifiableTreeModel) getModel() ).removeFromParent( parentPath.pathByAddingChild( child ) );
        }
        else
        {
            throw new IllegalStateException( "remove not supported by " + getModel().getClass().getName() );
        }
    }

    protected void insert( final TreePath child, final TreePath parent, final int pos )
    {
        if ( getModel() instanceof NotifiableTreeModel )
        {
            ( (NotifiableTreeModel) getModel() ).insertInto( child, parent, pos );
        }
        else
        {
            throw new IllegalStateException( "insert not supported by " + getModel().getClass().getName() );
        }
    }

    public int getChildCount( final Object parent )
    {
        int count = 0;
        for ( int i = 0; i < getModel().getChildCount( parent ); i++ )
        {
            final Object child = getModel().getChild( parent, i );
            if ( isShown( child ) )
            {
                count++;
            }
            else
            {
                count += getChildCount( child );
            }
        }
        return count;
    }

    public Object getChild( final Object parent, final int index )
    {
        int count = 0;
        for ( int i = 0; i < getModel().getChildCount( parent ); i++ )
        {
            final Object child = getModel().getChild( parent, i );
            if ( isShown( child ) )
            {
                if ( count++ == index )
                {
                    return child;
                }
            }
            else
            {
                final Object child2 = getChild( child, index - count );
                if ( child2 != null )
                {
                    return child2;
                }
                count += getChildCount( child );
            }
        }
        return null;
    }

    public void valueForPathChanged( final TreePath path, final Object newValue )
    {
        model.valueForPathChanged( path, newValue );
    }

    //--- Interface

    public TreeModel getModel()
    {
        return model;
    }

    public void setModel( final TreeModel model )
    {
        final TreeModel old = getModel();
        if ( old != null )
        {
            old.removeTreeModelListener( listener );
        }
        this.model = model;
        super.setRoot( model == null ? null : model.getRoot() );
        if ( model != null )
        {
            model.addTreeModelListener( listener );
        }
        reload();
        firePropertyChange( PROPERTYNAME_MODEL, old, model );
    }

    //--- Filterable

    public Filter getFilter()
    {
        return filter;
    }

    public void setFilter( final Filter filter )
    {
        this.filter = filter;
        filter();
    }

    public void deleteFilter()
    {
        setFilter( Filter.TRUEFILTER );
    }

    public void filter()
    {
        firePropertyChange( PROPERTYNAME_FILTER, null, filter );
        reload();
    }

    //--- Helper

    private boolean isShown( final Object node )
    {
        return filter.matches( node );
    }
}
