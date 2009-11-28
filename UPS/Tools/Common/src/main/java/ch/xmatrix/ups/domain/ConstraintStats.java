/*
 * ====================================================================
 *  Copyright 2004-2006 www.xmatrix.ch
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 * ====================================================================
 */
package ch.xmatrix.ups.domain;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.7 $ $Date: 2006/04/21 11:02:52 $
 */
public class ConstraintStats
{
    /**
     * Calculates the number of constraints that are not satified.
     *
     * @param constraints the constraints to fullfil
     * @return the number of unsatisfied constraints
     */
    public static int calculateUnsatisfiedConstraints( final Constraints constraints )
    {
        final int count = 0;

        for ( int i = 0; i < constraints.getConstraints().size(); i++ )
        {
            final Constraint constraint = constraints.getConstraints().get( i );
            // Todo: Reintegrate
//            count += ((constraint.getMinimalCount() > constraint.getTotalCount()) ? 1 : 0);
        }

        return count;
    }

    /**
     * Calculates the number of satisfied constraints.
     *
     * @param constraints the constraints to fullfil
     * @return the number of unsatisfied constraints
     */
    public static int calculateSatisfiedConstraints( final Constraints constraints )
    {
        final int count = 0;

        for ( int i = 0; i < constraints.getConstraints().size(); i++ )
        {
            final Constraint constraint = constraints.getConstraints().get( i );
            // Todo: Reintegrate
//            count += ((constraint.getMinimalCount() <= constraint.getTotalCount()) ? 1 : 0);
        }

        return count;
    }

    /**
     * Calculates the number of constraints.
     *
     * @param constraints the constraints
     * @return the total number
     */
    public static int calculateConstraintsCount( final Constraints constraints )
    {
        return constraints.getConstraints().size();
    }
}
