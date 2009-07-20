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

package ch.jfactory.component.splash;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * Simple splash. The size will be adopted from the picture.
 *
 * @author Daniel Frey
 * @version $Revision: 1.2 $ $Date: 2005/11/17 11:54:58 $
 */
public class SimpleSplash extends JFrame implements SplashProvider {
    /**
     * The label that holds the background image.
     */
    private JLabel label = new JLabel();

    /**
     * Cerates a new splash window. The image passed will be painted in the background.
     *
     * @param backgroundImage image for the background
     */
    public SimpleSplash(final ImageIcon backgroundImage) {

        init();
        setBackgroundImage(backgroundImage);

        getContentPane().setLayout(null);
        getContentPane().add(label);

        start();
    }

    /**
     * Creates a new splash window. The image passed will be painted in the background. The component is displayed in
     * the foreground according to its bounds.
     *
     * @param backgroundImage     image for the background
     * @param foregroundComponent foregroundComponent for the foreground
     */
    public SimpleSplash(final ImageIcon backgroundImage, final Component foregroundComponent) {

        init();
        setBackgroundImage(backgroundImage);

        getContentPane().setLayout(null);
        getContentPane().add(foregroundComponent);
        getContentPane().add(label);

        start();
    }

    /**
     * Init the components listeners and properties.
     */
    private void init() {
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addFocusListener(new FocusAdapter() {
            public void focusLost(final FocusEvent e) {
                toFront();
            }
        });
    }

    //
    // Interface SplashProvider
    //

    /**
     * Shows the splash screen.
     */
    public void start() {
        setVisible(true);
    }

    /**
     * Destroies the splash screen.
     */
    public void stop() {
        dispose();
    }

    /**
     * Sets the image on the splash window. The size of the window will be adopted to the images size. The old image is
     * discarded.
     *
     * @param backgroundImageIcon the image to set
     */
    public void setBackgroundImage(final ImageIcon backgroundImageIcon) {
        if (backgroundImageIcon == null) {
            label.setText("-= Null-ImageIcon defined =-");
        }
        else if (backgroundImageIcon.getIconHeight() < 0) {
            label.setText("-= ImageIcon \"" + backgroundImageIcon + "\" not found =-");
        }
        else {
            label.setText(null);
            label.setIcon(backgroundImageIcon);
        }

        final Dimension size = label.getPreferredSize();
        label.setSize(size);
        setSize(size);
    }
}
