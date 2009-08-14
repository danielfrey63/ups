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
package ch.jfactory.file;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/** Filter to show folders and files with one of the extentions given. */
public class ExtentionFileFilter extends FileFilter implements java.io.FileFilter
{
    private String[] extentions;

    private String description;

    private boolean showDirectories;

    public ExtentionFileFilter(final String description, final String[] extentions, final boolean showDirectories)
    {
        this.description = description;
        this.extentions = extentions;
        this.showDirectories = showDirectories;
    }

    /**
     * Simple fixed description.
     *
     * @return the description
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Accepts diretories and the the given extentions.
     *
     * @param file the file to decide whether it should be accepted.
     * @return whether accept or not
     */
    public boolean accept(final File file)
    {
        boolean result = false;
        for (String extention : extentions)
        {
            result |= file.getName().endsWith(extention);
        }
        return (showDirectories && file.isDirectory()) || result;
    }

    public String[] getExtentions()
    {
        return extentions;
    }
}
