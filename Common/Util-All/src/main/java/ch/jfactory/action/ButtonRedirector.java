package ch.jfactory.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import javax.swing.JButton;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:57 $
 */
class ButtonRedirector extends AbstractAction
{
    protected JButton button;

    public ButtonRedirector(final JButton button)
    {
        super.putValue(AbstractAction.NAME, button.getText());
        this.button = button;
    }

    public void actionPerformed(final ActionEvent e)
    {
        if (button.isEnabled())
        {
            final ActionListener[] listeners = button.getActionListeners();
            for (final ActionListener listener : listeners)
            {
                listener.actionPerformed(e);
            }
        }
    }
}
