package ch.jfactory.model.graph;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:58 $
 */
public interface Role extends GraphNode
{
    public static final Role ROLE_NULL = new RoleNull("NULLROLE");

    public static final Object ROLE_ALL = new Object();

    public static final Class CLASSES_ALL = ROLE_ALL.getClass();

    public static final Class CLASSES_NULL = ROLE_NULL.getClass();
}
