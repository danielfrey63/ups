package ch.jfactory.component;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:57 $
 */
public abstract class SimpleDocumentListener implements DocumentListener
{
    public void insertUpdate(final DocumentEvent e)
    {
        changedUpdate(e);
    }

    public void removeUpdate(final DocumentEvent e)
    {
        changedUpdate(e);
    }

    public abstract void changedUpdate(DocumentEvent e);
}
