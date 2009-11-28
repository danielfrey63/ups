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

import ch.jfactory.binding.InfoModel;
import com.jgoodies.uif.AbstractFrame;
import com.jgoodies.uif.application.Application;
import javax.swing.JComponent;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.7 $ $Date: 2007/05/16 17:00:16 $
 */
public class MainFrame extends AbstractFrame
{
    private final MainModel model;

    private final InfoModel infoModel;

    /**
     * Constructs an <code>AbstractMainFrame</code> with the specified title.
     *
     * @param mainModel the applications model
     */
    public MainFrame( final MainModel mainModel, final InfoModel infoModel )
    {
        super( Application.getDescription().getWindowTitle() );
        this.model = mainModel;
        this.infoModel = infoModel;
        mainModel.setMainFrame( this );
    }

    /**
     * Builds all the content, consisting of a border, a card panel, and a status bar.
     *
     * @return the content
     */
    protected JComponent buildContentPane()
    {
        final MainBuilder builder = new MainBuilder( model, infoModel );
        return builder.getPanel();
    }

    protected void configureCloseOperation()
    {
        setDefaultCloseOperation( DO_NOTHING_ON_CLOSE );
        addWindowListener( Application.getApplicationCloseOnWindowClosingHandler() );
    }

    public String getWindowID()
    {
        return "UPSStudentToolMainFrame";
    }
}
