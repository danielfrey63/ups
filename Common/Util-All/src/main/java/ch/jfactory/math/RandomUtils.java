/*
 * Herbar CD-ROM version 2
 *
 * RandomArrayUtils.java
 *
 * Created on 5. Juli 2002, 10:41
 * Created by Daniel Frey
 */
package ch.jfactory.math;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Array manipulation and randomization methods.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.2 $ $Date: 2006/04/15 23:03:21 $
 */
public class RandomUtils {

    /**
     * Last random seed used
     */
    private static long seed;
    private static Random r;

    /**
     * Returns the last seed used in randomization.
     *
     * @return the seed as a long
     */
    public static long getLastSeed() {
        return seed;
    }

    /**
     * Randomizes an array. The seed used may be retrieved with {@link #getLastSeed() getLastSeed()}.
     *
     * @param array the array to randomize
     * @return the random seed used to rendomize
     */
    public static long randomize(final Object[] array) {
        seed = System.currentTimeMillis();
        randomize(array, seed);
        return seed;
    }

    /**
     * Randomizes an array. The seed used may be retrieved with {@link #getLastSeed() getLastSeed()}.
     *
     * @param array the array to randomize
     * @return the random seed used to rendomize
     */
    public static long randomize(final int[] array) {
        seed = System.currentTimeMillis();
        randomize(array, seed);
        return seed;
    }

    /**
     * Randomizes an array using the given seed. The given seed will overwrite the last seed used.
     *
     * @param array the array to randomize
     * @param seed  the seed to use for randomization
     */
    public static void randomize(final Object[] array, final long seed) {
        RandomUtils.seed = seed;
        r = new Random(seed);
        randomizeNext(array);
    }

    /**
     * Randomizes an array using the given seed. The given seed will overwrite the last seed used.
     *
     * @param array the array to randomize
     * @param seed  the seed to use for randomization
     */
    public static void randomize(final int[] array, final long seed) {
        RandomUtils.seed = seed;
        r = new Random(seed);
        randomizeNext(array);
    }

    public static void randomize(final List list, final long seed) {
        RandomUtils.seed = seed;
        r = new Random(seed);
        randomizeNext(list);
    }

    /**
     * Lets reproduce sequences of random number.
     *
     * @param array
     */
    public static void randomizeNext(final Object[] array) {
        for (int i = 0; i < array.length; i++) {
            swap(array, i, r.nextInt(array.length));
        }
    }

    /**
     * Lets reproduce sequences of random number.
     *
     * @param array
     */
    public static void randomizeNext(final int[] array) {
        for (int i = 0; i < array.length; i++) {
            swap(array, i, r.nextInt(array.length));
        }
    }

    public static void randomizeNext(final List list) {
        for (int i = 0; i < list.size(); i++) {
            swap(list, i, r.nextInt(list.size()));
        }
    }

    /**
     * Randomizes the given list. The seed used may be retrieved with {@link #getLastSeed() getLastSeed()}.
     *
     * @param list the list to randomize
     * @return the random seed used to rendomize
     */
    public static long randomize(final List<Object> list) {
        final Object[] objects = list.toArray();
        final long seed = randomize(objects);
        list.removeAll(list);
        list.addAll(Arrays.asList(objects));
        return seed;
    }

    /**
     * Swaps two elements of a given array.
     *
     * @param o the array to swap elements of
     * @param i the element to swap with the k-th element
     * @param k the element to swap with the i-th element
     */
    private static void swap(final Object[] o, final int i, final int k) {
        final Object old = o[k];
        o[k] = o[i];
        o[i] = old;
    }

    /**
     * Swaps two elements of a given array.
     *
     * @param o the array to swap elements of
     * @param i the element to swap with the k-th element
     * @param k the element to swap with the i-th element
     */
    public static void swap(final int[] o, final int i, final int k) {
        final int old = o[k];
        o[k] = o[i];
        o[i] = old;
    }

    public static void swap(final List list, final int i, final int k) {
        final Object old = list.get(k);
        list.set(k, list.get(i));
        list.set(i, old);
    }
}

// $Log: RandomUtils.java,v $
// Revision 1.2  2006/04/15 23:03:21  daniel_frey
// Examsets calculator working again
//
// Revision 1.1  2005/06/16 06:28:58  daniel_frey
// Completely merged and finished for UST version 2.0-20050616
//
// Revision 1.1  2004/10/01 13:22:22  daniel_frey
// no message
//
// Revision 1.1  2004/04/19 10:31:21  daniel_frey
// Replaced top level package com by ch
//
// Revision 1.3  2002/07/08 11:14:31  Dani
// CVS keyword $Source: /repository/HerbarCD/Version2.1/xmatrix/src/java/ch/jfactory/math/RandomUtils.java,v $ added
//
// Revision 1.2  2002/07/08 10:37:00  Dani
// Added seed accessors and randomizers
//
// Revision 1.1  2002/07/05 08:55:46  Dani
// initial
//

