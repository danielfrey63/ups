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

import java.util.EventObject;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:57 $
 */
public class SourceStateEvent extends EventObject
{
    /** Indicates an opened persistence source. */
    public static final SourceStateEventType OPENED = new SourceStateEventType( "OPENED" );

    /** Indicates that data has been saved to the persistence layer. */
    public static final SourceStateEventType SAVED = new SourceStateEventType( "SAVED" );

    /** Indicates that the data has been changed but not saved. */
    public static final SourceStateEventType DIRTY = new SourceStateEventType( "DIRTY" );

    /** Indicates that a new data has been created. */
    public static final SourceStateEventType NEW = new SourceStateEventType( "NEW" );

    /** The type of this event. */
    private final SourceStateEventType type;

    /**
     * Constructs a source change Event.
     *
     * @param source The object on which the Event initially occurred.
     */
    public SourceStateEvent( final Object source, final SourceStateEventType type )
    {
        super( source );
        this.type = type;
    }

    /**
     * Returns the source state type registered with this event.
     *
     * @return the type
     */
    public SourceStateEventType getType()
    {
        return type;
    }

    public static class SourceStateEventType
    {
        private final String key;

        private SourceStateEventType( final String key )
        {
            this.key = key;
        }

        public String toString()
        {
            return key;
        }
    }
}
