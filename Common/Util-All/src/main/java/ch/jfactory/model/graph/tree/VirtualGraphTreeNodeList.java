package ch.jfactory.model.graph.tree;

import ch.jfactory.model.graph.GraphNodeList;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:58 $
 */
public class VirtualGraphTreeNodeList extends GraphNodeList
{
    public VirtualGraphTreeNodeList()
    {
    }

    public VirtualGraphTreeNodeList( final VirtualGraphTreeNode element )
    {
        add( element );
    }

    public void add( final VirtualGraphTreeNode element )
    {
        super.add( element );
    }

    public void addAll( final VirtualGraphTreeNodeList elements )
    {
        super.addAll( elements );
    }

    public VirtualGraphTreeNode getTreeNode( final int index )
    {
        return (VirtualGraphTreeNode) super.get( index );
    }

}
