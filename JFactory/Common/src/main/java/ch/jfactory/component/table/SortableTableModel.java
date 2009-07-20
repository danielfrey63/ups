package ch.jfactory.component.table;

import ch.jfactory.lang.ArrayUtils;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

/**
 * Wrapper for a TableModel which holds indices of sorted rows.
 *
 * @author Nobuo Tamemasa, $Author: daniel_frey $
 * @version $Revision: 1.4 $ $Date: 2007/09/27 10:41:47 $
 */
public class SortableTableModel extends AbstractTableModel {

    private final PropertyChangeListener sortStateListener = new SortStateListener();

    private int[] indexes;
    private TableSorter sorter;
    private TableModel tableModel;
    private SortState sortState;

    public SortableTableModel(final TableModel tableModel) {
        this.tableModel = tableModel;
        tableModel.addTableModelListener(new TableModelListener() {
            public void tableChanged(final TableModelEvent e) {
                if (e.getType() == TableModelEvent.DELETE) {
                    removeIndexes(e.getFirstRow(), e.getLastRow());
                }
                else if (e.getType() == TableModelEvent.INSERT) {
                    insertIndexes(e.getFirstRow(), e.getLastRow());
                }
            }
        });
        setSortState(new SortState());
    }

    private void removeIndexes(final int firstRow, final int lastRow) {
        for (int i = firstRow; i <= lastRow; i++) {
            for (int index = 0; index < indexes.length; index++) {
                if (indexes[ index ] == i) {
                    indexes = ArrayUtils.remove(indexes, index);
                    index--;
                }
                else if (indexes[ index ] > i) {
                    indexes[ index ] -= 1;
                }
            }
        }
        fireTableRowsDeleted(firstRow, lastRow);
    }

    private void insertIndexes(final int firstRow, final int lastRow) {
        final int[] newIndexes = new int[lastRow - firstRow + 1];
        for (int i = firstRow; i <= lastRow; i++) {
            newIndexes[ i - firstRow ] = i;
        }
        indexes = ArrayUtils.addAll(indexes, newIndexes);
        fireTableRowsInserted(firstRow, lastRow);
    }

    private void sort(final int column, final SortState.State state) {
        if (sorter == null) {
            sorter = new TableSorter(this);
        }
        sorter.sort(column, state);
        fireTableDataChanged();
    }

    public String getColumnName(final int column) {
        return tableModel.getColumnName(column);
    }

    public Class getColumnClass(final int columnIndex) {
        return tableModel.getColumnClass(columnIndex);
    }

    public int getColumnCount() {
        return tableModel.getColumnCount();
    }

    public int getRowCount() {
        return tableModel.getRowCount();
    }

    public Object getValueAt(final int row, final int col) {
        int rowIndex = row;
        if (indexes != null) {
            rowIndex = indexes[ row ];
        }
        return tableModel.getValueAt(rowIndex, col);
    }

    public boolean isCellEditable(final int rowIndex, final int columnIndex) {
        return tableModel.isCellEditable(rowIndex, columnIndex);
    }

    public void setValueAt(final Object value, final int row, final int col) {
        int rowIndex = row;
        if (indexes != null) {
            rowIndex = indexes[ row ];
        }
        tableModel.setValueAt(value, rowIndex, col);
    }

    /**
     * Moves the values at the given positions by one position up. No action is taken if the positions contain already
     * the topmost index (0).
     *
     * @param indices the positions array
     * @return the same array but with the new positions
     */
    public int[] moveUp(final int[] indices) {
        if (ArrayUtils.contains(indices, 0)) {
            return indices;
        }
        return move(indices, -1);
    }

    /**
     * Moves the values at the given positions by one position down. No action is taken if the positions contain already
     * the last index (length of the internal index table).
     *
     * @param indices the positions array
     * @return the same array but with the new positions
     */
    public int[] moveDown(final int[] indices) {
        if (ArrayUtils.contains(indices, getRowCount() - 1)) {
            return indices;
        }
        return move(indices, 1);
    }

    /**
     * Shifts the given indices by the given delta. Swaps each value at each index with the value at each index plus
     * delta.
     *
     * @param indices the indices to shift
     * @param delta   the offset to shift
     * @return the indices at the shifted position, i.e. each index of the array incremented by delta
     */
    public int[] move(final int[] indices, final int delta) {
        getIndexes();
        final int inc = delta / Math.abs(delta);
        final boolean up = inc == -1;
        for (int i = (up ? 0 : indices.length - 1); (up ? i < indices.length : i >= 0); i -= inc) {
            final int pos = indices[ i ];
            for (int j = 0; j < Math.abs(delta); j++) {
                final int temp = indexes[ pos ];
                indexes[ pos ] = indexes[ pos + inc ];
                indexes[ pos + inc ] = temp;
            }
        }
        fireTableDataChanged();
        return ArrayUtils.shift(indices, delta);
    }

    /**
     * Initializes the indexes table.
     *
     * @return the indexes table
     */
    public int[] getIndexes() {
        final int n = getRowCount();
        if (indexes != null) {
            if (indexes.length == n) {
                return indexes;
            }
        }
        indexes = new int[n];
        for (int i = 0; i < n; i++) {
            indexes[ i ] = i;
        }
        return indexes;
    }

    public SortState getSortState() {
        return sortState;
    }

    public void setSortState(final SortState sortState) {
        if (this.sortState != null) {
            this.sortState.removePropertyChangeListener(sortStateListener);
        }
        this.sortState = sortState;
        sortState.addPropertyChangeListener(SortState.PROPERTYNAME_DIRECTIVE, sortStateListener);

    }

    private class SortStateListener implements PropertyChangeListener {

        public void propertyChange(final PropertyChangeEvent evt) {
            final SortState.Directive directive = (SortState.Directive) evt.getNewValue();
            sort(directive.getColumn(), directive.getState());
        }
    }


}
