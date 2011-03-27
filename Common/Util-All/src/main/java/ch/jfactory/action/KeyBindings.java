/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.action;

import ch.jfactory.resource.Strings;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.swing.KeyStroke;
import org.apache.commons.lang.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.4 $ $Date: 2008/01/06 10:16:23 $
 */
public class KeyBindings
{
    private static final Logger LOG = LoggerFactory.getLogger( KeyBindings.class );

    private static final String ALT = Strings.getString( "MODIFIER.ALT.TEXT" );

    private static final String CTRL = Strings.getString( "MODIFIER.CTRL.TEXT" );

    private static final String META = Strings.getString( "MODIFIER.META.TEXT" );

    private static final String SHIFT = Strings.getString( "MODIFIER.SHIFT.TEXT" );

    private static int c, a, m, s;

    private static final String STRING_MODIFIER_GLUE = "+";

    private static final String STRING_KEYSTROKE_GLUE = "-";

    /**
     * Changes the mapping between symbolic modifier key names (<code>C</code>, <code>A</code>, <code>M</code>, <code>S</code>) and Java modifier flags.
     *
     * @param c The modifier to map the <code>C</code> modifier to
     * @param a The modifier to map the <code>A</code> modifier to
     * @param m The modifier to map the <code>M</code> modifier to
     * @param s The modifier to map the <code>S</code> modifier to
     * @since jEdit 4.1pre3
     */
    public static void setModifierMapping( final int c, final int a, final int m, final int s )
    {
        KeyBindings.c = c;
        KeyBindings.a = a;
        KeyBindings.m = m;
        KeyBindings.s = s;
    }

    /**
     * Converts a string to a keystroke. The string should be of the form <i>modifiers</i>+<i>shortcut</i> where <i>modifiers</i> is any combination of A for Alt, C for Control, S for Shift or M for Meta, and <i>shortcut</i> is either a single character, or a keycode name from the <code>KeyEvent</code> class, without the <code>VK_</code> prefix.
     *
     * @param keyStroke A string description of the key stroke
     * @return the key sroke object
     */
    public static KeyStroke parseKeyStroke( final String keyStroke )
    {
        if ( keyStroke == null )
        {
            return null;
        }
        int modifiers = 0;
        final int index = keyStroke.indexOf( '+' );
        if ( index != -1 )
        {
            for ( int i = 0; i < index; i++ )
            {
                switch ( Character.toUpperCase( keyStroke.charAt( i ) ) )
                {
                    case 'A':
                        modifiers |= a;
                        break;
                    case 'C':
                        modifiers |= c;
                        break;
                    case 'M':
                        modifiers |= m;
                        break;
                    case 'S':
                        modifiers |= s;
                        break;
                }
            }
        }
        final String key = keyStroke.substring( index + 1 );
        if ( key.length() == 1 )
        {
            final char ch = key.charAt( 0 );
            if ( modifiers == 0 )
            {
                return KeyStroke.getKeyStroke( ch );
            }
            else
            {
                return KeyStroke.getKeyStroke( Character.toUpperCase( ch ), modifiers );
            }
        }
        else if ( key.length() == 0 )
        {
            return null;
        }
        else
        {
            final int ch;
            try
            {
                ch = KeyEvent.class.getField( "VK_".concat( key ) ).getInt( null );
            }
            catch ( Exception e )
            {
                LOG.error( "Invalid key stroke: " + keyStroke );
                return null;
            }

            return KeyStroke.getKeyStroke( ch, modifiers );
        }
    }

    static
    {
        if ( SystemUtils.IS_OS_MAC_OSX )
        {
            setModifierMapping( InputEvent.META_MASK,
                    InputEvent.ALT_MASK,
                    InputEvent.CTRL_MASK,
                    InputEvent.SHIFT_MASK );
        }
        else
        {
            setModifierMapping( InputEvent.CTRL_MASK,
                    InputEvent.ALT_MASK,
                    InputEvent.META_MASK,
                    InputEvent.SHIFT_MASK );
        }
    }

    /**
     * Returns a string representation for the given shortcut.
     *
     * @param modifierString the string to translate
     * @return the string representation
     */
    public static String getSymbolicModifierName( final String modifierString )
    {
        final int index = modifierString.indexOf( '+' );
        final StringBuffer buffer = new StringBuffer();
        if ( index != -1 )
        {
            for ( int i = 0; i < index; i++ )
            {
                switch ( Character.toUpperCase( modifierString.charAt( i ) ) )
                {
                    case 'A':
                        if ( buffer.length() > 0 )
                        {
                            buffer.append( STRING_MODIFIER_GLUE );
                        }
                        buffer.append( ALT );
                        break;
                    case 'C':
                        if ( buffer.length() > 0 )
                        {
                            buffer.append( STRING_MODIFIER_GLUE );
                        }
                        buffer.append( CTRL );
                        break;
                    case 'M':
                        if ( buffer.length() > 0 )
                        {
                            buffer.append( STRING_MODIFIER_GLUE );
                        }
                        buffer.append( META );
                        break;
                    case 'S':
                        if ( buffer.length() > 0 )
                        {
                            buffer.append( STRING_MODIFIER_GLUE );
                        }
                        buffer.append( SHIFT );
                        break;
                }
            }
        }
        if ( buffer.length() > 0 )
        {
            buffer.append( STRING_KEYSTROKE_GLUE );
        }
        buffer.append( modifierString.substring( index + 1 ) );
        return buffer.toString();
    }
}
