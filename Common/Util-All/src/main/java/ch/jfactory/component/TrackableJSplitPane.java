/*
 * Copyright x-matrix Switzerland (c) 2002
 *
 * TrackableJSplitPane.java
 *
 * Created on Jan 24, 2003 2:36:39 PM
 * Created by Daniel
 */
package ch.jfactory.component;

import ch.jfactory.component.split.NiceSplitPane;
import java.awt.Component;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JSplitPane;

/**
 * This split pane notifies registert DividerListener instances of changes in the splitter location.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.2 $ $Date: 2006/03/14 21:27:55 $
 */
public class TrackableJSplitPane extends NiceSplitPane
{
    private boolean added = false;

    private final Set<DividerListener> listeners = new HashSet<DividerListener>();

    public TrackableJSplitPane( final int newOrientation, final Component newLeftComponent, final Component newRightComponent )
    {
        super( newOrientation, newLeftComponent, newRightComponent );
    }

    protected void addImpl( final Component comp, final Object constraints, final int index )
    {
        super.addImpl( comp, constraints, index );
        if ( !added )
        {
            comp.addComponentListener( new ComponentAdapter()
            {
                public void componentResized( final ComponentEvent e )
                {
                    super.componentResized( e );
                    fireDividerChangedEvent( new DividerChangeEvent( TrackableJSplitPane.this ) );
                }
            } );
            added = true;
        }
    }

    private void fireDividerChangedEvent( final DividerChangeEvent event )
    {
        for ( final Object listener1 : listeners )
        {
            final DividerListener listener = (DividerListener) listener1;
            listener.dividerMoved( event );
        }
    }

    public void addDividerListener( final DividerListener listener )
    {
        listeners.add( listener );
    }

    public class DividerChangeEvent
    {
        private JSplitPane who = null;

        public DividerChangeEvent( final JSplitPane who )
        {
            this.who = who;
        }

        public JSplitPane getSource()
        {
            return who;
        }
    }

    public interface DividerListener
    {
        public void dividerMoved( DividerChangeEvent e );
    }
}