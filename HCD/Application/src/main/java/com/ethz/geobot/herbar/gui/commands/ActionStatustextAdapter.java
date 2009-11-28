package com.ethz.geobot.herbar.gui.commands;

import ch.jfactory.application.view.status.StatusBar;
import javax.swing.JMenuItem;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ActionStatustextAdapter implements ChangeListener
{
    private final String helpText;

    private final StatusBar statusBar;

    public ActionStatustextAdapter( final String helpText, final StatusBar statusBar )
    {
        this.helpText = helpText;
        this.statusBar = statusBar;
    }

    public void stateChanged( final ChangeEvent evt )
    {
        if ( ( (JMenuItem) evt.getSource() ).isArmed() )
        {
            statusBar.setText( helpText );
        }
        else
        {
            statusBar.setText( " " );
        }
    }
}
