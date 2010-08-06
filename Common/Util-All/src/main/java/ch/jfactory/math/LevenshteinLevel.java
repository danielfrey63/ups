/*
 * Herbar CD-ROM version 2
 *
 * LevensteinLevel.java
 *
 * Created on Feb 9, 2003 3:31:20 PM
 * Created by Daniel
 */
package ch.jfactory.math;

/**
 * The LevenshteinLevel indicates whether a guess may be accepted under different strengthy levels. For all levels an
 * equal weight for additions, deletions and changes is assumed.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:58 $
 */
public class LevenshteinLevel
{
    /** Only identical results are tolerated. */
    public final static LevenshteinLevel LEVEL_EQUAL = new LevenshteinLevel( 1, 1, 1, 0 );

    /** Only results with one error are tolerated. */
    public final static LevenshteinLevel LEVEL_RESTRICTIVE = new LevenshteinLevel( 1, 1, 1, 1 );

    /** Only results with two errors are tolerated. */
    public final static LevenshteinLevel LEVEL_TOLERANT = new LevenshteinLevel( 1, 1, 1, 2 );

    /** Only results with three errors are tolerated. */
    public final static LevenshteinLevel LEVEL_LOUSY = new LevenshteinLevel( 1, 1, 1, 3 );

    private final int add;

    private final int del;

    private final int change;

    private final int total;

    private LevenshteinLevel( final int add, final int del, final int change, final int total )
    {
        this.add = add;
        this.del = del;
        this.change = change;
        this.total = total;
    }

    public int getAdditionCost()
    {
        return add;
    }

    public int getDeletionCost()
    {
        return del;
    }

    public int getChangeCost()
    {
        return change;
    }

    public int getTotal()
    {
        return total;
    }

    public EvaluationResult getEval( final String str1, final String str2 )
    {
        return EvaluationResult.eval( LevenshteinDistance.getDistance( str1, str2, this ) <= total );
    }
}
