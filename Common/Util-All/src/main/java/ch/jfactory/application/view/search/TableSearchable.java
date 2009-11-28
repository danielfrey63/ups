// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 09.06.2005 09:02:35
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) fieldsfirst nonlb

package ch.jfactory.application.view.search;

import java.awt.event.KeyEvent;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

// Referenced classes of package com.jidesoft.swing:
//            Searchable

public class TableSearchable extends Searchable
        implements TableModelListener
{
    private int _mainIndex;

    public TableSearchable( final JTable jtable )
    {
        super( jtable );
        _mainIndex = 0;
        jtable.getModel().addTableModelListener( this );
    }

    public void uninstallListeners()
    {
        super.uninstallListeners();
        if ( component instanceof JTable )
        {
            ( (JTable) component ).getModel().removeTableModelListener( this );
        }
    }

    protected void setSelectedIndex( final int i, final boolean flag )
    {
        final JTable jtable = (JTable) component;
        final TableModel tablemodel = jtable.getModel();
        if ( jtable.getColumnSelectionAllowed() && !jtable.getRowSelectionAllowed() )
        {
            final int j = i;
            final int i1 = getMainIndex();
            if ( j != -1 )
            {
                if ( !flag )
                {
                    jtable.getColumnModel().getSelectionModel().clearSelection();
                    jtable.getColumnModel().getSelectionModel().setValueIsAdjusting( false );
                }
                else
                {
                    jtable.getColumnModel().getSelectionModel().setValueIsAdjusting( true );
                }
                jtable.getColumnModel().getSelectionModel().addSelectionInterval( j, j );
                jtable.getSelectionModel().addSelectionInterval( i1, i1 );
                jtable.scrollRectToVisible( jtable.getCellRect( i1, j, true ) );
            }
        }
        else if ( !jtable.getColumnSelectionAllowed() && jtable.getRowSelectionAllowed() )
        {
            final int k = i;
            final int j1 = getMainIndex();
            if ( k != -1 )
            {
                if ( !flag )
                {
                    jtable.getSelectionModel().clearSelection();
                    jtable.getSelectionModel().setValueIsAdjusting( false );
                }
                else
                {
                    jtable.getSelectionModel().setValueIsAdjusting( true );
                }
                jtable.getSelectionModel().addSelectionInterval( k, k );
                jtable.getColumnModel().getSelectionModel().addSelectionInterval( j1, j1 );
                jtable.scrollRectToVisible( jtable.getCellRect( k, j1, true ) );
            }
        }
        else
        {
            final int l = i / tablemodel.getColumnCount();
            final int k1 = i % tablemodel.getColumnCount();
            if ( !flag )
            {
                jtable.getSelectionModel().clearSelection();
                jtable.getColumnModel().getSelectionModel().clearSelection();
            }
            jtable.getSelectionModel().setValueIsAdjusting( true );
            jtable.getSelectionModel().addSelectionInterval( l, l );
            jtable.getColumnModel().getSelectionModel().addSelectionInterval( k1, k1 );
            jtable.getSelectionModel().setValueIsAdjusting( false );
            jtable.scrollRectToVisible( jtable.getCellRect( l, k1, true ) );
        }
    }

    protected int getSelectedIndex()
    {
        final JTable jtable = (JTable) component;
        if ( jtable.getColumnSelectionAllowed() && !jtable.getRowSelectionAllowed() )
        {
            return jtable.getColumnModel().getSelectionModel().getLeadSelectionIndex();
        }
        if ( !jtable.getColumnSelectionAllowed() && jtable.getRowSelectionAllowed() )
        {
            return jtable.getSelectionModel().getLeadSelectionIndex();
        }
        else
        {
            return jtable.getSelectionModel().getLeadSelectionIndex() * jtable.getColumnCount() + jtable.getColumnModel().getSelectionModel().getLeadSelectionIndex();
        }
    }

    protected Object getElementAt( final int i )
    {
        final TableModel tablemodel = ( (JTable) component ).getModel();
        final JTable jtable = (JTable) component;
        if ( jtable.getColumnSelectionAllowed() && !jtable.getRowSelectionAllowed() )
        {
            if ( getMainIndex() >= 0 && getMainIndex() < tablemodel.getRowCount() )
            {
                return tablemodel.getValueAt( getMainIndex(), i );
            }
            else
            {
                return null;
            }
        }
        if ( !jtable.getColumnSelectionAllowed() && jtable.getRowSelectionAllowed() )
        {
            if ( getMainIndex() >= 0 && getMainIndex() < tablemodel.getColumnCount() )
            {
                return tablemodel.getValueAt( i, getMainIndex() );
            }
            else
            {
                return null;
            }
        }
        else
        {
            return tablemodel.getValueAt( i / tablemodel.getColumnCount(), i % tablemodel.getColumnCount() );
        }
    }

    protected int getElementCount()
    {
        final TableModel tablemodel = ( (JTable) component ).getModel();
        final JTable jtable = (JTable) component;
        if ( jtable.getColumnSelectionAllowed() && !jtable.getRowSelectionAllowed() )
        {
            return tablemodel.getColumnCount();
        }
        if ( !jtable.getColumnSelectionAllowed() && jtable.getRowSelectionAllowed() )
        {
            return tablemodel.getRowCount();
        }
        else
        {
            return tablemodel.getColumnCount() * tablemodel.getRowCount();
        }
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

    public int getMainIndex()
    {
        return _mainIndex;
    }

    public void setMainIndex( final int i )
    {
        _mainIndex = i;
    }

    protected boolean isFindNextKey( final KeyEvent keyevent )
    {
        final int i = keyevent.getKeyCode();
        final JTable jtable = (JTable) component;
        if ( jtable.getColumnSelectionAllowed() && !jtable.getRowSelectionAllowed() )
        {
            return i == 39;
        }
        if ( !jtable.getColumnSelectionAllowed() && jtable.getRowSelectionAllowed() )
        {
            return i == 40;
        }
        else
        {
            return i == 40 || i == 39;
        }
    }

    protected boolean isFindPreviousKey( final KeyEvent keyevent )
    {
        final int i = keyevent.getKeyCode();
        final JTable jtable = (JTable) component;
        if ( jtable.getColumnSelectionAllowed() && !jtable.getRowSelectionAllowed() )
        {
            return i == 37;
        }
        if ( !jtable.getColumnSelectionAllowed() && jtable.getRowSelectionAllowed() )
        {
            return i == 38;
        }
        else
        {
            return i == 38 || i == 37;
        }
    }

    public void tableChanged( final TableModelEvent tablemodelevent )
    {
        hidePopup();
    }
}