package ch.jfactory.model.graph;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:58 $
 */
public interface RoleFactory {

    public GraphNode getRole(Class role);

    public GraphNode getRole(String type);
}
