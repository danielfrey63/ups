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

import javax.swing.Icon;
import javax.swing.JLabel;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.2 $ $Date: 2006/04/21 11:02:52 $
 */
public class ObjectLabel extends JLabel
{

    private Object object;

    public ObjectLabel()
    {
        super();
    }

    public ObjectLabel(final Icon image)
    {
        super(image);
    }

    public ObjectLabel(final Icon image, final int horizontalAlignment)
    {
        super(image, horizontalAlignment);
    }

    public ObjectLabel(final Object object)
    {
        super();
        setObject(object);
    }

    public ObjectLabel(final Object object, final int horizontalAlignment)
    {
        super(object.toString(), horizontalAlignment);
        setObject(object);
    }

    public ObjectLabel(final Object object, final Icon icon, final int horizontalAlignment)
    {
        super(icon, horizontalAlignment);
        setObject(object);
    }

    public void setObject(final Object object)
    {
        this.object = object;
        setText(object.toString());
    }

    public Object getObject()
    {
        return object;
    }

    public String toString()
    {
        return getText();
    }
}
