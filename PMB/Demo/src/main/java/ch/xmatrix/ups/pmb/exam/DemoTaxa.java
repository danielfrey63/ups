package ch.xmatrix.ups.pmb.exam;

import ch.jfactory.file.FileUtils;
import com.sun.org.apache.xpath.internal.SourceTree;
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
                "/demo/Anchusa officinalis/Herbar (0 100 0 100).jpg",
                "/demo/Bupleurum ranunculoides/Herbar (0 100 0 100).jpg",
                "/demo/Centaurium erythraea/Herbar (0 100 0 100).jpg",
                "/demo/Cyclamen purpurascens/Herbar (0 100 0 100).jpg",
                "/demo/Ephedra helvetica/Herbar (0 100 0 100).jpg",
                "/demo/Epilobium fleischeri/Herbar (0 100 0 100).jpg",
                "/demo/Euphorbia peplus/Herbar (0 100 0 100).jpg",
                "/demo/Genista germanica/Herbar (0 100 0 100).jpg",
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
                "/demo/Helianthus annuus/Herbar (0 100 0 100).jpg",
                "/demo/Helianthus annuus/Standort (0 100 0 100).jpg",
                "/demo/Helianthus annuus/Stengel (0 100 0 100).jpg",
                "/demo/Helianthus annuus/Stengel.Detail (0 100 0 100).jpg",
                "/demo/Helianthus annuus/Stengel.Querschnitt (0 100 0 100).jpg",
                "/demo/Helianthus annuus/Stengel.Querschnitt.Detail (0 100 0 100).jpg",
                "/demo/Helianthus annuus/Wurzel (0 100 0 100).jpg",
                "/demo/Hottonia palustris/Herbar (0 100 0 100).jpg",
                "/demo/Linnaea borealis/Herbar (0 100 0 100).jpg",
                "/demo/Lotus maritimus/Herbar (0 100 0 100).jpg",
                "/demo/Mentha spicata/Herbar (0 100 0 100).jpg",
                "/demo/Menyanthes trifoliata/Herbar (0 100 0 100).jpg",
                "/demo/Paeonia officinalis/Herbar (0 100 0 100).jpg",
                "/demo/Potamogeton natans/Herbar (0 100 0 100).jpg",
                "/demo/Pyrola rotundifolia/Herbar (0 100 0 100).jpg",
                "/demo/Ribes alpinum/Herbar (0 100 0 100).jpg",
                "/demo/Sanguisorba officinalis/Herbar (0 100 0 100).jpg",
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
                "/demo/Zea mays/Herbar (0 100 0 100).jpg",
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
