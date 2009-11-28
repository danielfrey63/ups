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

package net.java.jveez.ui.fstree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.tree.TreeNode;
import net.java.jveez.utils.Utils;
import net.java.jveez.vfs.Directory;
import net.java.jveez.vfs.Vfs;

public class LazyDirectoryNode implements TreeNode
{
    private final Directory directory;

    private final LazyDirectoryNode parent;

    private List<LazyDirectoryNode> children;

    private final LazyDirectoryTreeModel model;

    private final AtomicBoolean loadingFlag = new AtomicBoolean( false );

    public LazyDirectoryNode( final LazyDirectoryTreeModel model, final LazyDirectoryNode parent, final Directory directory )
    {
        this.model = model;
        this.parent = ( parent == null ? this : parent );
        this.directory = directory;
    }

    public Directory getDirectory()
    {
        return directory;
    }

    protected boolean _loadContent()
    {
        if ( children != null )
        {
            return false;
        }

        if ( loadingFlag.getAndSet( true ) )
        {
            return true;
        }

        Utils.executeAsync( new Runnable()
        {
            public void run()
            {
                final Collection<? extends Directory> subDirectories = getSubDirectories( directory );
                final List<LazyDirectoryNode> list = new ArrayList<LazyDirectoryNode>( subDirectories.size() );
                for ( final Directory dir : subDirectories )
                {
                    list.add( new LazyDirectoryNode( model, LazyDirectoryNode.this, dir ) );
                }

                children = list;
                loadingFlag.set( false );
                model.nodeLoaded( LazyDirectoryNode.this );
            }
        } );

        return true;
    }

    private Collection<? extends Directory> getSubDirectories( final Directory directory )
    {
        return Vfs.getInstance().getSubDirectories( directory );
    }

    public TreeNode getChildAt( final int childIndex )
    {
        if ( _loadContent() )
        {
            return null;
        }
        else
        {
            return children.get( childIndex );
        }
    }

    public int getChildCount()
    {
        if ( _loadContent() )
        {
            return 0;
        }
        else
        {
            return children.size();
        }
    }

    public TreeNode getParent()
    {
        return parent;
    }

    public int getIndex( final TreeNode node )
    {
        if ( _loadContent() )
        {
            return -1;
        }
        else
        {
            return children.indexOf( node );
        }
    }

    public boolean getAllowsChildren()
    {
        return true;
    }

    public boolean isLeaf()
    {
        if ( _loadContent() )
        {
            return false;
        }
        else
        {
            return children.isEmpty();
        }
    }

    public Enumeration children()
    {
        if ( _loadContent() )
        {
            return new Enumeration()
            {
                public boolean hasMoreElements()
                {
                    return false;
                }

                public Object nextElement()
                {
                    return null;
                }
            };
        }
        else
        {
            return new Enumeration()
            {
                Iterator<LazyDirectoryNode> iterator = children.iterator();

                public boolean hasMoreElements()
                {
                    return iterator.hasNext();
                }

                public Object nextElement()
                {
                    return iterator.next();
                }
            };
        }
    }

    public String toString()
    {
        return ( directory == null ? "[ROOT]" : directory.getAbsolutePath() );
    }
}
