package ch.jfactory.component.table;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Nobuo Tamemasa, $Author: daniel_frey $
 * @version $Revision: 1.2 $ $Date: 2005/11/17 11:54:58 $
 */
public class TableSorter {

    private SortableTableModel model;
    private Map columnComparators = new HashMap();

    public static final Comparator COMPARABLE_COMAPRATOR = new Comparator() {
        public int compare(final Object o1, final Object o2) {
            return ((Comparable) o1).compareTo(o2);
        }
    };
    public static final Comparator LEXICAL_COMPARATOR = new Comparator() {
        public int compare(final Object o1, final Object o2) {
            return o1.toString().compareTo(o2.toString());
        }
    };

    public TableSorter(final SortableTableModel model) {
        this.model = model;
    }

    // stable bubble sort
    public void sort(final int column, final SortState.State state) {
        final int[] data = model.getIndexes();
        final int n = data.length;
        int temp;
        for (int i = 0; i < n; i++) {
            final boolean noSort = state == SortState.SORT_NONE;
            for (int j = 1; j < n - i && !noSort; j++) {
                final Object o1 = model.getValueAt(j, column);
                final Object o2 = model.getValueAt(j - 1, column);
                if (state == SortState.SORT_ASCENDING && getComparator(column).compare(o1, o2) < 0) {
                    temp = data[j];
                    data[j] = data[j - 1];
                    data[j - 1] = temp;
                }
                else if (state == SortState.SORT_DESCENDING && getComparator(column).compare(o1, o2) > 0) {
                    temp = data[j];
                    data[j] = data[j - 1];
                    data[j - 1] = temp;
                }
            }
            if (noSort) {
                data[i] = i;
            }
        }
    }

    public void setColumnComparator(final Class type, final Comparator comparator) {
        if (comparator == null) {
            columnComparators.remove(type);
        }
        else {
            columnComparators.put(type, comparator);
        }
    }

    private Comparator getComparator(final int column) {
        final Class columnType = model.getColumnClass(column);
        final Comparator comparator = (Comparator) columnComparators.get(columnType);
        if (comparator != null) {
            return comparator;
        }
        if (Comparable.class.isAssignableFrom(columnType)) {
            return COMPARABLE_COMAPRATOR;
        }
        return LEXICAL_COMPARATOR;
    }
}