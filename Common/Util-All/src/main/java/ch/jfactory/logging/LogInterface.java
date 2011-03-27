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
 * Interface to log to.
 *
 * @author Daniel Frey 05.03.2010 09:42:08
 */
public interface LogInterface
{
    void info( Object message );

    void info( Object message, Throwable exception );

    void debug( Object message );

    void debug( Object message, Throwable exception );

    void warn( Object message );

    void warn( Object message, Throwable exception );

    void error( Object message );

    void error( Throwable exception );

    void error( Object message, Throwable exception );

    void fatal( Object message );

    void fatal( Throwable exception );

    void fatal( Object message, Throwable exception );

    boolean isInfoEnabled();

    boolean isDebugEnabled();
}
