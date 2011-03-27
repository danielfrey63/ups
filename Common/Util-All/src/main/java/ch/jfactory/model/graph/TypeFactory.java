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
public interface TypeFactory
{
    /** Returns an instance of the given clazz, or an implementation of it if the clazz is an interface and an appropriate implementation exists. */
    public GraphNode getInstance( Class clazz );

    /** Returns an instance of the given type. */
    public GraphNode getInstance( String type );

    /** Returns an implementation class for the given GraphNode. */
    public String getType( GraphNode node );
}
