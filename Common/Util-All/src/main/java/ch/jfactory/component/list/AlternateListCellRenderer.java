/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.component.list;

import java.awt.Color;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2005/11/17 11:54:58 $
 */
public class AlternateListCellRenderer extends DefaultListCellRenderer
{
    public Component getListCellRendererComponent( final JList list, final Object value, final int index, final boolean isSelected, final boolean cellHasFocus )
    {
        super.getListCellRendererComponent( list, value, index, isSelected, cellHasFocus );

        final Color bg1 = list.getSelectionBackground();
        final Color bg2 = new Color( bg1.getRed(), bg1.getGreen(), bg1.getBlue(), 50 );

        setBackground( isSelected ? bg1 : ( index % 2 == 0 ? bg2 : list.getBackground() ) );
        setForeground( isSelected ? list.getSelectionForeground() : list.getForeground() );

        return this;
    }
}
