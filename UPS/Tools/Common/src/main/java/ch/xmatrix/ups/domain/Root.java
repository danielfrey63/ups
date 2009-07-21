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

import com.jgoodies.binding.beans.Model;
import java.util.ArrayList;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.3 $ $Date: 2006/04/21 11:02:52 $
 */
public class Root extends Model {

    public static final String PERSON_DATA = Root.class.getName() + ".personData";
    public static final String EXAM_SPECIES = Root.class.getName() + ".examSpecies";
    public static final String ROOT_TAXON = Root.class.getName() + ".rootTaxon";

    private PersonData personData = new PersonData();

    private ArrayList examSpecies;

    private SimpleTaxon rootTaxon;

    public void setPersonData(final PersonData personData) {
        final PersonData old = this.personData;
        this.personData = personData;
        firePropertyChange(PERSON_DATA, old, personData);
    }

    public PersonData getPersonData() {
        return personData;
    }

    public void setExamSpecies(final ArrayList examSpecies) {
        final ArrayList old = this.examSpecies;
        this.examSpecies = examSpecies;
        firePropertyChange(EXAM_SPECIES, old, examSpecies);
    }

    public ArrayList getExamSpecies() {
        return examSpecies;
    }

    public void setRootTaxon(final SimpleTaxon rootTaxon) {
        final SimpleTaxon old = this.rootTaxon;
        this.rootTaxon = rootTaxon;
        firePropertyChange(ROOT_TAXON, old, rootTaxon);
    }

    public SimpleTaxon getRootTaxon() {
        return rootTaxon;
    }
}
