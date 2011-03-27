/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.application.view.search;

import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

// Referenced classes of package com.jidesoft.swing:
//            Searchable

public class ListSearchable extends Searchable
        implements ListDataListener
{
    public ListSearchable( final JList jlist )
    {
        super( jlist );
        jlist.getModel().addListDataListener( this );
    }

    public void uninstallListeners()
    {
        super.uninstallListeners();
        if ( component instanceof JList )
        {
            ( (JList) component ).getModel().removeListDataListener( this );
        }
    }

    protected void setSelectedIndex( final int i, final boolean flag )
    {
        if ( flag )
        {
            ( (JList) component ).addSelectionInterval( i, i );
        }
        else
        {
            ( (JList) component ).setSelectedIndex( i );
        }
        ( (JList) component ).ensureIndexIsVisible( i );
    }

    protected int getSelectedIndex()
    {
        return ( (JList) component ).getSelectedIndex();
    }

    protected Object getElementAt( final int i )
    {
        final ListModel listmodel = ( (JList) component ).getModel();
        return listmodel.getElementAt( i );
    }

    protected int getElementCount()
    {
        final ListModel listmodel = ( (JList) component ).getModel();
        return listmodel.getSize();
    }

    protected String convertElementToString( final Object obj )
    {
        if ( obj != null )
        {
            return obj.toString();
        }
        else
        {
            return "";
        }
    }

    public void contentsChanged( final ListDataEvent listdataevent )
    {
        hidePopup();
    }

    public void intervalAdded( final ListDataEvent listdataevent )
    {
        hidePopup();
    }

    public void intervalRemoved( final ListDataEvent listdataevent )
    {
        hidePopup();
    }
}