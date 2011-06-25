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

import ch.jfactory.xstream.ValidatingDateConverter;
import ch.xmatrix.ups.domain.PersonData;
import ch.xmatrix.ups.domain.PlantList;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.MultiColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.extended.SqlTimestampConverter;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.StringWriter;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.itextpdf.text.Element.ALIGN_LEFT;
import static com.itextpdf.text.Element.ALIGN_RIGHT;
import static com.itextpdf.text.pdf.PdfWriter.ALLOW_PRINTING;
import static com.itextpdf.text.pdf.PdfWriter.ENCRYPTION_AES_128;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.2 $ $Date: 2008/01/23 22:19:20 $
 */
public class PDFGenerator
{
    private static final Logger LOG = LoggerFactory.getLogger( PDFGenerator.class );

    private static final ResourceBundle bundle = ResourceBundle.getBundle( "ch.xmatrix.ups.pdf.Strings" );

    public static final String KEY_USERNAME = "username";

    public static final String KEY_PASSWORD = "password";

    public static final String KEY_FIRSTNAME = "firstname";

    public static final String KEY_FAMILYNAME = "familyname";

    /** The course of the student. */
    public static final String KEY_DEPARTMENT = "department";

    /** The identification number of the student. */
    public static final String KEY_ID = "id";

    /** Subject field in the generated pdf file. */
    public static final String KEY_SUBJECT = "subject";

    /** Author field in the generated pdf file. */
    public static final String KEY_AUTHOR = "author";

    /** Title field in the generated pdf file. */
    public static final String KEY_TITLE = "title";

    /** Put here a byte array with the xstream-encoded ArrayList of species strings. */
    public static final String KEY_SPECIES = "species";

    private static final byte[] PROTECTION_PASSWORD = "ups".getBytes();

    private static final byte[] USER_PASSWORD = null;

    public static byte[] createPdf( final Properties context ) throws DocumentException, IOException
    {
        byte[] byteArray = null;
        try
        {
            final SimpleDateFormat date = new SimpleDateFormat( "d. MMMM yyyy, HH:mm:ss" );
            final String timeStamp = date.format( new Date() );

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
            final PdfWriter writer = PdfWriter.getInstance( document, result );
            writer.setEncryption( USER_PASSWORD, PROTECTION_PASSWORD, ALLOW_PRINTING, ENCRYPTION_AES_128 );

            final Font font0 = new Font( base1, 10 );
            final Font font1 = new Font( base3, 10 );
            final Font font2 = new Font( base3, 10 );
            final Font font3 = new Font( base1, 8 );

            writer.setPageEvent( new PdfPageEventHelper()
            {
                public void onEndPage( final PdfWriter writer, final Document document )
                {
                    final PdfContentByte content = writer.getDirectContent();
                    final int number = document.getPageNumber();
                    final Phrase page = new Phrase( "Seite " + number, font3 );
                    final Phrase stamp = new Phrase( timeStamp, font3 );
                    final float y = document.bottom() - 20;
                    final boolean even = ( number % 2 ) == 0;
                    final int inner = even ? ALIGN_RIGHT : ALIGN_LEFT;
                    final int outer = even ? ALIGN_LEFT : ALIGN_RIGHT;
                    final float innerAlign = even ? document.right() : document.left();
                    final float outerAlign = even ? document.left() : document.right();
                    ColumnText.showTextAligned( content, inner, stamp, innerAlign, y, 0 );
                    ColumnText.showTextAligned( content, outer, page, outerAlign, y, 0 );
                }
            } );

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

            final PdfPTable table = new PdfPTable( 2 );
            table.setWidthPercentage( 100 );
            table.getDefaultCell().setBorder( Rectangle.NO_BORDER );
            table.getDefaultCell().setPadding( 0 );

            table.addCell( new Phrase( "Einreich-Datum und -Zeit: ", font1 ) );
            table.addCell( new Paragraph( timeStamp, font2 ) );
            table.addCell( new Paragraph( bundle.getString( "pdfgeneration.firstname" ), font1 ) );
            table.addCell( new Paragraph( context.getProperty( KEY_FIRSTNAME ), font2 ) );
            table.addCell( new Paragraph( bundle.getString( "pdfgeneration.lastname" ), font1 ) );
            table.addCell( new Paragraph( context.getProperty( KEY_FAMILYNAME ), font2 ) );
            table.addCell( new Paragraph( bundle.getString( "pdfgeneration.id" ), font1 ) );
            final String id = context.getProperty( KEY_ID );
            final String idString = id != null && id.length() > 5 ? id.substring( 0, 2 ) + "-" + id.substring( 2, 5 ) + "-" + id.substring( 5 ) : "missing";
            table.addCell( new Paragraph( idString, font2 ) );

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
        converter.registerConverter( new ValidatingDateConverter( "yyyyMMddHHmmssSSS", "d.M.yyyy HH:mm", "yyyyMMddHHmm" ) );
        converter.alias( "person", PersonData.class );
        converter.alias( "root", PlantList.class );

        final FileInputStream inputStream = new FileInputStream( "daniel.xust" );
        final InputStreamReader streamReader = new InputStreamReader( inputStream, "UTF-8" );
        final StringWriter stringWriter = new StringWriter();
        IOUtils.copy( streamReader, stringWriter );
        context.put( PDFGenerator.KEY_SPECIES, stringWriter.toString().getBytes() );
        inputStream.close();
        streamReader.close();
        stringWriter.close();

        final FileOutputStream out = new FileOutputStream( "confirmation.pdf" );
        out.write( PDFGenerator.createPdf( context ) );
        out.close();
    }

}
