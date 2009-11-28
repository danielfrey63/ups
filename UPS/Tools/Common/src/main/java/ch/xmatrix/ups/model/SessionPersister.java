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

import ch.jfactory.jgoodies.model.DirtyCapableModel;
import ch.jfactory.model.SimpleModelList;
import com.thoughtworks.xstream.XStream;

/**
 * Holds constraints.
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2007/05/16 17:00:15 $
 */
public class SessionPersister extends DirtyCapableModel
{
    private static XStream converter;

    public static XStream getConverter()
    {
        if ( converter == null )
        {
            converter = SimpleModelList.getConverter();
            converter.setMode( XStream.NO_REFERENCES );
            converter.alias( "sessions", SimpleModelList.class );
            converter.alias( "session", SessionModel.class );
        }
        return converter;
    }
}
