package ch.jfactory.lang;

import java.text.Collator;
import java.util.Comparator;

/**
 * Invokes the toString() method and compares the resulting strings.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.2 $ $Date: 2006/03/14 21:27:55 $
 */
public class ToStringComparator implements Comparator
{

    /** @see java.util.Comparator#compare(Object, Object) */
    public int compare(final Object o1, final Object o2)
    {
        return Collator.getInstance().compare(o1.toString(), o2.toString());
    }
}
