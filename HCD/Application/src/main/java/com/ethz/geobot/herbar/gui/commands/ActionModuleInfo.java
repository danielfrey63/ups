package com.ethz.geobot.herbar.gui.commands;

import ch.jfactory.action.AbstractParametrizedAction;
import com.ethz.geobot.herbar.gui.AppHerbar;
import com.ethz.geobot.herbar.gui.about.ModuleInfoDialog;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:05:50 $
 */
public class ActionModuleInfo extends AbstractParametrizedAction
{
    /**
     * Constructor, need a parent frame to set about box on top.
     *
     * @param parent reference to parent frame
     */
    public ActionModuleInfo( final JFrame parent )
    {
        super( "MENU.ITEM.MODULEINFO", parent );
    }

    public void actionPerformed( final ActionEvent parm1 )
    {
        final ModuleInfoDialog dlg = new ModuleInfoDialog( parent, "DIALOG.MODULES" );
        dlg.setSize( 400, 400 );
        dlg.setLocationRelativeTo( AppHerbar.getMainFrame() );
        dlg.setVisible( true );
        dlg.toFront();
    }
}
