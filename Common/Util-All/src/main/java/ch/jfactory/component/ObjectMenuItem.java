/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.component;

import javax.swing.JMenuItem;

/**
 * Menu item wrapping an object.
 *
 * @author Daniel Frey 23.01.11 12:07
 */
public class ObjectMenuItem<T> extends JMenuItem
{
    private final T object;

    public ObjectMenuItem( final T object )
    {
        super( object.toString() );
        this.object = object;
    }

    public T getObject()
    {
        return object;
    }
}
