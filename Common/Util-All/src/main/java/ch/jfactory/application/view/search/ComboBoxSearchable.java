// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 09.06.2005 09:03:31
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) fieldsfirst nonlb

package ch.jfactory.application.view.search;

import java.awt.IllegalComponentStateException;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

// Referenced classes of package com.jidesoft.swing:
//            Searchable

public class ComboBoxSearchable extends Searchable
        implements ListDataListener {

    public ComboBoxSearchable(final JComboBox jcombobox) {
        super(jcombobox);
        jcombobox.getModel().addListDataListener(this);
    }

    public void uninstallListeners() {
        super.uninstallListeners();
        if (component instanceof JComboBox)
            ((JComboBox) component).getModel().removeListDataListener(this);
    }

    protected void setSelectedIndex(final int i, final boolean flag) {
        try {
            ((JComboBox) component).showPopup();
        } catch (IllegalComponentStateException illegalcomponentstateexception) {
        }
        ((JComboBox) component).setSelectedIndex(i);
    }

    protected int getSelectedIndex() {
        return ((JComboBox) component).getSelectedIndex();
    }

    protected Object getElementAt(final int i) {
        final ComboBoxModel comboboxmodel = ((JComboBox) component).getModel();
        return comboboxmodel.getElementAt(i);
    }

    protected int getElementCount() {
        final ComboBoxModel comboboxmodel = ((JComboBox) component).getModel();
        return comboboxmodel.getSize();
    }

    protected String convertElementToString(final Object obj) {
        if (obj != null)
            return obj.toString();
        else
            return "";
    }

    public void contentsChanged(final ListDataEvent listdataevent) {
        if (listdataevent.getIndex0() != -1 || listdataevent.getIndex1() != -1)
            hidePopup();
    }

    public void intervalAdded(final ListDataEvent listdataevent) {
        hidePopup();
    }

    public void intervalRemoved(final ListDataEvent listdataevent) {
        hidePopup();
    }
}