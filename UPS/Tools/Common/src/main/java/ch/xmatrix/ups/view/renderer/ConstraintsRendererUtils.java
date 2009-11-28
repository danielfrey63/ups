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

package ch.xmatrix.ups.view.renderer;

import ch.xmatrix.ups.domain.Constraint;
import ch.xmatrix.ups.domain.Constraints;
import ch.xmatrix.ups.domain.SimpleTaxon;
import ch.xmatrix.ups.model.TaxonModels;
import ch.xmatrix.ups.model.TaxonTree;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * {@link RendererPanel} based renderer aware of constraints (all constraints and constraints in focus) and default
 * taxa.
 *
 * @author Daniel Frey
 * @version $Revision: 1.2 $ $Date: 2007/04/06 11:01:11 $
 */
public class ConstraintsRendererUtils
{
    private static final ArrayList<String> DUMMY_LIST = new ArrayList<String>();

    private static final String[] EMPTY_STRINGS = new String[0];

    protected Constraints constraints;

    protected List<String> constraintTaxa;

    protected List<String> defaultTaxa;

    public void setConstraint( final Constraint constraint )
    {
        constraintTaxa = constraint == null ? null : constraint.getTaxa();
    }

    public void setConstraints( final Constraints constraints )
    {
        this.constraints = constraints;
        final String[] defaultTaxa = constraints == null ? EMPTY_STRINGS : constraints.getDefaultTaxa();
        this.defaultTaxa = defaultTaxa == null ? DUMMY_LIST : new ArrayList<String>( Arrays.asList( defaultTaxa ) );
    }

    public Constraints getConstraints()
    {
        return constraints;
    }

    /**
     * Configures a RendererPanel to display the constraints state as an extention. The extention has the form <code>
     * name (count/minimum)</code> where the <code>name</code> is optinally displayed in case it has not to be hidden,
     * <code>count</code> is the actual number of selected species and <code>minimum</code> is the minimal requirement
     * of species selected.
     *
     * @param panel       the RendererPanel
     * @param constraints the constraints
     * @param constraint  the constraint to display the state for
     * @param taxa        the taxa list
     * @param hideName    whether the constraint has the same name as the single taxon n it
     */
    public static void configureForConstraint( final RendererPanel panel, final Constraints constraints,
                                               final Constraint constraint, final ArrayList<String> taxa,
                                               final boolean hideName )
    {
        final StringBuilder builder = new StringBuilder();
        if ( constraint != null )
        {
            final int minimalCount = constraint.getMinimalCount();
            final int actualCount = getTotalCount( constraint, constraints, taxa );
            builder.append( " (" );
            builder.append( hideName ? "" : constraint.getName() );
            builder.append( hideName ? "" : " " );
            builder.append( actualCount );
            builder.append( "/" );
            builder.append( minimalCount );
            builder.append( ")" );
            panel.setExtentionText( builder.toString() );
            panel.setOk( actualCount >= minimalCount );
        }
        else
        {
            panel.setExtentionText( null );
        }
    }

    /**
     * Returns the total count of selected taxa that are part of the constraint (that is: lie in the hierarchical system
     * in the scope of the constraints taxa).
     *
     * @param constraint  the constraint (the scope)
     * @param constraints the constraints context
     * @param taxa        the taxa for which to decide (and count) whether they belong to the constraint or not
     * @return the number of taxa in the scope of the constraint
     */
    public static int getTotalCount( final Constraint constraint, final Constraints constraints,
                                     final ArrayList<String> taxa )
    {
        final String uid = constraints.getTaxaUid();
        final TaxonTree tree = TaxonModels.find( uid );
        final List<String> constraintTaxa = constraint.getTaxa();
        int count = 0;
        for ( int i = 0; tree != null && constraintTaxa != null && i < constraintTaxa.size(); i++ )
        {
            final String constraintTaxonName = constraintTaxa.get( i );
            final SimpleTaxon constraintTaxon = tree.findTaxonByName( constraintTaxonName );
            count += getTotalCount( constraintTaxon, taxa );
        }
        return count;
    }

    private static int getTotalCount( final SimpleTaxon constraintTaxon, final ArrayList<String> taxa )
    {
        final String name = constraintTaxon.getName();
        assert constraintTaxon == null : "taxon " + name + " not found in taxon tree";
        int count = 0;
        if ( taxa.contains( name ) )
        {
            count++;
        }
        final ArrayList<SimpleTaxon> childTaxa = constraintTaxon.getChildTaxa();
        for ( int i = 0; childTaxa != null && i < childTaxa.size(); i++ )
        {
            final SimpleTaxon child = childTaxa.get( i );
            count += getTotalCount( child, taxa );
        }
        return count;
    }

    /**
     * Calculates the number of complete constraints.
     *
     * @param constraints the constraints to look at
     * @param taxa        the taxa to take into account
     * @return the number of complete constraints
     */
    public static int getCompleteConstraints( final Constraints constraints, final ArrayList<String> taxa )
    {
        final ArrayList<Constraint> allConstraints = constraints.getConstraints();
        int count = 0;
        for ( final Constraint constraint : allConstraints )
        {
            final int minimalCount = constraint.getMinimalCount();
            final int actualCount = getTotalCount( constraint, constraints, taxa );
            if ( actualCount >= minimalCount )
            {
                count++;
            }
        }
        return count;
    }
}
