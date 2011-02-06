package net.java.jveez.utils;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import net.java.jveez.vfs.SimplePicture;

/**
 * // TODO: Comment
 *
 * @author Daniel Frey 06.02.11 14:43
 */
public enum SimplePictureSortingAlgorithm implements SortingAlgorithm<SimplePicture>
{
    ByName
            {
                public void sort( final List<? extends SimplePicture> list )
                {
                    Collections.sort( list, new Comparator<SimplePicture>()
                    {
                        public int compare( final SimplePicture p1, final SimplePicture p2 )
                        {
                            return p1.getName().compareTo( p2.getName() );
                        }
                    } );
                }
            };

    public abstract void sort( List<? extends SimplePicture> list );

    public static void sort( final File[] files )
    {
        Arrays.sort( files );
    }
}
