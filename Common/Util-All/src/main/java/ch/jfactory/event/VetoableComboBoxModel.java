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

import java.util.ArrayList;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2006/03/22 15:05:10 $
 */
public class VetoableComboBoxModel extends DefaultComboBoxModel
{
    private final ArrayList selectionListeners = new ArrayList();

    public VetoableComboBoxModel()
    {
    }

    public VetoableComboBoxModel( final Object[] items )
    {
        super( items );
    }

    public VetoableComboBoxModel( final Vector v )
    {
        super( v );
    }

    public void addVetoableSelectionListener( final VetoableComboBoxSelectionListener l )
    {
        selectionListeners.add( l );
    }

    public void removeVetoableSelectionListener( final VetoableComboBoxSelectionListener l )
    {
        selectionListeners.remove( l );
    }

    protected boolean fireVetoableSelectionChange( final Object oldValue, final Object newValue )
    {
        boolean result = true;
        final VetoableChangeEvent event = new VetoableChangeEvent( this, oldValue, newValue );
        for ( final Object selectionListener : selectionListeners )
        {
            final VetoableComboBoxSelectionListener l = (VetoableComboBoxSelectionListener) selectionListener;
            result &= l.selectionChanged( event );
        }
        return result;
    }

    public void setSelectedItem( final Object newItem )
    {
        final Object oldItem = getSelectedItem();
        super.setSelectedItem( newItem );
        if ( !fireVetoableSelectionChange( oldItem, newItem ) )
        {
            super.setSelectedItem( oldItem );
        }
    }
}
