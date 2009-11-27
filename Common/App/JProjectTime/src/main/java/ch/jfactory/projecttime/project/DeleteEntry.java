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
import com.jgoodies.binding.PresentationModel;
import org.pietschy.command.ActionCommand;
import org.pietschy.command.CommandManager;
import javax.swing.tree.TreeSelectionModel;

/**
 * TODO: document
 *
 * @author <a href="daniel.frey@xmatrix.ch">Daniel Frey</a>
 * @version $Revision: 1.1 $ $Date: 2005/11/17 11:56:29 $
 */
public class DeleteEntry extends ActionCommand {

    private ProjectModel model;

    public DeleteEntry(final CommandManager manager, final ProjectModel model) {
        super(manager, Commands.COMMANDID_DELETE);
        this.model = model;
    }

    protected void handleExecute() {
        final PresentationModel currentBeanModel = model.getCurrentBeanModel();
        final IFEntry entry = (IFEntry) currentBeanModel.getBean();
        if (entry != null && entry.getParent() != null) {
            final TreeSelectionModel selectionModel = (TreeSelectionModel) model.getSelectionModel().getValue();
            model.getTreeModel().removeFromParent(selectionModel.getSelectionPath());
        }
    }
}
