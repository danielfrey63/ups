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
package ch.jfactory.xstream;

import com.thoughtworks.xstream.io.HierarchicalStreamDriver;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.StreamException;
import com.thoughtworks.xstream.io.xml.DomReader;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Dom driver supporting an EntityResolver
 *
 * @author Daniel Frey
 * @version $Revision: 1.1 $ $Date: 2007/09/27 10:41:22 $
 */
public class DomDriver implements HierarchicalStreamDriver
{
    /**
     * The encoding to use for the xml interpretation.
     */
    private final String encoding;

    /**
     * The factory to create new dom document.
     */
    private final DocumentBuilderFactory documentBuilderFactory;

    /**
     * The entity resolver.
     */
    private final EntityResolver entityResolver;

    /**
     * The default encoding.
     */
    private static final String DEFAULT_ENCODING = "UTF-8";

    /**
     * Creates a dom driver without resolver.
     *
     * @param encoding the encoding
     */
    public DomDriver( final String encoding )
    {
        this( encoding, null );
    }

    /**
     * Creates a dom driver with default enconding (UTF-8) and no entity resolver.
     */
    public DomDriver()
    {
        this( DEFAULT_ENCODING, null );
    }

    /**
     * Creates a dom driver with a entity resolver.
     *
     * @param entityResolver the resolver to set
     */
    public DomDriver( final EntityResolver entityResolver )
    {
        this( DEFAULT_ENCODING, entityResolver );
    }

    /**
     * Creates a dom driver with an entity resolver and an encoding.
     *
     * @param encoding       the encoding to set
     * @param entityResolver the resolver to set
     */
    public DomDriver( final String encoding, final EntityResolver entityResolver )
    {
        documentBuilderFactory = DocumentBuilderFactory.newInstance();
        this.encoding = encoding;
        this.entityResolver = entityResolver;
    }

    public HierarchicalStreamReader createReader( final Reader xml )
    {
        return createReader( new InputSource( xml ) );
    }

    public HierarchicalStreamReader createReader( final InputStream xml )
    {
        return createReader( new InputSource( xml ) );
    }

    private HierarchicalStreamReader createReader( final InputSource source )
    {
        try
        {
            final DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            documentBuilder.setEntityResolver( entityResolver );
            source.setEncoding( encoding );
            final Document document = documentBuilder.parse( source );
            return new DomReader( document );
        }
        catch ( FactoryConfigurationError e )
        {
            throw new StreamException( e );
        }
        catch ( ParserConfigurationException e )
        {
            throw new StreamException( e );
        }
        catch ( SAXException e )
        {
            throw new StreamException( e );
        }
        catch ( IOException e )
        {
            throw new StreamException( e );
        }
    }

    public HierarchicalStreamWriter createWriter( final Writer out )
    {
        return new PrettyPrintWriter( out );
    }

    public HierarchicalStreamWriter createWriter( final OutputStream out )
    {
        return createWriter( new OutputStreamWriter( out ) );
    }
}
