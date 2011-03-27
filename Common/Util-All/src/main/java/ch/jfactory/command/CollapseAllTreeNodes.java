/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.command;

import ch.jfactory.component.tree.TreeUtils;
import javax.swing.JTree;
import org.pietschy.command.ActionCommand;
import org.pietschy.command.CommandManager;

/**
 * Collapses all nodes of a given tree but the root node.
 *
 * @author Daniel Frey
 * @version $Revision: 1.2 $ $Date: 2007/09/27 10:41:22 $
 */
public class CollapseAllTreeNodes extends ActionCommand
{
    private final JTree tree;

    public CollapseAllTreeNodes( final CommandManager commandManager, final JTree tree )
    {
        super( commandManager, CommonCommands.COMMANDID_TREECOLLAPSEALLNODES );
        this.tree = tree;
    }

    protected void handleExecute()
    {
        TreeUtils.collapseAll( tree );
    }
}
