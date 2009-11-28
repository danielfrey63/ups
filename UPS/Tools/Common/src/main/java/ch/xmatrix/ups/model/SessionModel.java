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

import ch.jfactory.application.AbstractMainModel;
import ch.xmatrix.ups.domain.AbstractTaxonBased;
import ch.xmatrix.ups.domain.Constraints;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Holds the informations about an exam, like due date, exam session id, description and constraints, on which the exam
 * is based.
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2007/05/16 17:00:15 $
 */
public class SessionModel extends AbstractTaxonBased
{
    private String constraintsUid;

    private String description;

    /**
     * Examination session id.
     */
    private String seskz;

    private Date due;

    public SessionModel()
    {
    }

    public SessionModel( final SessionModel copy )
    {
        super( copy );
        constraintsUid = copy.constraintsUid;
        description = copy.description;
        seskz = copy.seskz;
        due = copy.due;
    }

    public String getConstraintsUid()
    {
        return constraintsUid;
    }

    public void setConstraintsUid( final String constraintsUid )
    {
        this.constraintsUid = constraintsUid;
    }

    public String getTaxaUid()
    {
        final Constraints constraints = (Constraints) AbstractMainModel.findModel( constraintsUid );
        return constraints == null ? null : constraints.getTaxaUid();
    }

    public void setTaxaUid( final String taxonUid )
    {
        final Constraints constraints = (Constraints) AbstractMainModel.findModel( constraintsUid );
        if ( constraints != null && constraints.getTaxaUid().equals( taxonUid ) )
        {
            throw new IllegalStateException( "SessionModels constraints does not match with the taxon tree given." );
        }
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription( final String description )
    {
        this.description = description;
    }

    public Date getDue()
    {
        return due;
    }

    public void setDue( final Date due )
    {
        this.due = due;
    }

    public String getSeskz()
    {
        return seskz;
    }

    public void setSeskz( final String seskz )
    {
        this.seskz = seskz;
    }

    public String toString()
    {
        return getName();
    }

    public String toDebugString()
    {
        return new ToStringBuilder( this, ToStringStyle.SHORT_PREFIX_STYLE ).
                append( "uid", getUid() ).append( "name", getName() ).append( "modified", getModified() ).
                append( "due", getDue() ).append( "seskz", getSeskz() ).toString();
    }
}
