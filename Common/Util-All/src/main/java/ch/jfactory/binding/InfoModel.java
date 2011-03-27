/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.binding;

import java.beans.PropertyChangeListener;

/**
 * Model to set information messages in form of {@link Note}s.
 *
 * @author Daniel Frey
 * @version $Revision: 1.5 $ $Date: 2008/01/06 10:16:23 $
 */
public interface InfoModel
{
    static String PROPERTYNAME_NOTE = "note";

    void addPropertyChangeListener( PropertyChangeListener listener );

    void addPropertyChangeListener( String propertyName, PropertyChangeListener listener );

    void removePropertyChangeListener( PropertyChangeListener listener );

    void removePropertyChangeListener( String propertyName, PropertyChangeListener listener );

    Note getNote();

    void setNote( Note note );
}
