package ch.xmatrix.ups.pmb.list;

import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JTextField;

/**
 * // TODO: Comment
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2008/01/23 22:18:50 $
 */
// @author Santhosh Kumar T - santhosh@in.fiorano.com
public class DefaultListCellEditor extends DefaultCellEditor implements ListCellEditor{
    public DefaultListCellEditor(final JCheckBox checkBox){
        super(checkBox);
    }

    public DefaultListCellEditor(final JComboBox comboBox){
        super(comboBox);
    }

    public DefaultListCellEditor(final JTextField textField){
        super(textField);
    }

    public Component getListCellEditorComponent(final JList list, final Object value, final boolean isSelected, final int index){
        delegate.setValue(value);
        return editorComponent;
    }
}