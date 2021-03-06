/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.component.splash;

import java.awt.Dimension;
import javax.swing.ImageIcon;

/**
 * Simple splash provider.
 *
 * @author Daniel Frey
 * @version $Revision: 1.2 $ $Date: 2006/03/14 21:27:55 $
 */
public interface SplashProvider
{
    /** Start the splash screen. Make sure this method does show the splash window. */
    void start();

    /** Stop the splash screen. Make sure the window is displosed. */
    void stop();

    /**
     * Set the size of the splash window.
     *
     * @param dim the size to set
     */
    void setSize( Dimension dim );

    /**
     * Sets the backgournd image icon.
     *
     * @param backgroundImageIcon the icon to set
     */
    void setBackgroundImage( ImageIcon backgroundImageIcon );
}
