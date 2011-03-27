/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.application.view.border;

import java.awt.Component;
import java.awt.Graphics;
import javax.swing.border.LineBorder;

public class TransparentBorder extends LineBorder
{
    public TransparentBorder()
    {
        super( null );
    }

    public TransparentBorder( final int i )
    {
        super( null, i );
    }

    public void paintBorder( final Component c, final Graphics g, final int x, final int y, final int width, final int height )
    {
        lineColor = c.getBackground();
        super.paintBorder( c, g, x, y, width, height );
    }
}