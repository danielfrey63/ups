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
package ch.jfactory.application;

import ch.jfactory.application.view.builder.ActionCommandPanelBuilder;
import ch.jfactory.application.view.status.StatusBar;
import ch.jfactory.binding.CodedNote;
import ch.jfactory.binding.InfoModel;
import ch.jfactory.command.CommonCommands;
import ch.jfactory.component.Dialogs;
import ch.jfactory.component.I15nWelcomePanel;
import ch.jfactory.resource.Strings;
import com.jgoodies.uif.panel.CardPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.io.File;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import org.pietschy.command.ActionCommand;

/**
 * Provides some common MainBuilder tasks:
 *
 * <ul>
 *
 * <li>Creates a status bar</li>
 *
 * <li>Builds a card panel with a welcome card and some other cards by calling two abstract create methods.</li>
 *
 * </ul>
 *
 * @author Daniel Frey
 * @version $Revision: 1.4 $ $Date: 2006/08/29 13:10:43 $
 */
public abstract class AbstractMainBuilder extends ActionCommandPanelBuilder
{
    private AbstractMainModel model;

    private JPanel main;

    private final InfoModel infoModel;

    private final CardPanel cards = new CardPanel();

    private StatusBar statusBar;

    private final String[] modelLoadedCommands;

    private final String[] welcomePanelCommands;

    protected static final String TITLE_DIRTY_MARKER = " *";

    private static final String TITLE_SEPARATOR = " - ";

    protected AbstractMainBuilder( final AbstractMainModel model, final InfoModel infoModel,
                                   final String[] welcomePanelCommands, final String[] modelLoadedCommands )
    {
        this.model = model;
        this.infoModel = infoModel;
        this.welcomePanelCommands = welcomePanelCommands;
        this.modelLoadedCommands = modelLoadedCommands;
    }

    protected JComponent createMainPanel()
    {
        final JComponent welcomePanel = createWelcomePanel();
        cards.add( AbstractMainModel.CARDS_WELCOME, welcomePanel );
        model.queue( new Runnable()
        {
            public void run()
            {
                statusBar = createStatusBar();
            }
        } );
        model.queue( new Runnable()
        {
            public void run()
            {
                getInfoModel().setNote( new CodedNote( Strings.getString( "startup.content" ) ) );
                createNonWelcomePanels();
            }
        } );
        model.queue( new Runnable()
        {
            public void run()
            {
                model.setModelLoaded( true );
                getInfoModel().setNote( new CodedNote( Strings.getString( "startup.end" ) ) );
            }
        } );
        main = new JPanel( new BorderLayout() );
        main.add( cards, BorderLayout.CENTER );
        return main;
    }

    protected StatusBar createStatusBar()
    {
        statusBar = new StatusBar();
        statusBar.setBorder( new EmptyBorder( GuiConstants.NO_GAP, GuiConstants.SMALL_GAP, GuiConstants.NO_GAP, GuiConstants.SMALL_GAP ) );
        return statusBar;
    }

    protected JComponent createWelcomePanel()
    {
        final I15nWelcomePanel panel = new I15nWelcomePanel( "welcome", welcomePanelCommands, getCommandManager() );
        panel.setInfoModel( getInfoModel() );
        return panel;
    }

    /**
     * Here you add the rest of the panels to the cards. Use {@link #getCards()} to access the cards panel.
     */
    protected abstract void createNonWelcomePanels();

    protected abstract JMenuBar getMenuBar();

    /**
     * Initializes model listeners. Make sure to call the super method in order to init the common ones.
     */
    protected void initModelListeners()
    {
        model.queue( new Runnable()
        {
            public void run()
            {
                // Check whether the user really wants to close the opened file.
                model.addVetoableChangeListener( AbstractMainModel.EVENTNAME_CLOSING, new VetoableChangeListener()
                {
                    public void vetoableChange( final PropertyChangeEvent evt ) throws PropertyVetoException
                    {
                        if ( (Boolean) evt.getNewValue() && model.isDirty() )
                        {
                            final String title = Strings.getString( "closing.title" );
                            final String question = Strings.getString( "closing.text" );
                            final int res = Dialogs.showQuestionMessageOk( getCards(), title, question );
                            if ( res != Dialogs.OK )
                            {
                                throw new PropertyVetoException( "quit has been stopped by user", evt );
                            }
                        }
                    }
                } );
                // Repaint all uis.
                model.addPropertyChangeListener( AbstractMainModel.EVENTNAME_CLOSING, new PropertyChangeListener()
                {
                    public void propertyChange( final PropertyChangeEvent evt )
                    {
                        getCards().repaint();
                    }
                } );
                // Install error handler feedback
                model.addPropertyChangeListener( AbstractMainModel.PROPERTYNAME_ERROR, new PropertyChangeListener()
                {
                    public void propertyChange( final PropertyChangeEvent evt )
                    {
                        final String key = (String) evt.getNewValue();
                        Dialogs.showErrorMessage( getCards().getRootPane(),
                                Strings.getString( key + ".title" ), Strings.getString( key + ".text" ) );
                    }
                } );
                // Change page to display
                // Switch menu bar and status bar
                model.addPropertyChangeListener( AbstractMainModel.PROPERTYNAME_CURRENTCARD, new PropertyChangeListener()
                {
                    public void propertyChange( final PropertyChangeEvent evt )
                    {
                        getCards().showCard( evt.getNewValue().toString() );
                        final JFrame frame = (JFrame) getCards().getTopLevelAncestor();
                        if ( evt.getNewValue() == AbstractMainModel.CARDS_WELCOME )
                        {
                            frame.setJMenuBar( null );
                            main.remove( getStatusBar() );
                            getCards().showCard( AbstractMainModel.CARDS_WELCOME );
                            String title = removeChunk( frame.getTitle(), TITLE_DIRTY_MARKER );
                            title = removeChunk( title, model.getCurrentFile().toString() );
                            title = removeChunk( title, TITLE_SEPARATOR );
                            frame.setTitle( title );
                        }
                        else
                        {
                            frame.setJMenuBar( getMenuBar() );
                            main.add( getStatusBar(), BorderLayout.SOUTH );
                            getCards().showCard( evt.getNewValue().toString() );
                            if ( evt.getOldValue() == AbstractMainModel.CARDS_WELCOME )
                            {
                                String title = frame.getTitle();
                                title = removeChunk( title, TITLE_DIRTY_MARKER );
                                title += ( model.isDirty() ? TITLE_DIRTY_MARKER : "" );
                                frame.setTitle( title );
                            }
                        }
                    }
                } );
                // Enable dependent commands upon end of model load.
                model.addPropertyChangeListener( AbstractMainModel.PROPERTYNAME_MODELLOADED, new PropertyChangeListener()
                {
                    public void propertyChange( final PropertyChangeEvent evt )
                    {
                        for ( final String command : modelLoadedCommands )
                        {
                            getCommandManager().getCommand( command ).setEnabled( true );
                        }
                    }
                } );
                // Adjust title.
                model.addPropertyChangeListener( AbstractMainModel.PROPERTYNAME_DIRTY, new PropertyChangeListener()
                {
                    public void propertyChange( final PropertyChangeEvent evt )
                    {
                        final boolean dirty = (Boolean) evt.getNewValue();
                        // Display asterisk in frame title
                        final Component c = SwingUtilities.getRoot( getCards() );
                        if ( c instanceof Frame )
                        {
                            final Frame top = (Frame) c;
                            final String title = top.getTitle();
                            if ( dirty )
                            {
                                top.setTitle( title + TITLE_DIRTY_MARKER );
                            }
                            else
                            {
                                top.setTitle( title.substring( 0, title.length() - TITLE_DIRTY_MARKER.length() ) );
                            }
                        }
                    }
                } );
                // Adjust title.
                model.addPropertyChangeListener( AbstractMainModel.PROPERTYNAME_CURRENTFILE, new PropertyChangeListener()
                {
                    public void propertyChange( final PropertyChangeEvent evt )
                    {
                        final String oldFileName = evt.getOldValue().toString();
                        final File file = (File) evt.getNewValue();
                        final String newValue = file == null ? model.getCurrentFile().toString() : file.toString();
                        final Component c = SwingUtilities.getRoot( getCards() );
                        if ( c instanceof Frame )
                        {
                            final Frame top = (Frame) c;
                            String title = top.getTitle();
                            title = removeChunk( title, TITLE_DIRTY_MARKER );
                            title = removeChunk( title, oldFileName );
                            title = removeChunk( title, TITLE_SEPARATOR );
                            top.setTitle( title + TITLE_SEPARATOR + newValue + ( model.isDirty() ? TITLE_DIRTY_MARKER : "" ) );
                        }
                    }
                } );
                // Make sure an exit handler is installed that redirects to the quit command
                getCards().addAncestorListener( new AncestorListener()
                {
                    public void ancestorAdded( final AncestorEvent event )
                    {
                    }

                    public void ancestorMoved( final AncestorEvent event )
                    {
                        final ActionCommand quit = getCommandManager().getCommand( CommonCommands.COMMANDID_QUIT );
                        final Component c = SwingUtilities.getRoot( getCards() );
                        if ( c instanceof Frame )
                        {
                            final Frame top = (Frame) c;
                            final WindowListener[] listeners = top.getWindowListeners();
                            for ( final WindowListener listener : listeners )
                            {
                                top.removeWindowListener( listener );
                            }
                            top.addWindowListener( new WindowAdapter()
                            {
                                public void windowClosing( final WindowEvent e )
                                {
                                    quit.execute();
                                }
                            } );
                        }
                        getCards().removeAncestorListener( this );
                    }

                    public void ancestorRemoved( final AncestorEvent event )
                    {
                    }
                } );
            }
        } );
    }

    private String removeChunk( final String title, final String chunk )
    {
        if ( title.endsWith( chunk ) )
        {
            return title.substring( 0, title.length() - chunk.length() );
        }
        return title;
    }

    public InfoModel getInfoModel()
    {
        return infoModel;
    }

    public CardPanel getCards()
    {
        return cards;
    }

    public StatusBar getStatusBar()
    {
        return statusBar;
    }

    public void setModel( final AbstractMainModel model )
    {
        this.model = model;
    }
}
