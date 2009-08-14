/* ====================================================================
 *  Copyright 2004-2005 www.xmatrix.ch
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 * ====================================================================
 */
package ch.xmatrix.ups.ust.main.commands;

import ch.jfactory.command.CommonCommands;
import com.jgoodies.uifextras.convenience.DefaultAboutDialog;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import org.pietschy.command.ActionCommand;
import org.pietschy.command.CommandManager;

/**
 * Shows a default JGoodies about dialog.
 *
 * @author Daniel Frey
 * @version $Revision: 1.3 $ $Date: 2006/07/27 16:38:57 $
 */
public class AboutCommand extends ActionCommand
{

    private Icon icon = null;

    private String version;

    public AboutCommand(final CommandManager commandManager, final Icon icon, final String version)
    {
        super(commandManager, CommonCommands.COMMANDID_ABOUT);
        this.icon = icon;
        this.version = version;
    }

    protected void handleExecute()
    {
        final JLabel version = new JLabel("Version " + this.version, JLabel.CENTER);
        final Font font = new Font("Dialog", Font.BOLD, 10);
        version.setFont(font);
        version.setForeground(new Color(0xAD9300));
        version.setBorder(new EmptyBorder(6, 0, 0, 0));
        final JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.white);
        panel.add(new JLabel(icon), BorderLayout.CENTER);
        panel.add(version, BorderLayout.SOUTH);
        panel.setBorder(new EmptyBorder(70, 100, 130, 100));
        final DefaultAboutDialog dialog = new DefaultAboutDialog(null, panel);
        dialog.open();
    }
}
