/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.reflection;

import java.lang.reflect.Field;

/** Todo: document $Author: daniel_frey $ $Revision: 1.1 $ */
public class ReflectionUtils
{
    /**
     * Retrieve a value from a static class field.
     *
     * @param name       Name of the field
     * @param def        Default to return if field is not found or exception occurs
     * @param fieldClass Class to retrieve from
     * @param fieldType  Type of field
     * @return
     */
    public static Object getField( final String name, final Object def, final Class fieldClass, final Class fieldType )
    {
        if ( name == null )
        {
            return def;
        }
        else
        {
            final Field[] fields = fieldClass.getFields();
            for ( final Field field : fields )
            {
                if ( field.getType() == fieldType && field.getName().equalsIgnoreCase( name ) )
                {
                    try
                    {
                        return field.get( null );
                    }
                    catch ( IllegalArgumentException e )
                    {
                        return def;
                    }
                    catch ( IllegalAccessException e )
                    {
                        return def;
                    }
                }
            }
        }
        return def;
    }
}
