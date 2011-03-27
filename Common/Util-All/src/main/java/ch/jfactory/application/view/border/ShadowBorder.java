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

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.UIManager;
import javax.swing.border.AbstractBorder;

/** Project: $Id: ShadowBorder.java,v 1.1 2005/06/16 06:28:57 daniel_frey Exp $ $Source: /repository/HerbarCD/Version2.1/xmatrix/src/com/xmatrix/gui/border/ShadowBorder.java,v $ $Revision: 1.1 $, $Author: daniel_frey $ */
public class ShadowBorder extends AbstractBorder
{
    private final static Insets insets = new Insets( 1, 1, 3, 3 );

    public Insets getBorderInsets( final Component c )
    {
        return insets;
    }

    public void paintBorder( final Component c,
                             final Graphics g,
                             final int x,
                             final int y,
                             final int w,
                             final int h )
    {
        final Color shadow = UIManager.getColor( "controlShadow" );
        g.translate( x, y );
        g.setColor( shadow );
        g.drawLine( 0, 0, w - 4, 0 );
        g.drawLine( 0, 0, 0, h - 4 );
        g.drawLine( w - 3, 0, w - 3, h - 3 );
        g.drawLine( 0, h - 3, w - 2, h - 3 );
        // Shadow line 1
        final Color lightShadow =
                new Color( shadow.getRed(),
                        shadow.getGreen(),
                        shadow.getBlue(),
                        200 );
        g.setColor( lightShadow );
        g.drawLine( w - 2, 1, w - 2, h - 2 );
        g.drawLine( 1, h - 2, w - 3, h - 2 );
        // Shadow line2
        final Color lighterShadow =
                new Color( shadow.getRed(),
                        shadow.getGreen(),
                        shadow.getBlue(),
                        100 );
        g.setColor( lighterShadow );
        g.drawLine( w - 1, 2, w - 1, h - 2 );
        g.drawLine( 2, h - 1, w - 2, h - 1 );
        g.translate( -x, -y );
    }
}
