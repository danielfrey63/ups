package ch.jfactory.model.graph;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.2 $ $Date: 2006/03/14 21:27:55 $
 */
public abstract class AbsSimplePersistentGraphNode extends AbsSimpleGraphNode
{

    private static final GraphModel MODEL = AbsGraphModel.getModel();

    /** @see ch.jfactory.model.graph.GraphNode#setName(String) */
    public void setName(final String name)
    {
        super.setName(name);
        MODEL.addChanged(this);
    }

    /** @see ch.jfactory.model.graph.GraphNode#setRank(int) */
    public void setRank(final int rank)
    {
        super.setRank(rank);
        MODEL.addChanged(this);
    }

}
