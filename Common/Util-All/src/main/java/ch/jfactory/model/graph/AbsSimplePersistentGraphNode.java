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
 * @version $Revision: 1.2 $ $Date: 2006/03/14 21:27:55 $
 */
public abstract class AbsSimplePersistentGraphNode extends AbsSimpleGraphNode
{
    private static final GraphModel MODEL = AbsGraphModel.getModel();

    /** @see GraphNode#setName(String) */
    public void setName( final String name )
    {
        super.setName( name );
        MODEL.addChanged( this );
    }

    /** @see GraphNode#setRank(int) */
    public void setRank( final int rank )
    {
        super.setRank( rank );
        MODEL.addChanged( this );
    }

}
