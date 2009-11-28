package com.ethz.geobot.herbar.gui.commands;

import ch.jfactory.action.AbstractParametrizedAction;
import ch.jfactory.help.HelpViewer;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

/**
 * Action class to show help.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $
 */
public class ActionAppHelp extends AbstractParametrizedAction
{
    private static final String HELPSET = "hcdHelpMain/jhelpset.hs";

    /**
     * Constructor
     *
     * @param frame the main frame
     */
    public ActionAppHelp( final JFrame frame )
    {
        super( "MENU.ITEM.HELP", frame );

        // overwrite default
        putValue( Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke( KeyEvent.VK_F1, 0 ) );
    }

    public void actionPerformed( final ActionEvent parm1 )
    {
        new HelpViewer( parent.getRootPane(), HELPSET );
    }
}
