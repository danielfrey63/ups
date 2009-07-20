package ch.jfactory.component.tree;

import ch.jfactory.component.AbstractPlainDocument;
import java.awt.Toolkit;
import javax.swing.text.BadLocationException;

/**
 * Validates the input by finding the new value in the tree finder model given in constructor.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:58 $
 */
public class TreeFinderDocument extends AbstractPlainDocument {
    private TreeFinderModel treeFinderModel;

    /**
     * Constructor.
     *
     * @param treeFinderModel model to check for existing tree nodes
     */
    public TreeFinderDocument(final TreeFinderModel treeFinderModel) {
        this.treeFinderModel = treeFinderModel;
    }

    protected boolean validate(final String newValue) {
        treeFinderModel.find(newValue);
        return treeFinderModel.get() != null;
    }

    // TODO: replace by validate method
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
        final String newValue = buffer.toString();
        treeFinderModel.find(newValue);
        if (treeFinderModel.get() != null || len == length) {
            super.remove(offs, len);
        }
        else if (newValue.length() > 0) {
            Toolkit.getDefaultToolkit().beep();
        }
    }
}

