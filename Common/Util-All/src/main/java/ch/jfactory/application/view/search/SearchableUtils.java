// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 09.06.2005 09:02:39
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) fieldsfirst nonlb

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