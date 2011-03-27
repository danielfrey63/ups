/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.math;

/**
 * Enumeration used for evaluations.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:58 $
 */
public class EvaluationResult
{
    /** Evaluation has passed. */
    public static final EvaluationResult EVALUATION_PASSED = new EvaluationResult( true );

    /** Evaluation has failed. */
    public static final EvaluationResult EVALUATION_FAILED = new EvaluationResult( false );

    private final boolean passed;

    private EvaluationResult( final boolean bool )
    {
        this.passed = bool;
    }

    public boolean isPassed()
    {
        return passed;
    }

    public static EvaluationResult eval( final boolean bool )
    {
        if ( bool )
        {
            return EVALUATION_PASSED;
        }
        else
        {
            return EVALUATION_FAILED;
        }
    }
}
