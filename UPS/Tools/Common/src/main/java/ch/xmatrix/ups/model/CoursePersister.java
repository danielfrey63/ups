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
package ch.xmatrix.ups.model;

import ch.jfactory.jgoodies.model.DirtyCapableModel;
import ch.jfactory.model.SimpleModelList;
import com.thoughtworks.xstream.XStream;
import javax.swing.ListModel;

/**
 * Holds constraints.
 *
 * @author Daniel Frey
 * @version $Revision: 1.2 $ $Date: 2008/01/06 10:16:20 $
 */
public class CoursePersister extends DirtyCapableModel
{

    private static XStream converter;

    private static ListModel constraints;

    public static XStream getConverter()
    {
        if (converter == null)
        {
            converter = SimpleModelList.getConverter();
            converter.setMode(XStream.ID_REFERENCES);
            converter.alias("courses", SimpleModelList.class);
            converter.alias("course", CourseModel.class);
            converter.aliasField("lknr", CourseModel.class, "uid");
        }
        return converter;
    }
}
