package ch.jfactory.action;

import ch.jfactory.resource.Strings;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:57 $
 */
public abstract class AbstractParametrizedAction extends AbstractAction
{
    protected JFrame parent;

    public AbstractParametrizedAction( final String prefix, final JFrame parent )
    {
        super( Strings.getString( prefix + ".NAME" ) );
        this.parent = parent;

        putValue( Action.LONG_DESCRIPTION, Strings.getString( prefix + ".TEXT" ) );
        final char code = Strings.getChar( prefix + ".SC" );
        if ( code != Strings.MISSING_CHAR )
        {
            final KeyStroke keyStroke = KeyStroke.getKeyStroke( code, KeyEvent.ALT_MASK );
            putValue( Action.ACCELERATOR_KEY, keyStroke );
        }
        putValue( Action.MNEMONIC_KEY, new Integer( (int) Strings.getChar( prefix + ".MN" ) ) );
    }
}
