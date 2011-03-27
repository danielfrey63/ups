/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.logging;

/**
 * Exception thrown by the LogFactory.
 *
 * @author Daniel Frey 05.03.2010 09:52:59
 */
public class LogFactoryException extends Exception
{
    public LogFactoryException( final String message, final Throwable cause )
    {
        super( message, cause );
    }
}
