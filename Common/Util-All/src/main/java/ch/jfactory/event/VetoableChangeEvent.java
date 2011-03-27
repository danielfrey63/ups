/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.event;

import javax.swing.event.ChangeEvent;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2006/03/22 15:05:10 $
 */
public class VetoableChangeEvent extends ChangeEvent
{
    private final Object newValue;

    private final Object oldValue;

    public VetoableChangeEvent( final Object source, final Object oldValue, final Object newValue )
    {
        super( source );
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public Object getNewValue()
    {
        return newValue;
    }

    public Object getOldValue()
    {
        return oldValue;
    }
}
