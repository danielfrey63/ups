package net.java.jveez.utils;

import java.util.List;

/**
 * TODO: document
 *
 * @author Daniel Frey
 *
 */
public interface SortingAlgorithm<T> {

    void sort(List<? extends T> list);
}
