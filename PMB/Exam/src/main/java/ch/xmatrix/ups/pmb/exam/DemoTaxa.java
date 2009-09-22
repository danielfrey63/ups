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
public class DemoTaxa {

    public static String getDemoPictures() throws IOException {
        final String[] files = {
                "/demo/demo.xml",
                "/demo/Acer pseudoplatanus/Habitus (17 80 4 93).jpg",
                "/demo/Galanthus nivalis/Habitus (0 100 0 100).jpg",
                "/demo/Gentiana acaulis/Habitus (1 99 8 92).jpg",
                "/demo/Helianthus annuus/Blatt+.Oberseite (7 98 1 88).JPG",
                "/demo/Helianthus annuus/Blatt.Basis+.Oberseite (6 94 8 92).JPG",
                "/demo/Helianthus annuus/Blatt.Basis.Unterseite (6 94 8 92).JPG",
                "/demo/Helianthus annuus/Blatt.Oberseite.Detail (14 86 6 94).JPG",
                "/demo/Helianthus annuus/Blatt.Rand+.Oberseite (5 75 3 88).JPG",
                "/demo/Helianthus annuus/Blatt.Rand.Oberseite.Detail (40 88 14 72).JPG",
                "/demo/Helianthus annuus/Blatt.Rand.Unterseite (15 84 13 98).JPG",
                "/demo/Helianthus annuus/Blatt.Spitze+.Oberseite (24 76 0 100).JPG",
                "/demo/Helianthus annuus/Blatt.Spitze.Unterseite (25 75 0 100).JPG",
                "/demo/Helianthus annuus/Blatt.Stiel+.Oberseite (7 98 1 88).JPG",
                "/demo/Helianthus annuus/Blatt.Stiel.Unterseite (1 89 9 92).JPG",
                "/demo/Helianthus annuus/Blatt.Unterseite (5 97 0 87).JPG",
                "/demo/Helianthus annuus/Blatt.Unterseite.Detail (17 86 5 90).JPG",
                "/demo/Helianthus annuus/Blüte+.Oberseite (17 69 6 94).JPG",
                "/demo/Helianthus annuus/Blüte.Detail+.Rand (6 92 4 86).JPG",
                "/demo/Helianthus annuus/Blüte.Detail.Zentrum (26 71 23 66).JPG",
                "/demo/Helianthus annuus/Blüte.Detail.Zentrum.Detail (14 58 17 60).JPG",
                "/demo/Helianthus annuus/Blüte.Längsschnitt (25 75 8 92).JPG",
                "/demo/Helianthus annuus/Blüte.Längsschnitt.Detail (26 70 14 88).JPG",
                "/demo/Helianthus annuus/Blüte.Oberseite.Detail (21 91 0 100).JPG",
                "/demo/Helianthus annuus/Blüte.Oberseite.Detail.Detail (11 53 3 95).JPG",
                "/demo/Helianthus annuus/Blüte.Unterseite (22 72 7 93).JPG",
                "/demo/Helianthus annuus/Frucht (17 74 0 100).JPG",
                "/demo/Helianthus annuus/Frucht.Detail (18 76 7 79).JPG",
                "/demo/Helianthus annuus/Habitus (6 94 8 92).JPG",
                "/demo/Helianthus annuus/Standort (7 57 5 91).JPG",
                "/demo/Helianthus annuus/Stengel (16 88 4 95).JPG",
                "/demo/Helianthus annuus/Stengel.Detail (15 85 7 93).JPG",
                "/demo/Helianthus annuus/Stengel.Detail.Detail (31 79 0 100).JPG",
                "/demo/Helianthus annuus/Stengel.Querschnitt (6 94 8 92).JPG",
                "/demo/Helianthus annuus/Stengel.Querschnitt.Detail (13 83 6 93).JPG",
                "/demo/Helianthus annuus/Wurzel (6 94 8 92).JPG",
                "/demo/Viola calcarata/Habitus (21 91 2 97).jpg",
                "/demo/Zea mays/Blatt+.Oberseite (2 98 8 92).jpg",
                "/demo/Zea mays/Blatt.Basis (22 92 6 93).JPG",
                "/demo/Zea mays/Blatt.Rand+.Oberseite (6 94 8 92).JPG",
                "/demo/Zea mays/Blatt.Rand.Unterseite (6 94 8 92).JPG",
                "/demo/Zea mays/Blatt.Spitze+.Oberseite (13 71 24 96).JPG",
                "/demo/Zea mays/Blatt.Spitze.Unterseite (20 78 22 94).JPG",
                "/demo/Zea mays/Blatt.Unterseite (3 97 8 92).JPG",
                "/demo/Zea mays/Blütenstand+.endständig (6 82 2 96).JPG",
                "/demo/Zea mays/Blütenstand.endständig.Detail (12 70 0 100).JPG",
                "/demo/Zea mays/Blütenstand.endständig.Detail.Detail (8 56 0 100).JPG",
                "/demo/Zea mays/Blütenstand.endständig.Detail.Detail.Detail (21 61 8 97).JPG",
                "/demo/Zea mays/Blütenstand.seitenständig (14 86 5 95).JPG",
                "/demo/Zea mays/Blütenstand.seitenständig.Detail (25 83 4 77).JPG",
                "/demo/Zea mays/Blütenstand.seitenständig.geöffnet (8 92 2 98).JPG",
                "/demo/Zea mays/Blütenstand.seitenständig.geöffnet.Detail (15 85 7 93).JPG",
                "/demo/Zea mays/Frucht (6 79 4 95).JPG",
                "/demo/Zea mays/Frucht.Detail (4 74 5 93).JPG",
                "/demo/Zea mays/Habitus (15 85 7 93).JPG",
                "/demo/Zea mays/Standort (29 86 2 99).JPG",
                "/demo/Zea mays/Standort.Detail (21 79 0 100).JPG",
                "/demo/Zea mays/Stengel (15 85 7 93).JPG",
                "/demo/Zea mays/Stengel.Detail (21 91 6 93).JPG",
                "/demo/Zea mays/Stengel.Querschnitt (8 78 9 96).JPG",
                "/demo/Zea mays/Stengel.Querschnitt.Detail (37 95 14 86).JPG",
                "/demo/Zea mays/Wurzel (23 93 8 95).JPG"
        };
        final File directory = FileUtils.createTemporaryDirectory("pmbx", "demo");
        directory.deleteOnExit();
        for (final String file : files) {
            final InputStream in = DemoTaxa.class.getResourceAsStream(file);
            if (in == null) {
                throw new IllegalStateException("resource not found: " + file);
            }
            final File absoluteFile = new File(directory, file);
            absoluteFile.getParentFile().mkdirs();
            final FileOutputStream out = new FileOutputStream(absoluteFile);
            IOUtils.copy(in, out);
            IOUtils.closeQuietly(out);
            IOUtils.closeQuietly(in);
        }
        return directory.getAbsolutePath();
    }
}
