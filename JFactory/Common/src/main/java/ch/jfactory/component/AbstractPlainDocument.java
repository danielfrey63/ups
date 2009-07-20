package ch.jfactory.component;

import java.awt.Toolkit;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * Overwrite this class to make a new Document which just validates with another logic.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:57 $
 */
public abstract class AbstractPlainDocument extends PlainDocument {
    /* (non-Javadoc)
     * @see javax.swing.text.Document#insertString(int, java.lang.String, javax.swing.text.AttributeSet)
     */
    public void insertString(final int offset, final String chunk, final AttributeSet a) throws BadLocationException {
        final String newValue;
        if (chunk.equals("")) return;
        final int length = getLength();
        if (length == 0) {
            newValue = chunk;
        }
        else {
            final String currentContent = getText(0, length);
            final StringBuffer currentBuffer = new StringBuffer(currentContent);
            currentBuffer.insert(offset, chunk);
            newValue = currentBuffer.toString();
        }
        final boolean valid = validate(newValue);
        if (valid) {
            super.insertString(offset, chunk, a);
        }
        else {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    public void remove(final int offs, final int len) throws BadLocationException {
        final int length = getLength();
        if (length == 0) return;
        final String currentContent = getText(0, length);
        final StringBuffer buffer = new StringBuffer();
        if (offs > 0) {
            buffer.append(currentContent.substring(0, offs));
        }
        if (offs + len < length) {
            buffer.append(currentContent.substring(offs + len));
        }
        if (validate(buffer.toString())) {
            super.remove(offs, len);
        }
        else {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    /**
     * Implement this method to validate the new document string as it would appear after inserting the new chunk.
     *
     * @param newValue the new string after inserting the chunk
     * @return
     */
    protected abstract boolean validate(String newValue);
}
