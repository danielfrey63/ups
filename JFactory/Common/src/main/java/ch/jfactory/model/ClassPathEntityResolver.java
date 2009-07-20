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
package ch.jfactory.model;

import java.io.IOException;
import java.io.InputStream;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2007/09/27 10:41:22 $
 */
public class ClassPathEntityResolver implements EntityResolver {

    private final String encoding;
    private static final String DEFAULT_ENCODING = "UTF-8";

    public ClassPathEntityResolver() {
        this(DEFAULT_ENCODING);
    }

    public ClassPathEntityResolver(final String encoding) {
        this.encoding = encoding;
    }

    public InputSource resolveEntity(final String publicId, final String systemId) throws SAXException, IOException {
        String resource = null;
        if (systemId.startsWith("classpath:")) {
            resource = systemId.substring(10);
        }
        else if (systemId.startsWith("jar:")) {
            resource = systemId.substring(4);
        }
        final InputSource source;
        if (resource != null) {
            final InputStream stream = DomDriver.class.getResourceAsStream(resource);
            source = new InputSource(stream);
            source.setEncoding(encoding);
        }
        else {
            source = null;
        }
        return source;
    }
}
