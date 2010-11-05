/*
 * This project is made available under the terms of the BSD license, more information can be found at : http://www.opensource.org/licenses/bsd-license.html
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met:
 * - Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * - Neither the name of the java.net website nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS
 * OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY
 * AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER
 * IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright (c) 2004, The JVeez Project Team.
 * All rights reserved.
 */

package net.java.jveez.vfs.impl;

import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifDirectory;
import java.io.File;
import javax.swing.Icon;
import javax.swing.filechooser.FileSystemView;
import net.java.jveez.utils.Utils;
import net.java.jveez.vfs.ExifPicture;

public class PictureImpl implements ExifPicture
{
    protected static FileSystemView fileSystemView = FileSystemView.getFileSystemView();

    private final File file;

    private String displayName;

    private String description;

    private Long lastModifiedDate;  // cache this info otherwise each access would require a slooow native call ...

    private Long length;            // cache this info otherwise each access would require a slooow native call ...

    private Icon icon;

    private String absolutePath;

    private boolean exifDataLoaded;

    private String exifDate;

    private String exifFocal;

    private String exifExposure;

    private String exifAperture;

    private String exifCamera;

    private String exifModel;

    public PictureImpl( final File file )
    {
        this.file = file;
    }

    public String getAbsolutePath()
    {
        if ( absolutePath == null )
        {
            absolutePath = file.getAbsolutePath();
        }
        return absolutePath;
    }

    public Icon getIcon()
    {
        if ( icon == null )
        {
            icon = Utils.getSystemIconForFile( getFile() );
        }
        return icon;
    }

    public File getFile()
    {
        return file;
    }

    public long getLength()
    {
        if ( length == null )
        {
            length = file.length();
        }
        return length;
    }

    public long getLastModifiedDate()
    {
        if ( lastModifiedDate == null )
        {
            lastModifiedDate = file.lastModified();
        }
        return lastModifiedDate;
    }

    public String getName()
    {
        if ( displayName == null )
        {
            displayName = fileSystemView.getSystemDisplayName( file );
        }
        return displayName;
    }

    public String getDescription()
    {
        if ( description == null )
        {
            description = fileSystemView.getSystemTypeDescription( file );
        }
        return description;
    }

    public String getExifDate()
    {
        if ( !exifDataLoaded )
        {
            loadExifData();
        }
        return exifDate;
    }

    public String getExifFocal()
    {
        if ( !exifDataLoaded )
        {
            loadExifData();
        }
        return exifFocal;
    }

    public String getExifExposure()
    {
        if ( !exifDataLoaded )
        {
            loadExifData();
        }
        return exifExposure;
    }

    public String getExifAperture()
    {
        if ( !exifDataLoaded )
        {
            loadExifData();
        }
        return exifAperture;
    }

    public String getExifCamera()
    {
        if ( !exifDataLoaded )
        {
            loadExifData();
        }
        return exifCamera;
    }

    public String getExifModel()
    {
        if ( !exifDataLoaded )
        {
            loadExifData();
        }
        return exifModel;
    }

    private void loadExifData()
    {
        try
        {
            final Metadata metadata = JpegMetadataReader.readMetadata( getFile() );
            final ExifDirectory exifDirectory = (ExifDirectory) metadata.getDirectory( ExifDirectory.class );
            if ( exifDirectory != null )
            {
                exifDate = exifDirectory.getString( ExifDirectory.TAG_DATETIME_ORIGINAL );
                exifFocal = exifDirectory.getString( ExifDirectory.TAG_FOCAL_LENGTH );
                if ( exifFocal != null )
                {
                    exifFocal += " mm";
                }
                exifExposure = exifDirectory.getString( ExifDirectory.TAG_EXPOSURE_TIME );
                if ( exifExposure != null )
                {
                    exifExposure += " s";
                }
                exifAperture = exifDirectory.getString( ExifDirectory.TAG_APERTURE );
                if ( exifAperture != null )
                {
                    exifAperture += " APEX";
                }
                exifCamera = exifDirectory.getString( ExifDirectory.TAG_MAKE );
                exifModel = exifDirectory.getString( ExifDirectory.TAG_MODEL );
            }
        }
        catch ( JpegProcessingException e )
        {
            // ignored
        }
        finally
        {
            exifDataLoaded = true;
        }
    }

    public boolean equals( final Object o )
    {
        if ( this == o )
        {
            return true;
        }
        if ( !( o instanceof PictureImpl ) )
        {
            return false;
        }

        final PictureImpl picture = (PictureImpl) o;

        return getAbsolutePath().equals( picture.getAbsolutePath() );
    }

    public int hashCode()
    {
        return getAbsolutePath().hashCode();
    }

    public String toString()
    {
        return getName();
    }
}
