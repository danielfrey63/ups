/*
 * Copyright x-matrix Switzerland (c) 2002
 *
 * $Source: /repository/HerbarCD/Version2.1/xmatrix/src/java/ch/jfactory/math/Combinatorial.java,v $
 *
 * Created on 1. Juli 2002, 15:26
 * Created by Daniel Frey
 */
package ch.jfactory.math;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * General combinatorial methods for corss products and subsets.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.2 $ $Date: 2006/04/18 11:14:34 $
 */
public class Combinatorial
{

    /**
     * Returns all combinations of elements between the groups. Order does not matter, and repetitions are not
     * calculated.<p> <p/> Given an array of groups <code>{{a1 a2}{b1 b2 b3}}</code> the result will be <code> {{a1
     * b1}{a2 b1}{a1 b2} {a2 b2}{a1 b3}{a2 b3}}. </code> The order of the given groups will be maintained in the
     * resulting combinations.<p> <p/> The result is not randomized, so a logical order will be maintained. In order to
     * randomize it, use i.e. {@link ch.jfactory.math.RandomUtils#randomize(Object[]) ArrayUtils.randomize}.<p> <p/>
     * Note: Cross products can reach huge sizes if explicitely enumerated, as done in this method. These huge number
     * are not allways senseful and a subsample may be more appropriate. To get a randomized subsamples of cross
     * products, see {@link #getCrossProduct(Object[][], int) getCrossProduct}.
     *
     * @param groups an array of groups of elements
     * @return an array of all combinations of elements between groups
     * @see #getCrossProduct(Object[][], int) getCrossProduct
     */
    public static Object[][] getCrossProduct(final Object[][] groups)
    {
        final int numberOfCombinations = getCrossProductCount(groups);
        return calculateCrossProducts(groups, numberOfCombinations, 0);
    }

    /**
     * Returns all cross products, or a subsample if the sample size is exceeded. Order does not matter, and repetitions
     * are not calculated. The order of the given groups will be maintained in the resulting combinations. For a short
     * example see {@link #getCrossProduct(Object[][])}.<p> <p/> Cross products can reach huge sizes if explicitely
     * enumerated. These huge number are not allways senseful and a subsample may be more appropriate. With this method,
     * the size may be limited.<p> <p/> The method delivers all or a subsample of all theoretically possible cross
     * product combinations. The time used for the constitution of the subsample is guaranteed to be less than for all
     * cross product combination. The returned subset will be a random subset of all cross products. The {@link
     * ch.jfactory.math.RandomUtils#randomize(Object[]) ArrayUtils.randomize} method is used to randomize. Refer to this
     * method if you want to get the seed used for randomization.<p> <p/> To make randomizing reproducable, see {@link
     * ch.jfactory.math.RandomUtils#randomize(Object[]) ArrayUtils.randomize} for how to get the seed used to randomize.
     * To reproduce a former subset, refer to the appropriate {@link #getCrossProduct(Object[][], int, long)
     * getCrossProduct(groups, size, seed)} method. <p> <p/> Note: As a side effect, the original group elements are
     * randomized within their groups.
     *
     * @param groups an array of groups of elements
     * @param size   the maximum number of cross products requested
     * @return an array of the given maximum of cross products
     * @see ch.jfactory.math.RandomUtils#randomize(Object[])
     */
    public static Object[][] getCrossProduct(final Object[][] groups, int size)
    {
        size = Math.min(size, getCrossProductCount(groups));
        return calculateCrossProducts(groups, size, 0);
    }

    /**
     * Does the same as {@link #getCrossProduct(Object[][], int) getCrossProduct(groups, size)}, but allows to use the
     * given seed for reproducable randomization.
     *
     * @param groups an array of groups of elements
     * @param size   the maximum number of cross products requested
     * @param seed   the seed used to randomize with groups
     * @return an array of the given maximum of cross products
     * @see ch.jfactory.math.RandomUtils#randomize(Object[])
     */
    public static Object[][] getCrossProduct(final Object[][] groups, int size, final long seed)
    {
        size = Math.min(size, getCrossProductCount(groups));
        return calculateCrossProducts(groups, size, seed);
    }

    /**
     * Returns the number of cross products which will result for the given group sizes.
     *
     * @param sizes the sizes of the groups used for the cross product
     * @return the number of cross products for the given sizes
     */
    public static int getCrossProductCount(final int[] sizes)
    {
        int result = 1;
        for (final int size : sizes)
        {
            result *= size;
        }
        return result;
    }

    /**
     * Returns the number of cross products which will result for the given group sizes.
     *
     * @param groups an array of groups of elements
     * @return the number of cross products for the given sizes
     */
    public static int getCrossProductCount(final Object[][] groups)
    {
        final int numberOfGroups = groups.length;
        // Calculate the size of the resulting array and initialize it
        int numberOfCombinations = 1;
        for (int i = 0; i < numberOfGroups; i++)
        {
            numberOfCombinations *= groups[i].length;
        }
        return numberOfCombinations;
    }

    public static void main(final String[] args)
    {
        final int[][] res = getSortedCombinationsWithRepetitions(5, 6);
        printIntMatrix(res);
    }

    public static double getSortedCombinationsWithRepetitionsCount(final int n, final int k)
    {

        return Math.pow(n, k);
    }

    /**
     * @param t the traits
     * @param k length of the tuple | number of places
     * @return all combinations
     */
    public static int[][] getSortedCombinationsWithRepetitions(final int[] t, final int k)
    {

        final int[][] r = getSortedCombinationsWithRepetitions(t.length, k);
        final int[][] result = new int[r.length][k];
        for (int i = 0; i < r.length; i++)
        {
            final int[] row = r[i];
            for (int j = 0; j < row.length; j++)
            {
                final int index = row[j];
                result[i][j] = t[index];
            }
        }

        return result;
    }

    /**
     * @param n the number of traits
     * @param k length of the tuple | number of places
     * @return all sorted combinations
     */
    public static int[][] getSortedCombinationsWithRepetitions(final int n, final int k)
    {

        final int r = (int) getSortedCombinationsWithRepetitionsCount(n, k);
        final int[][] result = new int[r][k];
        for (int i = 0; i < k; i++)
        {
            final int pos = k - i - 1;
            for (int j = 0; j < r; j++)
            {
                final int a = (int) (j / Math.pow(n, pos));
                result[j][i] = a % n;
            }
        }

        return result;
    }

    /**
     * Get number of non-repetitive, order-independent k-tuples out of n places.
     *
     * @param n the length of the subset
     * @param k the size of the tuples
     * @return the number of combinations
     */
    public static int getUnsortedCombinationsWithoutRepetitionsCount(final int n, final int k)
    {

        final int max = Math.max(k, n - k);
        final int min = Math.min(k, n - k);
        int result = 1;
        for (int i = max + 1; i <= n; i++)
        {
            result *= i;
        }
        for (int i = 1; i <= min; i++)
        {
            result /= i;
        }

        return result;
    }

    /**
     * Calculates the number of combinations to take k elements out of a set of n elements. Repetitions are allowed and
     * the order is not important.
     *
     * <pre>
     * / n + k - 1 \   / n + k - 1 \
     * |           | = |           |
     * \     k     /   \   n - 1   /
     * </pre>
     *
     * An efficient calculation can be achieved by the product formula:
     *
     * <pre>
     * / a \     b     a + 1 - i
     * |   | = Product ---------
     * \ b /    i=1        i
     * </pre>
     *
     * and thus
     *
     * <pre>
     * / n + k - 1 \     k     n + k - i
     * |           | = Product ---------
     * \     k     /    i=1       i
     * </pre>
     *
     * @param n the number of elements that can be choosen
     * @param k the set size to choose
     * @return the number of combinations
     */
    public static long getUnsortedCombinationsWithRepetitionsCount(final int n, final int k)
    {
        long result = 1;
        int s = n + k;
        for (int i = 1; i < k + 1; i++)
        {
            result *= s - i;
            result /= i;
        }

        return result;
    }

    public static int[][] getUnsortedCombinationsWithRepetitions(final int n, final int k)
    {

        final long r = getUnsortedCombinationsWithRepetitionsCount(n, k);
        final int[][] result = new int[(int) r][k];
        for (int i = 0; i < k; i++)
        {
            result[0][i] = 1;
        }
        for (int i = 1; i < r; i++)
        {
            final int[] row = result[i];
            for (int j = 0; j < k; j++)
            {
                final int[] lastRow = result[i - 1];
                final int lastValue = lastRow[j];
                if (j == k - 1 || lastRow[j + 1] == n)
                {
                    if (lastRow[j] == n)
                    {
                        row[j] = row[j - 1];
                    }
                    else
                    {
                        row[j] = lastValue + 1;
                    }
                }
                else
                {
                    row[j] = lastRow[j];
                }
            }
        }

        return result;
    }

    /**
     * Calculate all given k-tuples of a given array. Order does not matter and repetitions are not allowed
     * (combinatorial subset).
     *
     * @param k     the subset (tuple) size
     * @param array the array to get the subsets (k-tuples) from
     * @return a List containing all the variations
     */
    public static List<List<Object>> getSubsets(final List<Object> array, final int k)
    {
        // the List to store the results in
        final List<List<Object>> results = new ArrayList<List<Object>>();
        // initiate recursive call
        getSubsets(results, new ArrayList<Object>(), array, k);
        return results;
    }

    /**
     * Returns the factorial of the given integer.
     *
     * @param n the integer to get the factorial of
     * @return the factorial of the given integer
     */
    public static int factorial(final int n)
    {
        int ret = 1;
        if (n > 1)
        {
            for (int i = 1; i <= n; i++)
            {
                ret *= i;
            }
        }
        return ret;
    }

    /**
     * Recursive chunk of code, which takes the result List, a prefix holder, the original array and the tuple size as
     * its parameters. The result List is filled subsequently with one List for each new subset.
     *
     * @param prefix  holds a chunk of the array to prefix subsequent calls with
     * @param array   the array containing the data to subset from
     * @param k       the tuple size
     * @param results the List that is filled with each new subset
     */
    private static void getSubsets(final List<List<Object>> results, final List<Object> prefix, final List<Object> array, final int k)
    {
        for (int i = 0; i < array.size() - k + 1; i++)
        {
            if (k > 1)
            {
                // it is important to make new list objects as subsequent loops
                // would not have the same prerequisites otherwise.
                final List<Object> newbase = new ArrayList<Object>(prefix);
                final List<Object> newobjs = new ArrayList<Object>(array);
                // need to remove the first i elements of the new object array
                for (int j = 0; j < i; j++)
                {
                    newobjs.remove(0);
                }
                // finally add the new prefix to the existing one
                newbase.add(newobjs.remove(0));
                getSubsets(results, newbase, newobjs, k - 1);
            }
            else
            {
                final List<Object> chunk = new ArrayList<Object>(prefix);
                chunk.add(array.get(i));
                results.add(chunk);
            }
        }
    }

    /**
     * Calculates size cross products of the given groups.
     *
     * @param groups an array of groups of elements
     * @param size   the maximum number of cross products requested
     * @param seed   the random seed used for randomization
     * @return an array of the given maximum of cross products
     */
    private static Object[][] calculateCrossProducts(final Object[][] groups, final int size, final long seed)
    {
        final int numberOfGroups = groups.length;
        // Initialize one counter for each group
        final int[] withinGroupCounters = new int[numberOfGroups];
        // Choose randomly some rows
        final int totalSize = getCrossProductCount(groups);
        final Random rand = new Random(seed);
        final Object[][] result = new Object[size][];
        for (int i = 0; i < size; i++)
        {
            final int randomRow = (seed == 0 ? i : rand.nextInt(totalSize));
            int divider = 1;
            final Object[] combination = new Object[numberOfGroups];
            for (int j = 0; j < numberOfGroups; j++)
            {
                final int groupSize = groups[j].length;
                // Each counter will be initialized in turn by the given value.
                // For the first group, the counter is a simple index modulo
                // group size. But for each subsequent group, the index is
                // corrected by the product of all preceeding group sizes to get
                // the correct shift.
                withinGroupCounters[j] = (randomRow / divider) % groupSize;
                divider *= groupSize;
                // Store the combination
                combination[j] = groups[j][withinGroupCounters[j]];
            }
            result[i] = combination;
        }
        return result;
    }

    private static void printIntMatrix(final int[][] res)
    {
        for (final int[] re : res)
        {
            int sum = 0;
            for (final int s : re)
            {
                sum += s;
                System.out.print(" " + s);
            }
            if (sum == 8)
            {
                System.out.println(" <--");
            }
            else
            {
                System.out.println("");
            }
        }
    }
}

// $Log: Combinatorial.java,v $
// Revision 1.2  2006/04/18 11:14:34  daniel_frey
// *** empty log message ***
//
// Revision 1.1  2005/06/16 06:28:58  daniel_frey
// Completely merged and finished for UST version 2.0-20050616
//
// Revision 1.2  2004/10/01 13:22:22  daniel_frey
// no message
//
// Revision 1.1  2004/04/19 10:31:21  daniel_frey
// Replaced top level package com by ch
//
// Revision 1.7  2003/05/25 21:38:47  daniel_frey
// - Optimized imports
// - Replaced static access by proper class access instead of object access
//
// Revision 1.6  2003/04/27 19:39:48  daniel_frey
// - Switched from arrays to lists
//
// Revision 1.5  2002/08/27 12:30:36  Dani
// - Removed unused local varibales
//
// Revision 1.4  2002/08/01 16:12:48  Dani
// Commented main
//
// Revision 1.3  2002/07/08 11:14:31  Dani
// CVS keyword $Source: /repository/HerbarCD/Version2.1/xmatrix/src/java/ch/jfactory/math/Combinatorial.java,v $ added
//
// Revision 1.2  2002/07/08 10:37:40  Dani
// Reproducible subsets and cross-products
//
// Revision 1.1  2002/07/05 08:34:09  Dani
// initial
//
// Revision 1.1  2002/07/04 13:58:05  lilo
// inital
//

