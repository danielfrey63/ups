/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.component;

import java.awt.Toolkit;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

/**
 * <p>The FixedFormatTextField is useful for fields that have a static appearance. Examples: You want to display a number in a fixed format, say 12-345-677, or 123.45, or 01.05.2004. Dashes and dots do not belong to the characters a user should influence (formatting characters). This class can take a pattern in a form containing any characters, where the following characters have a special meaning:</p>
 *
 * <ul>
 *
 * <li><code>N</code>: a digit</li>
 *
 * <li><code>A</code>: an alphanumeric character</li>
 *
 * <li><code>C</code>: a character</li>
 *
 * </ul>
 *
 * <p>In the above example, you would initialize an instance of this class with <code>NN-NNN-NNN</code>, <code>NNN.NN</code> or <code>NN.NN.NNNN</code>. Another example: A serial number like 2653-6SG would be of the form <code>NNNN-AAA</code>, if the first block is purely numeric or <code>AAAA-AAA</code> if it is alphanumeric.</p>
 *
 * <p>This class of text field allows you to: </p>
 *
 * <ul>
 *
 * <li>Type in one character at the time. The cursor advances and overwrites the existing value. Formatting characters have not to be typed in. Instead, the cursor jumps over them so that typing is not interrupted.</li>
 *
 * <li>Delete one character at the time. The deleted part is replaced by the corresponding default. Formatting characters are not deleted but are jumped over.</li>
 *
 * <li>Paste a chunk of characters. The chunk must fit into the place defined by the pattern. Formatting characters are inserted as needed. If the pasted chunk contains formatting characters, these have to be at the right position. Otherwise the chunk cannot be inserted. If the chunk will exceed the end of the pattern, it is truncated and a system beep will warn you that something has been discarded.</li>
 *
 * <li>Deletion of a chunk of text. Here the selection is important and marks the text to delete. The deleted text is replaced by the corresponding default part.</li>
 *
 * </ul>
 *
 * @author Daniel Frey
 * @version $Revision: 1.2 $
 */
public class FixedFormatTextField extends JTextField
{
    private String pattern = "";

    private String defaultText = "";

    public FixedFormatTextField()
    {
        super();
        init();
    }

    /**
     * Constructs a field with the given pattern and default string.
     *
     * @param pattern the pattern to use for the field
     * @param def     the default string
     */
    public FixedFormatTextField( final String pattern, final String def )
    {
        super( def );
        setDefault( def );
        setPattern( pattern );
        init();
    }

    /**
     * Constructs a field with the given pattern, default string and number of columns.
     *
     * @param pattern the pattern to use for the field
     * @param def     the default string
     * @param columns the number of columns for the width of the field
     */
    public FixedFormatTextField( final String pattern, final String def, final int columns )
    {
        super( def, columns );
        setDefault( def );
        setPattern( pattern );
        init();
    }

    /**
     * Constructs a field with the given pattern, default string, document model, text and number of columns.
     *
     * @param pattern the pattern to use for the field
     * @param def     the default string
     * @param doc     the document model
     * @param text    the initial text
     * @param columns the number of columns for the width of the field
     */
    public FixedFormatTextField( final String pattern, final String def, final Document doc, final String text, final int columns )
    {
        super( doc, text, columns );
        setDefault( def );
        setPattern( pattern );
        init();
    }

    /**
     * @param pattern the pattern to use for the field
     * @param def     the default string
     * @param text    the initial text
     */
    public FixedFormatTextField( final String pattern, final String def, final String text )
    {
        super( text );
        setDefault( def );
        setPattern( pattern );
        init();
    }

    /**
     * @param pattern the pattern to use for the field
     * @param def     the default string
     * @param text    the initial text
     * @param columns the number of columns for the width of the field
     */
    public FixedFormatTextField( final String pattern, final String def, final String text, final int columns )
    {
        super( text, columns );
        setDefault( def );
        setPattern( pattern );
        init();
    }

    /**
     * Set the patter for this field.
     *
     * @param pattern the pattern to set.
     */
    public void setPattern( final String pattern )
    {
        this.pattern = pattern;
        init();
    }

    /**
     * Returns the pattern of this field.
     *
     * @return the pattern string
     */
    public String getPattern()
    {
        return pattern;
    }

    public void setDefault( final String defaultText )
    {
        this.defaultText = defaultText;
    }

    public String getDefault()
    {
        return defaultText;
    }

    /** Initializes the field. */
    private void init()
    {
        setDocument( new FixedFormatDocument( pattern, defaultText ) );
        setText( defaultText );
    }

    /** Document class for the FixedFormatTextField. See {@link FixedFormatTextField} for a description of the behaviour. */
    private final class FixedFormatDocument extends PlainDocument
    {
        /** The default string. */
        private final String def;

        /** The format pattern. */
        private final String pattern;

        /** The positions of formatting characters. */
        private final int[] jumpers;

        /** The possible formatting symbols. */
        private final String keys = "NAC";

        /** Defaults for the pattern symbols. */
        private final String defaultCharacters = "0  ";

        /**
         * Constructs a document with the given pattern and default string.
         *
         * @param pattern the pattern
         * @param def     the default string
         */
        private FixedFormatDocument( final String pattern, final String def )
        {
            if ( def == null || def.equals( "" ) && pattern != null && !pattern.equals( "" ) )
            {
                String temp = pattern;
                for ( int i = 0; i < keys.length(); i++ )
                {
                    temp = temp.replace( keys.charAt( i ), defaultCharacters.charAt( i ) );
                }
                this.def = temp;
            }
            else
            {
                this.def = def;
            }
            this.pattern = pattern;

            if ( !validate( this.def ) )
            {
                throw new IllegalArgumentException( this.def + " is not of pattern " + pattern );
            }

            final int length = pattern.replaceAll( "[" + keys + "]", "" ).length();
            int c = 0;
            jumpers = new int[length];
            for ( int i = 0; i < pattern.length(); i++ )
            {
                if ( keys.indexOf( pattern.charAt( i ) ) == -1 )
                {
                    jumpers[c++] = i;
                }
            }
        }

        /**
         * Inserts a string at the given offset. The string is successfully inserted if it fits into the pattern.
         *
         * @param offset the position
         * @param chunk  the string to insert
         * @param a      the attribute set to use
         * @throws BadLocationException
         */
        public void insertString( final int offset, final String chunk, final AttributeSet a ) throws BadLocationException
        {
            final String newValue;
            if ( chunk.equals( "" ) )
            {
                return;
            }

            final int length = getLength();
            if ( length == 0 )
            {
                super.insertString( offset, def, a );
                return;
            }
            else
            {
                final StringBuffer currentBuffer = new StringBuffer( getText( 0, length ) );
                currentBuffer.replace( offset, chunk.length() + offset, chunk );
                newValue = currentBuffer.toString();
            }
            if ( validate( newValue ) )
            {
                super.remove( offset, chunk.length() );
                super.insertString( offset, chunk, a );
            }
            else
            {
                Toolkit.getDefaultToolkit().beep();
            }

            // Make sure the caret jumps over the dashes
            while ( isIn( getCaretPosition() ) )
            {
                setCaretPosition( getCaretPosition() + 1 );
            }
        }

        /**
         * Removes the given number of characters at the given position.
         *
         * @param offs the position
         * @param len  the number of characters to remove
         * @throws BadLocationException
         */
        public void remove( final int offs, final int len ) throws BadLocationException
        {
            final int length = getLength();
            if ( length == 0 )
            {
                return;
            }

            final String currentContent = getText( 0, length );
            final StringBuffer buffer = new StringBuffer();
            if ( offs > 0 )
            {
                buffer.append( currentContent.substring( 0, offs ) );
            }
            buffer.append( def.substring( offs, offs + len ) );
            if ( ( offs + len ) < length )
            {
                buffer.append( currentContent.substring( offs + len ) );
            }
            if ( validate( buffer.toString() ) )
            {
                super.remove( offs, len );
                super.insertString( offs, def.substring( offs, offs + len ), null );
            }
            else
            {
                Toolkit.getDefaultToolkit().beep();
            }

            // Make sure the caret jumps over the dashes
            while ( isIn( getCaretPosition() - 2 ) )
            {
                setCaretPosition( getCaretPosition() - 1 );
            }

            // Ajust caret position after removal
            final int newPosition = getCaretPosition() - len;
            if ( newPosition >= 0 && offs >= 0 )
            {
                setCaretPosition( newPosition );
            }
            else
            {
                setCaretPosition( 0 );
            }
        }

        /**
         * Makes sure the given string is appropriate for the pattern. If not, false is returned.
         *
         * @param newValue the total field content to check
         * @return whether the total field content would be maching the pattern
         */
        protected boolean validate( final String newValue )
        {
            if ( newValue.length() != pattern.length() )
            {
                return false;
            }

            boolean ret = true;
            for ( int i = 0; i < newValue.length(); i++ )
            {
                final char c = pattern.charAt( i );
                final char v = newValue.charAt( i );
                if ( c == 'N' )
                {
                    ret &= Character.isDigit( v );
                }
                else if ( c == 'A' )
                {
                    ret &= Character.isLetterOrDigit( v );
                }
                else if ( c == 'C' )
                {
                    ret &= Character.isLetter( v );
                }
                else
                {
                    ret &= ( v == pattern.charAt( i ) );
                }
            }
            return ret;
        }

        /**
         * Checks whether the given int is part of the jumper positions.
         *
         * @param toFind the int to find in the jumpers
         * @return whether this int is present
         */
        private boolean isIn( final int toFind )
        {
            for ( final int temp : jumpers )
            {
                if ( toFind == temp )
                {
                    return true;
                }
            }
            return false;
        }
    }
}
