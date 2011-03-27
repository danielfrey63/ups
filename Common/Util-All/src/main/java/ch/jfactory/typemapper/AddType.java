/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.typemapper;

import org.pietschy.command.ActionCommand;
import org.pietschy.command.CommandManager;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2005/11/17 11:54:59 $
 */
public class AddType extends ActionCommand
{
    private final TypeModel model;

    public AddType( final CommandManager commandManager, final TypeModel model )
    {
        super( commandManager, Commands.COMMANDID_ADD );
        this.model = model;
    }

    protected void handleExecute()
    {
        final TypeMapping mapping = new TypeMapping( "Neuer Typ", "/16x16/outline/man.png" );
        model.getDataModel().add( mapping );
        model.getSelectionInList().setSelection( mapping );
    }
}
