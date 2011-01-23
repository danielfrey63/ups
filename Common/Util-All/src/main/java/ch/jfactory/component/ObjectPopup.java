package ch.jfactory.component;

import ch.jfactory.collection.cursor.Cursor;
import ch.jfactory.collection.cursor.DefaultCursor;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPopupMenu;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ObjectPopup<T> extends JPopupMenu implements ActionListener
{
    private static final Logger LOGGER = LoggerFactory.getLogger( ObjectPopup.class );

    public ObjectPopup( final String title, final T[] objects )
    {
        super( title );
        setCursor( new DefaultCursor<T>( objects ) );
    }

    public ObjectPopup( final T[] objects )
    {
        this( null, objects );
    }

    public void setObjects( final T[] objects )
    {
        setCursor( new DefaultCursor<T>( objects ) );
    }

    public void showPopUp( final Component jb )
    {
        showPopUp( jb, null, null );
    }

    public void showPopUp( final Component jb, final T selected )
    {
        showPopUp( jb, null, selected );
    }

    @SuppressWarnings( "unchecked" )
    public void showPopUp( final Component jb, final T[] enabled, final T selected )
    {
        try
        {
            final int iWidthJb = jb.getSize().width;
            this.setPreferredSize( null );
            final int iWidthMe = this.getPreferredSize().width;
            final int iHeight = this.getPreferredSize().height;
            if ( iWidthJb >= iWidthMe )
            {
                this.setPreferredSize( new Dimension( iWidthJb, iHeight ) );
            }
            else
            {
                this.setPreferredSize( null );
            }
            int iLevel = -1;
            for ( int i = getComponentCount() - 1; i >= 0; i-- )
            {
                final ObjectMenuItem<T> jmi = (ObjectMenuItem<T>) getComponent( i );
                final T object = jmi.getObject();
                if ( enabled != null )
                {
                    jmi.setEnabled( ArrayUtils.contains( enabled, object ) && object != selected );
                }
                if ( object == selected )
                {
                    jmi.setEnabled( false );
                }
                if ( selected == object )
                {
                    iLevel = i;
                }
            }
            final int iBHeight = jb.getBounds().height;
            final int iJbOnScreen = jb.getLocationOnScreen().y;
            if ( iLevel < 0 )
            {
                iLevel = 0;
            }
            Component c = getComponent( iLevel );
            // to be able to calculate the display point correctly even the
            // first time when bounds have not yet been set, I take for the
            // height the preferred size instead of the bounds.
            int iCHeight = c.getPreferredSize().height;
            final int iMin = Math.min( iLevel, iJbOnScreen / iCHeight );
            c = getComponent( iMin );
            iCHeight = c.getPreferredSize().height;
            show( jb, 0, ( iBHeight - iCHeight ) / 2 - c.getBounds().y );
        }
        catch ( Exception p_ex )
        {
            LOGGER.error( "error in object pop-up", p_ex );
        }
    }

    @SuppressWarnings( "unchecked" )
    public void actionPerformed( final ActionEvent e )
    {
        final ObjectMenuItem<T> jmi = (ObjectMenuItem<T>) e.getSource();
        itemSelected( jmi.getObject() );
    }

    private void setCursor( final Cursor<T> cursor )
    {
        this.removeAll();
        if ( !cursor.isEmpty() )
        {
            addItem( cursor.getCurrent() );
        }
        while ( cursor.hasNext() )
        {
            addItem( cursor.next() );
        }
    }

    private void addItem( final T obj )
    {
        final ObjectMenuItem<T> jmi = new ObjectMenuItem<T>( obj );
        jmi.addActionListener( this );
        super.add( jmi );
    }

    abstract public void itemSelected( T obj );
}


