/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.component.list;

import java.util.Arrays;
import java.util.Collection;
import javax.swing.JList;
import javax.swing.ListModel;

/**
 * Extends the JList component with multiple value selection.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2005/11/17 11:54:58 $
 */
public class DefaultJList<T> extends JList
{
    public DefaultJList()
    {
    }

    public void setSelectedValues( final T[] selectedObjects )
    {
        final ListModel model = getModel();
        clearSelection();

        if ( selectedObjects.length > 0 )
        {
            final Collection col = Arrays.asList( selectedObjects );
            int start = -1;
            for ( int i = 0; i < model.getSize(); i++ )
            {
                final Object obj = model.getElementAt( i );
                if ( col.contains( obj ) )
                {
                    if ( start < 0 )
                    {
                        start = i;
                    }
                }
                else
                {
                    if ( start >= 0 )
                    {
                        this.addSelectionInterval( start, i - 1 );
                        start = -1;
                    }
                }
            }
            if ( start >= 0 )
            {
                this.addSelectionInterval( start, model.getSize() - 1 );
            }
        }
    }
}
