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

import javax.help.JHelpSearchNavigator;
import javax.help.Map;
import javax.help.NavigatorView;
import javax.help.plaf.basic.BasicSearchNavigatorUI;
import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.plaf.ComponentUI;

/**
 * Resolves Suns bug 4788532
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.2 $ $Date: 2006/03/14 21:27:55 $
 */
public class PatchedBasicSearchNavigatorUI extends BasicSearchNavigatorUI
{
    public static ComponentUI createUI( final JComponent x )
    {
        return new PatchedBasicSearchNavigatorUI( (JHelpSearchNavigator) x );
    }

    public PatchedBasicSearchNavigatorUI( final JHelpSearchNavigator b )
    {
        super( b );
    }

    protected void setCellRenderer( final NavigatorView view, final JTree tree )
    {
        if ( view == null )
        {
            return;
        }
        final Map map = view.getHelpSet().getCombinedMap();
        tree.setCellRenderer( new PatchedBasicSearchCellRenderer( map ) );
    }
}
