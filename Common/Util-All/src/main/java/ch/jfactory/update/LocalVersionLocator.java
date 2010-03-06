package ch.jfactory.update;

import ch.jfactory.xstream.XStreamConverter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    private static final String NAME = "META-INF/VersionInfo.xml";

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
            final XStreamConverter<VersionInfo> converter = new XStreamConverter<VersionInfo>( aliases );
            final Enumeration jarsEnum = Thread.currentThread().getContextClassLoader().getResources( NAME );
            while ( jarsEnum.hasMoreElements() )
            {
                final URL url = (URL) jarsEnum.nextElement();
                final InputStream inputStream = url.openStream();
                final InputStreamReader reader = new InputStreamReader( inputStream );
                final VersionInfo versionInfo = converter.from( reader );
                reader.close();
                map.put( versionInfo.getName(), versionInfo );
            }
        }
        catch ( IOException e )
        {
            LOGGER.error( "could not load versions", e );
        }
        return map;
    }
}
