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
package ch.xmatrix.ups.view;

import org.pietschy.command.ActionCommand;
import org.pietschy.command.CommandManager;
import ch.jfactory.application.AbstractMainModel;

/**
 * Sets the models current card property to the card id passed during construction.
 *
 * @author Daniel Frey
 * @version $Revision: 1.3 $ $Date: 2006/04/21 11:02:52 $
 */
public class ChangeCardCommand extends ActionCommand {

    private AbstractMainModel model;
    private String cardId;

    public ChangeCardCommand(final CommandManager commandManager, final AbstractMainModel model,
                             final String commandId, final String cardId) {
        super(commandManager, commandId);
        this.model = model;
        this.cardId = cardId;
    }

    protected void handleExecute() {
        model.setCurrentCard(cardId);
    }
}
