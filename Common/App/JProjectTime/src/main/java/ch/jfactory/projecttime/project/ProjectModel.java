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
package ch.jfactory.projecttime.project;

import ch.jfactory.projecttime.domain.api.IFEntry;
import ch.jfactory.projecttime.domain.impl.Invoice;
import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.beans.Model;
import com.jgoodies.binding.value.ValueHolder;
import com.jgoodies.binding.value.ValueModel;
import java.util.Map;
import javax.swing.tree.DefaultTreeSelectionModel;

/**
 * Model for the {@link ProjectBuilder}.
 *
 * @author <a href="daniel.frey@xmatrix.ch">Daniel Frey</a>
 * @version $Revision: 1.1 $ $Date: 2005/11/17 11:56:29 $
 */
public class ProjectModel extends Model
{
    public static final String PROPERTYNAME_NEWCHILD = "newChild";

    private IFEntry newChild;

    public static final String PROPERTYNAME_DELETEDCHILD = "deletedChild";

    private IFEntry deletedChild;

    public static final String PROPERTYNAME_ROOT = "root";

    private IFEntry root;

    public static final String PROPERTYNAME_RUNNING = "running";

    private IFEntry running;

    public static final String PROPERTYNAME_TREEMODEL = "treeModel";

    private ProjectTreeModel treeModel;

    private final ValueModel selectionModel = new ValueHolder( new DefaultTreeSelectionModel() );

    private final PresentationModel currentBeanModel = new PresentationModel( new ValueHolder( null, true ) );

    public static final String PROPERTYNAME_INVOICEADDED = "invoiceAdded";

    public static final String PROPERTYNAME_INVOICEREMOVED = "invoiceRemoved";

    private final ValueModel entry2InvoiceMap;

    public ProjectModel( final IFEntry root, final ValueModel entry2InvoiceMap )
    {
        this.root = root;
        this.entry2InvoiceMap = entry2InvoiceMap;
        treeModel = new ProjectTreeModel( root );
    }

    public IFEntry getNewChild()
    {
        return newChild;
    }

    public void setNewChild( final IFEntry newChild )
    {
        final IFEntry old = getNewChild();
        this.newChild = newChild;
        firePropertyChange( PROPERTYNAME_NEWCHILD, old, newChild );
    }

    public IFEntry getDeletedChild()
    {
        return deletedChild;
    }

    public void setDeletedChild( final IFEntry deletedChild )
    {
        final IFEntry old = getDeletedChild();
        this.deletedChild = deletedChild;
        firePropertyChange( PROPERTYNAME_DELETEDCHILD, old, deletedChild );
    }

    public IFEntry getRoot()
    {
        return root;
    }

    public void setRoot( final IFEntry root )
    {
        final IFEntry old = getRoot();
        this.root = root;
        firePropertyChange( PROPERTYNAME_ROOT, old, root );
    }

    public IFEntry getRunning()
    {
        return running;
    }

    public void setRunning( final IFEntry running )
    {
        final IFEntry old = getRunning();
        this.running = running;
        firePropertyChange( PROPERTYNAME_RUNNING, old, running );
    }

    // Submodels

    public ProjectTreeModel getTreeModel()
    {
        return treeModel;
    }

    public void setTreeModel( final ProjectTreeModel treeModel )
    {
        final ProjectTreeModel old = getTreeModel();
        this.treeModel = treeModel;
        firePropertyChange( PROPERTYNAME_TREEMODEL, old, treeModel );
    }

    public PresentationModel getCurrentBeanModel()
    {
        return currentBeanModel;
    }

    public ValueModel getSelectionModel()
    {
        return selectionModel;
    }

    public Invoice getInvoice( final IFEntry entry )
    {
        final Map map = (Map) entry2InvoiceMap.getValue();
        return (Invoice) map.get( entry );
    }

    public void addInvoice( final IFEntry entry, final Invoice invoice )
    {
        final Map map = (Map) entry2InvoiceMap.getValue();
        final Invoice old = (Invoice) map.put( entry, invoice );
        if ( invoice == null )
        {
            System.err.println( "invoice is null" );
            return;
        }
        invoice.addEntry( entry );
        firePropertyChange( PROPERTYNAME_INVOICEADDED, old, invoice );
    }

    public void removeInvoice( final IFEntry entry )
    {
        final Map map = (Map) entry2InvoiceMap.getValue();
        final Invoice old = (Invoice) map.remove( entry );
        if ( old != null )
        {
            old.removeEntry( entry );
        }
        firePropertyChange( PROPERTYNAME_INVOICEREMOVED, old, null );
    }

    public ValueModel getEntry2InvoiceMap()
    {
        return entry2InvoiceMap;
    }

    public void setEntry2InvoiceMap( final Map map )
    {
        this.entry2InvoiceMap.setValue( map );
    }
}
