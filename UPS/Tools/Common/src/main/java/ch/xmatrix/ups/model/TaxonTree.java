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
package ch.xmatrix.ups.model;

import ch.jfactory.resource.Version;
import ch.jfactory.model.IdAware;
import ch.xmatrix.ups.domain.SimpleLevel;
import ch.xmatrix.ups.domain.SimpleTaxon;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Holds a taxon tree with metainformation like name, version, modification date aso.
 *
 * @author Daniel Frey
 * @version $Revision: 1.8 $ $Date: 2007/05/16 17:00:15 $
 */
public class TaxonTree implements IdAware {

    private String uid;
    private String name;
    private String description;
    private String lastModified;
    private Version version;
    private SimpleLevel rootLevel;
    private SimpleTaxon rootTaxon;
    private Map index;

    public static final TaxonTree DEFAULT = new TaxonTree();

    public String getUid() {
        return uid;
    }

    public void setUid(final String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(final String lastModified) {
        this.lastModified = lastModified;
    }

    public Version getVersion() {
        return version;
    }

    public void setVersion(final Version version) {
        this.version = version;
    }

    public SimpleTaxon getRootTaxon() {
        return rootTaxon;
    }

    public void setRootTaxon(final SimpleTaxon rootTaxon) {
        this.rootTaxon = rootTaxon;
    }

    public SimpleLevel getRootLevel() {
        return rootLevel;
    }

    public void setRootLevel(final SimpleLevel rootLevel) {
        this.rootLevel = rootLevel;
    }

    public SimpleTaxon findTaxonByName(final String taxon) {
        return (SimpleTaxon) index.get(taxon);
    }

    public void setIndex(final Map index) {
        this.index = index;
    }

    public String toString() {
        return name;
    }

    public String toDebugString() {
        return new ToStringBuilder(this).toString();
    }

    //--- Utilities

    /**
     * Sorts taxa strings according to the taxonomical order.
     *
     * @param taxa the taxa to sort
     * @return a new list with the sorted taxa
     */
    public ArrayList<String> sortTaxaStrings(final ArrayList<String> taxa) {
        final ArrayList<SimpleTaxon> taxaToSort = new ArrayList<SimpleTaxon>();
        for (int i = 0; i < taxa.size(); i++) {
            final String taxonString = (String) taxa.get(i);
            final SimpleTaxon taxon = findTaxonByName(taxonString);
            taxaToSort.add(taxon);
        }
        final ArrayList<SimpleTaxon> sortedTaxa = sortSimpleTaxa(taxaToSort);
        final ArrayList<String> sorted = new ArrayList<String>();
        for (int i = 0; i < sortedTaxa.size(); i++) {
            final SimpleTaxon taxon = (SimpleTaxon) sortedTaxa.get(i);
            sorted.add(taxon.getName());
        }
        return sorted;
    }

    /**
     * Sorts taxa strings according to the taxonomical order.
     *
     * @param taxa the taxa to sort
     * @return a new list with the sorted taxa
     */
    public ArrayList<SimpleTaxon> sortSimpleTaxa(final ArrayList<SimpleTaxon> taxa) {
        final Comparator<SimpleTaxon> taxonComparator = new Comparator<SimpleTaxon>() {
            public int compare(final SimpleTaxon t1, final SimpleTaxon t2) {
                // find common parent.
                final ArrayList<SimpleTaxon> tp1 = getPathToRoot(t1);
                final ArrayList<SimpleTaxon> tp2 = getPathToRoot(t2);
                final ArrayList<SimpleTaxon> common = (ArrayList<SimpleTaxon>) CollectionUtils.intersection(tp1, tp2);
                final int size = common.size();
                // Common is not ordered, so take the common parent from one of the original arrays.
                final SimpleTaxon commonParent = tp1.get(size - 1);
                final ArrayList<SimpleTaxon> children = commonParent.getChildTaxa();
                return new Integer(children.indexOf(tp1.get(size))).compareTo(new Integer(children.indexOf(tp2.get(size))));
            }
        };
        Collections.sort(taxa, taxonComparator);
        return taxa;
    }

    private ArrayList<SimpleTaxon> getPathToRoot(final SimpleTaxon taxon) {
        final ArrayList<SimpleTaxon> taxons = new ArrayList<SimpleTaxon>();
        SimpleTaxon current = taxon;
        while (current != null) {
            taxons.add(0, current);
            current = current.getParentTaxon();
        }
        return taxons;
    }
}
