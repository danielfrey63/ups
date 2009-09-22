/*
 * Copyright (c) 2008, Your Corporation. All Rights Reserved.
 */

package ch.xmatrix.ups.pmb.exam.filter;

import javax.swing.AbstractListModel;
import javax.swing.ListModel;

/**
 * Filters a list model.
 *
 * @author Daniel Frey 06.08.2008 15:03:58
 */
public class FilteredListModel<T> extends AbstractListModel {

    private final ListModel delegate;

    private int[] indexes = new int[0];

    public FilteredListModel(final Validator<T> validator, final ListModel delegate) {
        this.delegate = delegate;
        setFilter(validator);
    }

    public int getSize() {
        return indexes.length;
    }

    public Object getElementAt(final int index) {
        return delegate.getElementAt(indexes[index]);
    }

    public void setFilter(final Validator<T> validator) {

        final int oldSize = indexes.length;
        indexes = new int[0];
        fireIntervalRemoved(this, 0, oldSize);

        final int[] temp = new int[delegate.getSize()];
        int counter = 0;
        for (int i = 0; i < delegate.getSize(); i++) {
            if (validator == null || validator.isValid((T) delegate.getElementAt(i))) {
                temp[counter++] = i;
            }
        }
        indexes = new int[counter];
        System.arraycopy(temp, 0, indexes, 0, counter);
        fireIntervalAdded(this, 0, indexes.length);
        fireContentsChanged(this, 0, Math.max(indexes.length, oldSize));
    }
}
