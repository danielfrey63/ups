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

import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import org.pietschy.command.ActionCommand;
import org.pietschy.command.CommandManager;

/**
 * Closes the application after giving each {@link VetoableChangeListener} registered with the model a chance to cancel closing.
 *
 * @author Daniel Frey
 * @version $Revision: 1.3 $ $Date: 2006/03/14 21:27:55 $
 */
public class QuitCommand extends ActionCommand
{
    private final ClosingModel model;

    public QuitCommand( final CommandManager commandManager, final ClosingModel model )
    {
        super( commandManager, CommonCommands.COMMANDID_QUIT );
        this.model = model;
    }

    protected void handleExecute()
    {
        try
        {
            model.setClosing();
            System.exit( 0 );
        }
        catch ( PropertyVetoException e )
        {
        }
    }
}
