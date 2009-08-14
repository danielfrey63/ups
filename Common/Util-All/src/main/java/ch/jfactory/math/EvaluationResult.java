/*
 * Herbar CD-ROM version 2
 *
 * EvaluationResult.java
 *
 * Created on Feb 9, 2003 3:30:02 PM
 * Created by Daniel
 */
package ch.jfactory.math;

/**
 * Enumeration used for evalutations.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:58 $
 */
public class EvaluationResult
{

    /** Evaluation has passed. */
    public static final EvaluationResult EVAL_PASSED = new EvaluationResult(true);

    /** Evaluation has failed. */
    public static final EvaluationResult EVAL_FAILED = new EvaluationResult(false);

    private boolean passed;

    private EvaluationResult(final boolean bool)
    {
        this.passed = bool;
    }

    public boolean isPassed()
    {
        return passed;
    }

    public static EvaluationResult eval(final boolean bool)
    {
        if (bool)
        {
            return EVAL_PASSED;
        }
        else
        {
            return EVAL_FAILED;
        }
    }
}
