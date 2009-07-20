/*
 * Copyright 2002 by x-matrix Switzerland
 *
 * ThinBevelBorder.java
 */
package ch.jfactory.application.view.border;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.io.Serializable;
import javax.swing.UIManager;
import javax.swing.border.Border;

/**
 * This border appears one pixel lowered or raised.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:57 $
 */
public class ThinBevelBorder implements Serializable, Border {

    public static final Insets INSETS = new Insets(1, 1, 1, 1);

    protected BevelDirection bevelType = BevelDirection.RAISED;
    protected Color highlight = UIManager.getColor("controlHighlight");
    protected Color shadow = UIManager.getColor("controlShadow");

    public ThinBevelBorder(final BevelDirection bevelType) {
        this.bevelType = bevelType;
    }

    public void setBevelType(final BevelDirection type) {
        bevelType = type;
    }

    public BevelDirection getBevelType() {
        return bevelType;
    }

    public Insets getBorderInsets(final Component c) {
        return INSETS;
    }

    public boolean isBorderOpaque() {
        return true;
    }

    public void paintBorder(final Component c,
                            final Graphics g, final int x, final int y, final int width,
                            final int height) {

        final Color upperLeft;

        final Color lowerRight;

        if (bevelType == BevelDirection.RAISED) {
            upperLeft = highlight;
            lowerRight = shadow;
        }
        else {
            upperLeft = shadow;
            lowerRight = highlight;
        }

        final int left = x;
        final int right = width - 1;
        final int top = y;
        final int bottom = height - 1;

        g.setColor(upperLeft);
        g.drawLine(left, top, right, top);
        g.drawLine(left, top, left, bottom);

        g.setColor(lowerRight);
        g.drawLine(right, bottom, left, bottom);
        g.drawLine(right, bottom, right, top);
    }
}

