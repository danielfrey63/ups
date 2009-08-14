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

import ch.jfactory.math.RandomGUID;
import java.util.Date;

/**
 * Model that is based on a name, an uid, a taxa uid, a modification date and a fixed state.
 *
 * @author Daniel Frey
 * @version $Revision: 1.3 $ $Date: 2006/04/21 11:02:52 $
 */
public abstract class AbstractTaxonBased implements TaxonBased
{

    private String name;

    private String uid;

    private String taxaUid;

    private Date modified;

    private boolean fixed;

    /**
     * Constructs a new object. Sets the name to a default string, resets the uid to a newly generated one and adjusts
     * the modified timestamp. The taxa uid is null and the fixed state false.
     */
    public AbstractTaxonBased()
    {
        setName("Neu");
        setUid(new RandomGUID().toString());
        setModified(new Date());
    }

    /**
     * Copy constructor. Does set the name to the original name plus a suffix. Does reset the fixed flag to false. A new
     * uid is generated and set. The taxa uid is copied to the this object.
     *
     * @param orig original to copy
     */
    public AbstractTaxonBased(final TaxonBased orig)
    {
        this();
        setName(orig.getName() + " Kopie");
        setTaxaUid(orig.getTaxaUid());
    }

    /**
     * Returns the taxa uid.
     *
     * @return the taxa uid <code>String</code>
     */
    public String getTaxaUid()
    {
        return taxaUid;
    }

    /**
     * Sets the taxa uid.
     *
     * @param uid the new taxa uid <code>String</code>
     */
    public void setTaxaUid(final String uid)
    {
        checkFixed();
        taxaUid = uid;
    }

    /**
     * Returns the name.
     *
     * @return the name <code>String</code>
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param name the name <code>String</code>
     */
    public void setName(final String name)
    {
        checkFixed();
        this.name = name;
    }

    /** Sets the models fixed. */
    public void setFixed()
    {
        fixed = true;
    }

    /**
     * Returns whether this taxon based model is fixed, i.e. not modifiable.
     *
     * @return whether fixed
     */
    public boolean isFixed()
    {
        return fixed;
    }

    /**
     * Sets the uid of this model.
     *
     * @param uid the uid <code>String</code>
     */
    public void setUid(final String uid)
    {
        checkFixed();
        this.uid = uid;
    }

    /**
     * Returns the uid of this model.
     *
     * @return the uid <code>String</code>
     */
    public String getUid()
    {
        return uid;
    }

    /**
     * Sets the modification date of this model.
     *
     * @param modified the new modification date
     */
    public void setModified(final Date modified)
    {
        checkFixed();
        this.modified = modified;
    }

    /**
     * Returns the modification date of this model.
     *
     * @return the modification <code>Date</code>
     */
    public Date getModified()
    {
        return modified;
    }

    private void checkFixed()
    {
        if (fixed)
        {
            throw new IllegalArgumentException("model is fixed and may not be altered");
        }
    }
}
