// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 09.06.2005 09:02:43
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) fieldsfirst nonlb

package ch.jfactory.application.view.search;

import ch.jfactory.application.view.search.event.SearchableEvent;
import ch.jfactory.application.view.search.event.SearchableListener;
import ch.jfactory.resource.Strings;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.IllegalComponentStateException;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.EventListenerList;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public abstract class Searchable
{
    private class SearchPopup extends JPanel
    {
        private SearchField textField;

        private JLabel noMatch;

        private void initComponents( final String s )
        {
            final Color foreground = getForeground();
            final Color color = getBackground();
            final JLabel label = new JLabel( searchLabel );
            label.setForeground( foreground );
            label.setVerticalAlignment( 3 );
            noMatch = new JLabel();
            noMatch.setForeground( getMismatchForeground() );
            noMatch.setVerticalAlignment( 3 );
            textField = new SearchField();
            textField.setOpaque( false );
            textField.setBorder( BorderFactory.createEmptyBorder() );
            textField.setForeground( foreground );
            textField.setCursor( getCursor() );
            textField.setDocument( new PlainDocument()
            {
                public void insertString( final int j, final String s1, final AttributeSet attributeset ) throws BadLocationException
                {
                    String s2;
                    try
                    {
                        s2 = getText( 0, getLength() );
                    }
                    catch ( BadLocationException badlocationexception )
                    {
                        s2 = "";
                    }
                    final String s3 = s2.substring( 0, j ) + s1 + s2.substring( j );
                    super.insertString( j, s1, attributeset );
                    if ( findFromCursor( s3 ) == -1 )
                    {
                        textField.setForeground( getMismatchForeground() );
                    }
                    else
                    {
                        textField.setForeground( foreground );
                    }
                }
            } );
            textField.setText( s );
            setBackground( color );
            setBorder( BorderFactory.createCompoundBorder( BorderFactory.createLineBorder( UIManager.getColor( "controlShadow" ), 1 ), BorderFactory.createEmptyBorder( 0, 6, 1, 8 ) ) );
            setLayout( new BorderLayout() );
            final Dimension dimension = label.getPreferredSize();
            dimension.height = textField.getPreferredSize().height;
            label.setPreferredSize( dimension );
            add( label, "Before" );
            add( textField, "Center" );
            add( noMatch, "After" );
            final int i = findFromCursor( textField.getText() );
            select( i, null, s );
        }

        public void processKeyEvent( final KeyEvent keyevent )
        {
            textField.processKeyEvent( keyevent );
            if ( keyevent.isConsumed() )
            {
                final String s = textField.getText();
                if ( isSelectAllKey( keyevent ) )
                {
                    int i = findFirst( s );
                    if ( i != -1 )
                    {
                        setSelectedIndex( i, false );
                        cursor = i;
                    }
                    while ( i != -1 )
                    {
                        final int k = findNext( s );
                        if ( i == k )
                        {
                            i = -1;
                        }
                        else
                        {
                            i = k;
                        }
                        select( i, keyevent, s );
                    }
                    return;
                }
                final int j;
                if ( isFindPreviousKey( keyevent ) )
                {
                    j = findPrevious( s );
                }
                else if ( isFindNextKey( keyevent ) )
                {
                    j = findNext( s );
                }
                else if ( isFindFirstKey( keyevent ) )
                {
                    j = findFirst( s );
                }
                else if ( isFindLastKey( keyevent ) )
                {
                    j = findLast( s );
                }
                else
                {
                    j = findFromCursor( s );
                }
                select( j, keyevent, s );
            }
        }

        private void select( final int i, final KeyEvent keyevent, final String s )
        {
            if ( i != -1 )
            {
                setSelectedIndex( i, keyevent != null && isIncrementalSelectKey( keyevent ) );
                cursor = i;
                textField.setForeground( getForeground() );
                noMatch.setText( "" );
            }
            else
            {
                textField.setForeground( getMismatchForeground() );
                noMatch.setText( noMatchLabel );
            }
            if ( popup != null )
            {
                popup.setSize( popup.getPreferredSize() );
                popup.validate();
            }
            firePropertyChangeEvent( s );
            if ( i != -1 )
            {
                final Object obj = getElementAt( i );
                fireSearchableEvent( new SearchableEvent( Searchable.this, 3002, s, obj, convertElementToString( obj ) ) );
            }
            else
            {
                fireSearchableEvent( new SearchableEvent( Searchable.this, 3003, s ) );
            }
        }

        public SearchPopup( final String s )
        {
            super();
            initComponents( s );
        }
    }

    private class SearchField extends JTextField
    {
        public Dimension getPreferredSize()
        {
            final Dimension dimension = super.getPreferredSize();
            dimension.width = getFontMetrics( getFont() ).stringWidth( getText() ) + 4;
            return dimension;
        }

        public void processKeyEvent( final KeyEvent keyevent )
        {
            final int i = keyevent.getKeyCode();
            if ( i == 8 && getDocument().getLength() == 0 )
            {
                keyevent.consume();
                return;
            }
            if ( isDeactivateKey( keyevent ) && !isNavigationKey( keyevent ) )
            {
                hidePopup();
                if ( i == 27 )
                {
                    keyevent.consume();
                }
                return;
            }
            super.processKeyEvent( keyevent );
            if ( i == 8 || isNavigationKey( keyevent ) )
            {
                keyevent.consume();
            }
            if ( isSelectAllKey( keyevent ) )
            {
                keyevent.consume();
            }
        }

        SearchField()
        {
            super();
            setFocusable( false );
        }
    }

    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport( this );

    protected final JComponent component;

    private SearchPopup popup;

    private JLayeredPane layeredPane;

    private Pattern pattern;

    private String searchText;

    private String previousSearchText;

    private boolean caseSensitive;

    private Color mismatchForeground;

    private Color foreground;

    private Color background;

    protected ComponentAdapter componentListener;

    public static final String PROPERTY_SEARCH_TEXT = "searchText";

    private KeyAdapter keyListener;

    private FocusAdapter focusListener;

    private int cursor;

    private String searchLabel;

    private final String noMatchLabel;

    protected EventListenerList listenerList;

    public Searchable( final JComponent jcomponent )
    {
        caseSensitive = false;
        foreground = null;
        background = null;
        cursor = -1;
        searchLabel = Strings.getString( "searchable.searchfor" );
        noMatchLabel = Strings.getString( "searchable.nomatch" );
        listenerList = new EventListenerList();
        previousSearchText = null;
        component = jcomponent;
        installListeners();
    }

    protected abstract int getSelectedIndex();

    protected abstract void setSelectedIndex( int i, boolean flag );

    protected abstract int getElementCount();

    protected abstract Object getElementAt( int i );

    protected abstract String convertElementToString( Object obj );

    protected void hidePopup()
    {
        if ( popup != null )
        {
            popup.setVisible( false );
            popup = null;
            fireSearchableEvent( new SearchableEvent( this, 3000 ) );
        }
        cursor = -1;
    }

    public void installListeners()
    {
        keyListener = new KeyAdapter()
        {
            public void keyTyped( final KeyEvent keyevent )
            {
                keyTypedOrPressed( keyevent );
            }

            public void keyPressed( final KeyEvent keyevent )
            {
                keyTypedOrPressed( keyevent );
            }
        };
        component.addKeyListener( keyListener );
        focusListener = new FocusAdapter()
        {
            public void focusLost( final FocusEvent focusevent )
            {
                hidePopup();
            }
        };
        component.addFocusListener( focusListener );
    }

    public void uninstallListeners()
    {
        if ( componentListener != null )
        {
            component.removeComponentListener( componentListener );
            if ( getScrollPane( component ) != null )
            {
                getScrollPane( component ).removeComponentListener( componentListener );
            }
            componentListener = null;
        }
        if ( keyListener != null )
        {
            component.removeKeyListener( keyListener );
            keyListener = null;
        }
        if ( focusListener != null )
        {
            component.removeFocusListener( focusListener );
            focusListener = null;
        }
    }

    public void addPropertyChangeListener( final PropertyChangeListener propertychangelistener )
    {
        propertyChangeSupport.addPropertyChangeListener( propertychangelistener );
    }

    public void removePropertyChangeListener( final PropertyChangeListener propertychangelistener )
    {
        propertyChangeSupport.removePropertyChangeListener( propertychangelistener );
    }

    private void firePropertyChangeEvent( final String s )
    {
        if ( !s.equals( previousSearchText ) )
        {
            propertyChangeSupport.firePropertyChange( "searchText", previousSearchText, s );
            fireSearchableEvent( new SearchableEvent( this, 3004, s, previousSearchText ) );
            previousSearchText = s;
        }
    }

    protected boolean compare( final Object obj, final String s )
    {
        final String s1 = convertElementToString( obj );
        return s1 != null && compare( isCaseSensitive() ? s1 : s1.toLowerCase(), s );
    }

    protected boolean compare( final String s, final String s1 )
    {
        final int i = s1.indexOf( '*' );
        final int j = s1.indexOf( '?' );
        if ( ( i == -1 || i == s1.length() - 1 ) && ( j == -1 || j == s1.length() - 1 ) )
        {
            return s.startsWith( s1 );
        }
        if ( searchText != null && searchText.equals( s1 ) && pattern != null )
        {
            return pattern.matcher( s ).find();
        }
        searchText = s1;
        final StringBuffer stringbuffer = new StringBuffer();
        final int k = s1.length();
        stringbuffer.append( '^' );
        for ( int l = 0; l < k; l++ )
        {
            final char c = s1.charAt( l );
            if ( c == '?' && l != k - 1 )
            {
                stringbuffer.append( "." );
            }
            else if ( c == '*' && l != k - 1 )
            {
                stringbuffer.append( "." );
                stringbuffer.append( c );
            }
            else if ( "(){}[].^$\\".indexOf( c ) != -1 )
            {
                stringbuffer.append( '\\' );
                stringbuffer.append( c );
            }
            else
            {
                stringbuffer.append( c );
            }
        }

        try
        {
            pattern = Pattern.compile( stringbuffer.toString(), isCaseSensitive() ? 0 : 2 );
            return pattern.matcher( s ).find();
        }
        catch ( PatternSyntaxException patternsyntaxexception )
        {
            return false;
        }
    }

    private int findNext( final String s )
    {
        final String s1 = s.trim().toLowerCase();
        final int i = getElementCount();
        if ( i == 0 )
        {
            return -1;
        }
        final int j = cursor == -1 ? getSelectedIndex() : cursor;
        for ( int k = j + 1; k < i; k++ )
        {
            final Object obj = getElementAt( k );
            if ( compare( obj, s1 ) )
            {
                return k;
            }
        }

        return j != -1 ? compare( getElementAt( j ), s1 ) ? j : -1 : -1;
    }

    private int findPrevious( final String s )
    {
        final String s1 = s.trim().toLowerCase();
        final int i = getElementCount();
        if ( i == 0 )
        {
            return -1;
        }
        final int j = cursor == -1 ? getSelectedIndex() : cursor;
        for ( int k = j - 1; k >= 0; k-- )
        {
            final Object obj = getElementAt( k );
            if ( compare( obj, s1 ) )
            {
                return k;
            }
        }

        return j != -1 ? compare( getElementAt( j ), s1 ) ? j : -1 : -1;
    }

    private int findFromCursor( final String s )
    {
        final String s1 = s.trim().toLowerCase();
        int i = cursor == -1 ? getSelectedIndex() : cursor;
        if ( i < 0 )
        {
            i = 0;
        }
        final int j = getElementCount();
        if ( j == 0 )
        {
            return -1;
        }
        for ( int k = i; k < j; k++ )
        {
            final Object obj = getElementAt( k );
            if ( compare( obj, s1 ) )
            {
                return k;
            }
        }

        for ( int l = 0; l < i; l++ )
        {
            final Object obj1 = getElementAt( l );
            if ( compare( obj1, s1 ) )
            {
                return l;
            }
        }

        return -1;
    }

    private int findFirst( final String s )
    {
        final String s1 = s.trim().toLowerCase();
        final int i = getElementCount();
        if ( i == 0 )
        {
            return -1;
        }
        for ( int j = 0; j < i; j++ )
        {
            final Object obj = getElementAt( j );
            if ( compare( obj, s1 ) )
            {
                return j;
            }
        }

        return -1;
    }

    private int findLast( final String s )
    {
        final String s1 = s.trim().toLowerCase();
        final int i = getElementCount();
        if ( i == 0 )
        {
            return -1;
        }
        for ( int j = i - 1; j >= 0; j-- )
        {
            final Object obj = getElementAt( j );
            if ( compare( obj, s1 ) )
            {
                return j;
            }
        }

        return -1;
    }

    private void keyTypedOrPressed( final KeyEvent keyevent )
    {
        if ( keyevent.isAltDown() )
        {
            return;
        }
        if ( popup != null )
        {
            popup.processKeyEvent( keyevent );
            return;
        }
        if ( keyevent.getID() == 400 )
        {
            final char c = keyevent.getKeyChar();
            if ( isActivateKey( keyevent ) )
            {
                final String s = String.valueOf( c );
                fireSearchableEvent( new SearchableEvent( this, 2999, s ) );
                createPopup( new SearchPopup( s ) );
                keyevent.consume();
            }
        }
    }

    public String getSearchingText()
    {
        return popup != null ? popup.textField.getText() : "";
    }

    private void createPopup( final SearchPopup searchpopup )
    {
        if ( popup != null && layeredPane != null )
        {
            layeredPane.remove( popup );
            layeredPane.validate();
            layeredPane.repaint();
            layeredPane = null;
        }
        else if ( !component.isShowing() )
        {
            popup = null;
        }
        else
        {
            popup = searchpopup;
        }
        if ( popup == null || !component.isDisplayable() )
        {
            return;
        }
        final JRootPane jrootpane = component.getRootPane();
        if ( jrootpane != null )
        {
            layeredPane = jrootpane.getLayeredPane();
        }
        else
        {
            layeredPane = null;
        }
        if ( layeredPane == null )
        {
            System.err.println( "Failed to find layeredPane." );
            return;
        }
        layeredPane.add( popup, JLayeredPane.POPUP_LAYER );
        if ( componentListener == null )
        {
            componentListener = new ComponentAdapter()
            {
                public void componentHidden( final ComponentEvent componentevent )
                {
                    super.componentHidden( componentevent );
                    hidePopup();
                }

                public void componentResized( final ComponentEvent componentevent )
                {
                    super.componentResized( componentevent );
                    updateSizeAndLocation();
                }

                public void componentMoved( final ComponentEvent componentevent )
                {
                    super.componentMoved( componentevent );
                    updateSizeAndLocation();
                }
            };
        }
        component.addComponentListener( componentListener );
        if ( getScrollPane( component ) != null )
        {
            getScrollPane( component ).addComponentListener( componentListener );
        }
        updateSizeAndLocation();
        popup.setVisible( true );
        popup.validate();
    }

    private void updateSizeAndLocation()
    {
        JComponent jcomponent = getScrollPane( component );
        if ( jcomponent == null )
        {
            jcomponent = component;
        }
        Point point = null;
        try
        {
            point = jcomponent.getLocationOnScreen();
        }
        catch ( IllegalComponentStateException illegalcomponentstateexception )
        {
            return;
        }
        SwingUtilities.convertPointFromScreen( point, layeredPane );
        if ( popup != null )
        {
            final Dimension dimension = popup.getPreferredSize();
            point.y -= dimension.height;
            if ( point.y < 0 )
            {
                point.y = 0;
            }
            popup.setLocation( point );
            popup.setSize( dimension );
        }
    }

    private JComponent getScrollPane( final JComponent jcomponent )
    {
        JComponent jcomponent1 = jcomponent;
        if ( jcomponent1.getParent() != null && jcomponent1.getParent().getParent() != null && ( jcomponent1.getParent().getParent() instanceof JScrollPane ) )
        {
            jcomponent1 = (JComponent) jcomponent1.getParent().getParent();
            return jcomponent1;
        }
        else
        {
            return null;
        }
    }

    protected boolean isFindFirstKey( final KeyEvent keyevent )
    {
        return keyevent.getKeyCode() == 36;
    }

    protected boolean isFindLastKey( final KeyEvent keyevent )
    {
        return keyevent.getKeyCode() == 35;
    }

    protected boolean isFindPreviousKey( final KeyEvent keyevent )
    {
        return keyevent.getKeyCode() == 38;
    }

    protected boolean isFindNextKey( final KeyEvent keyevent )
    {
        return keyevent.getKeyCode() == 40;
    }

    protected boolean isNavigationKey( final KeyEvent keyevent )
    {
        return isFindFirstKey( keyevent ) || isFindLastKey( keyevent ) || isFindNextKey( keyevent ) || isFindPreviousKey( keyevent );
    }

    protected boolean isActivateKey( final KeyEvent keyevent )
    {
        final char c = keyevent.getKeyChar();
        return Character.isLetterOrDigit( c ) || c == '*' || c == '?';
    }

    protected boolean isDeactivateKey( final KeyEvent keyevent )
    {
        final int i = keyevent.getKeyCode();
        return i == 10 || i == 27 || i == 33 || i == 34 || i == 36 || i == 35 || i == 37 || i == 39 || i == 38 || i == 40;
    }

    protected boolean isSelectAllKey( final KeyEvent keyevent )
    {
        return keyevent.isControlDown() && keyevent.getKeyCode() == 65;
    }

    protected boolean isIncrementalSelectKey( final KeyEvent keyevent )
    {
        return keyevent.isControlDown();
    }

    public Color getMismatchForeground()
    {
        if ( mismatchForeground == null )
        {
            return Color.RED;
        }
        else
        {
            return mismatchForeground;
        }
    }

    public void setMismatchForeground( final Color color )
    {
        mismatchForeground = color;
    }

    public boolean isCaseSensitive()
    {
        return caseSensitive;
    }

    public void setCaseSensitive( final boolean flag )
    {
        caseSensitive = flag;
    }

    public Color getForeground()
    {
        if ( foreground == null )
        {
            return UIManager.getColor( "ToolTip.foreground" );
        }
        else
        {
            return foreground;
        }
    }

    public void setForeground( final Color color )
    {
        foreground = color;
    }

    public Color getBackground()
    {
        if ( background == null )
        {
            return UIManager.getColor( "ToolTip.background" );
        }
        else
        {
            return background;
        }
    }

    public void setBackground( final Color color )
    {
        background = color;
    }

    public String getSearchLabel()
    {
        return searchLabel;
    }

    public void setSearchLabel( final String s )
    {
        searchLabel = s;
    }

    public void addSearchableListener( final SearchableListener searchablelistener )
    {
        listenerList.add( SearchableListener.class, searchablelistener );
    }

    public void removeSearchableListener( final SearchableListener searchablelistener )
    {
        listenerList.remove( SearchableListener.class, searchablelistener );
    }

    public SearchableListener[] getSearchableListeners()
    {
        return listenerList.getListeners( SearchableListener.class );
    }

    protected void fireSearchableEvent( final SearchableEvent searchableevent )
    {
        final Object[] aobj = listenerList.getListenerList();
        for ( int i = aobj.length - 2; i >= 0; i -= 2 )
        {
            if ( aobj[i] == ( SearchableListener.class ) )
            {
                ( (SearchableListener) aobj[i + 1] ).searchableEventFired( searchableevent );
            }
        }

    }
}