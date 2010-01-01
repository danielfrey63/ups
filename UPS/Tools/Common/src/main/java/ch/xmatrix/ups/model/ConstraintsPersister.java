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

import ch.jfactory.convert.Converter;
import ch.jfactory.jgoodies.model.DirtyCapableModel;
import ch.jfactory.model.SimpleModelList;
import ch.jfactory.xstream.XStreamConverter;
import ch.xmatrix.ups.domain.Constraint;
import ch.xmatrix.ups.domain.Constraints;
import java.util.HashMap;
import java.util.Map;
import javax.swing.AbstractListModel;

/**
 * Holds constraints.
 *
 * @author Daniel Frey
 * @version $Revision: 1.2 $ $Date: 2007/05/16 17:00:15 $
 */
public class ConstraintsPersister extends DirtyCapableModel
{
    private static Converter<SimpleModelList> converter;

    public static Converter<SimpleModelList> getConverter()
    {
        if ( converter == null )
        {
            final Map<String, Class> aliases = new HashMap<String, Class>();
            aliases.put( "constraintsModels", SimpleModelList.class );
            aliases.put( "constraintsModel", Constraints.class );
            aliases.put( "constraintModel", Constraint.class );

            final Map<Class, String> implicitCollections = new HashMap<Class, String>();
            implicitCollections.put( SimpleModelList.class, "models" );
            implicitCollections.put( Constraints.class, "constraints" );
            implicitCollections.put( Constraint.class, "taxa" );

            final Map<Class, String> omits = new HashMap<Class, String>();
            omits.put( AbstractListModel.class, "listenerList" );

            final Map<String, XStreamConverter.NamedAlias> namedFields = new HashMap<String, XStreamConverter.NamedAlias>();
            namedFields.put( "index", new XStreamConverter.NamedAlias( Constraints.class, "indexToConstraints" ) );

            converter = new XStreamConverter<SimpleModelList>( aliases, implicitCollections, omits, namedFields );
        }
        return converter;
    }
}
