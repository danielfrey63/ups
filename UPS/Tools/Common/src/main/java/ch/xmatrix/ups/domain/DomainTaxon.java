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
 * A simple {@link TreeObject} wrapper for a {@link SimpleTaxon}.
 *
 * @author Daniel Frey
 * @version $Revision: 1.2 $ $Date: 2006/04/21 11:02:52 $
 */
public class DomainTaxon implements TreeObject {

    private SimpleTaxon taxon;
    private TreeObject[] children;
    private TreeObject parent;

    public DomainTaxon(final SimpleTaxon taxon) {
        this.setTaxon(taxon);
    }

    public void setTaxon(final SimpleTaxon taxon) {
        this.taxon = taxon;
    }

    public SimpleTaxon getTaxon() {
        return taxon;
    }

    public TreeObject[] getChildren() {
        return children;
    }

    public void setChildren(final TreeObject[] children) {
        this.children = children;
    }

    public TreeObject getParent() {
        return parent;
    }

    public void setParent(final TreeObject parent) {
        this.parent = parent;
    }

    public String toString() {
        return taxon.toString();
    }
}
