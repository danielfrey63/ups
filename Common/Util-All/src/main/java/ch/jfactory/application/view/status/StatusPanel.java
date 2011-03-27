/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.application.view.status;

import ch.jfactory.application.view.border.BevelDirection;
import ch.jfactory.application.view.border.ThinBevelBorder;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class StatusPanel extends JPanel
{
    private final JComponent component;

    private Dimension maxPreferredSize;

    public StatusPanel( final JComponent component )
    {
        this.component = component;
        setBorder( BorderFactory.createCompoundBorder( new ThinBevelBorder( BevelDirection.LOWERED ), BorderFactory.createEmptyBorder( 2, 8, 2, 8 ) ) );
        setLayout( new BorderLayout() );
        add( component, BorderLayout.CENTER );
        maxPreferredSize = super.getPreferredSize();
    }

    public Dimension getPreferredSize()
    {
        super.getPreferredSize();
        if ( super.getPreferredSize().width > maxPreferredSize.width )
        {
            maxPreferredSize = super.getPreferredSize();
        }
        return maxPreferredSize;
    }

    public String toString()
    {
        return component.toString();
    }
}
