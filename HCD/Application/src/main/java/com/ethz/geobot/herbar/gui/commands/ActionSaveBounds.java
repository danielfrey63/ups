package com.ethz.geobot.herbar.gui.commands;

import ch.jfactory.action.AbstractParametrizedAction;
import com.ethz.geobot.herbar.gui.MainFrame;
import java.awt.event.ActionEvent;
import java.util.prefs.Preferences;
import javax.swing.JFrame;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:05:50 $
 */
public class ActionSaveBounds extends AbstractParametrizedAction {

    private Preferences prefNode;

    public ActionSaveBounds(JFrame frame, Preferences prefs) {
        super("MENU.ITEM.SAVE.BOUNDS", frame);
        this.prefNode = prefs;
    }

    public void actionPerformed(ActionEvent e) {
        prefNode.putInt(MainFrame.PREF_X, parent.getLocation().x);
        prefNode.putInt(MainFrame.PREF_Y, parent.getLocation().y);
        prefNode.putInt(MainFrame.PREF_W, parent.getSize().width);
        prefNode.putInt(MainFrame.PREF_H, parent.getSize().height);
    }
}
