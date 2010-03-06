package ch.jfactory.xstream;

import ch.jfactory.convert.Converter;
import ch.jfactory.model.ClassPathEntityResolver;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.basic.DateConverter;
import com.thoughtworks.xstream.converters.extended.ISO8601DateConverter;
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
    private final XStream xstream = new XStream( new DomDriver( "windows-1252", new ClassPathEntityResolver( "windows-1252" ) ) );

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
        xstream.registerConverter( new ISO8601DateConverter() );
        xstream.registerConverter( new SqlTimestampConverter() );
        xstream.registerConverter( new DateConverter( "yyyyMMddHHmmssSSS", new String[]{
                "yyyyMMddHHmmssSSS", "yyyyMMddHHmmss", "yyyyMMddHHmm"} ) );
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

    public static class NamedAlias {
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
