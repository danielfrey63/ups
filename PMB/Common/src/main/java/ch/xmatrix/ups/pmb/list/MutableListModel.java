package ch.xmatrix.ups.pmb.list;

import javax.swing.ListModel;

/**
 * // TODO: Comment
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2008/01/23 22:18:50 $
 */
// @author Santhosh Kumar T - santhosh@in.fiorano.com
public interface MutableListModel extends ListModel
{
    public boolean isCellEditable( int index );

    public void setValueAt( Object value, int index );
}