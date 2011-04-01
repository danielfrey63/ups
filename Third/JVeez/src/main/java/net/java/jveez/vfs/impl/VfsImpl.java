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

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.swing.filechooser.FileSystemView;
import net.java.jveez.JVeez;
import net.java.jveez.utils.Utils;
import net.java.jveez.vfs.Directory;
import net.java.jveez.vfs.Picture;
import net.java.jveez.vfs.Vfs;
import org.apache.log4j.Logger;

public class VfsImpl extends Vfs
{
    private static final Logger LOG = Logger.getLogger( VfsImpl.class );

    private static final FileFilter DIRECTORY_FILE_FILTER = new FileFilter()
    {
        public boolean accept( final File pathname )
        {
            return pathname.isDirectory();
        }
    };

    private static final FileFilter PICTURE_FILE_FILTER = new FileFilter()
    {
        public boolean accept( final File pathname )
        {
            return pathname.isFile() && Utils.isSupportedImage( pathname );
        }
    };

    private static final FileSystemView FILE_SYSTEM_VIEW = FileSystemView.getFileSystemView();

    private static final List<DirectoryImpl> EMPTY_DIRECTORY_LIST = Collections.unmodifiableList( new ArrayList<DirectoryImpl>( 0 ) );

    private static final List<PictureImpl> EMPTY_PICTURE_LIST = Collections.unmodifiableList( new ArrayList<PictureImpl>( 0 ) );

    private final List<DirectoryImpl> rootDirectories = new ArrayList<DirectoryImpl>();

    private final ConcurrentMap<DirectoryImpl, List<DirectoryImpl>> directoryMap = new ConcurrentHashMap<DirectoryImpl, List<DirectoryImpl>>();

    private final ConcurrentMap<DirectoryImpl, List<PictureImpl>> pictureMap = new ConcurrentHashMap<DirectoryImpl, List<PictureImpl>>();

    public VfsImpl()
    {
        initRootDirectories();
    }

    private void initRootDirectories()
    {
        LOG.debug( "Loading root directoryMap from file system" );

        final File[] roots = FILE_SYSTEM_VIEW.getRoots();
//    File[] roots = File.listRoots();
        for ( final File root : roots )
        {
            rootDirectories.add( new DirectoryImpl( null, root ) );
        }

        LOG.debug( String.format( "%d root directorie(s) has been found", rootDirectories.size() ) );
    }

    public boolean isCached( final Directory directory )
    {
        if ( directory == null )
        {
            return true;
        }

        return directoryMap.containsKey( directory );
    }

    public Collection<? extends Directory> getRootDirectories()
    {
        return Collections.unmodifiableList( rootDirectories );
    }

    public boolean hasSubDirectories( final Directory directory )
    {
        if ( directory == null )
        {
            return !rootDirectories.isEmpty();
        }

        final Collection<? extends Directory> directories = getSubDirectories( directory );
        return !directories.isEmpty();
    }

    public boolean hasPictures( final Directory directory )
    {
        if ( directory == null )
        {
            return !rootDirectories.isEmpty();
        }

        final Collection<? extends Picture> pictures = getPictures( directory );
        return !pictures.isEmpty();
    }

    public Collection<? extends Directory> getSubDirectories( final Directory directory )
    {
        if ( directory == null )
        {
            return rootDirectories;
        }

        List<DirectoryImpl> children;
        synchronized ( directory )
        {
            children = directoryMap.get( directory );
            if ( children == null )
            {
                final DirectoryImpl parent = (DirectoryImpl) directory;
                children = loadDirectories( parent );
                directoryMap.put( parent, children );
            }
        }

        assert children != null;

        return children;
    }

    public Collection<? extends Picture> getPictures( final Directory directory )
    {
        if ( directory == null )
        {
            return EMPTY_PICTURE_LIST;
        }

        List<PictureImpl> pictures;
        synchronized ( directory )
        {
            pictures = pictureMap.get( directory );
            if ( pictures == null )
            {
                final DirectoryImpl parent = (DirectoryImpl) directory;
                pictures = loadPictures( parent );
                pictureMap.put( parent, pictures );
            }
        }

        assert pictures != null;

        return pictures;
    }

    private List<DirectoryImpl> loadDirectories( final DirectoryImpl parent )
    {
        LOG.info( "Loading directories from " + parent );

        JVeez.showActivity( true );
        try
        {
            final File[] children = FILE_SYSTEM_VIEW.getFiles( parent.getFile(), false );

            if ( children != null && children.length > 0 )
            {
                final List<DirectoryImpl> directories = new ArrayList<DirectoryImpl>( children.length );
                for ( final File child : children )
                {
                    if ( !DIRECTORY_FILE_FILTER.accept( child ) )
                    {
                        continue;
                    }

                    final DirectoryImpl directoryImpl = new DirectoryImpl( parent, child );
                    directories.add( directoryImpl );
                }
                return directories;
            }
            else
            {
                return EMPTY_DIRECTORY_LIST;
            }
        }
        finally
        {
            JVeez.showActivity( false );
        }
    }

    private List<PictureImpl> loadPictures( final DirectoryImpl parent )
    {
        LOG.info( "Loading pictures from " + parent );

        JVeez.showActivity( true );
        try
        {
            final File[] files = parent.getFile().listFiles( PICTURE_FILE_FILTER );
            if ( files != null && files.length > 0 )
            {
                final List<PictureImpl> pictures = new ArrayList<PictureImpl>( files.length );
                for ( final File file : files )
                {
                    pictures.add( new PictureImpl( file ) );
                }
                return pictures;
            }
            else
            {
                return EMPTY_PICTURE_LIST;
            }
        }
        finally
        {
            JVeez.showActivity( false );
        }
    }

    /** In this method, we will scan the directory hierarchy under the root path and create the corresponding pages. */
    public synchronized void synchronize()
    {
        LOG.debug( "synchronize()" );
    }
}