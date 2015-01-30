/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */

/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.cache;

/**
 * Exception during image caching. Overwrites equals so it returns true if the messages of the causes are the same.
 *
 * @author Daniel Frey 05.02.11 14:31
 */
public class ImageRetrieveException extends Exception
{
    public ImageRetrieveException( final String message, final Throwable cause )
    {
        super( message, cause );
    }

    public boolean equals( final Object other )
    {
        if ( !(other instanceof ImageRetrieveException) )
        {
            return false;
        }
        final Throwable otherCause = ((ImageRetrieveException) other).getCause();
        final Throwable cause = getCause();
        return cause.getMessage().equals( otherCause.getMessage() );
    }
}
