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

import ch.jfactory.component.tree.AbstractTreeModel;
import ch.jfactory.lang.ArrayUtils;
import ch.jfactory.projecttime.domain.api.IFEntry;
import javax.swing.tree.TreePath;

/**
 * TODO: document
 *
 * @author <a href="daniel.frey@xmatrix.ch">Daniel Frey</a>
 * @version $Revision: 1.1 $ $Date: 2005/11/17 11:56:29 $
 */
public class ProjectTreeModel extends AbstractTreeModel
{
    public ProjectTreeModel( final Object root )
    {
        super( root );
    }

    protected void remove( final Object child, final TreePath parentPath )
    {
        final IFEntry parent = (IFEntry) parentPath.getLastPathComponent();
        final IFEntry[] children = parent.getChildren();
        final IFEntry[] newChildren = (IFEntry[]) ArrayUtils.remove( children, child, new IFEntry[0] );
        parent.setChildren( newChildren );
    }

    protected void insert( final TreePath childPath, final TreePath parentPath, final int pos )
    {
        final IFEntry parent = (IFEntry) parentPath.getLastPathComponent();
        final IFEntry child = (IFEntry) childPath.getLastPathComponent();
        final IFEntry[] children = parent.getChildren();
        final IFEntry[] newChildren = (IFEntry[]) ArrayUtils.insert( children, child, pos, new IFEntry[0] );
        parent.setChildren( newChildren );
        child.setParent( parent );
    }

    public int getChildCount( final Object parent )
    {
        final IFEntry parentEntry = (IFEntry) parent;
        final IFEntry[] children = parentEntry.getChildren();
        return children == null ? 0 : children.length;
    }

    public Object getChild( final Object parent, final int index )
    {
        final IFEntry parentEntry = (IFEntry) parent;
        return parentEntry.getChildren()[index];
    }

    public void valueForPathChanged( final TreePath path, final Object newValue )
    {
    }
}
