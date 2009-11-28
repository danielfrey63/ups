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
package ch.xmatrix.ups.view;

import ch.jfactory.resource.Strings;
import javax.swing.Icon;
import javax.swing.JLabel;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.2 $ $Date: 2006/04/21 11:02:52 $
 */
public class I15nLabel extends JLabel
{
    public I15nLabel()
    {
        super();
    }

    public I15nLabel( final Icon image )
    {
        super( image );
    }

    public I15nLabel( final Icon image, final int horizontalAlignment )
    {
        super( image, horizontalAlignment );
    }

    public I15nLabel( final String key )
    {
        super( Strings.getString( key ) );
    }

    public I15nLabel( final String key, final int horizontalAlignment )
    {
        super( Strings.getString( key ), horizontalAlignment );
    }

    public I15nLabel( final String key, final Icon icon, final int horizontalAlignment )
    {
        super( Strings.getString( key ), icon, horizontalAlignment );
    }

    public void setI15nText( final String key )
    {
        super.setText( Strings.getString( key ) );
    }
}
