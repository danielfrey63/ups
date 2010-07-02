/*
 * Herbar CD-ROM version 2
 *
 * MainFrame.java
 *
 * Created on 2. April 2002
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
import com.ethz.geobot.herbar.gui.commands.ActionModusSelection;
import com.ethz.geobot.herbar.gui.commands.ActionQuit;
import com.ethz.geobot.herbar.gui.commands.ActionSaveBounds;
import com.ethz.geobot.herbar.gui.commands.ActionStatustextAdapter;
import com.ethz.geobot.herbar.gui.commands.ActionWizard;
import com.ethz.geobot.herbar.gui.mode.ModeStateModel;
import com.ethz.geobot.herbar.gui.mode.ModeWizard;
import com.ethz.geobot.herbar.modeapi.Mode;
import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
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

    public static final String PREF_CD = "cd";

    private static final Logger LOG = LoggerFactory.getLogger( MainFrame.class );

    private final BorderLayout borderLayout1 = new BorderLayout();

    private JPanel contentPane;

    private final JMenuBar menubar = new JMenuBar();

    private final ModeStateModel modeStateModel = new ModeStateModel();

    private final Preferences prefNode = Preferences.userNodeForPackage( MainFrame.class );

    private JMenuItem quitItem;

    private StatusBar statusBar;

    private Action wizardAction;

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
                        if ( ModeWizard.getInstance().hasWizard( newMode ) )
                        {
                            wizardAction.setEnabled( true );
                        }
                        else
                        {
                            wizardAction.setEnabled( false );
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

    // create Menus and adds them to the menubar

    private JMenu createMenu( final String prefix )
    {
        final JMenu sub = menubar.add( new JMenu( Strings.getString( prefix + ".NAME" ) ) );
        sub.setMnemonic( Strings.getChar( prefix + ".MN" ) );
        return sub;
    }

    private JMenuItem createMenuItem( final JMenu menu, final Action action )
    {
        final JMenuItem item = menu.add( action );
        final String helpText = (String) action.getValue( Action.LONG_DESCRIPTION );
        item.addChangeListener( new ActionStatustextAdapter( helpText, statusBar ) );
        return item;
    }

    // Component, Statusbar and Menu initialization

    private void init() throws Exception
    {
        statusBar = new StatusBar();

        setTitle( Strings.getString( "APPLICATION.FRAME.TITLE" ) );
        setIconImage( ImageLocator.getIcon( Strings.getString( "APPLICATION.FRAME.ICON" ) ).getImage() );

        JMenu sub = null;
        sub = createMenu( "MENU.APPLICATION" );
        createMenuItem( sub, new ActionSaveBounds( this, prefNode ) );
        createMenuItem( sub, new ActionConvertFilterFromUst( this ) );
        sub.addSeparator();
        quitItem = createMenuItem( sub, new ActionQuit( this ) );

        sub = createMenu( "MENU.SETTINGS" );
        createMenuItem( sub, new ActionModusSelection( this, prefNode ) );
        createMenuItem( sub, wizardAction = new ActionWizard( this ) );

        sub = createMenu( "MENU.HERBAR" );
        createMenuItem( sub, new ActionAppHelp( this ) );
        createMenuItem( sub, new ActionModuleInfo( this ) );
        createMenuItem( sub, new ActionAbout( this ) );

        setJMenuBar( menubar );

        contentPane = (JPanel) this.getContentPane();
        contentPane.setLayout( borderLayout1 );
        contentPane.add( statusBar, BorderLayout.SOUTH );
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
}
