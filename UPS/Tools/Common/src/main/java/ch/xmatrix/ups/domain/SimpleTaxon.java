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

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.8 $ $Date: 2006/08/04 15:50:01 $
 */
public class SimpleTaxon {

    public static final SimpleTaxon DEFAULT = new SimpleTaxon("Default-Taxon", null, new SimpleLevel("Default-Level", null, 0), 0);
    public static final SimpleTaxon DUMMY = new SimpleTaxon("", null, null, 0);

    private SimpleTaxon parent;
    private SimpleLevel level;
    private ArrayList<SimpleTaxon> children = new ArrayList<SimpleTaxon>();
    private String name;
    private int rank;

    public SimpleTaxon(final String name, final SimpleTaxon parent, final SimpleLevel level, final int rank) {
        this.name = name;
        this.level = level;
        this.rank = rank;
        this.parent = parent;
        if (parent != null) {
            parent.children.add(this);
        }
    }

    public ArrayList<SimpleTaxon> getChildTaxa() {
        return children;
    }

    public SimpleLevel getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }

    public SimpleTaxon getParentTaxon() {
        return parent;
    }

    public int getRank() {
        return rank;
    }

    public String toString() {
        return name;
    }

    /**
     * Returns whether the given taxon is on the species level or false for null.
     *
     * @param taxon the taxon to test
     * @return whether a species
     */
    public static boolean isSpecies(final SimpleTaxon taxon) {
        return taxon != null && taxon.getLevel() != null && taxon.getLevel().getChildLevel() == null;
    }

    public static boolean isGenus(final SimpleTaxon taxon) {
        final SimpleLevel childLevel = taxon.getLevel().getChildLevel();
        return childLevel != null && childLevel.getChildLevel() == null;
    }
}
