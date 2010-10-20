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
    public static int[] remove( final int[] array, final int indexToRemove )
    {
        final int[] newArray = new int[array.length - 1];
        int diff = 0;
        for ( int i = 0; i < array.length; i++ )
        {
            final int value = array[i];
            if ( i != indexToRemove )
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

    public static <T> T[] remove( final T[] array, final T objectToRemove, final T[] type )
    {
        final List<T> list = new ArrayList<T>( Arrays.asList( array ) );
        list.remove( objectToRemove );
        return list.toArray( type );
    }

    public static <T> T[] remove( final T[] array, final int index, final T[] type )
    {
        final List<T> list = new ArrayList<T>( Arrays.asList( array ) );
        list.remove( index );
        return list.toArray( type );
    }

    public static <T> T[] remove( final T[] array, final int[] indexes, final T[] type )
    {
        final List<T> list = new ArrayList<T>( Arrays.asList( array ) );
        final List<T> copy = new ArrayList<T>( list );
        for ( final int index : indexes )
        {
            list.remove( copy.get( index ) );
        }
        return list.toArray( type );
    }

    public static boolean haveCommon( final Object[] group1, final Object[] group2 )
    {
        for ( final Object o1 : group1 )
        {
            for ( final Object o2 : group2 )
            {
                if ( o1 == o2 )
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
     * @param type   the type of the resulting array
     * @return the elements of group1 (and in its order) which occur in group2
     */
    public static <T> T[] intersect( final T[] group1, final T[] group2, final T[] type )
    {
        final List<T> list = new ArrayList<T>();
        for ( final T o1 : group1 )
        {
            for ( final T o2 : group2 )
            {
                if ( o1 == o2 )
                {
                    list.add( o1 );
                }
            }
        }
        return list.toArray( type );
    }

    public static int[] insert( final int[] array, final int newInt, final int pos )
    {
        final int length = array.length;
        final int[] newIntegers = new int[length + 1];
        int diff = 0;
        for ( int i = 0; i < length; i++ )
        {
            if ( i == pos )
            {
                diff = 1;
            }
            newIntegers[i + diff] = array[i];
        }
        newIntegers[pos] = newInt;
        return newIntegers;
    }

    /**
     * Inserts the given object at the given position. If the position is negative, the insertion point index is
     * calculated from the end of the array. I.e. an insertion position of -1 will insert the object at the end of the
     * array.
     *
     * @param array  the array to insert the object to
     * @param object the object to insert
     * @param pos    the position to insert at
     * @param type   an instance of the array type to return
     * @return the inserted array
     */
    public static <T> T[] insert( final T[] array, final T object, final int pos, final T[] type )
    {
        final int index = ( pos < 0 ? array.length + 1 + pos : pos );
        final List<T> list = new ArrayList<T>( Arrays.asList( array ) );
        list.add( index, object );
        return list.toArray( type );
    }

    public static <T> T[] removeAll( final T[] all, final T[] toRemove, final T[] type )
    {
        final List<T> list = new ArrayList<T>( Arrays.asList( all ) );
        for ( final T remove : toRemove )
        {
            list.remove( remove );
        }
        return list.toArray( type );
    }

    public static <T> T[] unite( final T[] group1, final T[] group2, final T[] type, final Comparator<T> comparator )
    {
        final Set<T> set = new HashSet<T>( Arrays.asList( group1 ) );
        set.addAll( Arrays.asList( group2 ) );
        final T[] result = set.toArray( type );
        Arrays.sort( result, comparator );
        return result;
    }

    /**
     * Adds the given diff to each int value in the array
     *
     * @param integers the array of integers
     * @param diff     the difference to add
     * @return the same array with changed content
     */
    public static int[] shift( final int[] integers, final int diff )
    {
        for ( int i = 0; i < integers.length; i++ )
        {
            integers[i] += diff;
        }
        return integers;
    }

    public static boolean isIn( final Object[] list, final Object key )
    {
        if ( list == null )
        {
            return false;
        }
        for ( final Object object : list )
        {
            if ( object == key )
            {
                return true;
            }
        }
        return false;
    }
}
