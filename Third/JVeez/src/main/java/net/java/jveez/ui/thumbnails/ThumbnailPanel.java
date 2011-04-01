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
package net.java.jveez.ui.thumbnails;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import net.java.jveez.ui.ThumbnailSortingBox;
import net.java.jveez.ui.directory.DirectoryBrowser;
import net.java.jveez.utils.PictureSortingAlgorithm;
import net.java.jveez.utils.Utils;
import net.java.jveez.vfs.Directory;
import net.java.jveez.vfs.Picture;
import net.java.jveez.vfs.Vfs;
import net.java.jveez.vfs.VfsEvent;
import net.java.jveez.vfs.VfsEventListener;
import org.apache.log4j.Logger;

public class ThumbnailPanel extends JPanel implements VfsEventListener
{
    private static final Logger LOG = Logger.getLogger( ThumbnailPanel.class );

    private final ThumbnailSortingBox sortingBox = new ThumbnailSortingBox( this, PictureSortingAlgorithm.ByDate );

    //  private JLabel currentDirectoryLabel = new JLabel();
    private final DirectoryBrowser directoryBrowser = new DirectoryBrowser();

    public ThumbnailList thumbnailList;

    public ThumbnailPanel( final ThumbnailList thumbnailList )
    {
        super();
        this.thumbnailList = thumbnailList;
        setupComponents();
        layoutComponents();

        Vfs.getInstance().addVfsListener( this );
    }

    private void layoutComponents()
    {
        setLayout( new BorderLayout() );
        final JPanel topPanel = new JPanel( new BorderLayout() );

        topPanel.add( directoryBrowser, BorderLayout.CENTER );

        topPanel.add( sortingBox, BorderLayout.EAST );
        add( topPanel, BorderLayout.NORTH );

        final JScrollPane comp = new JScrollPane( thumbnailList );
        add( comp, BorderLayout.CENTER );
    }

    private void setupComponents()
    {
        directoryBrowser.addPropertyChangeListener( "directory", new PropertyChangeListener()
        {
            public void propertyChange( final PropertyChangeEvent evt )
            {
                final Directory directory = (Directory) evt.getNewValue();
                setCurrentDirectory( directory );
            }
        } );
    }

    public void setCurrentDirectory( final Directory directory )
    {
        directoryBrowser.setCurrentDirectory( directory );
        final ThumbnailListModel model = thumbnailList.getThumbnailListModel();
        Utils.executeAsyncIfDisptachThread( new Runnable()
        {
            public void run()
            {
                model.clear();
                thumbnailList.repaint();

                if ( directory != null )
                {
                    try
                    {
                        thumbnailList.setLoading( true );

                        final Collection<? extends Picture> pictures = Vfs.getInstance().getPictures( directory );
                        model.setPictures( pictures );
                        model.sort( PictureSortingAlgorithm.ByName );

                    }
                    finally
                    {
                        thumbnailList.setLoading( false );
                    }
                }
            }
        } );
    }

    public void setSortingAlgorithm( final PictureSortingAlgorithm value )
    {
        thumbnailList.setSortingAlgorithm( value );
    }

    public void vfsEventDispatched( final VfsEvent event )
    {
        LOG.debug( "vfsEventDispatched(" + event + ")" );

        switch ( event.getEventType() )
        {
            case VFS_CLOSED:
                setCurrentDirectory( null );
                break;
        }
    }
}
