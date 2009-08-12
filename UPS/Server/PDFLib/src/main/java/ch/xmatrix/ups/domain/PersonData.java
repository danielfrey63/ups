/* ====================================================================
 *  Copyright 2004-2005 www.xmatrix.ch
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
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
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2006/08/04 15:50:02 $
 */
public class PersonData {
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
    public PersonData(final PersonData person) {
        copy(person);
    }

    /**
     * Init object with defaults.
     */
    public PersonData() {
        // Do nothing.
    }

    /**
     * Copies the specified person data to this data.
     *
     * @param person the person to copy
     */
    public void copy(final PersonData person) {
        setFirstName(person.firstName);
        setLastName(person.lastName);
        setId(person.id);
        setCourse(person.course);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(final String course) {
        this.course = course;
    }

    public String toString() {

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
