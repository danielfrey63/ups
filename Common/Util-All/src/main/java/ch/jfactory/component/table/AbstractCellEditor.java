/*
 * @(#)AbstractCellEditor.java	1.11 03/12/19
 *
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package ch.jfactory.component.table;

import ch.jfactory.color.ColorUtils;
import java.awt.Color;
import java.io.Serializable;
import java.util.EventObject;
import javax.swing.CellEditor;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.EventListenerList;

/**
 * A base class for <code>CellEditors</code>, providing default implementations for the methods in the
 * <code>CellEditor</code> interface except <code>getCellEditorValue()</code>. Like the other abstract implementations
 * in Swing, also manages a list of listeners.
 *
 * <p> <strong>Warning:</strong> Serialized objects of this class will not be compatible with future Swing releases. The
 * current serialization support is appropriate for short term storage or RMI between applications running the same
 * version of Swing.  As of 1.4, support for long term storage of all JavaBeans<sup><font size="-2">TM</font></sup> has
 * been added to the <code>java.beans</code> package. Please see {@link java.beans.XMLEncoder}.
 *
 * @author Philip Milne
 * @version 1.11 12/19/03
 */

public abstract class AbstractCellEditor implements CellEditor, Serializable {

    protected JTextField editor = new JTextField();
    protected EventListenerList listenerList = new EventListenerList();
    transient protected ChangeEvent changeEvent = null;

    protected AbstractCellEditor(final Color background) {
        editor.setBorder(new EmptyBorder(0, 1, 0, 1));
        editor.setBackground(background);
    }

    protected AbstractCellEditor() {
        this(ColorUtils.fade(Color.red, 0.9));
    }

    /**
     * Returns true.
     *
     * @param e an event object
     * @return true
     */
    public boolean isCellEditable(final EventObject e) {
        return true;
    }

    /**
     * Returns true.
     *
     * @param anEvent an event object
     * @return true
     */
    public boolean shouldSelectCell(final EventObject anEvent) {
        return true;
    }

    /**
     * Calls <code>fireEditingStopped</code> and returns true.
     *
     * @return true
     */
    public boolean stopCellEditing() {
        fireEditingStopped();
        return true;
    }

    /**
     * Calls <code>fireEditingCanceled</code>.
     */
    public void cancelCellEditing() {
        fireEditingCanceled();
    }

    /**
     * Adds a <code>CellEditorListener</code> to the listener list.
     *
     * @param l the new listener to be added
     */
    public void addCellEditorListener(final CellEditorListener l) {
        listenerList.add(CellEditorListener.class, l);
    }

    /**
     * Removes a <code>CellEditorListener</code> from the listener list.
     *
     * @param l the listener to be removed
     */
    public void removeCellEditorListener(final CellEditorListener l) {
        listenerList.remove(CellEditorListener.class, l);
    }

    /**
     * Returns an array of all the <code>CellEditorListener</code>s added to this AbstractCellEditor with
     * addCellEditorListener().
     *
     * @return all of the <code>CellEditorListener</code>s added or an empty array if no listeners have been added
     * @since 1.4
     */
    public CellEditorListener[] getCellEditorListeners() {
        return (CellEditorListener[]) listenerList.getListeners(
                CellEditorListener.class);
    }

    /**
     * Notifies all listeners that have registered interest for notification on this event type.  The event instance is
     * created lazily.
     *
     * @see javax.swing.event.EventListenerList
     */
    protected void fireEditingStopped() {
        // Guaranteed to return a non-null array
        final Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == CellEditorListener.class) {
                // Lazily create the event:
                if (changeEvent == null)
                    changeEvent = new ChangeEvent(this);
                ((CellEditorListener) listeners[i + 1]).editingStopped(changeEvent);
            }
        }
    }

    /**
     * Notifies all listeners that have registered interest for notification on this event type.  The event instance is
     * created lazily.
     *
     * @see javax.swing.event.EventListenerList
     */
    protected void fireEditingCanceled() {
        // Guaranteed to return a non-null array
        final Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == CellEditorListener.class) {
                // Lazily create the event:
                if (changeEvent == null)
                    changeEvent = new ChangeEvent(this);
                ((CellEditorListener) listeners[i + 1]).editingCanceled(changeEvent);
            }
        }
    }
}
