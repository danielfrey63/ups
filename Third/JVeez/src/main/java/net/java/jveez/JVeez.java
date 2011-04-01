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
package net.java.jveez;

import javax.swing.UIManager;
import net.java.jveez.cache.ImageStore;
import net.java.jveez.cache.ThumbnailStore;
import net.java.jveez.ui.MainFrame;
import net.java.jveez.ui.StatusBar;
import net.java.jveez.vfs.Vfs;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

/**
 * JVeez class that starts the application.
 *
 * @author Eric Georges
 * @author Sebastien Petrucci
 */
public class JVeez
{
    private static final Logger LOG = Logger.getLogger( JVeez.class );

    private static MainFrame mainFrame;

    public static MainFrame getMainFrame()
    {
        return mainFrame;
    }

    public static void showActivity( final boolean b )
    {
        final MainFrame mainFrame = getMainFrame();
        if ( mainFrame != null )
        {
            final StatusBar statusBar = mainFrame.getStatusBar();
            if ( statusBar != null )
            {
                statusBar.showActivity( b );
            }
        }
    }

    public static void main( final String[] args )
    {
        BasicConfigurator.configure();

        ImageStore.getInstance();
        ThumbnailStore.getInstance();

//    RepaintManager.setCurrentManager(new CheckingRepaintManager());

        try
        {
            UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
        }
        catch ( Throwable t )
        {
            LOG.warn( "Could not setup look & feel !", t );
        }

        mainFrame = new MainFrame();
        mainFrame.setVisible( true );

        Vfs.getInstance().synchronize();   // this will initialize the folder tree
    }

}