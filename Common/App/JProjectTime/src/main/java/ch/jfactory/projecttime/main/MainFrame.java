/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package ch.jfactory.projecttime.main;

import com.jgoodies.uif.AbstractFrame;
import com.jgoodies.uif.application.Application;
import javax.swing.JComponent;

/**
 * TODO: document
 *
 * @author <a href="daniel.frey@xmatrix.ch">Daniel Frey</a>
 * @version $Revision: 1.3 $ $Date: 2006/11/16 13:25:17 $
 */
public class MainFrame extends AbstractFrame {

    private MainModel model;
    private MainBuilder mainBuilder;

    public MainFrame(MainModel model) {
        super(Application.getDescription().getWindowTitle());
        this.model = model;
    }

    public String getWindowID() {
        return "JProjectTimeMainFrame";
    }

    protected void configureCloseOperation() {
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(Application.getApplicationCloseOnWindowClosingHandler());
    }

    protected JComponent buildContentPane() {
        mainBuilder = new MainBuilder(model);
        final JComponent content = mainBuilder.getPanel();
        initMenus();
        return content;
    }

    private void initMenus() {
        setJMenuBar(mainBuilder.getMenuBar());
    }
}
