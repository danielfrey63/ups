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

import java.util.ArrayList;
import java.util.List;

/**
 * This class holds the information relevant for a constraint. This includes the name of the constraint, the taxa this
 * constraint is meant for and the minimal count of taxa neede to fullfill this contraint.
 *
 * @author Daniel Frey
 * @version $Revision: 1.11 $ $Date: 2007/04/06 11:01:11 $
 */
public class Constraint {

    private String name;
    private int minimalCount;
    private List<String> taxa;
    private Constraints constraints;

    public Constraint() {
        name = "<Neue Vorgabe>";
    }

    public Constraint(final Constraint orig) {
        name = orig.name;
        minimalCount = orig.minimalCount;
        constraints = orig.constraints;
        taxa = new ArrayList<String>(orig.taxa);
    }

    /**
     * Returns the name of this constraint.
     *
     * @return the name of this constraint.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this constraint.
     *
     * @param name the name to set.
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Returns the minimal count of selected taxa needed to fullfill this constraint.
     *
     * @return the minimal count of selected taxa needed.
     */
    public int getMinimalCount() {
        return minimalCount;
    }

    /**
     * Sets the minimal count of selected taxa needed to fullfill this contraint.
     *
     * @param minimalCount the minimal count of selected taxa.
     */
    public void setMinimalCount(final int minimalCount) {
        this.minimalCount = minimalCount;
    }

    /**
     * Returns the taxa this constraint is meant for.
     *
     * @return the taxa of this constraint.
     */
    public List<String> getTaxa() {
        return taxa;
    }

    public void setTaxa(final List<String> taxa) {
        this.taxa = taxa;
    }

    /**
     * Returns a specific taxon of this constraint.
     *
     * @param i the index of the taxon to return.
     * @return the taxon
     */
    public String getTaxon(final int i) {
        return taxa.get(i);
    }

    public Constraints getConstraints() {
        return constraints;
    }

    public void setConstraints(final Constraints constraints) {
        this.constraints = constraints;
    }

    public String toString() {
        return name;
    }

    public String toDebugString() {
        final StringBuffer buffer = new StringBuffer(name);
        buffer.append(" - ");
        for (int i = 0; i < taxa.size(); i++) {
            final String taxonString = taxa.get(i);
            buffer.append(taxonString);
            if (i < taxa.size() - 1) buffer.append(", ");
        }
        return buffer.toString();
    }
}
