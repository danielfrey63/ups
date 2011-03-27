/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.resource;

import java.awt.Color;
import org.apache.commons.beanutils.MethodUtils;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:58 $
 */
public class ResourceHelper
{
    /**
     * Decodes a string given. The string might be a hexadecimal representation of a color (starting with '#') or a name of a color as specified in the Color class constants.
     *
     * @param value the string to translate
     * @return the color representing the string or null, if none can be found
     */
    public static Color decode( final String value )
    {
        if ( value.startsWith( "#" ) )
        {
            return Color.decode( value );
        }
        else
        {
            final Class clazz = Color.class;
            return (Color) getConstant( clazz, value, clazz );
        }
    }

    public static Object getConstant( final Class clazz, final String value, final Class resultType )
    {
        try
        {
            final Object fieldValue = clazz.getField( value ).get( null );
            final Class fieldClass = fieldValue.getClass();
            final Class wrapper;
            if ( resultType.isPrimitive() )
            {
                wrapper = MethodUtils.getPrimitiveWrapper( resultType );
            }
            else
            {
                wrapper = resultType;
            }
            if ( fieldClass.isAssignableFrom( wrapper ) )
            {
                return fieldValue;
            }
            else
            {
                return null;
            }
        }
        catch ( Exception e )
        {
            return null;
        }
    }
}
