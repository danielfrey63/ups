/*
 * Herbar CD-ROM version 2
 *
 * Variations.java
 *
 * Created on 1. Juli 2002, 15:26
 * Created by Daniel Frey
 */
package com.ethz.geobot.herbar.game.util;

import java.util.Vector;

/**
 * generate variations of length k from n objects.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:06 $
 */
public class Variations {

    public static int factorial(int n) {
        int ret = 1;
        if (n > 1) {
            for (int i = 1; i <= n; i++) {
                ret *= i;
            }
        }
        return ret;
    }

    /**
     * Calculate all given k-tuples of the objs array. Order does not matter and repetitions are not allowed
     * (combinatorial subset). (Note: Implementation appears a little bit hacky, but I did not find a shorter/better way
     * to calculate it.)
     *
     * @param k     the subset (tuple) size
     * @param array the array to get the subsets (k-tuples) from
     * @return a Vector containing all the variations
     */
    public static Vector subsets(Vector array, int k) {
        // the Vector to store the results in
        Vector results = new Vector();
        // initiate recursive call
        calcVariation(results, new Vector(), array, k);
        return results;
    }

    /**
     * Recursive chunk of code, which takes the result Vector, a prefix holder, the original array and the tuple size as
     * its parameters. The result Vector is filled subsequently with one Vector for each new subset.
     *
     * @param prefix  holds a chunk of the array to prefix subsequent calls with
     * @param array   the array containing the data to subset from
     * @param k       the tuple size
     * @param results the Vector that is filled with each new subset
     */
    private static void calcVariation(Vector results, Vector prefix, Vector array, int k) {
        for (int i = 0; i < array.size() - k + 1; i++) {
            if (k > 1) {
                // it is important to make new Vector objects as subsequent loops
                // would not have the same prerequisites otherwise.
                Vector newbase = new Vector(prefix);
                Vector newobjs = new Vector(array);
                // need to remove the first i elements of the new object array
                for (int j = 0; j < i; j++) {
                    newobjs.remove(0);
                }
                // finally add the new prefix to the existing one
                newbase.add(newobjs.remove(0));
                calcVariation(results, newbase, newobjs, k - 1);
            }
            else {
                Vector chunk = new Vector(prefix);
                chunk.add(array.get(i));
                results.add(chunk);
            }
        }
    }
}

// $Log: Variations.java,v $
// Revision 1.1  2007/09/17 11:07:06  daniel_frey
// - Version 3.0.20070401
//
// Revision 1.2  2003/01/23 10:54:27  daniel_frey
// - Optimized imports
//
// Revision 1.1  2002/07/04 13:58:05  lilo
// inital
//

