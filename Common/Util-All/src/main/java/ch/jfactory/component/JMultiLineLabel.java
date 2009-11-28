/*
 * JMultiLineLabel.java
 *
 * Created on 10. September 2002, 13:39
 */

package ch.jfactory.component;

import java.awt.Font;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 * Multi line JLabel, displays a label expanding over multiple lines with word warp.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.3 $ $Date: 2006/03/14 21:27:55 $
 */
public class JMultiLineLabel extends JTextPane
{
    /**
     * Creates a new instance of JMultiLineLabel
     */
    public JMultiLineLabel()
    {
        super();

        setFocusable( false );
        setEditable( false );
        setOpaque( false );
        setBorder( null );

        final Font font = UIManager.getFont( "Label.font" );

        final SimpleAttributeSet attributes = new SimpleAttributeSet();
        StyleConstants.setFontSize( attributes, font.getSize() );
        StyleConstants.setFontFamily( attributes, font.getFamily() );
        StyleConstants.setAlignment( attributes, StyleConstants.ALIGN_LEFT );

        final StyledDocument doc = getStyledDocument();
        doc.setParagraphAttributes( 0, doc.getLength(), attributes, false );
    }

    /**
     * Create a new instance of JMultiLineLabel and initialize it with the given text.
     *
     * @param text the text to display
     */
    public JMultiLineLabel( final String text )
    {
        this();
        setText( text );
    }

    /**
     * Create a new instance of JMultiLineLabel and initialize it with the given text. Sets the horizontal text
     * alignment.
     *
     * @param text       the text to display
     * @param alignement on of <code>StyleConstants.ALIGN_LEFT</code>, <code>StyleConstants.ALIGN_CENTER</code>,
     *                   <code>StyleConstants.ALIGN_RIGHT</code>
     */
    public JMultiLineLabel( final String text, final int alignement )
    {
        this();
        setText( text );
        setHorizontalTextAlignment( alignement );
    }

    /**
     * Sets the horizontal text alignment.
     *
     * @param alignement on of <code>StyleConstants.ALIGN_LEFT</code>, <code>StyleConstants.ALIGN_CENTER</code>,
     *                   <code>StyleConstants.ALIGN_RIGHT</code>
     */
    public void setHorizontalTextAlignment( final int alignement )
    {
        final SimpleAttributeSet attributes = new SimpleAttributeSet();
        StyleConstants.setAlignment( attributes, alignement );

        final StyledDocument doc = getStyledDocument();
        doc.setParagraphAttributes( 0, doc.getLength(), attributes, false );
    }
}
