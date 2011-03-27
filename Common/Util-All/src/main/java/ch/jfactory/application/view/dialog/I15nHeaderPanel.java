/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.application.view.dialog;

import ch.jfactory.application.presentation.Constants;
import ch.jfactory.color.ColorUtils;
import ch.jfactory.component.JMultiLineLabel;
import ch.jfactory.resource.ImageLocator;
import ch.jfactory.resource.Strings;
import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.Sizes;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.3 $ $Date: 2006/03/22 15:05:10 $
 */
public class I15nHeaderPanel extends JPanel
{
    public I15nHeaderPanel( final String prefix )
    {
        final int gap = Constants.GAP_BETWEEN_GROUP;
        final int gapTL = Constants.GAP_BORDER_LEFT_TOP;
        final int gapBR = Constants.GAP_BORDER_RIGHT_BOTTOM;
        final int gapT = Constants.GAP_WITHIN_TOGGLES;

        final JMultiLineLabel title1 = new JMultiLineLabel();
        title1.setText( Strings.getString( prefix + ".TEXT1" ) );
        title1.setFont( title1.getFont().deriveFont( Font.BOLD ) );
        title1.setBorder( Borders.createEmptyBorder( Sizes.DLUY8, Sizes.DLUX8, Sizes.DLUY4, Sizes.DLUX8 ) );
        title1.setOpaque( false );

        final JMultiLineLabel title2 = new JMultiLineLabel();
        title2.setText( Strings.getString( prefix + ".TEXT2" ) );
        title2.setBorder( Borders.createEmptyBorder( Sizes.dluY( 0 ), Sizes.dluX( 16 ), Sizes.DLUY8, Sizes.DLUX8 ) );
        title2.setOpaque( false );

        final JLabel symbol = new JLabel();
        final String iconString = Strings.getSilentString( prefix + ".SYMBOL" );
        symbol.setIcon( ImageLocator.getIcon( iconString ) );
        symbol.setBorder( new EmptyBorder( gap, gap, gap, gapBR ) );
        symbol.setOpaque( false );

        final JPanel textPanel = new JPanel( new BorderLayout() );
        textPanel.add( title1, BorderLayout.NORTH );
        textPanel.add( title2, BorderLayout.CENTER );
        textPanel.setMaximumSize( new Dimension( 400, 400 ) );
        textPanel.setOpaque( false );

        setLayout( new BorderLayout() );
        add( textPanel, BorderLayout.CENTER );
        add( symbol, BorderLayout.EAST );
//        setBorder(new ThinBevelBorder(BevelDirection.LOWERED));
        setBackground( UIManager.getColor( "Tree.background" ) );
    }

    protected void paintComponent( final Graphics g )
    {
        final int width1 = getWidth();
        final int height1 = getHeight();
        final Graphics2D g2 = (Graphics2D) g;
        final int x1;
        final int y1;
        final int w;
        final int h;
        final Rectangle clip = g2.getClipBounds();
        if ( clip != null )
        {
            x1 = clip.x;
            y1 = clip.y;
            w = clip.width;
            h = clip.height;
        }
        else
        {
            x1 = 0;
            y1 = 0;
            w = width1;
            h = height1;
        }
        paintHorizontalGradient( g2, x1, y1, w, h, width1, height1 );
        g2.setColor( ColorUtils.fade( Color.black, 0.8 ) );
        g2.fillRect( 0, h - 1, w, 1 );
    }

    /*
    * Paints a horizontal gradient background from white to the control color.
    */
    private static void paintHorizontalGradient( final Graphics2D g2, final int x, final int y, final int w, final int h, final int width, final int height )
    {
        final Color gradientColor = getHorizontalGradientColor( UIManager.getColor( "control" ) );
        g2.setPaint( new GradientPaint( 0, height * 2, Color.WHITE, width, -width + height, gradientColor ) );
        g2.fillRect( x, y, w, h );
    }

    /** Computes and answers a <code>Color</code> that has a minimum brightness. */
    private static Color getHorizontalGradientColor( final Color color )
    {
        final float minimumBrightness = 0.7f;
        final float[] hsbValues = new float[3];
        Color.RGBtoHSB( color.getRed(), color.getGreen(), color.getBlue(), hsbValues );
        final float brightness = hsbValues[2];
        if ( brightness > minimumBrightness )
        {
            return color;
        }
        final float hue = hsbValues[0];
        final float saturation = hsbValues[1];
        return Color.getHSBColor( hue, saturation, Math.max( minimumBrightness, brightness ) );
    }
}
