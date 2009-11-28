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

import ch.xmatrix.ups.pmb.ui.model.Settings;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.2 $ $Date: 2007/09/27 10:47:37 $
 */
public class Entry implements Comparable<Entry>
{
    private String path;

    private final Entry parent;

    private final List<Entry> list;

    private FileEntry def;

    private final Settings settings;

    public Entry( final String name, final Entry parent, final Settings settings )
    {
        this.path = name.replaceAll( "\\\\", "/" );
        this.settings = settings;
        this.parent = parent;
        if ( parent != null )
        {
            parent.list.add( this );
        }
        list = new ArrayList<Entry>();
    }

    public Entry get( final int i )
    {
        return list.size() > i ? list.get( i ) : null;
    }

    public Entry get( final String name )
    {
        for ( final Entry entry : list )
        {
            if ( entry.path.equals( name ) )
            {
                return entry;
            }
        }
        return null;
    }

    public List<Entry> getList()
    {
        return list;
    }

    public void add( final Entry e )
    {
        list.add( e );
    }

    public int size()
    {
        return list.size();
    }

    public Entry getParent()
    {
        return parent;
    }

    public String getPath()
    {
        return path;
    }

    public void setPath( final String path )
    {
        this.path = path;
    }

    public FileEntry getDefault()
    {
        return def;
    }

    public void setDefault( final FileEntry def )
    {
        this.def = def;
    }

    public Settings getSettings()
    {
        return settings;
    }

    @Override
    public String toString()
    {
        return path;
    }

    public int compareTo( final Entry o )
    {
        return path.compareTo( o.path );
    }
}
