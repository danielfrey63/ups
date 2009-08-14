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
 * The LevensteinLevel indicates whether a guess may be accepted under different strengthy levels. For all levels an
 * equal weight for additions, deletions and changes is assumed.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:58 $
 */
public class LevensteinLevel
{

    /** Only identical results are tolerated. */
    public final static LevensteinLevel LEVEL_EQUAL = new LevensteinLevel(1, 1, 1, 0);

    /** Only results with one error are tolerated. */
    public final static LevensteinLevel LEVEL_RESTRICTIVE = new LevensteinLevel(1, 1, 1, 1);

    /** Only results with two errors are tolerated. */
    public final static LevensteinLevel LEVEL_TOLERANT = new LevensteinLevel(1, 1, 1, 2);

    /** Only results with three errors are tolerated. */
    public final static LevensteinLevel LEVEL_LOUSY = new LevensteinLevel(1, 1, 1, 3);

    private int add;

    private int del;

    private int change;

    private int total;

    private LevensteinLevel(final int add, final int del, final int change, final int total)
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

    public EvaluationResult getEval(final String str1, final String str2)
    {
        return EvaluationResult.eval(LevensteinDistance.getDistance(str1, str2, this) <= total);
    }
}
