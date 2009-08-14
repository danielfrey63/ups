package ch.jfactory.lang;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.4 $ $Date: 2008/01/06 10:16:23 $
 */
public class ArrayUtils extends org.apache.commons.lang.ArrayUtils
{

    public static int[] remove(final int[] array, final int indexToRemove)
    {
        final int[] newArray = new int[array.length - 1];
        int diff = 0;
        for (int i = 0; i < array.length; i++)
        {
            final int value = array[i];
            if (i != indexToRemove)
            {
                newArray[i + diff] = value;
            }
            else
            {
                diff = -1;
            }
        }
        return newArray;
    }

    public static Object[] remove(final Object[] array, final Object objectToRemove, final Object[] type)
    {
        final List list = new ArrayList(Arrays.asList(array));
        list.remove(objectToRemove);
        return list.toArray(type);
    }

    public static Object[] remove(final Object[] array, final int index, final Object[] type)
    {
        final List list = new ArrayList(Arrays.asList(array));
        list.remove(index);
        return list.toArray(type);
    }

    public static Object[] remove(final Object[] array, final int[] indices, final Object[] type)
    {
        final List list = new ArrayList(Arrays.asList(array));
        final List copy = new ArrayList(list);
        for (final int index : indices)
        {
            list.remove(copy.get(index));
        }
        return list.toArray(type);
    }

    public static boolean haveCommon(final Object[] group1, final Object[] group2)
    {
        for (final Object o1 : group1)
        {
            for (final Object o2 : group2)
            {
                if (o1 == o2)
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Makes an intersection between the two groups.
     *
     * @param group1 first group
     * @param group2 second group
     * @param type   the type to convert the result to
     * @return the elements of group1 (and in its order) which occur in group2
     */
    public static Object[] intersect(final Object[] group1, final Object[] group2, final Object[] type)
    {
        final List list = new ArrayList();
        for (final Object o1 : group1)
        {
            for (final Object o2 : group2)
            {
                if (o1 == o2)
                {
                    list.add(o1);
                }
            }
        }
        return list.toArray(type);
    }

    /**
     * Makes an intersection between the two groups.
     *
     * @param group1 first group
     * @param group2 second group
     * @return the elements of group1 (and in its order) which occur in group2
     */
    public static Object[] intersect(final Object[] group1, final Object[] group2)
    {
        return intersect(group1, group2, new Object[0]);
    }

    public static int[] insert(final int[] array, final int newInt, final int pos)
    {
        final int length = array.length;
        final int[] newInts = new int[length + 1];
        int diff = 0;
        for (int i = 0; i < length; i++)
        {
            if (i == pos)
            {
                diff = 1;
            }
            newInts[i + diff] = array[i];
        }
        newInts[pos] = newInt;
        return newInts;
    }

    /**
     * Inserts the given object at the given position. If the position is negativ, the insertion point index is
     * calculated from the end of the array. I.e. an insertion position of -1 will insert the object at the end of the
     * array.
     *
     * @param array  the array to insert the object to
     * @param object the object to insert
     * @param pos    the position to insert at
     * @param type   an instance of the array type to return
     * @return
     */
    public static Object[] insert(final Object[] array, final Object object, final int pos, final Object[] type)
    {
        final int index = (pos < 0 ? array.length + 1 + pos : pos);
        final List list = new ArrayList(Arrays.asList(array));
        list.add(index, object);
        return list.toArray(type);
    }

    public static Object[] removeAll(final Object[] all, final Object[] toRemove, final Object[] type)
    {
        final List list = new ArrayList(Arrays.asList(all));
        for (final Object remove : toRemove)
        {
            list.remove(remove);
        }
        return list.toArray(type);
    }

    public static Object[] unite(final Object[] group1, final Object[] group2, final Object[] type, final Comparator comparator)
    {
        final Set set = new HashSet(Arrays.asList(group1));
        set.addAll(Arrays.asList(group2));
        final Object[] result = set.toArray(type);
        Arrays.sort(result, comparator);
        return result;
    }

    /**
     * Adds the given diff to each int value in the array
     *
     * @param ints the array of ints
     * @param diff the difference to add
     * @return the same array with changed content
     */
    public static int[] shift(final int[] ints, final int diff)
    {
        for (int i = 0; i < ints.length; i++)
        {
            ints[i] += diff;
        }
        return ints;
    }

    public static boolean isIn(final Object[] list, final Object key)
    {
        if (list == null)
        {
            return false;
        }
        for (final Object object : list)
        {
            if (object == key)
            {
                return true;
            }
        }
        return false;
    }
}
