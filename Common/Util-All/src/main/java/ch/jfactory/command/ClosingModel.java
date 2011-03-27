/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.command;

import java.beans.PropertyVetoException;

/**
 * Implement this interface to signal that a model is capable of handle closing of the application or a current modification of some model data.
 *
 * @author Daniel Frey
 * @version $Revision: 1.3 $ $Date: 2006/03/14 21:27:55 $
 */
public interface ClosingModel
{
    /**
     * Indicates that currently modfied data is about to be discarded. This method fires a vetoable property change event and a property change event. The closing state is set to false after the last property change listener notification. So only calls with the argument set to true make sense. Calls with an argument set to false will fire no events.
     *
     * @throws PropertyVetoException if a vetoable listener throws one
     */
    void setClosing() throws PropertyVetoException;
}
