package ch.jfactory.lang;

import java.text.Collator;
import java.util.Comparator;

/**
 * Invokes the toString() method and compares the resulting strings.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.2 $ $Date: 2006/03/14 21:27:55 $
 */
public class ToStringComparator<T> implements Comparator<T>
{
    /** @see Comparator#compare(Object, Object) */
    public int compare( final T o1, final T o2 )
    {
        return Collator.getInstance().compare( o1.toString(), o2.toString() );
    }
}
