/* ====================================================================
 *  Copyright 2004-2005 www.xmatrix.ch
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 * ====================================================================
 */
package ch.xmatrix.ups.pdf;

import ch.xmatrix.ups.domain.PersonData;
import ch.xmatrix.ups.domain.PlantList;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.MultiColumnText;
import com.lowagie.text.pdf.PdfWriter;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.basic.DateConverter;
import com.thoughtworks.xstream.converters.extended.SqlTimestampConverter;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import org.apache.log4j.Logger;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.2 $ $Date: 2008/01/23 22:19:20 $
 */
public class PDFGenerator
{
    private static final Logger LOG = Logger.getLogger( PDFGenerator.class );

    private static final ResourceBundle bundle = ResourceBundle.getBundle( "ch.xmatrix.ups.pdf.Strings" );

    public static final String KEY_USERNAME = "username";

    public static final String KEY_PASSWORD = "password";

    public static final String KEY_FIRSTNAME = "firstname";

    public static final String KEY_FAMILYNAME = "familyname";

    /**
     * The course of the student.
     */
    public static final String KEY_DEPARTMENT = "department";

    /**
     * The identification number of the student.
     */
    public static final String KEY_ID = "id";

    /**
     * Subject field in the generated pdf file.
     */
    public static final String KEY_SUBJECT = "subject";

    /**
     * Author field in the generated pdf file.
     */
    public static final String KEY_AUTHOR = "author";

    /**
     * Title field in the generated pdf file.
     */
    public static final String KEY_TITLE = "title";

    /**
     * Put here a byte array with the xstream-encoded ArrayList of species strings.
     */
    public static final String KEY_SPECIES = "species";

    public static byte[] createPdf( final Properties context ) throws DocumentException, IOException
    {
        byte[] byteArray = null;
        try
        {
            if ( LOG.isDebugEnabled() )
            {
                final Properties copy = new Properties();
                copy.putAll( context );
                copy.remove( KEY_SPECIES );
                final ByteArrayOutputStream stream = new ByteArrayOutputStream();
                final PrintStream printStream = new PrintStream( stream );
                copy.list( printStream );
                final String contextContents = new String( stream.toByteArray() );
                LOG.debug( "writing pdf with context:\n" + contextContents );
            }

            final Document document = new Document( PageSize.A4, 72, 72, 72, 72 );

            final String base1name = PDFGenerator.class.getResource( "/fonts/ETL_____.TTF" ).toString();
            final String base3name = PDFGenerator.class.getResource( "/fonts/ETSB____.TTF" ).toString();

            final BaseFont base1 = BaseFont.createFont( base1name, BaseFont.CP1252, BaseFont.EMBEDDED );
            final BaseFont base3 = BaseFont.createFont( base3name, BaseFont.CP1252, BaseFont.EMBEDDED );

            final ByteArrayOutputStream result = new ByteArrayOutputStream();
            final PdfWriter byteWriter = PdfWriter.getInstance( document, result );
            byteWriter.setEncryption( true, null, null, PdfWriter.AllowPrinting | PdfWriter.AllowDegradedPrinting );

            final Font font0 = new Font( base1, 10 );
            final Font font1 = new Font( base3, 10 );
            final Font font2 = new Font( base3, 10 );
            final Font font3 = new Font( base1, 8 );

            document.addAuthor( context.getProperty( KEY_AUTHOR ) );
            document.addSubject( context.getProperty( KEY_SUBJECT ) );
            document.addCreationDate();
            document.addTitle( context.getProperty( KEY_TITLE ) );

            document.open();

            final String imageResource = bundle.getString( "pdfgeneration.logo" );
            final URL imageUrl = PDFGenerator.class.getResource( imageResource );
            final Image image = Image.getInstance( imageUrl );
            image.scalePercent( 72 * 100 / 300 );
            document.add( image );
            document.add( Chunk.NEWLINE );
            document.add( new Paragraph( " " ) );

            final Table table = new Table( 2, 5 );
            table.setBorder( Rectangle.NO_BORDER );
            table.setDefaultCellBorder( 0 );
            table.setWidth( 100 );

            table.addCell( new Phrase( "Einreich-Datum und -Zeit: ", font1 ) );
            final SimpleDateFormat date = new SimpleDateFormat( "d. MMMM yyyy, HH:mm:ss" );
            table.addCell( new Paragraph( date.format( new Date() ), font2 ) );
            table.addCell( new Paragraph( bundle.getString( "pdfgeneration.firstname" ), font1 ) );
            table.addCell( new Paragraph( context.getProperty( KEY_FIRSTNAME ), font2 ) );
            table.addCell( new Paragraph( bundle.getString( "pdfgeneration.lastname" ), font1 ) );
            table.addCell( new Paragraph( context.getProperty( KEY_FAMILYNAME ), font2 ) );
            table.addCell( new Paragraph( bundle.getString( "pdfgeneration.id" ), font1 ) );
            final String id = context.getProperty( KEY_ID );
            table.addCell( new Paragraph( id.substring( 0, 2 ) + "-" + id.substring( 2, 5 ) + "-" + id.substring( 5 ), font2 ) );
            table.addCell( new Paragraph( bundle.getString( "pdfgeneration.field" ), font1 ) );
            table.addCell( new Paragraph( context.getProperty( KEY_DEPARTMENT ), font2 ) );

            document.add( table );

            final String text = bundle.getString( "pdfgeneration.text" );
            for ( StringTokenizer t = new StringTokenizer( text, "\n", true ); t.hasMoreTokens(); )
            {
                final String token = t.nextToken();
                final Element element = ( token.trim().equals( "\n" ) ? Chunk.NEWLINE : new Paragraph( token, font0 ) );
                document.add( element );
            }

            document.newPage();

            document.add( new Paragraph( bundle.getString( "pdfgeneration.listtitle" ), font0 ) );
            document.add( new Paragraph( " " ) );

            final MultiColumnText columns = new MultiColumnText();
            columns.addRegularColumns( document.left(), document.right(), 10f, 3 );

            final byte[] bytes = (byte[]) context.get( KEY_SPECIES );
            final XStream reader = new XStream();
            final ArrayList list = (ArrayList) reader.fromXML( new String( bytes ) );
            for ( final Object aList : list )
            {
                columns.addElement( new Paragraph( (String) aList, font3 ) );
            }

            document.add( columns );
            document.close();

            byteArray = result.toByteArray();
            result.close();
        }
        catch ( IOException e )
        {
            LOG.error( "could not produce pdf", e );
        }
        catch ( Throwable e )
        {
            LOG.error( "unknown error during pdf generation", e );
        }

        return byteArray;
    }

    public static void main( final String[] args ) throws DocumentException, IOException
    {
        final Properties context = new Properties();
        context.put( PDFGenerator.KEY_FIRSTNAME, "Daniel" );
        context.put( PDFGenerator.KEY_FAMILYNAME, "Frey" );
        context.put( PDFGenerator.KEY_ID, "85707743" );
        context.put( PDFGenerator.KEY_USERNAME, "dfrey" );
        context.put( PDFGenerator.KEY_PASSWORD, "leni1234" );
        context.put( PDFGenerator.KEY_SUBJECT, "Bestätigung für das Einreichen der Prüfungsliste" );
        context.put( PDFGenerator.KEY_TITLE, "Prüfungslisten-Bestätigung" );
        context.put( PDFGenerator.KEY_AUTHOR, "Daniel Frey" );

        final XStream converter;
        converter = new XStream( new DomDriver() );
        converter.setMode( XStream.ID_REFERENCES );
        converter.registerConverter( new SqlTimestampConverter() );
        converter.registerConverter( new DateConverter( "yyyyMMddHHmmssSSS", new String[]{"d.M.yyyy HH:mm", "yyyyMMddHHmm"} ) );
        converter.alias( "person", PersonData.class );
        converter.alias( "root", PlantList.class );

        final FileInputStream reader = new FileInputStream( "E:/Daten/Prüfungen/Prüfungen 2005 Herbst/Zwischenprüfung/00-907-071.xust" );
        final InputStreamReader streamReader = new InputStreamReader( reader, "UTF-8" );
        final PlantList plantList = (PlantList) converter.fromXML( streamReader );
        reader.close();
        streamReader.close();

        final XStream w = new XStream();
        final String s = w.toXML( plantList.getTaxa() );

        context.put( PDFGenerator.KEY_SPECIES, s.getBytes() );
        final byte[] pdf = PDFGenerator.createPdf( context );
        final FileOutputStream out = new FileOutputStream( "C:/test2.pdf" );
        out.write( pdf );
        out.close();

        final Document doc = new Document( PageSize.A4 );
        final OutputStream stream1 = new FileOutputStream( "C:/Temp/Test1.pdf" );
        final PdfWriter writer1 = PdfWriter.getInstance( doc, stream1 );
        final ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
        final PdfWriter writer2 = PdfWriter.getInstance( doc, stream2 );
        doc.open();
        doc.add( new Paragraph( "Hallo" ) );
        final MultiColumnText cols = new MultiColumnText();
        cols.addRegularColumns( doc.left(), doc.right(), 10f, 3 );
        for ( int i = 0; i < 100; i++ )
        {
            cols.addElement( new Paragraph( "Hallo" ) );
        }
        doc.add( cols );
        doc.add( new Paragraph( " " ) );
        final FileOutputStream fileStream = new FileOutputStream( "C:/Temp/Test2.pdf" );
        fileStream.write( stream2.toByteArray() );
        fileStream.close();
        doc.close();
        stream1.close();
        stream2.close();
        writer1.close();
        writer2.close();
    }
}
