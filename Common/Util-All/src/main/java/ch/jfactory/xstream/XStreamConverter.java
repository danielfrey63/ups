/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.xstream;

import ch.jfactory.convert.Converter;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.extended.SqlTimestampConverter;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

/**
 * Converter implementation for XStream.
 *
 * @author Daniel Frey 01.01.2010 14:36:52
 */
public class XStreamConverter<T> implements Converter<T>
{
    private final XStream xstream = new XStream();

    public XStreamConverter( final Map<String, Class> aliases )
    {
        this( aliases, new HashMap<Class, String>(), new HashMap<Class, String>() );
    }

    public XStreamConverter( final Map<String, Class> aliases, final Map<Class, String> implicitCollections )
    {
        this( aliases, implicitCollections, new HashMap<Class, String>() );
    }

    public XStreamConverter( final Map<String, Class> aliases, final Map<Class, String> implicitCollections, final Map<Class, String> omits )
    {
        this( aliases, implicitCollections, omits, new HashMap<String, NamedAlias>() );
    }

    public XStreamConverter( final Map<String, Class> aliases, final Map<Class, String> implicitCollections,
                             final Map<Class, String> omits, final Map<String, NamedAlias> aliasesWithNames )
    {
        XStream.setupDefaultSecurity( xstream );
        xstream.registerConverter( new SqlTimestampConverter() );
        xstream.registerConverter( new ValidatingDateConverter(
                "yyyyMMddHHmmssSSS", "yyyyMMddHHmmssSSS", "yyyyMMddHHmmss", "yyyyMMddHHmm",
                "yyyy-MM-dd HH:mm:ss.S z", "yyyy-MM-dd HH:mm:ss.S a",
                "yyyy-MM-dd HH:mm:ssz", "yyyy-MM-dd HH:mm:ss z", // JDK 1.3 needs both versions
                "yyyy-MM-dd HH:mm:ssa" ) );
        xstream.setMode( XStream.ID_REFERENCES );
        for ( final String alias : aliases.keySet() )
        {
            xstream.alias( alias, aliases.get( alias ) );
        }
        for ( final Class implicitCollection : implicitCollections.keySet() )
        {
            xstream.addImplicitCollection( implicitCollection, implicitCollections.get( implicitCollection ) );
        }
        for ( final Class omit : omits.keySet() )
        {
            xstream.omitField( omit, omits.get( omit ) );
        }
        for ( final String aliasWithName : aliasesWithNames.keySet() )
        {
            final NamedAlias namedAlias = aliasesWithNames.get( aliasWithName );
            xstream.aliasField( aliasWithName, namedAlias.getClazz(), namedAlias.getName() );
        }
    }

    @SuppressWarnings( "unchecked" )
    public T from( final Reader reader )
    {
        return (T) xstream.fromXML( reader );
    }

    public String from( final T object )
    {
        return xstream.toXML( object );
    }

    public static class NamedAlias
    {
        private final Class clazz;

        private final String name;

        public NamedAlias( final Class clazz, final String name )
        {
            this.clazz = clazz;
            this.name = name;
        }

        public Class getClazz()
        {
            return clazz;
        }

        public String getName()
        {
            return name;
        }
    }
}
