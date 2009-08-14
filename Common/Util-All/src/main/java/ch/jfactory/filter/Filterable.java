package ch.jfactory.filter;

/**
 * Interface to set and get a filter.
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2005/11/17 11:54:58 $
 */
public interface Filterable {

    /**
     * Propertyname for the filter.
     */
    String PROPERTYNAME_FILTER = "filter";

    void setFilter(Filter filter);

    void deleteFilter();

    Filter getFilter();

    void filter();
}
