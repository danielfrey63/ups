package ch.xmatrix.ups.pmb.list;

import java.awt.Component;
import javax.swing.CellEditor;
import javax.swing.JList;

/**
 * // TODO: Comment
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2008/01/23 22:18:50 $
 */
// @author Santhosh Kumar T - santhosh@in.fiorano.com
public interface ListCellEditor extends CellEditor
{
    Component getListCellEditorComponent( JList list, Object value, boolean isSelected, int index );
}