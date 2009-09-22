package ch.xmatrix.ups.pmb.exam;

import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * Filters exam set files.
*
* @author Daniel Frey
*/
class ExamsetFileFilter extends FileFilter {
    public boolean accept(final File pathname) {
        return pathname.isDirectory() || pathname.getName().endsWith(".xml");
    }

    public String getDescription() {
        return "Pr�fungssets (XML-Datei mit Endung *.xml)";
    }
}
