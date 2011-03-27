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

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.pietschy.command.ActionCommand;

/**
 * Wraps an {@link ActionCommand} into an {@link Action}.
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2005/11/17 11:54:58 $
 */
public class CommandAction extends AbstractAction
{
    private final ActionCommand command;

    public CommandAction( final ActionCommand command )
    {
        this.command = command;
    }

    public void actionPerformed( final ActionEvent e )
    {
        command.execute();
    }

}
