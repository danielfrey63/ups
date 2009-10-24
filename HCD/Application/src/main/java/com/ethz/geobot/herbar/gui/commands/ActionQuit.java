package com.ethz.geobot.herbar.gui.commands;

import ch.jfactory.action.AbstractParametrizedAction;
import ch.jfactory.application.SystemUtil;
import com.ethz.geobot.herbar.gui.MainFrame;
import com.ethz.geobot.herbar.modeapi.Mode;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.Action;
import javax.swing.KeyStroke;

/**
 * @author $author$
 * @version $revision$ $date$
 */
public class ActionQuit extends AbstractParametrizedAction {

    public ActionQuit(MainFrame parent) {
        super("MENU.ITEM.QUIT", parent);

        // overwrite default in super class
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F4, KeyEvent.ALT_MASK));
    }

    public void actionPerformed(ActionEvent parm1) {
        MainFrame mainFrame = ((MainFrame) parent);
        mainFrame.storeSettings();
        Mode mode = mainFrame.getModel().getMode();
        if (mode != null && mode.queryDeactivate()) {
            SystemUtil.EXIT.exit(0);
        }
    }
}