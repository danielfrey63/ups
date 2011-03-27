/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import javax.swing.JButton;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:57 $
 */
class ButtonRedirector extends AbstractAction
{
    protected JButton button;

    public ButtonRedirector( final JButton button )
    {
        super.putValue( AbstractAction.NAME, button.getText() );
        this.button = button;
    }

    public void actionPerformed( final ActionEvent e )
    {
        if ( button.isEnabled() )
        {
            final ActionListener[] listeners = button.getActionListeners();
            for ( final ActionListener listener : listeners )
            {
                listener.actionPerformed( e );
            }
        }
    }
}
