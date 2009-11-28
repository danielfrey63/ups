/*
 * ====================================================================
 *  Copyright 2004-2006 www.xmatrix.ch
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 * ====================================================================
 */
package ch.xmatrix.ups.ust.main.commands;

import ch.xmatrix.ups.model.SessionModel;
import ch.xmatrix.ups.ust.main.MainModel;
import java.beans.PropertyVetoException;
import org.pietschy.command.ActionCommand;
import org.pietschy.command.CommandManager;

/**
 * Sets the closeing and opening event in the main model.
 *
 * @author Daniel Frey
 * @version $Revision: 1.4 $ $Date: 2007/05/16 17:00:16 $
 */
public class NewFileCommand extends ActionCommand
{
    private final MainModel model;

    public NewFileCommand( final CommandManager commandManager, final MainModel model )
    {
        super( commandManager, Commands.COMMANDID_NEW );
        this.model = model;
    }

    /**
     * Sets the closeing and opening event in the main model.
     */
    protected void handleExecute()
    {
        try
        {
            final SessionModel sessionModel = Commands.runExamInfoChooser( model );
            if ( sessionModel != null )
            {
                model.sessionModels.setSelection( sessionModel );
                Commands.setNewUserModel( model );
                model.setOpening();
            }
        }
        catch ( PropertyVetoException e )
        {
        }
    }
}
