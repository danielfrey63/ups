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

package net.java.jveez.config;

import java.io.File;
import net.java.jveez.cache.impl.PersistentThumbnailLoader;
import org.apache.log4j.Logger;
import org.garret.perst.Storage;
import org.garret.perst.StorageFactory;

public class ConfigurationManager
{
    private static final Logger LOG = Logger.getLogger( PersistentThumbnailLoader.class );

    private static final int CONFIG_PAGE_POOL_SIZE = 256 * 1024;  // 4 ko

    private static final ConfigurationManager instance = new ConfigurationManager();

    private final File jveezHome;

    private Storage storage;

    private final ConfigurationRoot configurationRoot;

    public static ConfigurationManager getInstance()
    {
        return instance;
    }

    private ConfigurationManager()
    {
        super();

        jveezHome = new File( System.getProperty( "user.home" ), ".jveez" );
        jveezHome.mkdirs();

        final File configFile = new File( jveezHome, "config.db" );

        storage = StorageFactory.getInstance().createStorage();
        storage.setProperty( "perst.object.cache.kind", "weak" );
        storage.open( configFile.getAbsolutePath(), CONFIG_PAGE_POOL_SIZE );
        if ( storage.getRoot() == null )
        {
            LOG.info( "Creating new configuration ..." );

            configurationRoot = new ConfigurationRoot();
            configurationRoot.systemConfiguration = new SystemConfiguration();
            configurationRoot.systemConfiguration.resetToDefaults();
            configurationRoot.mainFrameConfiguration = new MainFrameConfiguration();
            configurationRoot.mainFrameConfiguration.resetToDefaults();

            storage.setRoot( configurationRoot );
        }
        else
        {
            configurationRoot = (ConfigurationRoot) storage.getRoot();
            LOG.info( "Opening existing system configuration" );
        }

        Runtime.getRuntime().addShutdownHook( new Thread()
        {
            public void run()
            {
                release();
            }
        } );
    }

    public File getJveezHome()
    {
        return jveezHome;
    }

    public SystemConfiguration getSystemConfiguration()
    {
        return configurationRoot.systemConfiguration;
    }

    public MainFrameConfiguration getMainFrameConfiguration()
    {
        return configurationRoot.mainFrameConfiguration;
    }

    public void flushPendingChanges()
    {
        if ( storage != null && storage.isOpened() )
        {
            LOG.info( "Flushing configuration changes ..." );
            storage.commit();
        }
    }

    private void release()
    {
        if ( storage != null && storage.isOpened() )
        {
            LOG.info( "Releasing system configuration" );
            configurationRoot.systemConfiguration.setFirstRun( false );
            storage.commit();
            storage.close();
            storage = null;
        }
    }
}
