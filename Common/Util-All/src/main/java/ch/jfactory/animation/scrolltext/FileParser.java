/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.animation.scrolltext;

import java.awt.Graphics;
import java.util.List;

/**
 * Line parsing interface. Used to parse a list of lines into an array of {@link FileLine} objects.
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:57 $
 */
public interface FileParser
{
    /**
     * Parses the given lines into FileLine objects.
     *
     * @param fileLines a List of lines
     * @return an array of FileLine objects
     */
    FileLine[] initText( List fileLines );

    /**
     * Returns the Formatter objects used for string formatting. May be called in some implementations after {@link FileParser#initText(List)}.
     *
     * @param g the graphics
     * @return an array of Formatter objects
     */
    Formatter[] getFormatters( Graphics g );
}
