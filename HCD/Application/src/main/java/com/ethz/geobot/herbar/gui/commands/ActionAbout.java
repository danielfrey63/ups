package com.ethz.geobot.herbar.gui.commands;

import ch.jfactory.action.AbstractParametrizedAction;
import ch.jfactory.application.presentation.WindowUtils;
import com.ethz.geobot.herbar.gui.AboutBox;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;

/**
 * Action class to show the about box.
 *
 * @author $Author: daniel_frey $ $Date: 2007/09/17 11:05:50 $
 * @version $Revision: 1.1 $
 */
public class ActionAbout extends AbstractParametrizedAction {

    /**
     * Constructor, need a parent frame to set about box on top.
     *
     * @param parent reference to parent frame
     */
    public ActionAbout(JFrame parent) {
        super("MENU.ITEM.ABOUT", parent);
    }

    public void actionPerformed(ActionEvent parm1) {
        AboutBox dlg = new AboutBox(parent);
        WindowUtils.centerOnComponent(dlg, parent);
        dlg.setVisible(true);
        dlg.toFront();
    }
}
