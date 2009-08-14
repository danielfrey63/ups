package ch.jfactory.component;

import ch.jfactory.resource.Strings;
import java.awt.Container;
import java.lang.reflect.InvocationTargetException;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * Common dialogs to call from modes.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.2 $ $Date: 2007/09/27 10:41:22 $
 */
public class Dialogs
{

    public static final int CANCEL = 1;

    public static final int OK = 0;

    /**
     * Reporting an error to the user
     *
     * @param caller  the component to infer the root window from
     * @param title   the title to use
     * @param message the message to shown to the user
     */
    public static void showErrorMessage(final JComponent caller, final String title, final String message)
    {
        JOptionPane.showMessageDialog(getWindow(caller), message, title, JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Warns the user
     *
     * @param caller  the component to infer the root window from
     * @param title   the title to use
     * @param message the message to shown to the user
     */
    public static void showWarnMessage(final JComponent caller, final String title, final String message)
    {
        JOptionPane.showMessageDialog(getWindow(caller), message, title, JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Informs the user
     *
     * @param caller  the component to infer the root window from
     * @param title   the title to use
     * @param message the message to shown to the user
     */
    public static void showInfoMessage(final JComponent caller, final String title, final String message)
    {
        JOptionPane.showMessageDialog(getWindow(caller), message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Asks the user a question which may be answered with yes or no. Default is cancel. Escape triggers cancel as
     * well.
     *
     * @param caller  the component to infer the root window from
     * @param title   the title to use
     * @param message the message to shown to the user
     * @return a 1 if cancel was pressed
     */
    public static int showQuestionMessageCancel(final JComponent caller, final String title, final String message)
    {
        final String[] buttons = new String[]{Strings.getString("BUTTON.OK.TEXT"), Strings.getString("BUTTON.CANCEL.TEXT")};
        final int m = JOptionPane.QUESTION_MESSAGE;
        final int ret = JOptionPane.showOptionDialog(getWindow(caller), message, title, 0, m, null, buttons, buttons[CANCEL]);
        // ESC key returns a -1, which is handled as a cancel.
        return Math.abs(ret);
    }

    /**
     * Asks the user a question which may be answered with yes or no. Default is ok. Escape triggers cancel.
     *
     * @param caller  the component to infer the root window from
     * @param title   the title to use
     * @param message the message to shown to the user
     * @return a 1 if cancel was pressed
     */
    public static int showQuestionMessageOk(final JComponent caller, final String title, final String message)
    {
        final String[] buttons = new String[]{Strings.getString("BUTTON.OK.TEXT"), Strings.getString("BUTTON.CANCEL.TEXT")};
        final int m = JOptionPane.QUESTION_MESSAGE;
        final int ret = JOptionPane.showOptionDialog(getWindow(caller), message, title, 0, m, null, buttons, buttons[OK]);
        // ESC key returns a -1, which is handled as a cancel.
        return Math.abs(ret);
    }

    /**
     * Displays a series of buttons with the specified options.
     *
     * @param caller  the component to infer the root window from
     * @param title   the title to use
     * @param message the message to shown to the user
     * @param options the options to display as buttons
     * @return the options index chosen
     */
    public static int showOptionsQuestion(final JComponent caller, final String title, final String message,
                                          final String[] options, final String def)
    {
        final int[] ret = new int[1];
        if (!SwingUtilities.isEventDispatchThread())
        {
            try
            {
                SwingUtilities.invokeAndWait(new Runnable()
                {
                    public void run()
                    {
                        ret[0] = showDialog(caller, message, title, options, def);
                    }
                });
            }
            catch (InterruptedException e)
            {
                ret[0] = 0;
                e.printStackTrace();
            }
            catch (InvocationTargetException e)
            {
                ret[0] = 0;
                e.printStackTrace();
            }
        }
        else
        {
            ret[0] = showDialog(caller, message, title, options, def);
        }
        return ret[0];
    }

    private static int showDialog(final JComponent caller, final String message, final String title, final String[] options, final String def)
    {
        return JOptionPane.showOptionDialog(getWindow(caller), message, title, JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, options, def);
    }

    private static Container getWindow(final JComponent caller)
    {
        return (caller == null ? null : caller.getTopLevelAncestor());
    }

    public static void main(final String[] args)
    {
//        Dialogs.showErrorMessage(null, "Error Message", "An error occurred");
//        Dialogs.showWarnMessage(null, "Warn Message", "This is a warning");
//        Dialogs.showInfoMessage(null, "Info Message", "This is an info");
//        System.out.println(Dialogs.showQuestionMessageCancel(null, "Question Message", "Do I have a question?"));
//        System.out.println(Dialogs.showQuestionMessageOk(null, "Question Message", "Do I have a question?"));
        final String[] options = new String[]{"Option 1: Ist es das?", "Options 2: oder das?"};
        System.out.println(Dialogs.showOptionsQuestion(null, "Question Message", "Do I have a question?",
                options, options[1]));
        System.exit(0);
    }
}
