package ch.xmatrix.ups.pmb.exam;

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
public class DemoTaxa
{
    public static String getDemoPictures() throws IOException
    {
        final String[] files = {
                "/demo/demo.xml",
                "/demo/Alge klein/Habitus (0 100 0 100).jpg",
                "/demo/Ananas communis/Habitus (0 100 0 100).jpg",
                "/demo/Assis Regen/Habitus (0 100 0 100).jpg",
                "/demo/Capparis spinosa/Habitus (0 100 0 100).jpg",
                "/demo/Cocos nuccifera/Habitus (0 100 0 100).jpg",
                "/demo/Fee verte/Habitus (0 100 0 100).jpg",
                "/demo/Frau Kind/Habitus (0 100 0 100).jpg",
                "/demo/Helianthus annuus/Blatt+.Oberseite (0 100 0 100).jpg",
                "/demo/Helianthus annuus/Blatt.Basis+.Oberseite (0 100 0 100).jpg",
                "/demo/Helianthus annuus/Blatt.Basis.Unterseite (0 100 0 100).jpg",
                "/demo/Helianthus annuus/Blatt.Oberseite.Detail (0 100 0 100).jpg",
                "/demo/Helianthus annuus/Blatt.Rand+.Oberseite (0 100 0 100).jpg",
                "/demo/Helianthus annuus/Blatt.Rand.Unterseite (0 100 0 100).jpg",
                "/demo/Helianthus annuus/Blatt.Spitze+.Oberseite (0 100 0 100).jpg",
                "/demo/Helianthus annuus/Blatt.Spitze.Unterseite (0 100 0 100).jpg",
                "/demo/Helianthus annuus/Blatt.Stiel+.Oberseite (0 100 0 100).jpg",
                "/demo/Helianthus annuus/Blatt.Stiel.Unterseite (0 100 0 100).jpg",
                "/demo/Helianthus annuus/Blatt.Unterseite (0 100 0 100).jpg",
                "/demo/Helianthus annuus/Blatt.Unterseite.Detail (0 100 0 100).jpg",
                "/demo/Helianthus annuus/Blüte+.Oberseite (0 100 0 100).jpg",
                "/demo/Helianthus annuus/Blüte.Detail+.Rand (0 100 0 100).jpg",
                "/demo/Helianthus annuus/Blüte.Detail.Zentrum (0 100 0 100).jpg",
                "/demo/Helianthus annuus/Blüte.Detail.Zentrum.Detail (0 100 0 100).jpg",
                "/demo/Helianthus annuus/Blüte.Längsschnitt (0 100 0 100).jpg",
                "/demo/Helianthus annuus/Blüte.Längsschnitt.Detail (0 100 0 100).jpg",
                "/demo/Helianthus annuus/Blüte.Oberseite.Detail (0 100 0 100).jpg",
                "/demo/Helianthus annuus/Blüte.Oberseite.Detail.Detail (0 100 0 100).jpg",
                "/demo/Helianthus annuus/Blüte.Unterseite (0 100 0 100).jpg",
                "/demo/Helianthus annuus/Frucht (0 100 0 100).jpg",
                "/demo/Helianthus annuus/Frucht.Detail (0 100 0 100).jpg",
                "/demo/Helianthus annuus/Habitus (0 100 0 100).jpg",
                "/demo/Helianthus annuus/Standort (0 100 0 100).jpg",
                "/demo/Helianthus annuus/Stengel (0 100 0 100).jpg",
                "/demo/Helianthus annuus/Stengel.Detail (0 100 0 100).jpg",
                "/demo/Helianthus annuus/Stengel.Querschnitt (0 100 0 100).jpg",
                "/demo/Helianthus annuus/Stengel.Querschnitt.Detail (0 100 0 100).jpg",
                "/demo/Helianthus annuus/Wurzel (0 100 0 100).jpg",
                "/demo/Knoellchen bact/Habitus (0 100 0 100).jpg",
                "/demo/Krusten flechte/Habitus (0 100 0 100).jpg",
                "/demo/Kuh braun/Habitus (0 100 0 100).jpg",
                "/demo/Moos pleurocarp/Habitus (0 100 0 100).jpg",
                "/demo/Opuntia compressa/Habitus (0 100 0 100).jpg",
                "/demo/Orchidee orange/Habitus (0 100 0 100).jpg",
                "/demo/Physoplexis comosa/Habitus (0 100 0 100).jpg",
                "/demo/Pilz weiss/Habitus (0 100 0 100).jpg",
                "/demo/Walliser Schafe/Habitus (0 100 0 100).jpg",
                "/demo/Yucca filamentosa/Habitus (0 100 0 100).jpg",
                "/demo/Zantedeschia aethiopica/Habitus (0 100 0 100).jpg",
                "/demo/Zea mays/Blatt+.Oberseite (0 100 0 100).jpg",
                "/demo/Zea mays/Blatt.Basis (0 100 0 100).jpg",
                "/demo/Zea mays/Blatt.Rand+.Oberseite (0 100 0 100).jpg",
                "/demo/Zea mays/Blatt.Rand.Unterseite (0 100 0 100).jpg",
                "/demo/Zea mays/Blatt.Spitze+.Oberseite (0 100 0 100).jpg",
                "/demo/Zea mays/Blatt.Spitze.Unterseite (0 100 0 100).jpg",
                "/demo/Zea mays/Blatt.Unterseite (0 100 0 100).jpg",
                "/demo/Zea mays/Blütenstand+.endständig (0 100 0 100).jpg",
                "/demo/Zea mays/Blütenstand.endständig.Detail (0 100 0 100).jpg",
                "/demo/Zea mays/Blütenstand.endständig.Detail.Detail (0 100 0 100).jpg",
                "/demo/Zea mays/Blütenstand.endständig.Detail.Detail.Detail (0 100 0 100).jpg",
                "/demo/Zea mays/Blütenstand.seitenständig (0 100 0 100).jpg",
                "/demo/Zea mays/Blütenstand.seitenständig.Detail (0 100 0 100).jpg",
                "/demo/Zea mays/Blütenstand.seitenständig.geöffnet (0 100 0 100).jpg",
                "/demo/Zea mays/Blütenstand.seitenständig.geöffnet.Detail (0 100 0 100).jpg",
                "/demo/Zea mays/Frucht (0 100 0 100).jpg",
                "/demo/Zea mays/Frucht.Detail (0 100 0 100).jpg",
                "/demo/Zea mays/Habitus (0 100 0 100).jpg",
                "/demo/Zea mays/Standort (0 100 0 100).jpg",
                "/demo/Zea mays/Standort.Detail (0 100 0 100).jpg",
                "/demo/Zea mays/Stengel (0 100 0 100).jpg",
                "/demo/Zea mays/Stengel.Detail (0 100 0 100).jpg",
                "/demo/Zea mays/Stengel.Querschnitt (0 100 0 100).jpg",
                "/demo/Zea mays/Wurzel (0 100 0 100).jpg"
        };
        final File directory = FileUtils.createTemporaryDirectory( "pmbx", "demo" );
        directory.deleteOnExit();
        for ( final String file : files )
        {
            final InputStream in = DemoTaxa.class.getResourceAsStream( file );
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
