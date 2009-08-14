/*
 * Herbar CD-ROM version 2
 *
 * LevensteinDistance.java
 *
 * Created on 15. Mai 2002, 11:51
 * Created by lilo
 */
package ch.jfactory.math;

/**
 * calculates the difference between two Strings with a given tolerance.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:58 $
 */
public class LevensteinDistance
{

    /**
     * Returns the evaluation result for the comparison of the two strings under the given
     * <code>LevensteinLevel</code>.
     *
     * @param str1  first string to compare with second one
     * @param str2  second string to compare with first one
     * @param level LevensteinLevel at whitch evaluation takes place
     * @return EvaluationResult
     */
    public static int getDistance(final String str1, final String str2, final LevensteinLevel level)
    {
        int i;
        int j;
        final int len1;
        final int len2;
        len1 = str1.length();
        len2 = str2.length();
        final int arrSize = Math.max(len1, len2) + 1;
        final int[][] distance = new int[arrSize][arrSize];
        distance[0][0] = 0;
        for (j = 1; j < arrSize; j++)
        {
            distance[0][j] = distance[0][j - 1] + level.getAdditionCost();
        }
        for (j = 1; j < arrSize; j++)
        {
            distance[j][0] = distance[j - 1][0] + level.getDeletionCost();
        }
        for (i = 1; i <= len1; i++)
        {
            for (j = 1; j <= len2; j++)
            {
                distance[i][j] = smallestOf(distance[i - 1][j - 1] +
                        ((str1.charAt(i - 1) == str2.charAt(j - 1)) ? 0 : level.getChangeCost()),
                        distance[i][j - 1] + level.getAdditionCost(), distance[i - 1][j] + level.getDeletionCost());
            }
        }
        return distance[len1][len2];
    }

    private static int smallestOf(final int x, final int y, final int z)
    {
        return ((x < y) ? Math.min(x, z) : Math.min(y, z));
    }
}

// $Log: LevensteinDistance.java,v $
// Revision 1.1  2005/06/16 06:28:58  daniel_frey
// Completely merged and finished for UST version 2.0-20050616
//
// Revision 1.1  2004/04/19 10:31:21  daniel_frey
// Replaced top level package com by ch
//
// Revision 1.3  2003/02/27 12:21:57  daniel_frey
// - Removed bug where activate was called twice during initalization of mode
// - Moved some components common to lesson and exam to modeapi
// - Added additional functions to exam
//
// Revision 1.2  2003/02/10 12:01:05  daniel_frey
// - Introduced DistanceLevel
//
// Revision 1.1  2003/02/09 12:03:58  daniel_frey
// - Initial
//
// Revision 1.2  2002/05/28 10:01:21  Dani
// Adapted headers and footers
//
