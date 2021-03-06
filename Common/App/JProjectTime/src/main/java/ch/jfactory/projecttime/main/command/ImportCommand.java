/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package ch.jfactory.projecttime.main.command;

import ch.jfactory.projecttime.domain.api.IFEntry;
import ch.jfactory.projecttime.main.MainModel;
import com.thoughtworks.xstream.XStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Properties;
import javax.swing.JFileChooser;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.pietschy.command.ActionCommand;
import org.pietschy.command.CommandManager;

/**
 * TODO: document
 *
 * @author <a href="daniel.frey@xmatrix.ch">Daniel Frey</a>
 * @version $Revision: 1.1 $ $Date: 2005/09/04 19:54:10 $
 */
public class ImportCommand extends ActionCommand
{
    private final MainModel model;

    public ImportCommand( final MainModel model, final CommandManager manager )
    {
        super( manager, Commands.FILE_IMPORT );
        this.model = model;
    }

    protected void handleExecute()
    {
        try
        {
            final JFileChooser chooser = new JFileChooser();
            final int ret = chooser.showOpenDialog( null );
            if ( ret == JFileChooser.APPROVE_OPTION )
            {
                final File file = chooser.getSelectedFile();
                final IFEntry root = getRootFromImport( file );
                model.getProjectModel().setRoot( root );
            }
        }
        catch ( IOException e1 )
        {
            e1.printStackTrace();
        }
        catch ( TransformerException e1 )
        {
            e1.printStackTrace();
        }
    }

    private IFEntry getRootFromImport( final File file ) throws IOException, TransformerException
    {
        final File temp1 = File.createTempFile( "trans", ".xml" );
        transform( file, temp1, "Convert1.xslt", new Properties() );
        final File temp2 = File.createTempFile( "trans", ".xml" );
        transform( temp1, temp2, "Convert2.xslt", new Properties() );
        final XStream xstream = Commands.getSerializer();
        final FileInputStream stream = new FileInputStream( temp2 );
        final InputStreamReader reader = new InputStreamReader( stream, "UTF-8" );
        final Object object = xstream.fromXML( reader );
        final IFEntry root = (IFEntry) object;
        stream.close();
        reader.close();
        temp1.delete();
        temp2.delete();
        return root;
    }

    // Todo: merge with xmatrix library
    /**
     * Transforms the given in stream into the given out stream with the given xslt file.
     *
     * @param in     string for resource will be loaded using getResourceAsStream
     * @param out    as output file stream
     * @param xsl    string for resource will be loaded using getResourceAsStream
     * @param params the parameters to pass to the xslt script
     * @throws IOException
     * @throws TransformerException
     */
    public static void transform( final File in, final File out, final String xsl, final Properties params )
            throws IOException, TransformerException
    {
        InputStream input = null;
        OutputStream output = null;
        InputStream xslt = null;
        try
        {
            input = new FileInputStream( in );
            xslt = ImportCommand.class.getResourceAsStream( xsl );
            output = new FileOutputStream( out );
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

    public static void transform( final InputStream input, final OutputStream output, final InputStream xslt,
                                  final Properties params ) throws TransformerException
    {
        // Create transformer factory
        final TransformerFactory factory = TransformerFactory.newInstance();

        // Use the factory to create a template containing the xsl file
        final Templates template = factory.newTemplates( new StreamSource( xslt ) );

        // Use the template to create a transformer
        final Transformer xformer = template.newTransformer();
        xformer.setErrorListener( new ErrorListener()
        {
            public void error( final TransformerException e )
            {
                System.out.println( e.getMessageAndLocation() );
            }

            public void fatalError( final TransformerException e )
            {
                System.out.println( e.getMessageAndLocation() );
            }

            public void warning( final TransformerException e )
            {
                System.out.println( e.getMessageAndLocation() );
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

        // Prepare the input and output files
        final Source source = new StreamSource( input );
        final Result result = new StreamResult( output );

        // Apply the xsl file to the source file and write the result to the output file
        xformer.transform( source, result );
    }
}
