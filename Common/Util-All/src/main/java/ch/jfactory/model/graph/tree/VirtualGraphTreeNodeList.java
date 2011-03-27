/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.model.graph.tree;

import ch.jfactory.model.graph.GraphNodeList;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:58 $
 */
public class VirtualGraphTreeNodeList extends GraphNodeList
{
    public VirtualGraphTreeNodeList()
    {
    }

    public VirtualGraphTreeNodeList( final VirtualGraphTreeNode element )
    {
        add( element );
    }

    public void add( final VirtualGraphTreeNode element )
    {
        super.add( element );
    }

    public void addAll( final VirtualGraphTreeNodeList elements )
    {
        super.addAll( elements );
    }

    public VirtualGraphTreeNode getTreeNode( final int index )
    {
        return (VirtualGraphTreeNode) super.get( index );
    }

}
