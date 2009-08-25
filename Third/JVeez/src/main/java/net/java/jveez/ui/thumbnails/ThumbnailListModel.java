package net.java.jveez.ui.thumbnails;

import java.util.Collection;
import javax.swing.ListModel;
import net.java.jveez.utils.SortingAlgorithm;
import net.java.jveez.vfs.Picture;

/**
 * TODO: document
 *
 * @author Daniel Frey
 *
 */
public interface ThumbnailListModel<T> extends ListModel {

    Picture getPicture(int index);

    void setPictures(Collection<? extends Picture> pictures);

    void setPictureAt(int index, Picture picture);

    int getIndexOf(Picture picture);

    void clear();

    void sort(SortingAlgorithm<T> algorithm);

    void notifyAsUpdated(int index);
}
