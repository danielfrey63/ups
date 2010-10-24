package ch.jfactory.model.graph;

import java.util.Comparator;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:58 $
 */
public class GraphNodeRankComparator implements Comparator<GraphNode>
{
    /** @see Comparator#compare(Object, Object) */
    public int compare( final GraphNode o1, final GraphNode o2 )
    {
        return new Integer( o1.getRank() ).compareTo( o2.getRank() );
    }
}
