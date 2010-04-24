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
package ch.xmatrix.ups.uec.session.commands;

import ch.xmatrix.ups.uec.constraints.commands.Commands;
import javax.swing.JOptionPane;
import javax.swing.ListModel;
import org.pietschy.command.ActionCommand;
import org.pietschy.command.CommandManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.2 $ $Date: 2008/01/06 10:16:20 $
 */
public class UploadCommand extends ActionCommand
{
    private static final Logger LOG = LoggerFactory.getLogger( UploadCommand.class );

    private final ListModel model;

    public UploadCommand( final CommandManager commandManager, final ListModel model )
    {
        super( commandManager, Commands.COMMAND_ID_UPLOAD );
        this.model = model;
    }

    protected void handleExecute()
    {
        try
        {
            final UploadDialog dialog = new UploadDialog( model );
            dialog.pack();
            dialog.setVisible( true );
        }
        catch ( Exception e )
        {
            LOG.error( "error during in the dialog", e );
            JOptionPane.showMessageDialog( null,
                    "Beim Dialog wurde ein Fehler entdeckt:\n" + e.getMessage(),
                    "Fehler", JOptionPane.ERROR_MESSAGE );
        }
    }
}
