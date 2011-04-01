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

import ch.jfactory.model.IdAware;
import java.util.Date;

/**
 * Taxon based models should implement this interface.
 *
 * @author Daniel Frey
 * @version $Revision: 1.3 $ $Date: 2007/05/16 17:00:15 $
 */
public interface TaxonBased extends IdAware
{
    /**
     * Sets the id of this data object.
     *
     * @param uid the id
     */
    void setUid( String uid );

    /**
     * Retrieves the taxa uid of the taxon tree on which this data object is based.
     *
     * @return the taxa tree uid
     */
    String getTaxaUid();

    /**
     * Sets the uid of the taxon tree on which this data object is based.
     *
     * @param uid the taxa tree uid
     */
    void setTaxaUid( String uid );

    /**
     * Returns the name of this value object.
     *
     * @return the name
     */
    String getName();

    /**
     * The name of the data object.
     *
     * @param name the name
     */
    void setName( String name );

    /**
     * Returns the modification timestamp.
     *
     * @return modification timestamp
     */
    Date getModified();

    /**
     * Sets the timestamp of modification for this value object.
     *
     * @param modified the timestamp
     */
    void setModified( Date modified );

    /**
     * Returns whether this data object is readonly.
     *
     * @return the fixed status
     */
    boolean isFixed();

    /** Set the fixed status to true. There is no way to reverse this status. Instaed a copy of this data object has to be made and may be altered. */
    void setFixed();

    /**
     * A logging debug string that reveals some internal information about this object.
     *
     * @return debug string
     */
    String toDebugString();

}
