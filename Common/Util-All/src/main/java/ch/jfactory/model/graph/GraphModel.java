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

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:58 $
 */
public interface GraphModel
{
    /**
     * Returns the root data entry for the whole model.
     *
     * @return GraphNode
     */
    public GraphNode getRoot();

    /**
     * Creates a new GraphNode of the given type for the given parent.
     *
     * @param parent the parent
     * @param type   the type
     * @return the new node
     */
    public GraphNode createNode( GraphNode parent, Class type );

    /**
     * Returns a {@link GraphEdge} for the given parent and child {@link GraphNode}s. The edge is constructed newly if needed.
     *
     * @param parent the parent node
     * @param child  the child node
     * @return the matching edge
     */
    public GraphEdge createEdge( GraphNode parent, GraphNode child );

    public void addRemoved( GraphNode node );

    public void addRemoved( GraphEdge edge );

    public void addChanged( GraphNode node );

    public void addChanged( GraphEdge edge );

    public void save();

    public void doQuit();

    public void addDirtyListener( DirtyListener listener );

    public void removeDirtyListener( DirtyListener listener );

    public void setDirty( boolean dirty );

    public boolean getDirty();
}
