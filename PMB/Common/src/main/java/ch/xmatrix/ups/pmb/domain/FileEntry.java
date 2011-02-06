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
package ch.xmatrix.ups.pmb.domain;

import ch.xmatrix.ups.pmb.ui.model.PictureStateModel;
import ch.xmatrix.ups.pmb.ui.model.Settings;
import java.io.File;
import javax.swing.Icon;
import net.java.jveez.vfs.Picture;
import net.java.jveez.vfs.impl.PictureImpl;

/**
 * Holds the reference to the file and parses its name into the appropriate parts.
 *
 * <ul>
 *
 * <li>The hierarchic part.</li>
 *
 * <li>The alternative mode part. If a picture belong to an alternative part A, it is never displayed with another
 * picture of another alternative part B of the same species.</li>
 *
 * <li>The multiple choice part. If multiple pictures exist for a defined hierarchy.</li>
 *
 * <li>The picture state part. Which part of the picture should be shown (see {@link PictureStateModel
 * PictureStateModel})?</li>
 *
 * </ul>
 *
 * The accessor for the picture is lazy instantiating the picture to reduce memory consumption.
 *
 * @author Daniel Frey
 * @version $Revision: 1.2 $ $Date: 2007/09/27 10:47:37 $
 */
public class FileEntry extends Entry implements Picture
{
    private File file;

    private Picture picture;

    public FileEntry( final String path, final Entry parent, final Settings settings )
    {
        super( path, parent, settings );
        file = new File( path );
        picture = new PictureImpl( file );
    }

    public File getFile()
    {
        return file;
    }

    public String getAbsolutePath()
    {
        return file.getAbsolutePath();
    }

    public String getDescription()
    {
        return file.getName();
    }

    public Icon getIcon()
    {
        return null;
    }

    public long getLength()
    {
        return 0;
    }

    public long getLastModifiedDate()
    {
        return 0;
    }

    public String getName()
    {
        return file.getName();
    }

    public Picture getPicture()
    {
        return picture;
    }

    /**
     * Resets the picture in addition to the super class method.
     *
     * @param path the new path to set.
     */
    @Override
    public void setPath( final String path )
    {
        super.setPath( path );
        file = new File( path );
        picture = new PictureImpl( file );
    }
}
