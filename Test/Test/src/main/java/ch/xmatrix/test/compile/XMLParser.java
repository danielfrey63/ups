package ch.xmatrix.test.compile;

import java.io.File;
import java.io.FileWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLParser
{
    public static void main( final String[] argv )
    {
        try
        {
            final File file = new File( "C:/Dokumente und Einstellungen/Daniel Frey/Desktop/pruefungslisten.xml" );
            final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            final DocumentBuilder db = dbf.newDocumentBuilder();
            final Document doc = db.parse( file );
            doc.getDocumentElement().normalize();
            System.out.println( "Root element " + doc.getDocumentElement().getNodeName() );
            final NodeList nodeLst = doc.getElementsByTagName( "row" );
            System.out.println( "Information of all nodes" );

            for ( int s = 0; s < nodeLst.getLength(); s++ )
            {
                final Node fstNode = nodeLst.item( s );
                if ( fstNode.getNodeType() == Node.ELEMENT_NODE )
                {
                    final Element row = (Element) fstNode;
                    final NodeList fields = row.getElementsByTagName( "field" );
                    final Element firstField = (Element) fields.item( 0 );
                    final NodeList fstNm = firstField.getChildNodes();
                    final String fileName = fstNm.item( 0 ).getNodeValue();
                    System.out.println( "File is: " + fileName );
                    final Element lstNmElmnt = (Element) fields.item( 1 );
                    final NodeList lstNm = lstNmElmnt.getChildNodes();
                    final String examList = lstNm.item( 0 ).getNodeValue();
                    System.out.println( "Examlist is: " + examList );
                    final FileWriter writer = new FileWriter( "C:/Dokumente und Einstellungen/Daniel Frey/Desktop/2011H/" + fileName + ".xust" );
                    writer.write( examList );
                    writer.close();
                }
            }
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }
}