package ch.jfactory.model.graph;

import java.util.Comparator;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:58 $
 */
public class GraphNodeRankComparator implements Comparator {

    /**
     * @see java.util.Comparator#compare(Object, Object)
     */
    public int compare(final Object o1, final Object o2) {
        final GraphNode n1 = (GraphNode) o1;
        final GraphNode n2 = (GraphNode) o2;
        return new Integer(n1.getRank()).compareTo(new Integer(n2.getRank()));
    }

}
