/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.application.persistence;

import java.util.Properties;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:57 $
 */
public interface IFBusinessDelegate
{
    public static final String PROPERTY_SOURCE_NAME = "SOURCE_NAME";

    Object getRootObject();

    void cacheToSave( Object o );

    void cacheToDelete( Object o );

    /**
     * Saves to the persistent layer.
     *
     * @param properties the properties for the save action or null
     */
    void save( Properties properties );

    /**
     * Opens a new persistence layer.
     *
     * @param properties the properties to open the layer or null
     */
    void open( Properties properties ) throws SourceVetoedException;

    /**
     * Creates a new persistence layer.
     *
     * @param properties the properties to create the new layer or null
     */
    void createNew( Properties properties ) throws SourceVetoedException;

    Object addParent( Object child, Object parent, int rank );

    Properties getProperties();

    void addSourceChangeListener( SourceStateListener l );

    void removeSourceChangeListener( SourceStateListener l );

    void init();
}
