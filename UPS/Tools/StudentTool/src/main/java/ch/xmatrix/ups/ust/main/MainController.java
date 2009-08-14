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
package ch.xmatrix.ups.ust.main;

import ch.jfactory.application.AbstractMainController;
import ch.jfactory.application.AbstractMainModel;
import ch.jfactory.binding.InfoModel;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.7 $ $Date: 2006/07/27 16:38:58 $
 */
public class MainController extends AbstractMainController
{

    private static final Category LOGGER = Logger.getInstance(MainController.class);

    private MainModel model;

    public MainController(final MainModel model, final InfoModel infoModel)
    {
        super(model, infoModel);
        this.model = model;
    }

    protected void initModel(final AbstractMainModel model)
    {
    }
}
