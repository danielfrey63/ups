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
import ch.xmatrix.ups.domain.Constraint;
import ch.xmatrix.ups.domain.Constraints;
import com.thoughtworks.xstream.XStream;
import javax.swing.ListModel;

/**
 * Holds constraints.
 *
 * @author Daniel Frey
 * @version $Revision: 1.2 $ $Date: 2007/05/16 17:00:15 $
 */
public class ConstraintsPersister extends DirtyCapableModel {

    private static XStream converter;
    private static ListModel constraints;

    public static XStream getConverter() {
        if (converter == null) {
            converter = SimpleModelList.getConverter();
            converter.setMode(XStream.ID_REFERENCES);
            converter.alias("constraintsModels", SimpleModelList.class);
            converter.alias("constraintsModel", Constraints.class);
            converter.alias("constraintModel", Constraint.class);
            converter.aliasField("index", Constraints.class, "indexToConstraints");
            converter.addImplicitCollection(Constraints.class, "constraints");
            converter.addImplicitCollection(Constraint.class, "taxa");
        }
        return converter;
    }
}
