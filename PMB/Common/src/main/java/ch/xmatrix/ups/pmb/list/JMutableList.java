package ch.xmatrix.ups.pmb.list;

import java.applet.Applet;
import java.awt.Component;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.EventObject;
import javax.swing.AbstractAction;
import javax.swing.CellEditor;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.KeyStroke;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;

/**
 * // TODO: Comment
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2008/01/23 22:18:50 $
 */
public class JMutableList extends JList implements CellEditorListener {
    protected Component editorComp = null;
    protected int editingIndex = -1;
    protected ListCellEditor editor = null;
    private PropertyChangeListener editorRemover = null;

    public JMutableList() {
        super(new Object[]{"<no data>"});
        init();
    }

    public JMutableList(final ListModel dataModel) {
        super(dataModel);
        init();
    }

    private void init() {
        getActionMap().put("startEditing", new StartEditingAction());
        getActionMap().put("cancel", new CancelEditingAction());
        addMouseListener(new MouseListener());
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0), "startEditing");
        getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "cancel");
        putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
    }

    public void setListCellEditor(final ListCellEditor editor) {
        this.editor = editor;
    }

    public ListCellEditor getListCellEditor() {
        return editor;
    }

    public boolean isEditing() {
        return (editorComp != null);
    }

    public Component getEditorComponent() {
        return editorComp;
    }

    public int getEditingIndex() {
        return editingIndex;
    }

    public Component prepareEditor(final int index) {
        final Object value = getModel().getElementAt(index);
        final boolean isSelected = isSelectedIndex(index);
        final Component comp = editor.getListCellEditorComponent(this, value, isSelected, index);
        if (comp instanceof JComponent) {
            final JComponent jComp = (JComponent) comp;
            if (jComp.getNextFocusableComponent() == null) {
                jComp.setNextFocusableComponent(this);
            }
        }
        return comp;
    }

    public void removeEditor() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().
                removePropertyChangeListener("permanentFocusOwner", editorRemover);
        editorRemover = null;

        if (editor != null) {
            editor.removeCellEditorListener(this);

            if (editorComp != null) {
                remove(editorComp);
            }

            final Rectangle cellRect = getCellBounds(editingIndex, editingIndex);

            editingIndex = -1;
            editorComp = null;

            repaint(cellRect);
        }
    }

    public boolean editCellAt(final int index, final EventObject e) {
        if (editor != null && !editor.stopCellEditing())
            return false;

        if (index < 0 || index >= getModel().getSize())
            return false;

        if (!isCellEditable(index))
            return false;

        if (editorRemover == null) {
            final KeyboardFocusManager fm = KeyboardFocusManager.getCurrentKeyboardFocusManager();
            editorRemover = new CellEditorRemover(fm);
            fm.addPropertyChangeListener("permanentFocusOwner", editorRemover);
        }

        if (editor != null && editor.isCellEditable(e)) {
            editorComp = prepareEditor(index);
            if (editorComp == null) {
                removeEditor();
                return false;
            }
            editorComp.setBounds(getCellBounds(index, index));
            add(editorComp);
            editorComp.validate();

            editingIndex = index;
            editor.addCellEditorListener(this);

            return true;
        }
        return false;
    }

    public void removeNotify() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().
                removePropertyChangeListener("permanentFocusOwner", editorRemover);
        super.removeNotify();
    }

    // This class tracks changes in the keyboard focus state. It is used
    // when the XList is editing to determine when to cancel the edit.
    // If focus switches to a component outside of the XList, but in the
    // same window, this will cancel editing.
    class CellEditorRemover implements PropertyChangeListener {
        KeyboardFocusManager focusManager;

        public CellEditorRemover(final KeyboardFocusManager fm) {
            this.focusManager = fm;
        }

        public void propertyChange(final PropertyChangeEvent ev) {
            if (!isEditing() || getClientProperty("terminateEditOnFocusLost") != Boolean.TRUE) {
                return;
            }

            Component c = focusManager.getPermanentFocusOwner();
            while (c != null) {
                if (c == JMutableList.this) {
                    // focus remains inside the table
                    return;
                } else if ((c instanceof Window) ||
                        (c instanceof Applet && c.getParent() == null)) {
                    if (c == SwingUtilities.getRoot(JMutableList.this)) {
                        if (!getListCellEditor().stopCellEditing()) {
                            getListCellEditor().cancelCellEditing();
                        }
                    }
                    break;
                }
                c = c.getParent();
            }
        }
    }

    /*-------------------------------------------------[ Model Support ]---------------------------------------------------*/

    public boolean isCellEditable(final int index) {
        return getModel() instanceof MutableListModel && ((MutableListModel) getModel()).isCellEditable(index);
    }

    public void setValueAt(final Object value, final int index) {
        ((MutableListModel) getModel()).setValueAt(value, index);
    }

    /*-------------------------------------------------[ CellEditorListener ]---------------------------------------------------*/

    public void editingStopped(final ChangeEvent e) {
        if (editor != null) {
            final Object value = editor.getCellEditorValue();
            setValueAt(value, editingIndex);
            removeEditor();
        }
    }

    public void editingCanceled(final ChangeEvent e) {
        removeEditor();
    }

    /*-------------------------------------------------[ Editing Actions]---------------------------------------------------*/

    private static class StartEditingAction extends AbstractAction {
        public void actionPerformed(final ActionEvent e) {
            final JMutableList list = (JMutableList) e.getSource();
            if (!list.hasFocus()) {
                final CellEditor cellEditor = list.getListCellEditor();
                if (cellEditor != null && !cellEditor.stopCellEditing()) {
                    return;
                }
                list.requestFocus();
                return;
            }
            final ListSelectionModel rsm = list.getSelectionModel();
            final int anchorRow = rsm.getAnchorSelectionIndex();
            list.editCellAt(anchorRow, null);
            final Component editorComp = list.getEditorComponent();
            if (editorComp != null) {
                editorComp.requestFocus();
            }
        }
    }

    private class CancelEditingAction extends AbstractAction {
        public void actionPerformed(final ActionEvent e) {
            final JMutableList list = (JMutableList) e.getSource();
            list.removeEditor();
        }

        public boolean isEnabled() {
            return isEditing();
        }
    }

    private class MouseListener extends MouseAdapter {
        private Component dispatchComponent;

        private void setDispatchComponent(final MouseEvent e) {
            final Component editorComponent = getEditorComponent();
            final Point p = e.getPoint();
            final Point p2 = SwingUtilities.convertPoint(JMutableList.this, p, editorComponent);
            dispatchComponent = SwingUtilities.getDeepestComponentAt(editorComponent,
                    p2.x, p2.y);
        }

        private boolean repostEvent(final MouseEvent e) {
            // Check for isEditing() in case another event has
            // caused the editor to be removed. See bug #4306499.
            if (dispatchComponent == null || !isEditing()) {
                return false;
            }
            final MouseEvent e2 = SwingUtilities.convertMouseEvent(JMutableList.this, e, dispatchComponent);
            dispatchComponent.dispatchEvent(e2);
            return true;
        }

        private boolean shouldIgnore(final MouseEvent e) {
            return e.isConsumed() || (!(SwingUtilities.isLeftMouseButton(e) && isEnabled()));
        }

        public void mousePressed(final MouseEvent e) {
            if (shouldIgnore(e))
                return;
            final Point p = e.getPoint();
            final int index = locationToIndex(p);
            // The autoscroller can generate drag events outside the Table's range.
            if (index == -1)
                return;

            if (e.getClickCount() == 2) {
                if (editCellAt(index, e)) {
                    setDispatchComponent(e);
                    repostEvent(e);
                } else if (isRequestFocusEnabled())
                    requestFocus();
            }
        }
    }
}