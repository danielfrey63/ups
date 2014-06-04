/*
 * Copyright (c) 2011.
 *
 * Nutzung und Rechte
 *
 * Die Applikation eBot wurde für Studierende der ETH Zürich entwickelt. Sie  steht
 * allen   an   Hochschulen  oder   Fachhochschulen   eingeschriebenen Studierenden
 * (auch  ausserhalb  der  ETH  Zürich)  für  nichtkommerzielle  Zwecke  im Studium
 * kostenlos zur Verfügung. Nichtstudierende Privatpersonen, die die Applikation zu
 * ihrer  persönlichen  Weiterbildung  nutzen  möchten,  werden  gebeten,  für  die
 * nichtkommerzielle Nutzung einen einmaligen Beitrag von Fr. 20.– zu bezahlen.
 *
 * Postkonto
 *
 * Unterricht, 85-761469-0, Vermerk "eBot"
 * IBAN 59 0900 0000 8576  1469 0; BIC POFICHBEXXX
 *
 * Jede andere Nutzung der Applikation  ist vorher mit dem Projektleiter  (Matthias
 * Baltisberger, Email:  balti@ethz.ch) abzusprechen  und mit  einer entsprechenden
 * Vereinbarung zu regeln. Die  Applikation wird ohne jegliche  Garantien bezüglich
 * Nutzungsansprüchen zur Verfügung gestellt.
 */
package com.ethz.geobot.herbar.gui;

import ch.jfactory.application.presentation.WindowUtils;
import ch.jfactory.application.view.status.StatusBar;
import ch.jfactory.resource.ImageLocator;
import ch.jfactory.resource.OperatingSystem;
import ch.jfactory.resource.Strings;
import com.ethz.geobot.herbar.gui.commands.ActionAbout;
import com.ethz.geobot.herbar.gui.commands.ActionAppHelp;
import com.ethz.geobot.herbar.gui.commands.ActionConvertFilterFromUst;
import com.ethz.geobot.herbar.gui.commands.ActionModuleInfo;
import com.ethz.geobot.herbar.gui.commands.ActionQuit;
import com.ethz.geobot.herbar.gui.commands.ActionSaveBounds;
import com.ethz.geobot.herbar.gui.mode.ModeManager;
import com.ethz.geobot.herbar.gui.mode.ModeNotFoundException;
import com.ethz.geobot.herbar.gui.mode.ModeStateModel;
import com.ethz.geobot.herbar.modeapi.Mode;
import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:05:50 $
 */
public class MainFrame extends JFrame
{
    public static final String PREF_X = "bounds_x";

    public static final String PREF_Y = "bounds_y";

    public static final String PREF_W = "bounds_w";

    public static final String PREF_H = "bounds_h";

    private static final Logger LOG = LoggerFactory.getLogger( MainFrame.class );

    private final BorderLayout borderLayout1 = new BorderLayout();

    private JPanel contentPane;

    private final JMenuBar menuBar = new JMenuBar();

    private final ModeStateModel modeStateModel = new ModeStateModel();

    private final Preferences prefNode = Preferences.userNodeForPackage( MainFrame.class );

    private JMenuItem quitItem;

    private StatusBar statusBar;

    private ArrayList<Action> modeActions;

    //Construct the Mainframe

    public MainFrame()
    {
        enableEvents( AWTEvent.WINDOW_EVENT_MASK );
        try
        {
            init();
            modeStateModel.addPropertyChangeListener( new PropertyChangeListener()
            {
                public void propertyChange( final PropertyChangeEvent e )
                {
                    if ( e.getPropertyName().equals( "mode" ) )
                    {
                        final Mode oldMode = (Mode) e.getOldValue();
                        final Mode newMode = (Mode) e.getNewValue();
                        if ( oldMode != null )
                        {
                            oldMode.deactivate();
                        }
                        if ( newMode != null )
                        {
                            newMode.activate();
                        }
                    }
                    else if ( e.getPropertyName().equals( "viewComponent" ) )
                    {
                        final Component oldViewComponent = (Component) e.getOldValue();
                        final Component newViewComponent = (Component) e.getNewValue();
                        if ( oldViewComponent != null )
                        {
                            contentPane.remove( oldViewComponent );
                        }
                        if ( newViewComponent != null )
                        {
                            contentPane.add( newViewComponent, BorderLayout.CENTER );
                            contentPane.revalidate();
                            contentPane.validate();
                            contentPane.repaint();
                        }
                    }
                }
            } );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }

    public ModeStateModel getModel()
    {
        return modeStateModel;
    }

    public void loadSettings()
    {
        modeStateModel.loadState( prefNode );
        loadFrameBounds();
    }

    private void loadFrameBounds()
    {
        pack();
        WindowUtils.centerOnScreen( this );

        // make sure the window does not exceed the screen
        final int x = prefNode.getInt( PREF_X, getLocation().x );
        final int y = prefNode.getInt( PREF_Y, getLocation().y );
        final int h = prefNode.getInt( PREF_H, getSize().height );
        final int w = prefNode.getInt( PREF_W, getSize().width );
        final Rectangle size = new Rectangle( x, y, w, h );
        final Rectangle screen = OperatingSystem.getScreenBounds();
        setBounds( size.intersection( screen ) );
    }

    public void storeSettings()
    {
        modeStateModel.storeState( prefNode );

        // flush preferences
        try
        {
            prefNode.flush();
        }
        catch ( BackingStoreException ex )
        {
            LOG.error( "Error storing Mode preferences. ", ex );
        }
    }

    // overridden so we can exit when window is closed

    protected void processWindowEvent( final WindowEvent e )
    {
        super.processWindowEvent( e );
        if ( e.getID() == WindowEvent.WINDOW_CLOSING )
        {
            quitItem.doClick();
        }
    }

    // create Menus and adds them to the menu bar

    private JMenu createMenu( final String prefix )
    {
        final JMenu sub = menuBar.add( new JMenu( Strings.getString( prefix + ".NAME" ) ) );
        sub.setMnemonic( Strings.getChar( prefix + ".MN" ) );
        return sub;
    }

    // Component, status bar and menu initialization

    private void init() throws Exception
    {
        statusBar = new StatusBar();

        setTitle( Strings.getString( "APPLICATION.FRAME.TITLE" ) );
        setIconImage( ImageLocator.getIcon( Strings.getString( "APPLICATION.FRAME.ICON" ) ).getImage() );

        JMenu sub;
        sub = createMenu( "MENU.APPLICATION" );
        sub.add( new ActionSaveBounds( this, prefNode ) );
        sub.add( new ActionConvertFilterFromUst( this ) );
        sub.addSeparator();
        quitItem = sub.add( new ActionQuit( this ) );

        sub = createMenu( "MENU.SETTINGS" );
        modeActions = new ArrayList<Action>();
        final AbstractAction lesson = getModeMenu( "Lernen", "LessonMode" );
        sub.add( lesson );
        final JMenu game = new JMenu( "Spiele" );
        final AbstractAction hangman = getModeMenu( "Hang Man", "Hangman" );
        game.add( hangman );
        game.add( getModeMenu( "Hasch Mich", "Catcher" ) );
        game.add( getModeMenu( "Labyrinth", "Labyrinth" ) );
        sub.add( game );

        sub = createMenu( "MENU.HERBAR" );
        sub.add( new ActionAppHelp( this ) );
        sub.add( new ActionModuleInfo( this ) );
        sub.add( new ActionAbout( this ) );

        setJMenuBar( menuBar );

        contentPane = (JPanel) this.getContentPane();
        contentPane.setLayout( borderLayout1 );
        contentPane.add( statusBar, BorderLayout.SOUTH );
    }

    private AbstractAction getModeMenu( final String name, final String mode )
    {
        final ActionMode action = new ActionMode( name, mode );
        modeActions.add( action );
        return action;
    }

    /**
     * Returns the status bar object used in this frame.
     *
     * @return the status bar object.
     */
    public StatusBar getStatusBar()
    {
        return statusBar;
    }

    private class ActionMode extends AbstractAction
    {
        private final String mode;

        public ActionMode( String name, String mode )
        {
            super( name );
            this.mode = mode;
        }

        @Override
        public void actionPerformed( ActionEvent event )
        {
            try
            {
                getModel().setMode( ModeManager.getInstance().getMode( mode ) );
                for ( final Action action : modeActions )
                {
                    action.setEnabled( true );
                }
                this.setEnabled( false );
            }
            catch ( ModeNotFoundException e )
            {
                LOG.error( "Mode not found", e );
            }
        }
    }
}
