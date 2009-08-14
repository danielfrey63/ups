package ch.jfactory.component.tab;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalTabbedPaneUI;

public class NicedTabbedPaneUI extends MetalTabbedPaneUI
{

    Border contentBorder;

    private int size = 2;

    public NicedTabbedPaneUI()
    {
        super();
    }

    public void installDefaults()
    {
        super.installDefaults();
        contentBorder = BorderFactory.createLineBorder(Color.red, 10); //new TransparentBorder(0);//
    }

    protected Insets getContentBorderInsets(final int tabPlacement)
    {
        switch (tabPlacement)
        {
            case BOTTOM:
                return new Insets(contentBorder.getBorderInsets(null).top,
                        contentBorder.getBorderInsets(null).right,
                        contentBorder.getBorderInsets(null).bottom + size,
                        contentBorder.getBorderInsets(null).left);
            default:
                return new Insets(contentBorder.getBorderInsets(null).top + size,
                        contentBorder.getBorderInsets(null).right,
                        contentBorder.getBorderInsets(null).bottom,
                        contentBorder.getBorderInsets(null).left);
        }

    }

    protected void paintContentBorder(final Graphics g, final int tabPlacement, final int selectedIndex)
    {
        final int width = tabPane.getWidth();
        final int height = tabPane.getHeight();
        final Insets insets = tabPane.getInsets();

        int x = insets.left;
        int y = insets.top;
        int w = width - insets.right - insets.left;
        int h = height - insets.top - insets.bottom;

        int tah = 0;
        switch (tabPlacement)
        {
            case LEFT:
                x += calculateTabAreaWidth(tabPlacement, runCount, maxTabWidth);
                w -= (x - insets.left);
                w = insets.left + size;
                break;
            case RIGHT:
                w -= calculateTabAreaWidth(tabPlacement, runCount, maxTabWidth);
                x = x + w - (insets.left + size);
                w = insets.left + size;
                break;
            case BOTTOM:
                tah = calculateTabAreaHeight(tabPlacement, runCount, maxTabHeight);
                contentBorder.paintBorder(tabPane.getSelectedComponent(), g, x, y, w, h - tah - size);

                y = height - (insets.bottom + tah + insets.top + size);
                h = insets.top + size;
                break;
            case TOP:
            default:
                tah = calculateTabAreaHeight(tabPlacement, runCount, maxTabHeight);
                contentBorder.paintBorder(tabPane.getSelectedComponent(), g, x, y + size + tah, w, h - size - tah);
                y += tah;
                h = insets.top + size;//height-insets.bottom-y;
        }
        // Fill region behind content area
        final Color selectedColor = UIManager.getColor("TabbedPane.selected");
        if (selectedColor == null)
        {
            g.setColor(tabPane.getBackground());
        }
        else
        {
            g.setColor(selectedColor);
        }
        g.fillRect(x, y, w, h);
    }

    public static ComponentUI createUI(final JComponent x)
    {
        return new NicedTabbedPaneUI();
    }

    protected void paintTabBackground(final Graphics g, final int tabPlacement,
                                      final int tabIndex, final int x, final int y, final int w, final int h, final boolean isSelected)
    {
        if (isSelected)
        {
            g.setColor(selectColor);
        }
        else
        {
            g.setColor(UIManager.getColor("Panel.background"));
        }

        if (tabPane.getComponentOrientation().isLeftToRight())
        {
            switch (tabPlacement)
            {
                case LEFT:
                    g.fillRect(x + 5, y + 1, w - 5, h - 1);
                    g.fillRect(x + 2, y + 4, 3, h - 4);
                    break;
                case BOTTOM:
                    g.fillRect(x + 2, y, w - 2, h - 4);
                    g.fillRect(x + 5, y + (h - 1) - 3, w - 5, 3);
                    break;
                case RIGHT:
                    g.fillRect(x + 1, y + 1, w - 5, h - 1);
                    g.fillRect(x + (w - 1) - 3, y + 5, 3, h - 5);
                    break;
                case TOP:
                default:
                    g.fillRect(x + 4, y + 2, (w - 1) - 3, (h - 1) - 1);
                    g.fillRect(x + 2, y + 5, 2, h - 5);
            }
        }
        else
        {
            switch (tabPlacement)
            {
                case LEFT:
                    g.fillRect(x + 5, y + 1, w - 5, h - 1);
                    g.fillRect(x + 2, y + 4, 3, h - 4);
                    break;
                case BOTTOM:
                    g.fillRect(x, y, w - 5, h - 1);
                    g.fillRect(x + (w - 1) - 4, y, 4, h - 5);
                    g.fillRect(x + (w - 1) - 4, y + (h - 1) - 4, 2, 2);
                    break;
                case RIGHT:
                    g.fillRect(x + 1, y + 1, w - 5, h - 1);
                    g.fillRect(x + (w - 1) - 3, y + 5, 3, h - 5);
                    break;
                case TOP:
                default:
                    g.fillRect(x, y + 2, (w - 1) - 3, (h - 1) - 1);
                    g.fillRect(x + (w - 1) - 3, y + 4, 3, h - 4);
            }
        }
    }

}