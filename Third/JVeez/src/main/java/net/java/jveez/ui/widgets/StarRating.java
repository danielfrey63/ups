package net.java.jveez.ui.widgets;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Icon;
import javax.swing.JComponent;
import net.java.jveez.utils.Utils;

public class StarRating extends JComponent {

    private static Icon selectedStarIcon = Utils.loadIcon("net/java/jveez/icons/star_selected.png");
    private static Icon unselectedStarIcon = Utils.loadIcon("net/java/jveez/icons/star_unselected.png");

    private int value = 0;

    public StarRating() {
        super();
        setPreferredSize(new Dimension(selectedStarIcon.getIconWidth() * 3, selectedStarIcon.getIconHeight()));
        setOpaque(false);
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == e.BUTTON1) {
                    setValue((value + 1) % 4);
                }
                else if (e.getButton() == e.BUTTON2) {
                    setValue((value - 1) % 4);
                }
            }
        });
    }

    public StarRating(int value) {
        this();
        assert (value >= 0 && value <= 3);
        this.value = value;
    }

    public void setValue(int value) {
        assert (value >= 0 && value <= 3);
        if (this.value != value) {
            // TODO use a property instead
            this.value = value;
            if (isVisible()) {
                repaint();
            }
        }
    }

    public int getValue() {
        return value;
    }

    protected void paintComponent(Graphics g) {
        paintBorder(g);

        int iconWidth = selectedStarIcon.getIconWidth();
        int iconHeight = selectedStarIcon.getIconHeight();
        Insets insets = getInsets();

        int w = getWidth() - insets.left - insets.right;
        int h = getHeight() - insets.top - insets.bottom;
        int x = insets.left + (w - 3 * iconWidth) / 2;
        int y = insets.top + (h - iconHeight) / 2;

        if (value == 0) {
            unselectedStarIcon.paintIcon(this, g, x, y);
            unselectedStarIcon.paintIcon(this, g, x + iconWidth, y);
            unselectedStarIcon.paintIcon(this, g, x + 2 * iconWidth, y);
        }
        else if (value == 1) {
            selectedStarIcon.paintIcon(this, g, x, y);
            unselectedStarIcon.paintIcon(this, g, x + iconWidth, y);
            unselectedStarIcon.paintIcon(this, g, x + 2 * iconWidth, y);
        }
        else if (value == 2) {
            selectedStarIcon.paintIcon(this, g, x, y);
            selectedStarIcon.paintIcon(this, g, x + iconWidth, y);
            unselectedStarIcon.paintIcon(this, g, x + 2 * iconWidth, y);
        }
        else {
            selectedStarIcon.paintIcon(this, g, x, y);
            selectedStarIcon.paintIcon(this, g, x + iconWidth, y);
            selectedStarIcon.paintIcon(this, g, x + 2 * iconWidth, y);
        }
    }
}


