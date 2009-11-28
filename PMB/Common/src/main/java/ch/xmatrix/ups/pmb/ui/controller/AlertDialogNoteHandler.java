package ch.xmatrix.ups.pmb.ui.controller;

import ch.jfactory.application.view.status.Message;
import ch.jfactory.binding.Note;
import ch.jfactory.lang.ArrayUtils;
import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JOptionPane;

/**
 * Displays an alert dialog for {@link Note} messages.
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2008/01/23 22:18:48 $
 */
public class AlertDialogNoteHandler implements PropertyChangeListener
{
    private final Component parent;

    private final Message.Type[] levels;

    /**
     * Constructs an instance with the given arguments.
     *
     * @param parent the parent component to display the alert dialog on, may be null.
     * @param levels the levels on which the alert is shown.
     */
    public AlertDialogNoteHandler( final Component parent, final Message.Type... levels )
    {
        this.parent = parent;
        this.levels = levels;
    }

    public void propertyChange( final PropertyChangeEvent evt )
    {
        final Note note = (Note) evt.getNewValue();
        if ( ArrayUtils.isIn( levels, note.getType() ) )
        {
            final String message = note.getMessage();
            final String subject = note.getSubject();
            final String[] ok = {"OK"};
            JOptionPane.showOptionDialog( parent, message, subject, JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE,
                    null, ok, ok[0] );
        }
    }
}
