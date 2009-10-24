/*
 * Herbar CD-ROM version 2
 *
 * AboutBox.java
 *
 * Created on 4. April 2002
 */
package com.ethz.geobot.herbar.gui;

import ch.jfactory.logging.LogUtils;
import ch.jfactory.resource.ImageLocator;
import com.jgoodies.animation.old.AnimatedPanel;
import com.jgoodies.animation.old.Animation;
import com.jgoodies.animation.old.OverlayedAnimation;
import java.awt.Container;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JWindow;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:05:50 $
 */
public class AboutBox extends JWindow {

    private ImageIcon about = ImageLocator.getIcon("splash.jpg");
    private JButton buttonSplash = new JButton();
    private AnimatedPanel animator;

    public AboutBox(JFrame parent) {
        super(parent);
        animator = createAnimatedPanel();
        build();
        addWindowListener();
        addMouseListener();
    }

    private AnimatedPanel createAnimatedPanel() {
        AnimatedPanel animatedPanel = new AnimatedPanel(createAnimation(), true);
        animatedPanel.setBounds(0, 50, 400, 50);
        return animatedPanel;
    }

    private void addMouseListener() {
        buttonSplash.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                closeWindow();
            }
        });
    }

    private void addWindowListener() {
        addWindowListener(new WindowAdapter() {
            public void windowClosing() {
                closeWindow();
            }

        });
    }

    public void closeWindow() {
        setVisible(false);
        dispose();
    }

    private Animation createAnimation() {
        String size = "14";
        return OverlayedAnimation.createDefault(new String[][]{
                {"Matthias Baltisberger", size, "3000", "1500", "default", "default", "none", "center", "fill"},
                {"Christine Biber", size, "3000", "1500", "default", "default", "none", "center", "fill"},
                {"Daniel Frey", size, "3000", "1500", "default", "default", "none", "center", "fill"},
                {"Dirk Hoffmann", size, "3000", "1500", "default", "default", "none", "center", "fill"},
                {"Lilo Meier", size, "3000", "1500", "default", "default", "none", "center", "fill"},
                {"Thomas Wegmüller", size, "3000", "1500", "default", "default", "none", "center", "fill"}
        });
    }

    public void setVisible(boolean b) {
        if (b != this.isVisible()) {
            super.setVisible(b);
            if (b) {
                this.toFront();
                animator.start();
            }
            else {
                animator.stop();
            }
        }
    }

    private void build() {
        buttonSplash.setBorder(null);
        buttonSplash.setIcon(about);
        buttonSplash.setBounds(0, 0, 400, 300);

        Container contentPane = getContentPane();
        contentPane.setLayout(null);
        contentPane.add(animator);
        contentPane.add(buttonSplash);
        setSize(400, 300);
    }

    public static void main(String[] args) {
        LogUtils.init();
        JFrame parent = new JFrame();
        AboutBox dlg = new AboutBox(parent);
        dlg.getSize();
        dlg.setVisible(true);
        dlg.toFront();
    }
}