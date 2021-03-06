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

import com.jgoodies.binding.PresentationModel;
import org.pietschy.command.ActionCommand;
import org.pietschy.command.CommandManager;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2005/11/17 11:54:58 $
 */
public class CommitPresentationModel extends ActionCommand
{
    private final PresentationModel model;

    public CommitPresentationModel( final CommandManager manager, final PresentationModel model )
    {
        super( manager, CommonCommands.COMMANDID_TRIGGERAPPLY );
        this.model = model;
    }

    protected void handleExecute()
    {
        model.triggerCommit();
    }
}
