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

import ch.jfactory.application.AbstractMainModel;
import ch.jfactory.component.Dialogs;
import ch.jfactory.model.SimpleModelList;
import ch.xmatrix.ups.controller.Loader;
import ch.xmatrix.ups.model.ConstraintsPersister;
import ch.xmatrix.ups.model.CoursePersister;
import ch.xmatrix.ups.model.SessionPersister;
import ch.xmatrix.ups.model.TaxonModels;
import com.jgoodies.binding.list.SelectionInList;
import javax.swing.JFrame;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.9 $ $Date: 2007/09/27 10:48:43 $
 */
public class MainModel extends AbstractMainModel
{

    public static final String MODELID_CONSTRAINTS = "constraints";

    public static final String MODELID_COURSES = "courses";

    public static final String MODELID_SESSIONS = "sessions";

    public static final String MODELID_TAXONTREES = "taxonTrees";

    public static final String CARDS_EDIT = "editCard";

    public static final MainModel DEFAULT = new MainModel();

    public SelectionInList sessionModels = new SelectionInList();

    public SelectionInList constraintsModels = new SelectionInList();

    public SelectionInList taxonModels = new SelectionInList();

    public SelectionInList courseModels = new SelectionInList();

    private UserModel userModel;

    private JFrame mainFrame;

    public MainModel()
    {

        final SimpleModelList taxonTrees = TaxonModels.getTaxonTrees();
        registerModels(MODELID_TAXONTREES, taxonTrees);
        taxonModels.setListModel(taxonTrees);

        final SimpleModelList sessions = Loader.loadModel("/data/sessions.xml", "ups/ust", SessionPersister.getConverter());
        registerModels(MODELID_SESSIONS, sessions);
        sessionModels.setListModel(sessions);

        final SimpleModelList constraints = Loader.loadModel("/data/constraints.xml", "ups/ust", ConstraintsPersister.getConverter());
        registerModels(MODELID_CONSTRAINTS, constraints);
        constraintsModels.setListModel(constraints);

        final SimpleModelList courses = Loader.loadModel("/data/courses.xml", "ups/ust", CoursePersister.getConverter());
        registerModels(MODELID_COURSES, courses);
        courseModels.setListModel(courses);
    }

    public UserModel getUserModel()
    {
        return userModel;
    }

    public void setUserModel(final UserModel userModel)
    {
        this.userModel = userModel;
    }

    public JFrame getMainFrame()
    {
        return mainFrame;
    }

    public void setMainFrame(final JFrame frame)
    {
        mainFrame = frame;
    }

    public boolean modelValid()
    {
        final int numberOfSessions = findModelById(MODELID_SESSIONS).getSize();
        if (numberOfSessions == 0)
        {
            Dialogs.showErrorMessage(mainFrame.getRootPane(),
                    "Fehler", "Sorry: Es stehen im Moment keine Vorgaben- und Taxonomie-Modelle zur Verfügung");
            return false;
        }
        return true;
    }
}
