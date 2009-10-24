package com.ethz.geobot.herbar.gui.commands;

import ch.jfactory.application.view.status.StatusBar;
import javax.swing.JMenuItem;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ActionStatustextAdapter implements ChangeListener {

    private String helpText;
    private StatusBar statusBar;

    public ActionStatustextAdapter(String helpText, StatusBar statusBar) {
        this.helpText = helpText;
        this.statusBar = statusBar;
    }

    public void stateChanged(ChangeEvent evt) {
        if (((JMenuItem) evt.getSource()).isArmed()) {
            statusBar.setText(helpText);
        }
        else {
            statusBar.setText(" ");
        }
    }
}
