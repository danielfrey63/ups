package ch.xmatrix.ups.pmb.ui.controller;

import ch.jfactory.application.view.status.Message;
import ch.jfactory.binding.Note;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.apache.log4j.Logger;

/**
 * Logs {@link ch.jfactory.binding.InfoModel InfoModel} change events to the {@link Logger}. If the {@link Note}s type is {@link
 * ch.jfactory.application.view.status.Message.Type#INFO INFO}, the message is issued to the loggers info, otherwise to the error chanel.
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2008/01/23 22:18:48 $
 */
public class Log4jNoteHandler implements PropertyChangeListener {

    private final Logger logger;

    public Log4jNoteHandler(final Logger logger) {
        this.logger = logger;
    }

    public void propertyChange(final PropertyChangeEvent evt) {
        final Note note = (Note) evt.getNewValue();
        final String message = note.getMessage().replaceAll("\n", "");
        final String subject = note.getSubject() != null ? note.getSubject() + ": " : "";
        final String fullMessage = subject + message;
        if (note.getType() == Message.Type.INFO) {
            logger.info(fullMessage);
        } else {
            logger.error(fullMessage);
        }
    }
}
