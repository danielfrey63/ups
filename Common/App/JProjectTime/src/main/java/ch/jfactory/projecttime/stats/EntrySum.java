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
package ch.jfactory.projecttime.stats;

import ch.jfactory.component.tree.TreeUtils;
import ch.jfactory.lang.DateUtils;
import ch.jfactory.projecttime.domain.api.IFEntry;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import org.pietschy.command.ActionCommand;
import org.pietschy.command.CommandManager;

/**
 * TODO: document
 *
 * @author <a href="daniel.frey@xmatrix.ch">Daniel Frey</a>
 * @version $Revision: 1.1 $ $Date: 2005/11/17 11:56:29 $
 */
public class EntrySum extends ActionCommand {

    private StatsModel model;

    public EntrySum(final CommandManager manager, final StatsModel model) {
        super(manager, Commands.COMMANDID_SUM);
        this.model = model;
    }

    protected void handleExecute() {
        final TreeSelectionModel treeSelectionModel = (TreeSelectionModel) model.getSelectionModel().getValue();
        final TreePath[] paths = treeSelectionModel.getSelectionPaths();
        final IFEntry[] entries = (IFEntry[]) TreeUtils.getNodesForPaths(paths, new IFEntry[0]);
        model.setSum(DateUtils.dateDifference(StatUtils.sumAll(entries, 0), "HH:mm:ss"));
    }
}
