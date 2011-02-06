package ch.jfactory.resource;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import net.java.jveez.utils.SortingAlgorithm;
import net.java.jveez.vfs.SimplePicture;

/**
 * // TODO: Comment
 *
 * @author Daniel Frey 06.02.11 14:43
 */
public enum CachedImagePictureSortingAlgorithm implements SortingAlgorithm<CachedImagePicture>
{
    ByName
            {
                public void sort( final List<? extends CachedImagePicture> list )
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

    public abstract void sort( List<? extends CachedImagePicture> list );

    public static void sort( final File[] files )
    {
        Arrays.sort( files );
    }
}
