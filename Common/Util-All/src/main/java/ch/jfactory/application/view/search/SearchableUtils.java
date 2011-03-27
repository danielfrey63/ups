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

import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.JTree;

// Referenced classes of package com.jidesoft.swing:
//            TreeSearchable, TableSearchable, ListSearchable, ComboBoxSearchable,
//            Searchable

public class SearchableUtils
{
    public SearchableUtils()
    {
    }

    public static TreeSearchable installSearchable( final JTree jtree )
    {
        return new TreeSearchable( jtree );
    }

    public static TableSearchable installSearchable( final JTable jtable )
    {
        return new TableSearchable( jtable );
    }

    public static ListSearchable installSearchable( final JList jlist )
    {
        return new ListSearchable( jlist );
    }

    public static ComboBoxSearchable installSearchable( final JComboBox jcombobox )
    {
        return new ComboBoxSearchable( jcombobox );
    }

    public static void uninstallSearchable( final Searchable searchable )
    {
        searchable.uninstallListeners();
    }
}