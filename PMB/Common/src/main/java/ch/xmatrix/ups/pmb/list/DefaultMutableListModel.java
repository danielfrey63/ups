package ch.xmatrix.ups.pmb.list;

import javax.swing.DefaultListModel;

/**
 * // TODO: Comment
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2008/01/23 22:18:50 $
 */
// @author Santhosh Kumar T - santhosh@in.fiorano.com
public class DefaultMutableListModel extends DefaultListModel implements MutableListModel
{
    public boolean isCellEditable( final int index )
    {
        return true;
    }

    public void setValueAt( final Object value, final int index )
    {
        super.setElementAt( value, index );
    }
}