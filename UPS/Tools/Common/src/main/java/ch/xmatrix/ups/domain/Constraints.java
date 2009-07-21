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

import ch.jfactory.lang.ToStringComparator;
import ch.xmatrix.ups.model.TaxonModels;
import ch.xmatrix.ups.model.TaxonTree;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.13 $ $Date: 2007/05/16 17:00:15 $
 */
public class Constraints extends AbstractTaxonBased {

    private String description;
    private ArrayList<Constraint> constraints = new ArrayList<Constraint>();
    private String[] defaultList;
    private Map<String, Constraint> indexToConstraints;

    private transient Constraint current;

    public Constraints() {
        super();
    }

    public Constraints(final Constraints original) {
        super(original);
        description = original.description;
        defaultList = original.defaultList.clone();
        indexToConstraints = new HashMap<String, Constraint>();

        for (int i = 0; i < original.constraints.size(); i++) {
            final Constraint constraint = original.constraints.get(i);
            final Constraint newConstraint = new Constraint(constraint);
            newConstraint.setConstraints(this);
            constraints.add(newConstraint);
            final List<String> taxa = newConstraint.getTaxa();
            for (int j = 0; j < taxa.size(); j++) {
                final String taxon = taxa.get(j);
                indexToConstraints.put(taxon, newConstraint);
            }
        }
    }

    public ArrayList<Constraint> getConstraints() {
        return constraints;
    }

    public void setConstraints(final ArrayList<Constraint> constraints, final TreeObject rootTaxon) {
        this.constraints = constraints;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String[] getDefaultTaxa() {
        return defaultList;
    }

    public void setDefaultTaxa(final String[] defaultList) {
        this.defaultList = defaultList;
    }

    public Constraint getCurrent() {
        return current;
    }

    public void setCurrent(final Constraint constraint) {
        current = constraint;
    }

    public void addConstraint(final Constraint constraint) {
        constraints.add(constraint);
        Collections.sort(constraints, new ToStringComparator());
    }

    public void removeConstraint(final Constraint constraint) {
        constraints.remove(constraint);
        final List<String> taxa = constraint.getTaxa();
        for (int i = 0; taxa != null && i < taxa.size(); i++) {
            final String taxon = (String) taxa.get(i);
            indexToConstraints.remove(taxon);
        }
    }

    public void addTaxon(final String taxon) {
        List<String> taxa = current.getTaxa();
        if (taxa == null) {
            taxa = new ArrayList<String>();
            current.setTaxa(taxa);
        }
        Collections.sort(taxa);
        taxa.add(taxon);
        if (indexToConstraints == null) {
            indexToConstraints = new HashMap<String, Constraint>();
        }
        indexToConstraints.put(taxon, current);
    }

    public void removeTaxon(final String taxon) {
        final List<String> taxa = current.getTaxa();
        taxa.remove(taxon);
        indexToConstraints.remove(taxon);
    }

    public Constraint findConstraint(final String taxon) {
        return indexToConstraints == null ? null : indexToConstraints.get(taxon);
    }

    public String toString() {
        return getName();
    }

    public String toDebugString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("uid", getUid()).append("name", getName()).append("modified", getModified()).toString();
    }

    public ArrayList<String> getObligateTaxa() {
        final ArrayList<String> result = new ArrayList<String>();
        for (int i = 0; i < constraints.size(); i++) {
            final Constraint constraint = constraints.get(i);
            final List<String> taxa = constraint.getTaxa();
            if (taxa.size() == 1) {
                final String taxonName = taxa.get(0);
                final TaxonTree taxonModel = TaxonModels.find(getTaxaUid());
                final boolean isSpecies = SimpleTaxon.isSpecies(taxonModel.findTaxonByName(taxonName));
                if (isSpecies) {
                    result.add(taxonName);
                }
            }
        }
        return result;
    }
}
