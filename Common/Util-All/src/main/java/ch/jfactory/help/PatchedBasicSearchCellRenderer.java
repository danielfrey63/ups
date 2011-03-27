/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.help;

import java.awt.Font;
import javax.help.Map;
import javax.help.plaf.basic.BasicSearchCellRenderer;
import javax.swing.JLabel;

/**
 * Resolves Suns bug 4788532
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:58 $
 */
public class PatchedBasicSearchCellRenderer extends BasicSearchCellRenderer
{
    public PatchedBasicSearchCellRenderer( final Map map )
    {
        super( map );
    }

    public void setFont( final Font font )
    {
        // initialize title variable to avoid NullPointerException in super.setFont
        if ( title == null )
        {
            title = new JLabel();
        }
        super.setFont( font );
    }
}
