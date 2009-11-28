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
package ch.xmatrix.ups.view.renderer;

import ch.jfactory.resource.Strings;
import com.jgoodies.uif.panel.SimpleInternalFrame;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JToolBar;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.4 $ $Date: 2006/04/21 11:02:52 $
 */
public class I15nSimpleInternalFrame extends SimpleInternalFrame
{
    private String resourceKey;

    /**
     * Constructs a <code>SimpleInternalFrame</code> with the specified icon, and title.
     *
     * @param icon        the initial icon
     * @param resourceKey the base resourceKey for looking up the strings
     */
    public I15nSimpleInternalFrame( final Icon icon, final String resourceKey )
    {
        super( icon, Strings.getString( resourceKey + ".title" ) );
        initThis( resourceKey );
        initLayout();
    }

    /**
     * Constructs a <code>SimpleInternalFrame</code> with the specified icon, key, tool bar, and content panel.
     *
     * @param icon        the initial icon
     * @param resourceKey the base resourceKey for looking up the strings
     * @param bar         the initial tool bar
     * @param content     the initial content pane
     */
    public I15nSimpleInternalFrame( final Icon icon, final String resourceKey, final JToolBar bar, final JComponent content )
    {
        super( icon, Strings.getString( resourceKey + ".title" ), bar, content );
        initThis( resourceKey );
        initLayout();
    }

    /**
     * Constructs a <code>SimpleInternalFrame</code> with the specified key.
     *
     * @param resourceKey the base resourceKey for looking up the strings
     */
    public I15nSimpleInternalFrame( final String resourceKey )
    {
        super( Strings.getString( resourceKey + ".title" ) );
        initThis( resourceKey );
        initLayout();
    }

    /**
     * Constructs a <code>SimpleInternalFrame</code> with the specified key, tool bar, and content panel.
     *
     * @param resourceKey the base resourceKey for looking up the strings
     * @param bar         the initial tool bar
     * @param content     the initial content pane
     */
    public I15nSimpleInternalFrame( final String resourceKey, final JToolBar bar, final JComponent content )
    {
        super( Strings.getString( resourceKey + ".title" ), bar, content );
        initThis( resourceKey );
        initLayout();
    }

    /**
     * Constructs a <code>SimpleInternalFrame</code> with the specified icon, and title.
     *
     * @param icon        the initial icon
     * @param resourceKey the base resourceKey for looking up the strings
     */
    public I15nSimpleInternalFrame( final Icon icon, final String resourceKey, final Border border )
    {
        super( icon, Strings.getString( resourceKey + ".title" ) );
        initThis( resourceKey );
        initLayout( border );
    }

    /**
     * Constructs a <code>SimpleInternalFrame</code> with the specified icon, key, tool bar, and content panel.
     *
     * @param icon        the initial icon
     * @param resourceKey the base resourceKey for looking up the strings
     * @param bar         the initial tool bar
     * @param content     the initial content pane
     */
    public I15nSimpleInternalFrame( final Icon icon, final String resourceKey, final JToolBar bar, final JComponent content, final Border border )
    {
        super( icon, Strings.getString( resourceKey + ".title" ), bar, content );
        initThis( resourceKey );
        initLayout( border );
    }

    /**
     * Constructs a <code>SimpleInternalFrame</code> with the specified key.
     *
     * @param resourceKey the base resourceKey for looking up the strings
     */
    public I15nSimpleInternalFrame( final String resourceKey, final Border border )
    {
        super( Strings.getString( resourceKey + ".title" ) );
        initThis( resourceKey );
        initLayout( border );
    }

    /**
     * Constructs a <code>SimpleInternalFrame</code> with the specified key, tool bar, and content panel.
     *
     * @param resourceKey the base resourceKey for looking up the strings
     * @param bar         the initial tool bar
     * @param content     the initial content pane
     */
    public I15nSimpleInternalFrame( final String resourceKey, final JToolBar bar, final JComponent content, final Border border )
    {
        super( Strings.getString( resourceKey + ".title" ), bar, content );
        initThis( resourceKey );
        initLayout( border );
    }

    protected String getResourceKey()
    {
        return resourceKey;
    }

    private void initThis( final String resourceKey )
    {
        this.resourceKey = resourceKey;
    }

    protected void initLayout()
    {
        initLayout( new EmptyBorder( 0, 0, 0, 0 ) );
    }

    protected void initLayout( final Border innerBorder )
    {
        setBorder( new CompoundBorder( innerBorder, getBorder() ) );
    }
}
