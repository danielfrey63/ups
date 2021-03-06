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

import com.Ostermiller.util.Browser;
import java.io.IOException;
import org.pietschy.command.ActionCommand;
import org.pietschy.command.CommandManager;

/**
 * Calls the default system browser and displays the URL.
 *
 * @author Daniel Frey
 * @version $Revision: 1.2 $ $Date: 2006/03/14 21:27:55 $
 */
public class RunBrowserCommand extends ActionCommand
{
    /** The URL to open. */
    private final String site;

    public RunBrowserCommand( final CommandManager commandManager, final String commandId, final String site )
    {
        super( commandManager, commandId );
        this.site = site;
        Browser.init();
    }

    protected void handleExecute()
    {
        try
        {
            Browser.displayURL( site );
        }
        catch ( IOException e1 )
        {
            e1.printStackTrace();
        }
    }
}
