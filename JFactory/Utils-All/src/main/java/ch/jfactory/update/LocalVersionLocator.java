package ch.jfactory.update;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import org.apache.log4j.Category;


/**
 * This class is used to find update information on the local disk.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.2 $ $Date: 2007/09/27 10:41:22 $
 */
public class LocalVersionLocator {

    private static final Category CAT = Category.getInstance(LocalVersionLocator.class);
    private static final String NAME = "META-INF/VersionInfo.xml";

    private LocalVersionLocator() {
    }
    
    /**
     * This method returns a list of all updatable modules under the given location.
     *
     * @return a list of VersionInfo objects
     */
    public static List<VersionInfo> locateVersions() {
        final List<VersionInfo> result = new ArrayList<VersionInfo>();
        final Map<String, VersionInfo> map = locateVersionsAsMap();
        result.addAll(map.values());
        return result;
    }

    public static Map<String, VersionInfo> locateVersionsAsMap() {
        final Map<String, VersionInfo> map = new HashMap<String, VersionInfo>();
        try {
            final XStream xStream = new XStream(new DomDriver());
            xStream.alias("VersionInfo", VersionInfo.class);
            final Enumeration jarsEnum = Thread.currentThread().getContextClassLoader().getResources(NAME);
            while (jarsEnum.hasMoreElements()) {
                final URL url = (URL) jarsEnum.nextElement();
                final InputStream inputStream = url.openStream();
                final VersionInfo versionInfo = (VersionInfo) xStream.fromXML(inputStream);
                map.put(versionInfo.getName(), versionInfo);
            }
        }
        catch (IOException e) {
            CAT.error("could not load versions", e);
        }
        return map;
    }
}
