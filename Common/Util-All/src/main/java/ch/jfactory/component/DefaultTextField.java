/*
 * Copyright x-matrix Switzerland (c) 2003
 *
 * DefaultTextField.java
 *
 * Created on Feb 6, 2003 4:32:27 PM
 * Created by Daniel
 */
package ch.jfactory.component;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JTextField;
import javax.swing.text.Document;

/**
 * Initial text will be take as a default for greyed display until the first click or focus gained occurs which does
 * empty the field.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:57 $
 */
public class DefaultTextField extends JTextField
{
    public DefaultTextField()
    {
        this( null, "", 0 );
    }

    public DefaultTextField( final int columns )
    {
        this( null, "", columns );
    }

    public DefaultTextField( final String text )
    {
        this( null, text, 0 );
    }

    public DefaultTextField( final String text, final int columns )
    {
        this( null, text, columns );
    }

    public DefaultTextField( final Document doc, final String text, final int columns )
    {
        super( doc, text, columns );
        this.addFocusListener( new FocusListener()
        {
            public void focusGained( final FocusEvent e )
            {
                selectAll();
            }

            public void focusLost( final FocusEvent e )
            {
            }
        } );
    }
}
