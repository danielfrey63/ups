package ch.jfactory.xml;

import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Properties;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.commons.io.output.NullOutputStream;
import org.apache.log4j.Logger;

/**
 * // TODO: Comment
 *
 * @author Daniel Frey
 */
public class XMLUtils
{
    private static final Logger LOG = Logger.getLogger( XMLUtils.class );

    /**
     * This method applies the xslFile to inFile but does not write an output. Useful if the xslt script itself
     * generates the output files. In addition it passes the given properties to the xslt file.
     *
     * @param inFile  input file
     * @param xslFile xslt file
     * @param params  the parameters to pass to the xslt file
     * @throws javax.xml.transform.TransformerException
     *                     uppon error
     * @throws IOException uppon error
     */
    public static void transformNoOut( final String inFile, final String xslFile, final Properties params )
            throws IOException, TransformerException
    {
        Reader input = null;
        Writer output = null;
        Reader xslt = null;
        try
        {
            input = new FileReader( inFile );
            output = new OutputStreamWriter( new NullOutputStream() );
            xslt = new FileReader( xslFile );
            transform( input, output, xslt, params );
        }
        finally
        {
            if ( input != null )
            {
                input.close();
            }
            if ( output != null )
            {
                output.close();
            }
            if ( xslt != null )
            {
                xslt.close();
            }
        }
    }

    /**
     * This method applies the xslFilename to a null input file and writes the output the the given output file. Useful
     * if the xslt script itself reades directly from (different) intput files. In addition it passes the given
     * properties to the xslt file.
     *
     * @param output
     * @param xslt
     * @param params the parameters to pass to the xslt file
     * @throws javax.xml.transform.TransformerException
     *
     */
    public static void transformNoIn( final Writer output, final Reader xslt, final Properties params )
            throws IOException, TransformerException
    {
        Reader input = null;
        try
        {
            input = new InputStreamReader( new ByteArrayInputStream( "<DummyRoot/>".getBytes() ) );
            transform( input, output, xslt, params );
        }
        finally
        {
            if ( input != null )
            {
                input.close();
            }
        }
    }

    /**
     * Transforms the given in stream into the given out stream with the given xslt file.
     *
     * @param in     string for resource will be loaded using getResourceAsStream
     * @param out    as output file stream
     * @param xsl    string for resource will be loaded using getResourceAsStream
     * @param params the parameters to pass to the xslt script
     * @throws java.io.IOException
     * @throws javax.xml.transform.TransformerException
     *
     */
    public static void transform( final String in, final String out, final String xsl, final Properties params )
            throws IOException, TransformerException
    {
        Reader input = null;
        Writer output = null;
        Reader xslt = null;
        try
        {
            input = new InputStreamReader( XMLUtils.class.getResourceAsStream( in ) );
            xslt = new InputStreamReader( XMLUtils.class.getResourceAsStream( xsl ) );
            output = new FileWriter( out );
            transform( input, output, xslt, params );
        }
        finally
        {
            if ( input != null )
            {
                input.close();
            }
            if ( output != null )
            {
                output.close();
            }
            if ( xslt != null )
            {
                xslt.close();
            }
        }
    }

    public static void transform( final Reader input, final Writer output, final Reader xslt,
                                  final Properties params ) throws TransformerException
    {
        transform( input, output, xslt, params, null, TransformerFactory.newInstance() );
    }

    /**
     * Transforms the given input Reader with the given xslt Reader and writes the result to the Writer. The
     * transformation factory is configured using the properties and the uri resolver given.
     *
     * @param input    the source xml document reader.
     * @param output   the destination document writer.
     * @param xslt     the xslt stylesheet to perform the transformation.
     * @param params   the parameters to configure transformation. if no parameters are given, pass empty properties,
     *                 otherwise a NullPointerException will be thrown.
     * @param resolver the uri resolver or null.
     * @param factory  the tranformer factory, may not be null.
     * @throws TransformerException
     * @throws NullPointerException if the parameters are null
     */
    public static void transform( final Reader input, final Writer output, final Reader xslt, final Properties params,
                                  final URIResolver resolver, final TransformerFactory factory
    ) throws TransformerException
    {
        // Use the factory to create a template containing the xsl file
        final Templates template = factory.newTemplates( new StreamSource( xslt ) );

        // Use the template to create a transformer
        final Transformer xformer = template.newTransformer();
        xformer.setErrorListener( new ErrorListener()
        {
            public void error( final TransformerException e ) throws TransformerException
            {
                LOG.error( e.getMessageAndLocation(), e );
                throw e;
            }

            public void fatalError( final TransformerException e ) throws TransformerException
            {
                LOG.fatal( e.getMessageAndLocation(), e );
                throw e;
            }

            public void warning( final TransformerException e )
            {
                LOG.warn( e.getMessage() );
            }
        } );

        // Setup parameters
        final Enumeration keys = params.keys();
        while ( keys.hasMoreElements() )
        {
            final String key = (String) keys.nextElement();
            final String value = (String) params.get( key );
            xformer.setParameter( key, value );
        }
        if ( resolver != null )
        {
            xformer.setURIResolver( resolver );
        }

        // Prepare the input and output files
        final Source source = new StreamSource( input );
        final Result result = new StreamResult( output );

        // Apply the xsl file to the source file and write the result to the output file
        xformer.transform( source, result );
    }
}
