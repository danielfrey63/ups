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
package ch.xmatrix.ups.uec.groups;

import ch.jfactory.resource.ImageLocator;
import ch.jfactory.component.RendererPanel;
import java.awt.Component;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2006/04/25 11:07:45 $
 */
public class GroupsListRenderer implements ListCellRenderer
{
    private final RendererPanel panel = new RendererPanel( RendererPanel.SelectionType.ALL );

    private boolean enabled;

    public Component getListCellRendererComponent( final JList list, final Object value, final int index,
                                                   final boolean selected, final boolean hasFocus )
    {
        final GroupModel model = (GroupModel) value;
        panel.setEnabled( enabled );
        panel.setIcon( ImageLocator.getIcon( "group.gif" ) );
        panel.setText( model.toString() );
        panel.setSelected( selected );
        panel.update();
        return panel;
    }

    public void setEnabled( final boolean enabled )
    {
        this.enabled = enabled;
    }
}
