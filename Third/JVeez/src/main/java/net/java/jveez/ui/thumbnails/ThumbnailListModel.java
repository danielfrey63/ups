package net.java.jveez.ui.thumbnails;

import java.util.Collection;
import javax.swing.ListModel;
import net.java.jveez.utils.SortingAlgorithm;

/**
 * TODO: document
 *
 * @author Daniel Frey
 */
public interface ThumbnailListModel<T> extends ListModel
{
    T getPicture( int index );

    void setPictures( Collection<? extends T> pictures );

    void setPictureAt( int index, T picture );

    int getIndexOf( T picture );

    void clear();

    void sort( SortingAlgorithm<T> algorithm );

    void notifyAsUpdated( int index );
}
