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
package ch.jfactory.projecttime.project.builder;

import ch.jfactory.projecttime.domain.api.IFEntry;
import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.beans.Model;
import com.jgoodies.binding.value.Trigger;
import com.jgoodies.binding.value.ValueHolder;

/**
 * TODO: document
 *
 * @author <a href="daniel.frey@xmatrix.ch">Daniel Frey</a>
 * @version $Revision: 1.1 $ $Date: 2005/09/04 19:54:10 $
 */
public class ProjectModel extends Model
{
    public static final String PROPERTYNAME_NEWCHILD = "newChild";

    private IFEntry newChild;

    public static final String PROPERTYNAME_DELETEDCHILD = "deletedChild";

    private IFEntry deletedChild;

    public static final String PROPERTYNAME_ROOT = "root";

    private IFEntry root;

    private final PresentationModel currentBeanModel = new PresentationModel( new ValueHolder( null, true ), new Trigger() );

    public ProjectModel( final IFEntry root )
    {
        this.root = root;
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

    // Submodels

    public PresentationModel getCurrentBeanModel()
    {
        return currentBeanModel;
    }
}
