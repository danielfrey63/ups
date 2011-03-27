/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.component;

import ch.jfactory.application.presentation.Constants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.JWindow;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Displays a small window with the given label and invokes actions uppon up and down keys.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:57 $
 */
public class FinderWindow extends JWindow
{
    private static final Logger LOGGER = LoggerFactory.getLogger( FinderWindow.class );

    private static final String ACTION_KEY_NEXT = "HCD_SEARCH_NEXT";

    private static final String ACTION_KEY_PREV = "HCD_SEARCH_PREV";

    private static final KeyStroke KEY_STROKE_NEXT = KeyStroke.getKeyStroke( KeyEvent.VK_DOWN, 0, false );

    private static final KeyStroke KEY_STROKE_PREV = KeyStroke.getKeyStroke( KeyEvent.VK_UP, 0, false );

    private final InputMap inputMap;

    private final ActionMap actionMap;

    private JTextField field;

    private final JComponent component;

    private Point location;

    private final List documentListeners = new ArrayList();

    private Action nextAction;

    private Action prevAction;

    private Object originalNextKey;

    private Object originalPrevKey;

    private Action originalNextAction;

    private Action originalPrevAction;

    public FinderWindow( final JComponent component, final String label )
    {
        super( (Window) component.getTopLevelAncestor() );
        this.component = component;
        inputMap = component.getInputMap( JComponent.WHEN_FOCUSED );
        actionMap = component.getActionMap();
        registerTranslate();
        initGui( label );
    }

    /**
     * Register your action you want to perform on a down key with this method.
     *
     * @param action the action to execute on down
     */
    public void setNextAction( final Action action )
    {
        nextAction = action;
    }

    /**
     * Register your action you want to perform on an up key with this method.
     *
     * @param action the action to execute on up
     */
    public void setPrevAction( final Action action )
    {
        prevAction = action;
    }

    /**
     * Register the document listener for changes in the field here.
     *
     * @param documentListener to register
     */
    public void addDocumentListener( final DocumentListener documentListener )
    {
        documentListeners.add( documentListener );
        field.getDocument().addDocumentListener( documentListener );
    }

    /**
     * Sets the document for the search field
     *
     * @param document for the search field
     */
    public void setDocument( final Document document )
    {
        final Document old = field.getDocument();
        for ( final Object documentListener1 : documentListeners )
        {
            old.removeDocumentListener( (DocumentListener) documentListener1 );
        }
        field.setDocument( document );
        for ( final Object documentListener : documentListeners )
        {
            document.addDocumentListener( (DocumentListener) documentListener );
        }
    }

    private void initGui( final String text )
    {
        field = createEnterField();

        final int gap = Constants.GAP_WITHIN_TOGGLES;

        final JRootPane rootPane = getRootPane();
        rootPane.setBorder( new CompoundBorder( new LineBorder( Color.black, 1 ), new EmptyBorder( gap, gap, gap, gap ) ) );
        rootPane.setOpaque( true );
        rootPane.setBackground( Color.white );

        final Container contentPane = getContentPane();
        contentPane.setBackground( Color.white );
        contentPane.setLayout( new BorderLayout() );
        contentPane.add( createLabel( text ), BorderLayout.WEST );
        contentPane.add( field, BorderLayout.CENTER );

        registerKeyListener();
    }

    private JLabel createLabel( final String text )
    {
        final JLabel label = new JLabel( text );
        label.setFont( label.getFont().deriveFont( Font.BOLD ) );
        return label;
    }

    private JTextField createEnterField()
    {
        final JTextField textField = new JTextField();
        textField.setBorder( new EmptyBorder( 0, 0, 0, 0 ) );
        textField.getDocument().addDocumentListener( new SimpleDocumentListener()
        {
            public void changedUpdate( final DocumentEvent e )
            {
                calculateNewSize();
            }
        } );
        return textField;
    }

    private void registerTranslate()
    {
        component.addHierarchyListener( new HierarchyListener()
        {
            public void hierarchyChanged( final HierarchyEvent e )
            {
                final Container top = component.getTopLevelAncestor();
                if ( ( top instanceof JFrame || top instanceof JDialog ) && top.isVisible() )
                {
                    top.addComponentListener( new ComponentAdapter()
                    {
                        public void componentResized( final ComponentEvent e )
                        {
                            calculateNewLocation();
                        }

                        public void componentMoved( final ComponentEvent e )
                        {
                            calculateNewLocation();
                        }

                        public void componentShown( final ComponentEvent e )
                        {
                            calculateNewLocation();
                        }
                    } );
                    component.removeHierarchyListener( this );
                }
            }
        } );
    }

    private void calculateNewLocation()
    {
        final Container parent = component.getParent();
        if ( parent instanceof JViewport )
        {
            location = parent.getLocation();
            SwingUtilities.convertPointToScreen( location, parent );
        }
        else
        {
            location = component.getLocation();
            SwingUtilities.convertPointToScreen( location, component );
        }
        location.translate( -1, -20 );
        setLocation( location );
        repaint();
    }

    private void calculateNewSize()
    {
        pack();
        final Dimension size = field.getSize();
        field.setSize( new Dimension( size.width + 5, size.height ) );
    }

    private void registerKeyListener()
    {
        component.addKeyListener( new KeyAdapter()
        {
            public void keyTyped( final KeyEvent e )
            {
                try
                {
                    final char c = e.getKeyChar();
                    final Document document = field.getDocument();
                    if ( isAlphaNumeric( Character.toUpperCase( c ) ) || c == KeyEvent.VK_SPACE )
                    {
                        setLocation( location );
                        if ( !isVisible() )
                        {
                            field.setText( "" );
                            showWindow();
                        }
                        document.insertString( document.getLength(), "" + c, null );
                        calculateNewSize();
                    }
                    else if ( c == KeyEvent.VK_ESCAPE )
                    {
                        hideWindow();
                    }
                }
                catch ( BadLocationException e1 )
                {
                    LOGGER.error( "Error occured", e1 );
                }
            }

            public void keyReleased( final KeyEvent e )
            {
                final char c = e.getKeyChar();
                try
                {
                    if ( c == KeyEvent.VK_BACK_SPACE )
                    {
                        if ( field.getText().equals( "" ) )
                        {
                            hideWindow();
                        }
                        else
                        {
                            field.getDocument().remove( field.getCaretPosition() - 1, 1 );
                            calculateNewSize();
                        }
                    }
                }
                catch ( BadLocationException e1 )
                {
                    LOGGER.error( "Error occured", e1 );
                }
            }
        } );
    }

    private void showWindow()
    {
        linkNextAction( nextAction );
        linkPrevAction( prevAction );
        setVisible( true );
        field.requestFocus();
        toFront();
    }

    private void hideWindow()
    {
        restoreMaps( KEY_STROKE_PREV, prevAction, originalPrevAction, ACTION_KEY_PREV, originalPrevKey );
        restoreMaps( KEY_STROKE_NEXT, nextAction, originalNextAction, ACTION_KEY_NEXT, originalNextKey );
        setVisible( false );
        dispose();
    }

    private void restoreMaps( final KeyStroke keyStroke, final Action action, final Action originalAction, final String key, final Object originalKey )
    {
        if ( action != null )
        {
            if ( originalKey != null )
            {
                inputMap.put( keyStroke, originalKey );
                if ( originalAction != null )
                {
                    actionMap.put( originalKey, originalAction );
                }
                else
                {
                    actionMap.remove( key );
                }
            }
            else
            {
                inputMap.remove( keyStroke );
            }
        }
    }

    private void linkNextAction( final Action action )
    {
        if ( originalNextKey == null )
        {
            originalNextKey = inputMap.get( KEY_STROKE_NEXT );
        }
        if ( originalNextAction == null )
        {
            originalNextAction = actionMap.get( ACTION_KEY_NEXT );
        }
        linkAction( action, KEY_STROKE_NEXT, ACTION_KEY_NEXT );
    }

    private void linkPrevAction( final Action action )
    {
        if ( originalPrevKey == null )
        {
            originalPrevKey = inputMap.get( KEY_STROKE_PREV );
        }
        if ( originalPrevAction == null )
        {
            originalPrevAction = actionMap.get( ACTION_KEY_PREV );
        }
        linkAction( action, KEY_STROKE_PREV, ACTION_KEY_PREV );
    }

    private void linkAction( final Action action, final KeyStroke keyStroke, final String actionKey )
    {
        inputMap.put( keyStroke, actionKey );
        actionMap.put( actionKey, action );
    }

    private boolean isAlphaNumeric( final char c )
    {
        return c == KeyEvent.VK_0 || c == KeyEvent.VK_1 || c == KeyEvent.VK_2 || c == KeyEvent.VK_3
                || c == KeyEvent.VK_4 || c == KeyEvent.VK_5 || c == KeyEvent.VK_6 || c == KeyEvent.VK_7
                || c == KeyEvent.VK_8 || c == KeyEvent.VK_9 || c == KeyEvent.VK_0 || c == KeyEvent.VK_A
                || c == KeyEvent.VK_B || c == KeyEvent.VK_C || c == KeyEvent.VK_D || c == KeyEvent.VK_E
                || c == KeyEvent.VK_F || c == KeyEvent.VK_G || c == KeyEvent.VK_H || c == KeyEvent.VK_I
                || c == KeyEvent.VK_J || c == KeyEvent.VK_K || c == KeyEvent.VK_L || c == KeyEvent.VK_M
                || c == KeyEvent.VK_N || c == KeyEvent.VK_O || c == KeyEvent.VK_P || c == KeyEvent.VK_Q
                || c == KeyEvent.VK_R || c == KeyEvent.VK_S || c == KeyEvent.VK_T || c == KeyEvent.VK_U
                || c == KeyEvent.VK_V || c == KeyEvent.VK_W || c == KeyEvent.VK_X || c == KeyEvent.VK_Y
                || c == KeyEvent.VK_Z;
    }

    public static void main( final String[] args )
    {
        final JFrame parent = new JFrame();
        parent.setSize( 200, 200 );
        parent.setVisible( true );
        final FinderWindow window = new FinderWindow( parent.getRootPane(), "Suche: " );
        window.pack();
        window.setLocation( 100, 100 );
        window.setVisible( true );
        window.addDocumentListener( new SimpleDocumentListener()
        {
            public void changedUpdate( final DocumentEvent e )
            {
                System.out.println( "doc " + e.getDocument().getLength() );
                System.out.println( "len " + e.getLength() );
            }
        } );
    }
}
