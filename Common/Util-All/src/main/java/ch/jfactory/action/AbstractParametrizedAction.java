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

import ch.jfactory.resource.Strings;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:57 $
 */
public abstract class AbstractParametrizedAction extends AbstractAction
{
    protected JFrame parent;

    public AbstractParametrizedAction( final String prefix, final JFrame parent )
    {
        super( Strings.getString( prefix + ".NAME" ) );
        this.parent = parent;

        putValue( Action.LONG_DESCRIPTION, Strings.getString( prefix + ".TEXT" ) );
        final char code = Strings.getChar( prefix + ".SC" );
        if ( code != Strings.MISSING_CHAR )
        {
            final KeyStroke keyStroke = KeyStroke.getKeyStroke( code, KeyEvent.ALT_MASK );
            putValue( Action.ACCELERATOR_KEY, keyStroke );
        }
        putValue( Action.MNEMONIC_KEY, new Integer( (int) Strings.getChar( prefix + ".MN" ) ) );
    }
}
