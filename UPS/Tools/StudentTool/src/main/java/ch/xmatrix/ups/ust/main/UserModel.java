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
package ch.xmatrix.ups.ust.main;

import ch.jfactory.math.RandomGUID;
import ch.xmatrix.ups.model.TaxonModels;
import ch.xmatrix.ups.model.TaxonTree;
import ch.xmatrix.ups.model.TaxonomicComparator;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Model for a user plant list.
 *
 * @author Daniel Frey
 * @version $Revision: 1.2 $ $Date: 2006/11/16 13:25:24 $
 */
public class UserModel
{
    private String uid;

    private ArrayList<String> taxa;

    private String examInfoUid;

    private String constraintsUid;

    private String taxaUid;

    public UserModel()
    {
        uid = new RandomGUID().toString();
    }

    public String getUid()
    {
        return uid;
    }

    public void setUid( final String uid )
    {
        this.uid = uid;
    }

    public ArrayList<String> getTaxa()
    {
        if ( taxa == null )
        {
            taxa = new ArrayList<String>();
        }
        final TaxonTree taxonTree = TaxonModels.find( taxaUid );
        Collections.sort( taxa, new TaxonomicComparator( taxonTree ) );
        return taxa;
    }

    public void setTaxa( final ArrayList<String> taxa )
    {
        this.taxa = taxa;
    }

    public String getConstraintsUid()
    {
        return constraintsUid;
    }

    public void setConstraintsUid( final String constraintsUid )
    {
        this.constraintsUid = constraintsUid;
    }

    public String getExamInfoUid()
    {
        return examInfoUid;
    }

    public void setExamInfoUid( final String examInfoUid )
    {
        this.examInfoUid = examInfoUid;
    }

    public String getTaxaUid()
    {
        return taxaUid;
    }

    public void setTaxaUid( final String taxaUid )
    {
        this.taxaUid = taxaUid;
    }
}
