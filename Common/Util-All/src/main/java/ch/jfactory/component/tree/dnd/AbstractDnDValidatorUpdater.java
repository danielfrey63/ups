/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.component.tree.dnd;

import ch.jfactory.component.tree.NotifiableTreeModel;
import javax.swing.tree.TreePath;

/**
 * Some handling of insertions and deletions within the tree are done in this class.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.2 $ $Date: 2005/11/17 11:54:58 $
 */
public abstract class AbstractDnDValidatorUpdater implements DnDValidatorUpdater
{
    /** The tree model used for updates */
    private NotifiableTreeModel model;

    /**
     * If the tree can be given upon creation, this is the better way. See {@link NotifiableTreeModel} for further infos.
     *
     * @param model the model used for this validator
     */
    public AbstractDnDValidatorUpdater( final NotifiableTreeModel model )
    {
        setModel( model );
    }

    /** A cyclic dependency between this and the calling object might make it impossible to allocate the JTree before constructing this object. Use {@link #setModel(NotifiableTreeModel)} to register the model later. */
    public AbstractDnDValidatorUpdater()
    {
    }

    /**
     * Returns the model.
     *
     * @return DefaultTreeModel
     */
    public NotifiableTreeModel getModel()
    {
        return model;
    }

    /**
     * Sets the tree for this validator. All validations and updates will be performed on that tree or its model.
     *
     * @param model the new model object to set
     */
    public void setModel( final NotifiableTreeModel model )
    {
        this.model = model;
    }

    /** @see DnDValidatorUpdater #insertInto(TreePath, TreePath, int) */
    public void insertInto( final TreePath missile, final TreePath parent, final int pos )
    {
        getModel().insertInto( missile, parent, pos );
        updateNodesChanged();
    }

    /** @see DnDValidatorUpdater #removeFromParent(TreePath) */
    public void removeFromParent( final TreePath missile )
    {
        getModel().removeFromParent( missile );
        updateNodesChanged();
    }

    /** Informes the subclass of a change in the node structure. */
    public abstract void updateNodesChanged();
}

// $Log: AbstractDnDValidatorUpdater.java,v $
// Revision 1.2  2005/11/17 11:54:58  daniel_frey
// no message
//
// Revision 1.1  2005/06/16 06:28:58  daniel_frey
// Completely merged and finished for UST version 2.0-20050616
//
// Revision 1.3  2004/07/22 13:00:09  daniel_frey
// *** empty log message ***
//
// Revision 1.2  2004/07/08 22:17:52  daniel_frey
// Some FQN cleanup
//
// Revision 1.1  2004/06/16 21:17:17  daniel_frey
// Extracted package from xmatrix to jfactory
//
// Revision 1.1  2004/04/19 10:31:21  daniel_frey
// Replaced top level package com by ch
//
// Revision 1.5  2002/11/05 11:21:58  daniel_frey
// - Level with tree from GraphNode
//
// Revision 1.4  2002/09/25 14:41:35  daniel_frey
// - Introduced dynamic relevance object model
// - Replaced roles with relevances  by class types for each comination
// - Removed some caching issues
//
// Revision 1.3  2002/09/10 09:28:02  Dani
// - Added parameter to isMoveAllowed
//
// Revision 1.2  2002/08/05 19:21:32  Dani
// - Mor dnd working but not saving
//
// Revision 1.1  2002/08/05 13:40:08  Dani
// - Moved to ch.xmatrix
//
// Revision 1.2  2002/08/01 16:05:28  Dani
// Commented
//
// Revision 1.1  2002/08/01 15:58:18  Dani
// Initial
//

