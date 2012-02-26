package ch.xmatrix.ups.pmb.test;

import ch.jfactory.file.FileUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;

/**
 * // TODO: Comment
 *
 * @author Daniel Frey 07.07.2008 22:07:03
 */
public class TestTaxa
{
    public static String getTestPictures() throws IOException
    {
        final String[] files = {
                "/test/test.xml",
                "/test/Helianthus annuus/Blatt.Rand+.Oberseite (0 100 0 100).jpg",
                "/test/Helianthus annuus/Blatt.Rand.Unterseite (0 100 0 100).jpg",
                "/test/Helianthus annuus/Blatt.Spitze+.Oberseite (0 100 0 100).jpg",
                "/test/Helianthus annuus/Blatt.Spitze.Unterseite (0 100 0 100).jpg",
                "/test/Helianthus annuus/Blatt.Stiel+.Oberseite (0 100 0 100).jpg",
                "/test/Helianthus annuus/Blatt.Stiel.Unterseite (0 100 0 100).jpg",
                "/test/Helianthus annuus/Blatt.Unterseite (0 100 0 100).jpg",
                "/test/Helianthus annuus/Blatt.Unterseite.Detail (0 100 0 100).jpg",
                "/test/Helianthus annuus/Blüte+.Oberseite (0 100 0 100).jpg",
                "/test/Helianthus annuus/Blüte.Detail+.Rand (0 100 0 100).jpg",
                "/test/Helianthus annuus/Blüte.Detail.Zentrum (0 100 0 100).jpg",
                "/test/Helianthus annuus/Blüte.Detail.Zentrum.Detail (0 100 0 100).jpg",
                "/test/Helianthus annuus/Blüte.Längsschnitt (0 100 0 100).jpg",
                "/test/Helianthus annuus/Blüte.Längsschnitt.Detail (0 100 0 100).jpg",
                "/test/Helianthus annuus/Blüte.Oberseite.Detail (0 100 0 100).jpg",
                "/test/Helianthus annuus/Blüte.Oberseite.Detail.Detail (0 100 0 100).jpg",
                "/test/Helianthus annuus/Blüte.Unterseite (0 100 0 100).jpg",
                "/test/Helianthus annuus/Frucht (0 100 0 100).jpg",
                "/test/Helianthus annuus/Frucht.Detail (0 100 0 100).jpg",
                "/test/Helianthus annuus/Habitus (0 100 0 100).jpg",
                "/test/Helianthus annuus/Herbar (0 100 0 100).jpg",
                "/test/Helianthus annuus/Standort (0 100 0 100).jpg",
                "/test/Helianthus annuus/Stengel (0 100 0 100).jpg",
                "/test/Helianthus annuus/Stengel.Detail (0 100 0 100).jpg",
                "/test/Helianthus annuus/Stengel.Querschnitt (0 100 0 100).jpg",
                "/test/Helianthus annuus/Stengel.Querschnitt.Detail (0 100 0 100).jpg",
                "/test/Helianthus annuus/Wurzel (0 100 0 100).jpg",
                "/test/Zea mays/Blatt+.Oberseite (0 100 0 100).jpg",
                "/test/Zea mays/Blatt.Basis (0 100 0 100).jpg",
                "/test/Zea mays/Blatt.Rand+.Oberseite (0 100 0 100).jpg",
                "/test/Zea mays/Blatt.Rand.Unterseite (0 100 0 100).jpg",
                "/test/Zea mays/Blatt.Spitze+.Oberseite (0 100 0 100).jpg",
                "/test/Zea mays/Blatt.Spitze.Unterseite (0 100 0 100).jpg",
                "/test/Zea mays/Blatt.Unterseite (0 100 0 100).jpg",
                "/test/Zea mays/Blütenstand+.endständig (0 100 0 100).jpg",
                "/test/Zea mays/Blütenstand.endständig.Detail (0 100 0 100).jpg",
                "/test/Zea mays/Blütenstand.endständig.Detail.Detail (0 100 0 100).jpg",
                "/test/Zea mays/Blütenstand.endständig.Detail.Detail.Detail (0 100 0 100).jpg",
                "/test/Zea mays/Blütenstand.seitenständig (0 100 0 100).jpg",
                "/test/Zea mays/Blütenstand.seitenständig.Detail (0 100 0 100).jpg",
                "/test/Zea mays/Blütenstand.seitenständig.geöffnet (0 100 0 100).jpg",
                "/test/Zea mays/Blütenstand.seitenständig.geöffnet.Detail (0 100 0 100).jpg",
                "/test/Zea mays/Frucht (0 100 0 100).jpg",
                "/test/Zea mays/Frucht.Detail (0 100 0 100).jpg",
                "/test/Zea mays/Habitus (0 100 0 100).jpg",
                "/test/Zea mays/Herbar (0 100 0 100).jpg",
                "/test/Zea mays/Standort (0 100 0 100).jpg",
                "/test/Zea mays/Standort.Detail (0 100 0 100).jpg",
                "/test/Zea mays/Stengel (0 100 0 100).jpg",
                "/test/Zea mays/Stengel.Detail (0 100 0 100).jpg",
                "/test/Zea mays/Stengel.Querschnitt (0 100 0 100).jpg",
                "/test/Zea mays/Wurzel (0 100 0 100).jpg"        };
        final File directory = FileUtils.createTemporaryDirectory( "pmbx", "demo" );
        directory.deleteOnExit();
        for ( final String file : files )
        {
            final InputStream in = TestTaxa.class.getResourceAsStream( file );
            if ( in == null )
            {
                throw new IllegalStateException( "resource not found: " + file );
            }
            final File absoluteFile = new File( directory, file );
            absoluteFile.getParentFile().mkdirs();
            final FileOutputStream out = new FileOutputStream( absoluteFile );
            IOUtils.copy( in, out );
            IOUtils.closeQuietly( out );
            IOUtils.closeQuietly( in );
        }
        return directory.getAbsolutePath();
    }
}
