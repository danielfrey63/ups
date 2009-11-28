package ch.jfactory.component.list;

import java.util.Arrays;
import java.util.Collection;
import java.util.Vector;
import javax.swing.JList;
import javax.swing.ListModel;

/**
 * Extends the JList component with multiple value selection.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2005/11/17 11:54:58 $
 */
public class DefaultJList extends JList
{
    public DefaultJList( final ListModel dataModel )
    {
        super( dataModel );
    }

    public DefaultJList( final Object[] listData )
    {
        super( listData );
    }

    public DefaultJList( final Vector listData )
    {
        super( listData );
    }

    public DefaultJList()
    {
    }

    public void setSelectedValues( final Object[] selectedObjects )
    {
        final ListModel model = getModel();
        clearSelection();

        if ( selectedObjects.length > 0 )
        {
            final Collection col = Arrays.asList( selectedObjects );
            int start = -1;
            for ( int i = 0; i < model.getSize(); i++ )
            {
                final Object obj = model.getElementAt( i );
                if ( col.contains( obj ) )
                {
                    if ( start < 0 )
                    {
                        start = i;
                    }
                }
                else
                {
                    if ( start >= 0 )
                    {
                        this.addSelectionInterval( start, i - 1 );
                        start = -1;
                    }
                }
            }
            if ( start >= 0 )
            {
                this.addSelectionInterval( start, model.getSize() - 1 );
                start = -1;
            }
        }
    }
}
