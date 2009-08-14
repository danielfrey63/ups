package ch.jfactory.component.tree;

/**
 * A simple visitor interface for tree handling.
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2005/11/17 11:54:58 $
 */
public interface TreeVisitor {

    Object handle(Object object);

    void add(Object parent, Object child);
}
