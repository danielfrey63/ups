package com.ethz.geobot.herbar.gui.commands;

import ch.jfactory.action.AbstractParametrizedAction;
import com.Ostermiller.util.Browser;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.KeyStroke;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Action class to show help.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $
 */
public class ActionAppHelp extends AbstractParametrizedAction
{
    /** This class logger. */
    private static final Logger LOG = LoggerFactory.getLogger( ActionAppHelp.class );

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
        final String url = System.getProperty( "xmatrix.help.url" );
        try
        {
            Browser.init();
            Browser.displayURL( url );
        }
        catch ( IOException e )
        {
            LOG.error( "cannot display help at " + url, e );
        }
    }
}
