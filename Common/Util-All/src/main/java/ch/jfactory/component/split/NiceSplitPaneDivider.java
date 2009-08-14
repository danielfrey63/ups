package ch.jfactory.component.split;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.BorderFactory;
import javax.swing.JSplitPane;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import javax.swing.plaf.metal.MetalLookAndFeel;

/**
 * Project: $Id: NiceSplitPaneDivider.java,v 1.1 2005/06/16 06:28:57 daniel_frey Exp $ $Source:
 * /repository/HerbarCD/Version2.1/xmatrix/src/com/xmatrix/gui/component/split/NiceSplitPaneDivider.java,v $ $Revision:
 * 1.3 $, $Author: daniel_frey $
 */
public class NiceSplitPaneDivider extends BasicSplitPaneDivider
{
    private Color controlColor = MetalLookAndFeel.getControl();

    private Color shadow = MetalLookAndFeel.getControlShadow();

    private Color primaryControlColor = MetalLookAndFeel.getPrimaryControl();

    private static final double FACTOR = 0.95;

    public NiceSplitPaneDivider(final BasicSplitPaneUI ui)
    {
        super(ui);
        setBorder(BorderFactory.createEmptyBorder());
    }

    public void paint(final Graphics g)
    {
        Color c = primaryControlColor;
        Color gradient = c;
        if (!splitPane.hasFocus())
        {
            c = controlColor;
            gradient =
                    new Color(Math.max((int) (controlColor.getRed() * FACTOR), 0),
                            Math.max((int) (controlColor.getGreen() * FACTOR), 0),
                            Math.max((int) (controlColor.getBlue() * FACTOR), 0))
                    ;
        }
        final Rectangle clip = g.getClipBounds();
        g.setColor(c);
        g.fillRect(clip.x, clip.y, clip.width, clip.height);
        g.setColor(shadow);
        if (splitPane.getOrientation() == JSplitPane.VERTICAL_SPLIT)
        {
            g.fillRect(clip.x, clip.y, clip.width, 1);
        }
        else
        {
            g.fillRect(clip.x, clip.y, 1, clip.height);
        }
        super.paint(g);
    }

}
