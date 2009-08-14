/*
 * Copyright xmatrix Switzerland (c) 2002-2003
 *
 * PicturePanel.java
 *
 * Created on 28.5.2002
 * Created by thomas
 */
package ch.jfactory.component;

import ch.jfactory.layout.ScrollerLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicArrowButton;
import org.apache.log4j.Logger;

/**
 * This control is used to different Components as a Scrollable List without using ScrollPane, because of sizing
 * Problems
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:57 $
 */
public class ScrollerPanel extends JPanel
{
    private final static Logger LOGGER = Logger.getLogger(ScrollerPanel.class);

    protected JButton btnPrev = new BasicArrowButton(BasicArrowButton.WEST);

    protected JButton btnNext = new BasicArrowButton(BasicArrowButton.EAST);

    private ScrollerLayout layout;

    public ScrollerPanel()
    {
        initGUI();
    }

    private void initGUI()
    {
        this.setLayout(layout = new ScrollerLayout(this));
        btnPrev.setFocusPainted(false);
        btnNext.setFocusPainted(false);
        addButtons();
        addActionListeners();
    }

    public Component add(final Component c)
    {
        return super.add(c);
    }

    private void addButtons()
    {
        LOGGER.debug("addButtons()");
        add(btnPrev, ScrollerLayout.PREVSCROLLER);
        add(btnNext, ScrollerLayout.NEXTSCROLLER);
    }

    private void addActionListeners()
    {
        btnNext.addActionListener(new ActionListener()
        {
            public void actionPerformed(final ActionEvent e)
            {
                LOGGER.debug("actionPerformed(" + e + ")");
                layout.incStart();
            }
        });
        btnPrev.addActionListener(new ActionListener()
        {
            public void actionPerformed(final ActionEvent e)
            {
                LOGGER.debug("actionPerformed(" + e + ")");
                layout.decStart();
            }
        });
    }

    /** remove all components from the scrollable section of the panel */
    public void removeAll()
    {
        LOGGER.debug("removeAll()");
        super.removeAll();
        addButtons();
        repaint();
    }
}
