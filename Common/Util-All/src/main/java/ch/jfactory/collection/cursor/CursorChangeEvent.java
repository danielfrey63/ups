/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.collection.cursor;

/**
 * Class holding information about the CursorChangeEvent.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.2 $
 */
public class CursorChangeEvent
{
    public CursorChangeEvent( final Cursor cursor )
    {
        this.cursor = cursor;
    }

    /**
     * return a reference to the cursor object.
     *
     * @return reference to cursor object
     */
    public Cursor getCursor()
    {
        return cursor;
    }

    /**
     * return reference to current object.
     *
     * @return the current object
     */
    public Object getCurrentObject()
    {
        return cursor.getCurrent();
    }

    private final Cursor cursor;
}
