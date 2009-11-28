package ch.xmatrix.ups.pmb.exam;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 * Filters exam set files.
 *
 * @author Daniel Frey
 */
class ExamsetFileFilter extends FileFilter
{
    public boolean accept( final File pathname )
    {
        return pathname.isDirectory() || pathname.getName().endsWith( ".xml" );
    }

    public String getDescription()
    {
        return "Prüfungssets (XML-Datei mit Endung *.xml)";
    }
}
