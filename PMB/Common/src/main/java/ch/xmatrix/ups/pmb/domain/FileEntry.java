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
import net.java.jveez.vfs.Picture;
import net.java.jveez.vfs.impl.PictureImpl;

/**
 * Holds the reference to the file and parses its name into the appropriate parts.
 *
 * <ul>
 *
 * <li>The hieraric part.</li>
 *
 * <li>The alternative mode part. If a picture belong to an alternative part A, it is never displayed with another
 * picture of another alternative part B of the same species.</li>
 *
 * <li>The multiple choice part. If multiple pictures exist for a defined hierary.</li>
 *
 * <li>The picture state part. Which part of the picture should be shown (see {@link
 * PictureStateModel PictureStateModel})?</li>
 *
 * </ul>
 *
 * The accessor for the picture is lazy instantiating the picture to reduce memory consumption.
 *
 * @author Daniel Frey
 * @version $Revision: 1.2 $ $Date: 2007/09/27 10:47:37 $
 */
public class FileEntry extends Entry
{
    private Picture picture = null;

    public FileEntry( final String path, final Entry parent, final Settings settings )
    {
        super( path, parent, settings );
    }

    public Picture getPicture()
    {
        if ( picture == null )
        {
            picture = new PictureImpl( new File( getPath() ) );
        }
        return picture;
    }

    public PictureStateModel getPictureStateModel()
    {
        return parseIntoPictureStateModel( new File( getPath() ) );
    }

    /**
     * Resets the picture in addition to the superclass method.
     *
     * @param path the new path to set.
     */
    @Override
    public void setPath( final String path )
    {
        super.setPath( path );
        picture = null;
    }

    // Utilities

    /**
     * Parses the string into a PictureStateModel. If parsing ist not successful, returns null.
     *
     * @param file Full file path. Position information is in paranthesis as space separated x1, x2, y1, y2
     * @return PictureStateModel
     */
    private static PictureStateModel parseIntoPictureStateModel( final File file )
    {
        final PictureStateModel model;
        final String name = file.getName();
        final String position = getPositionAndZoom( name );
        final String[] parts = position.split( " " );
        if ( parts.length == 4 )
        {
            model = new PictureStateModel( file.getAbsolutePath() );
            model.setX1( Double.parseDouble( parts[0] ) / 100 );
            model.setX2( Double.parseDouble( parts[1] ) / 100 );
            model.setY1( Double.parseDouble( parts[2] ) / 100 );
            model.setY2( Double.parseDouble( parts[3] ) / 100 );
        }
        else
        {
            model = null;
        }
        return model;
    }

    private static String getPositionAndZoom( final String string )
    {
        final int i1 = string.indexOf( " (" );
        final int i2 = string.indexOf( ")" );
        final String result;
        if ( i1 < i2 )
        {
            result = string.substring( i1 + 2, i2 );
        }
        else
        {
            result = "";
        }
        return result;
    }

}
