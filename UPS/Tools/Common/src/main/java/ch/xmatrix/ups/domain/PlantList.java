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

import java.util.List;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.5 $ $Date: 2006/11/16 13:25:24 $
 */
public class PlantList
{

    private PersonData person;

    private List<String> taxa;

    public PersonData getPerson()
    {
        return person;
    }

    public void setPerson(final PersonData person)
    {
        this.person = person;
    }

    public List<String> getTaxa()
    {
        return taxa;
    }

    public void setTaxa(final List<String> taxa)
    {
        this.taxa = taxa;
    }

    public String toString()
    {
        return (person != null ? person.toString() + " - " : "") + taxa.size();
    }
}
