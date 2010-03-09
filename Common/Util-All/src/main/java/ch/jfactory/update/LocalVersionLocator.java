package ch.jfactory.update;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is used to find update information on the local disk.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.2 $ $Date: 2007/09/27 10:41:22 $
 */
public class LocalVersionLocator
{
    private static final Logger LOGGER = LoggerFactory.getLogger( LocalVersionLocator.class );

    private static final String META_INF = "META-INF";

    private static final String VERSION_INFO = META_INF + "/VersionInfo.xml";

    private static final String MANIFEST = META_INF + "/MANIFEST.MF";

    private LocalVersionLocator()
    {
    }

    /**
     * This method returns a list of all updatable modules under the given location.
     *
     * @return a list of VersionInfo objects
     */
    public static List<VersionInfo> locateVersions()
    {
        final List<VersionInfo> result = new ArrayList<VersionInfo>();
        final Map<String, VersionInfo> map = locateVersionsAsMap();
        result.addAll( map.values() );
        return result;
    }

    public static Map<String, VersionInfo> locateVersionsAsMap()
    {
        final Map<String, VersionInfo> map = new HashMap<String, VersionInfo>();
        try
        {
            final Map<String, Class> aliases = new HashMap<String, Class>();
            aliases.put( "VersionInfo", VersionInfo.class );
            final Enumeration jarsEnum = Thread.currentThread().getContextClassLoader().getResources( MANIFEST );
            while ( jarsEnum.hasMoreElements() )
            {
                final URL url = (URL) jarsEnum.nextElement();
                final InputStream inputStream = url.openStream();
                final Manifest manifest = new Manifest( inputStream );
                final VersionInfo info = new VersionInfo();
                final Attributes attributes = manifest.getMainAttributes();
                final String name = attributes.getValue( "Implementation-Vendor-Id" );
                if ( name != null && name.contains( "ch.xmatrix" ) )
                {
                    final String version = attributes.getValue( "Implementation-Version" );
                    info.setName( attributes.getValue( "Implementation-Title" ) );
                    info.setMajorVersion( getPart( version, 0 ) );
                    info.setMinorVersion( getPart( version, 1 ) );
                    info.setBuildVersion( getPart( version, 2 ) );
                    info.setAuthor( attributes.getValue( "Implementation-Vendor" ) );
                    info.setRevision( getPart( version, -1 ) );
                    map.put( getDirectory( url ), info );
                }
                inputStream.close();
            }

        }
        catch ( IOException e )
        {
            LOGGER.error( "could not load versions", e );
        }
        return map;
    }

    private static String getDirectory( final URL url )
    {
        final String string = url.toString();
        return string.substring( 0, string.lastIndexOf( "/" ) );
    }

    private static int getPart( final String version, final int index )
    {
        int result;
        if ( version != null )
        {
            final String[] chunks = version.replaceAll( "-", "." ).replaceAll( "_", "." ).split( "\\." );
            final int pos = index >= 0 ? index : chunks.length + index;
            if ( pos < chunks.length )
            {
                try
                {
                    result = Integer.parseInt( chunks[pos] );
                }
                catch ( NumberFormatException e )
                {
                    result = 0;
                }
            }
            else
            {
                result = 0;
            }
        }
        else
        {
            result = 0;
        }
        return result;
    }
}
