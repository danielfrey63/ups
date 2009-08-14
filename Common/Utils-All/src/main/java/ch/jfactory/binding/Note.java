package ch.jfactory.binding;

import java.awt.Color;
import ch.jfactory.application.view.status.Message;

/**
 * Interface to define human readable messages. The purpose of a note is to hold a message, so there should always be a
 * valid message there.<p/>
 *
 * The general contract with all other properties is:
 *
 * <ul>
 *
 * <li>If the property is set to <code>null</code>, a listener should not alter states affected by the same property of
 * a previous note.</li>
 *
 * <li>If the porperty is set to a value (even an empty string), a listener should take this value to alter any state
 * affected by the same property of a previous note.</li>
 *
 * </ul>
 *
 * So i.e. you implement a property change listener that updates a status bar. You register it with an {@link
 * InfoModel}. If the listener gets a property change event with a note that has <code>null</code> as subject, then the
 * previously set subject in the status bar should not be altered. However, when the note contains an empty or another
 * string as subject, this should replace the subject in the status bar.
 *
 * @author Daniel Frey
 * @version $Revision: 1.5 $ $Date: 2008/01/06 10:16:23 $
 */
public interface Note {

    /**
     * Message to hold. The purpose of a note is to hold a message, so this always should contain a valid value.
     *
     * @return the message, never <code>null</code>
     */
    String getMessage();

    /**
     * The optional subject the message is for.
     *
     * @return the subject, or an empty string if the subject has to be removed, or null if it remains the same as with
     *         the last note.
     */
    String getSubject();

    /**
     * The amount of work that is done.<p/>
     *
     * TODO: Move this to presentation view
     *
     * @return the amount of work that is done
     */
    Integer getPercentage();

    /**
     * The color to display the message in.<p/>
     *
     * TODO: Move this to presentation view
     *
     * @return the color
     */
    Color getColor();

    /**
     * The message type.
     *
     * @return the message type
     */
    Message.Type getType();
}
