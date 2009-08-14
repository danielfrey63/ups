/* ====================================================================
 *  Copyright 2004-2005 www.xmatrix.ch
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 * ====================================================================
 */
package ch.jfactory.image;

import ch.jfactory.color.ColorUtils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 * Displays an icon that is loaded from a path. The path may be an URL or a file name.
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2007/09/27 10:41:22 $
 */
public class SelectableImagePanel extends ImagePanel {

    private String iconPath;
    private boolean selected;
    private static final Color BACKGROUND_SELECTED = ColorUtils.fade(Color.ORANGE, 0.8);
    private static final Color BACKGROUND_DESELECTED = UIManager.getColor("Panel.background");
    private static final Border BORDER_SELECTED = new CompoundBorder(
            new LineBorder(new Color(139, 0, 0), 1), new EmptyBorder(1, 1, 1, 1));
    private static final Border BORDER_DESELECTED = new CompoundBorder(
            new LineBorder(new Color(139, 139, 139), 1), new EmptyBorder(1, 1, 1, 1));

    public SelectableImagePanel() {
        super(null);
    }

    public SelectableImagePanel(final Image image) {
        super(image);
    }

    public SelectableImagePanel(final Image image, final int width, final int height) {
        super(image, true);
        setSize(width, height);
        init(iconPath);
    }

    public SelectableImagePanel(final Image image, final boolean fill) {
        super(image, fill);
        init(iconPath);
    }

    private void init(final String iconPath) {
        this.iconPath = iconPath;
        this.setPreferredSize(new Dimension(getImage().getWidth(null), getImage().getHeight(null)));
        setSelected(false);
    }

    public void setSelected(final boolean selected) {
        this.selected = selected;
        setBorder(selected ? BORDER_SELECTED : BORDER_DESELECTED);
        setBackground(selected ? BACKGROUND_SELECTED : BACKGROUND_DESELECTED);
    }

    public boolean isSelected() {
        return selected;
    }

    @Override
    public Dimension getPreferredSize() {
        return super.getPreferredSize();
    }

    /**
     * Returns the path for the icon.
     *
     * @return the path to the icon
     */
    public String getIconPath() {
        return iconPath;
    }

    public static void main(final String[] args) throws IOException {
        final JFrame f = new JFrame();
        final String path = "E:/Beispiel-gross.jpg";
        final Image image = ImageUtils.createImage(path);
        final SelectableImagePanel panelSelectable = new SelectableImagePanel(image, true);
        panelSelectable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                panelSelectable.setSelected(!panelSelectable.isSelected());
            }
        });
        f.getRootPane().setBorder(new EmptyBorder(10, 10, 10, 10));
        f.add(panelSelectable, BorderLayout.CENTER);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(400, 400);
        f.setVisible(true);
    }
}
