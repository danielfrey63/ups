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

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:57 $
 */
public class SourceVetoedException extends Exception
{
    public SourceVetoedException()
    {
        super();
    }

    public SourceVetoedException( final Throwable cause )
    {
        super( cause );
    }

    public SourceVetoedException( final String message )
    {
        super( message );
    }

    public SourceVetoedException( final String message, final Throwable cause )
    {
        super( message, cause );
    }
}
