/* ====================================================================
 *  Copyright 2004-2005 www.xmatrix.ch
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 * ====================================================================
 */
package ch.jfactory.projecttime.invoice;

import ch.jfactory.component.tree.AbstractTreeModel;
import ch.jfactory.lang.DateUtils;
import ch.jfactory.projecttime.domain.api.IFEntry;
import ch.jfactory.projecttime.domain.impl.DefaultEntry;
import ch.jfactory.projecttime.domain.impl.Invoice;
import ch.jfactory.projecttime.stats.StatUtils;
import com.jgoodies.binding.beans.Model;
import com.jgoodies.binding.value.ValueModel;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultListSelectionModel;
import javax.swing.ListSelectionModel;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import org.apache.commons.collections.map.MultiKeyMap;
import org.jdesktop.swingx.treetable.TreeTableModel;

/**
 * TODO: document
 *
 * @author <a href="daniel.frey@xmatrix.ch">Daniel Frey</a>
 * @version $Revision: 1.1 $ $Date: 2005/11/17 11:56:29 $
 */
public class InvoiceModel extends Model
{
    public static final String PROPERTYNAME_INVOICESMODIFIED = "modifyInvoice";

    private final ArrayList invoices = new ArrayList();

    public static final String PROPERTYNAME_TABLEMODEL = "treeTableModel";

    private final InvoiceTreeTableModel treeTableModel = new InvoiceTreeTableModel( "Root" );

    public static final String PROPERTYNAME_CURRENTINVOICE = "currentInvoice";

    private Invoice currentInvoice;

    private final ListSelectionModel tableSelectionModel = new DefaultListSelectionModel();

    private final TreeSelectionModel treeSelectionModel = new DefaultTreeSelectionModel();

    private final ValueModel entry2InvoiceMap;

    public InvoiceModel( final ValueModel entry2InvoiceMap )
    {
        this.entry2InvoiceMap = entry2InvoiceMap;
        treeSelectionModel.setSelectionMode( TreeSelectionModel.SINGLE_TREE_SELECTION );
    }

    public List getInvoices()
    {
        return invoices;
    }

    public void addInvoice( final Invoice invoice )
    {
        invoices.add( invoice );
        firePropertyChange( PROPERTYNAME_INVOICESMODIFIED, null, invoice );
    }

    public void deleteInvoice( final int index )
    {
        final Object invoice = invoices.remove( index );
        firePropertyChange( PROPERTYNAME_INVOICESMODIFIED, invoice, null );
    }

    public int getIndexOfInvoice( final Invoice invoice )
    {
        return invoices.indexOf( invoice );
    }

    public Invoice getInvoice( final int index )
    {
        return (Invoice) invoices.get( index );
    }

    public ListSelectionModel getTableSelectionModel()
    {
        return tableSelectionModel;
    }

    public TreeSelectionModel getTreeSelectionModel()
    {
        return treeSelectionModel;
    }

    public InvoiceTreeTableModel getTreeTableModel()
    {
        return treeTableModel;
    }

    public void resetTreeTableModel()
    {
        treeTableModel.parent2ChildrenMap = new HashMap();
        treeTableModel.parentsMap = new MultiKeyMap();
    }

    public ValueModel getEntry2InvoiceMap()
    {
        return entry2InvoiceMap;
    }

    public Invoice getCurrentInvoice()
    {
        return currentInvoice;
    }

    public void setCurrentInvoice( final Invoice currentInvoice )
    {
        final Invoice old = getCurrentInvoice();
        this.currentInvoice = currentInvoice;
        firePropertyChange( PROPERTYNAME_CURRENTINVOICE, old, currentInvoice );
    }

    public class InvoiceTreeTableModel extends AbstractTreeModel implements TreeTableModel
    {
        private final String[] columnNames = {"Rechungsnummer", "Arbeit", "Datum 1", "Datum 2"};

        private MultiKeyMap parentsMap = new MultiKeyMap();

        private Map parent2ChildrenMap = new HashMap();

        private static final String TIME_DIFFERENCE_PATTERN = "HH:mm:ss";

        public InvoiceTreeTableModel( final Object root )
        {
            super( root );
        }

        // AbstractTreeModel

        protected void remove( final Object child, final TreePath parentPath )
        {
        }

        protected void insert( final TreePath childPath, final TreePath parentPath, final int pos )
        {
        }

        // TreeTableModel

        public Class getColumnClass( final int column )
        {
            switch ( column )
            {
                case 0:
                    return TreeTableModel.class;
                case 1:
                    return String.class;
                case 2:
                case 3:
                    return Calendar.class;
                default:
                    return Object.class;
            }
        }

        public int getColumnCount()
        {
            return 4;
        }

        public String getColumnName( final int column )
        {
            return columnNames[column];
        }

        public int getHierarchicalColumn()
        {
            return 0;
        }

        public Object getValueAt( final Object node, final int column )
        {
            if ( node instanceof Invoice )
            {
                final Invoice invoice = (Invoice) node;
                switch ( column )
                {
                    case 0:
                        return invoice.getNumber();
                    case 1:
                        final Collection entries = invoice.getEntries();
                        if ( entries == null )
                        {
                            return "0";
                        }
                        else
                        {
                            final IFEntry[] entriesArray = (IFEntry[]) entries.toArray( new IFEntry[0] );
                            return DateUtils.dateDifference( StatUtils.sumAll( entriesArray, 0 ), TIME_DIFFERENCE_PATTERN );
                        }
                    case 2:
                        return invoice.getCharged();
                    case 3:
                        return invoice.getDue();
                }
            }
            else
            {
                final IFEntry entry = (IFEntry) node;
                switch ( column )
                {
                    case 0:
                        return entry.getName();
                    case 1:
                        if ( entry.getEnd() != null )
                        {
                            return DateUtils.dateDifference( entry.getStart().getTime().getTime(), entry.getEnd().getTime().getTime(), TIME_DIFFERENCE_PATTERN );
                        }
                        else
                        {
                            final ArrayList list = (ArrayList) parent2ChildrenMap.get( entry );
                            final IFEntry[] children = (IFEntry[]) list.toArray( new IFEntry[0] );
                            final long sum = StatUtils.sumAll( children, 0 );
                            return DateUtils.dateDifference( sum, TIME_DIFFERENCE_PATTERN );
                        }
                    case 2:
                        if ( entry.getStart() != null )
                        {
                            return entry.getStart();
                        }
                        else
                        {
                            final ArrayList children = (ArrayList) parent2ChildrenMap.get( entry );
                            Calendar min = null;
                            for ( final Object aChildren : children )
                            {
                                final IFEntry child = (IFEntry) aChildren;
                                final Calendar start = child.getStart();
                                if ( min == null || start.compareTo( min ) < 0 )
                                {
                                    min = start;
                                }
                            }
                            final Calendar cal = Calendar.getInstance();
                            cal.setTime( min.getTime() );
                            return cal;
                        }
                    case 3:
                        if ( entry.getEnd() != null )
                        {
                            return entry.getEnd();
                        }
                        else
                        {
                            final ArrayList children = (ArrayList) parent2ChildrenMap.get( entry );
                            Calendar max = null;
                            for ( final Object aChildren : children )
                            {
                                final IFEntry child = (IFEntry) aChildren;
                                final Calendar end = child.getEnd();
                                if ( max == null || end.compareTo( max ) > 0 )
                                {
                                    max = end;
                                }
                            }
                            final Calendar cal = Calendar.getInstance();
                            cal.setTime( max.getTime() );
                            return cal;
                        }
                }
            }
            return null;
        }

        public boolean isCellEditable( final Object node, final int column )
        {
            return node instanceof Invoice && column != 1;
        }

        public void setValueAt( final Object value, final Object node, final int column )
        {
            try
            {
                final Invoice invoice = (Invoice) node;
                if ( column == 0 )
                {
                    final String text = (String) value;
                    invoice.setNumber( text );
                }
                else if ( column == 2 )
                {
                    final Calendar calendar = Calendar.getInstance();
                    calendar.setTime( DateFormat.getDateInstance().parse( (String) value ) );
                    invoice.setCharged( calendar );
                }
                else if ( column == 3 )
                {
                    final Calendar calendar = Calendar.getInstance();
                    calendar.setTime( DateFormat.getDateInstance().parse( (String) value ) );
                    invoice.setDue( calendar );
                }
            }
            catch ( ParseException e )
            {
                e.printStackTrace();
            }
        }

        // TreeModel

        public Object getChild( final Object parent, final int index )
        {
            if ( parent == getRoot() )
            {
                return invoices.get( index );
            }
            else if ( parent instanceof Invoice )
            {
                final Invoice invoice = (Invoice) parent;
                final ArrayList children = getChildren( invoice );
                return children.get( index );
            }
            else if ( parent instanceof IFEntry )
            {
                final ArrayList children = (ArrayList) parent2ChildrenMap.get( parent );
                return children.get( index );
            }
            return null;
        }

        public int getChildCount( final Object parent )
        {
            if ( parent == getRoot() )
            {
                return invoices.size();
            }
            else if ( parent instanceof Invoice )
            {
                final Invoice invoice = (Invoice) parent;
                final ArrayList children = getChildren( invoice );
                return children.size();
            }
            else if ( parent instanceof IFEntry )
            {
                final ArrayList children = (ArrayList) parent2ChildrenMap.get( parent );
                return ( children == null ? 0 : children.size() );
            }
            return 0;
        }

        public void valueForPathChanged( final TreePath path, final Object newValue )
        {
        }

        // Utils

        private ArrayList getChildren( final Invoice invoice )
        {
            ArrayList children = (ArrayList) parent2ChildrenMap.get( invoice );
            if ( children == null )
            {
                children = new ArrayList();
                parent2ChildrenMap.put( invoice, children );
                final Collection entries = invoice.getEntries();
                if ( entries != null )
                {
                    for ( final Object entry1 : entries )
                    {
                        final IFEntry entry = (IFEntry) entry1;
                        final IFEntry parent = entry.getParent();
                        IFEntry shadowParent = (IFEntry) parentsMap.get( invoice, parent );
                        if ( shadowParent == null )
                        {
                            shadowParent = new DefaultEntry( parent.getName(), parent.getType() );
                            parentsMap.put( invoice, parent, shadowParent );
                            children.add( shadowParent );
                        }
                        ArrayList childChildren = (ArrayList) parent2ChildrenMap.get( shadowParent );
                        if ( childChildren == null )
                        {
                            childChildren = new ArrayList();
                            parent2ChildrenMap.put( shadowParent, childChildren );
                        }
                        childChildren.add( entry );
                    }
                }
            }
            return children;
        }
    }
}
