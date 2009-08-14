package ch.jfactory.application;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:57 $
 */
public class CopyProtection
{
    /**
     * Reads two files and compares their content. If either file is null or empty, false is returned.
     *
     * @param file1 first file to read
     * @param file2 second file to read
     * @return whether the content of the two given files in the given directory is identical
     */
    public static boolean compareFiles(final File file1, final File file2)
    {
        if (file1 == null || file2 == null || !file1.exists() || !file2.exists())
        {
            return false;
        }
        try
        {
            return FileUtils.contentEquals(file1, file2);
        }
        catch (IOException e)
        {
            return false;
        }
    }
}
