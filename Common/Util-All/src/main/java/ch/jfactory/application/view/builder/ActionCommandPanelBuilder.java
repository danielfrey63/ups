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
package ch.jfactory.application.view.builder;

import ch.jfactory.image.SimpleIconFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.pietschy.command.ActionCommand;
import org.pietschy.command.CommandManager;
import org.pietschy.command.IconFactory;
import org.pietschy.command.LoadException;
import org.pietschy.command.ToggleCommand;

/**
 * This panel builder handles the instantiation and the configration of a {@link CommandManager CommandManager} object.
 * If you want to use it, make sure to have a <code>command.xml</code> and a set of properties in a file
 * <code>command.properties</code> in the same package as the subclass you build. This abstract class implements the
 * {@link Builder#getPanel()} method and calls subsequently:
 *
 * <ol>
 *
 * <li>{@link #initCommands()}: overwrite this method to init the {@link ActionCommand ActionCommand}s for the command
 * manager.</li>
 *
 * <li>{@link #createMainPanel()}: overwrite this method to build the content panel.</li>
 *
 * <li>{@link #initModelListeners()}: overwrite to init handlers.</li>
 *
 * <li>{@link #initComponentListeners()}: overwrite to init listeners to subcomponents.</li>
 *
 * <li>{@link #initSubpanelCommands()}: overwrite to init the commands of subcomponents.</li>
 *
 * </ol>
 *
 * @author Daniel Frey
 * @version $Revision: 1.10 $ $Date: 2008/01/06 10:16:23 $
 */
public abstract class ActionCommandPanelBuilder implements Builder
{
    private static final Logger LOG = Logger.getLogger( ActionCommandPanelBuilder.class );

    private static final boolean DEBUG = LOG.isDebugEnabled();

    private CommandManager commandManager;

    private JComponent panel;

    private transient int panelCallCounter = 0;

    public JComponent getPanel()
    {
        panelCallCounter++;
        if ( panelCallCounter > 1 )
        {
            throw new IllegalStateException( "don't call getPanel twice" );
        }
        try
        {
            initCommandManager();
            initCommands();
            initModelListeners();
            panel = createMainPanel();
            initComponentListeners();
            initSubpanelCommands();
        }
        catch ( LoadException e )
        {
            e.printStackTrace();
        }
        return panel;
    }

    private void initCommandManager() throws LoadException
    {
        final String name = getClass().getPackage().getName();
        final String base = StringUtils.replace( "/" + name, ".", "/" );
        final String[] locations = {base, base + "/commands"};
        InputStream resource = null;
        String location = "";
        for ( final String l : locations )
        {
            location = l;
            resource = getClass().getResourceAsStream( location + "/commands.xml" );
            if ( resource != null )
            {
                break;
            }
        }
        if ( resource != null )
        {
            commandManager = new CommandManager();
            final String prefix = System.getProperty( "ch.jfactory.iconprefix", "/16x16/fill" );
            final IconFactory iconFactory = new SimpleIconFactory( prefix );
            commandManager.setIconFactory( iconFactory );
            final String bundleName = location.substring( 1 ).replace( '/', '.' ) + ".commands";
            final ResourceBundle bundle = ResourceBundle.getBundle( bundleName );
            commandManager.setResourceBundle( bundle );
            if ( DEBUG )
            {
                try
                {
                    final String content = new String( IOUtils.toByteArray( resource ) );
                    LOG.debug( content );
                    resource = getClass().getResourceAsStream( location + "/commands.xml" );
                    assert resource != null : "resource is null";
                }
                catch ( IOException e )
                {
                    LOG.error( "could not print content of commands xml file at " + location, e );
                }
            }
            commandManager.load( resource );
        }
        else
        {
            final String locs = StringUtils.join( locations, ", " );
            final String clazz = getClass().getName();
            final String message = "command.xml for " + clazz + " not found in one of these locations: " + locs;
            if ( DEBUG )
            {
                LOG.debug( message );
            }
        }
    }

    /**
     * Init action commands in this implementation. This is an adapter (empty) implementaiton for this method (no need
     * to call super).
     */
    protected void initCommands()
    {
    }

    /**
     * If you want to insert commands to subpanels, these have to be inserted <em>after</em> the subpanels have been
     * built. Override this method to init commands which rely on a subpanels command manager. This is an adapter
     * (empty) implementaiton for this method (no need to call super).
     */
    protected void initSubpanelCommands()
    {
    }

    /**
     * Override this method to initialize your panel. Call super to instantate a JPanel if you like (no need to).
     *
     * @return the panel of this builder
     */
    protected JComponent createMainPanel()
    {
        return new JPanel();
    }

    /**
     * This is an adapter (empty) implementaiton for this method (no need to call super).
     */
    protected void initModelListeners()
    {
    }

    /**
     * This is an adapter (empty) implementaiton for this method (no need to call super).
     */
    protected void initComponentListeners()
    {
    }

    public ActionCommand getCommand( final String commandId )
    {
        return commandManager.getCommand( commandId );
    }

    public CommandManager getCommandManager()
    {
        return commandManager;
    }

    /**
     * Exports and disables the command.
     *
     * @param command the command to initialize
     * @return the action command
     */
    protected ActionCommand initCommand( final ActionCommand command )
    {
        return initCommand( command, false );
    }

    /**
     * Exports the command and sets its enabled state according to the given parameter.
     *
     * @param command the command to initialize
     * @param enabled whether to enable the command
     * @return the action command
     */
    protected ActionCommand initCommand( final ActionCommand command, final boolean enabled )
    {
        command.export();
        command.setEnabled( enabled );
        return command;
    }

    protected void initToggleCommand( final ToggleCommand command, final boolean selected )
    {
        initCommand( command, true );
        command.setSelected( selected );
    }
}
