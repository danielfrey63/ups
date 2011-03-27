/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.model.graph;

import java.io.Serializable;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:58 $
 */
public class GraphEdgeImpl implements GraphEdge, Serializable
{
    private int id;

    private int rank;

    private GraphNode child;

    private GraphNode parent;

    private GraphEdge recursive;

    public GraphEdgeImpl( final int id, final GraphNode parent, final GraphNode child )
    {
        this( id );
        this.parent = parent;
        this.child = child;
    }

    public GraphEdgeImpl( final int id )
    {
        setId( id );
    }

    /** @see GraphEdge#isRole(Class) */
    public boolean isRole( final Class role )
    {
        return role.isAssignableFrom( role.getClass() );
    }

    /** @see GraphEdge#getChild() */
    public GraphNode getChild()
    {
        return child;
    }

    /** @see GraphEdge#getParent() */
    public GraphNode getParent()
    {
        return parent;
    }

    /** @see GraphEdge#getRecursive() */
    public GraphEdge getRecursive()
    {
        return recursive;
    }

    /** @see GraphEdge#getRank() */
    public int getRank()
    {
        return rank;
    }

    /** @see GraphEdge#setChild(GraphNode) */
    public void setChild( final GraphNode child )
    {
        final GraphModel model = AbsGraphModel.getModel();
        model.addRemoved( this );
        this.child = child;
        model.addChanged( this );
    }

    /** @see GraphEdge#setParent(GraphNode) */
    public void setParent( final GraphNode parent )
    {
        final GraphModel model = AbsGraphModel.getModel();
        model.addRemoved( this );
        this.parent = parent;
        model.addChanged( this );
    }

    /** @see GraphEdge#setRecursive(GraphEdge) */
    public void setRecursive( final GraphEdge recursive )
    {
        this.recursive = recursive;
    }

    /** @see GraphEdge#setRank(int) */
    public void setRank( final int rank )
    {
        this.rank = rank;
    }

    /** @see GraphEdge#getId() */
    public int getId()
    {
        return id;
    }

    /** @see GraphEdge#setId(int) */
    public void setId( final int id )
    {
        this.id = id;
    }

    /** @see Object#toString() */
    public String toString()
    {
        String pId = "null node (NN)";
        String cId = pId;
        if ( parent != null )
        {
            pId = parent + "(" + parent.hashCode() + ")";
        }
        if ( child != null )
        {
            cId = child + "(" + child.hashCode() + ")";
        }
        return "[p=" + pId + ",c=" + cId
                + ",rank=" + rank
                + ",rec=" + recursive + "]";
    }

}
