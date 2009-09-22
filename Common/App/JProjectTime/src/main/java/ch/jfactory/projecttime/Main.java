/* ====================================================================
 *  Copyright 2004-2005 www.xmatrix.ch
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 * ====================================================================
 */

package ch.jfactory.projecttime;

import ch.jfactory.application.MainRunner;
import ch.jfactory.projecttime.main.MainModel;
import ch.jfactory.projecttime.main.MainFrame;
import com.jgoodies.uif.AbstractFrame;

/**
 * Main application frame.
 *
 * @author <a href="daniel.frey@xmatrix.ch">Daniel Frey</a>
 * @version $Revision: 1.3 $ $Date: 2006/11/16 13:25:17 $
 */
public final class Main extends MainRunner {

    public static void main(final String[] args) {
        new Main();
    }

    public AbstractFrame createMainFrame() {
        return new MainFrame((MainModel) getMainModel());
    }
}
