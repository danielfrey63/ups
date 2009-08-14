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

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.3 $ $Date: 2006/04/21 11:02:52 $
 */
public class PersonData extends Model
{
    public static final String FIRST_NAME = "firstName";

    public static final String LAST_NAME = "lastName";

    public static final String ID = "id";

    public static final String COURSE = "course";

    private String firstName = "";

    private String lastName = "";

    private String id = "00-000-000";

    private String course = "";

    /**
     * Copy contructor.
     *
     * @param person the person data to copy
     */
    public PersonData(final PersonData person)
    {
        copy(person);
    }

    /** Init object with defaults. */
    public PersonData()
    {
        // Do nothing.
    }

    /**
     * Copies the specified person data to this data.
     *
     * @param person the person to copy
     */
    public void copy(final PersonData person)
    {
        setFirstName(person.firstName);
        setLastName(person.lastName);
        setId(person.id);
        setCourse(person.course);
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(final String firstName)
    {
        final String old = getFirstName();
        this.firstName = firstName;
        firePropertyChange(FIRST_NAME, old, firstName);
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(final String lastName)
    {
        final String old = getLastName();
        this.lastName = lastName;
        firePropertyChange(LAST_NAME, old, lastName);
    }

    public String getId()
    {
        return id;
    }

    public void setId(final String id)
    {
        final String old = getId();
        this.id = id;
        firePropertyChange(ID, old, id);
    }

    public String getCourse()
    {
        return course;
    }

    public void setCourse(final String course)
    {
        final String old = getCourse();
        this.course = course;
        firePropertyChange(COURSE, old, course);
    }

    public String toString()
    {

        final StringBuffer text = new StringBuffer();
        text.append(getId());
        text.append(" - ");
        text.append(getLastName());
        text.append(" - ");
        text.append(getFirstName());
        text.append(" - ");
        text.append(getCourse());

        return text.toString();
    }
}
