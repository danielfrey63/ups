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
package ch.jfactory.command;

import ch.jfactory.component.tree.TreeUtils;
import javax.swing.JTree;
import org.pietschy.command.ActionCommand;
import org.pietschy.command.CommandManager;

/**
 * Expands all nodes of a given tree.
 *
 * @author Daniel Frey
 * @version $Revision: 1.2 $ $Date: 2006/03/14 21:27:55 $
 */
public class ExpandAllTreeNodes extends ActionCommand
{
    private final JTree tree;

    public ExpandAllTreeNodes( final CommandManager commandManager, final JTree tree )
    {
        super( commandManager, CommonCommands.COMMANDID_TREEEXPANDALLNODES );
        this.tree = tree;
    }

    protected void handleExecute()
    {
        TreeUtils.expandAll( tree );
    }
}
