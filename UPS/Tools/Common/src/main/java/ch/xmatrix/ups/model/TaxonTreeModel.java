/*
 * ====================================================================
 *  Copyright 2004-2006 www.xmatrix.ch
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 * ====================================================================
 */
package ch.xmatrix.ups.model;

import ch.jfactory.component.tree.AbstractTreeModel;
import ch.xmatrix.ups.domain.SimpleTaxon;
import java.util.ArrayList;
import javax.swing.tree.TreePath;

/**
 * TreeModel for SimpleTaxon objects.
 *
 * @author Daniel Frey
 * @version $Revision: 1.7 $ $Date: 2006/08/04 15:50:02 $
 */
public class TaxonTreeModel extends AbstractTreeModel
{
    public TaxonTreeModel( final SimpleTaxon root )
    {
        super( root );
    }

    protected void remove( final Object child, final TreePath parentPath )
    {
    }

    protected void insert( final TreePath childPath, final TreePath parentPath, final int pos )
    {
    }

    public int getChildCount( final Object parent )
    {
        final SimpleTaxon taxon = (SimpleTaxon) parent;
        final ArrayList children = taxon.getChildTaxa();
        return children == null ? 0 : children.size();
    }

    public Object getChild( final Object parent, final int index )
    {
        final SimpleTaxon taxon = (SimpleTaxon) parent;
        final ArrayList children = taxon.getChildTaxa();
        return children.get( index );
    }

    public void valueForPathChanged( final TreePath path, final Object newValue )
    {
    }

    public TreePath getPathToRoot( final SimpleTaxon taxon )
    {
        final ArrayList<SimpleTaxon> pathElements = new ArrayList<SimpleTaxon>();
        SimpleTaxon current = taxon;
        while ( current != null )
        {
            pathElements.add( 0, current );
            current = current.getParentTaxon();
        }
        final TreePath path;
        if ( pathElements.size() == 0 )
        {
            path = null;
        }
        else
        {
            path = new TreePath( pathElements.toArray( new SimpleTaxon[0] ) );
        }
        return path;
    }
}
