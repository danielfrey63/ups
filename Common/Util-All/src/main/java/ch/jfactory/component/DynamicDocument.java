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

import java.awt.Toolkit;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * Validates the input by comparing each new character with the given valid characters.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:57 $
 */
public class DynamicDocument extends PlainDocument
{
    private final String validInput;

    /**
     * Constructor.
     *
     * @param validInput allowed characters for input
     */
    public DynamicDocument( final String validInput )
    {
        this.validInput = validInput;
    }

    public void insertString( final int offset, final String string, final AttributeSet attributes ) throws BadLocationException
    {
        if ( string == null )
        {
            return;
        }
        for ( int i = 0; i < string.length(); i++ )
        {
            if ( validInput.indexOf( string.charAt( i ) ) == -1 )
            {
                Toolkit.getDefaultToolkit().beep();
                return;
            }
        }
        super.insertString( offset, string, attributes );
    }
}
