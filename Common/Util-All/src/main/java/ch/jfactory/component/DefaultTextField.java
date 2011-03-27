/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.component;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JTextField;
import javax.swing.text.Document;

/**
 * Initial text will be take as a default for greyed display until the first click or focus gained occurs which does empty the field.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:57 $
 */
public class DefaultTextField extends JTextField
{
    public DefaultTextField()
    {
        this( null, "", 0 );
    }

    public DefaultTextField( final int columns )
    {
        this( null, "", columns );
    }

    public DefaultTextField( final String text )
    {
        this( null, text, 0 );
    }

    public DefaultTextField( final String text, final int columns )
    {
        this( null, text, columns );
    }

    public DefaultTextField( final Document doc, final String text, final int columns )
    {
        super( doc, text, columns );
        this.addFocusListener( new FocusListener()
        {
            public void focusGained( final FocusEvent e )
            {
                selectAll();
            }

            public void focusLost( final FocusEvent e )
            {
            }
        } );
    }
}
