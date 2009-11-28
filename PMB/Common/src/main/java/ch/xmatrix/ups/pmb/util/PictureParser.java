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
package ch.xmatrix.ups.pmb.util;

import ch.xmatrix.ups.pmb.domain.Entry;
import ch.xmatrix.ups.pmb.domain.FileEntry;
import ch.xmatrix.ups.pmb.ui.model.Settings;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Parsees the picture tree and their names.
 *
 * @author Daniel Frey
 * @version $Revision: 1.3 $ $Date: 2008/01/23 22:18:50 $
 */
public class PictureParser extends Parser
{
    public PictureParser( final Settings settings )
    {
        super( settings );
    }

    public Collection<Entry> processFile( final File file )
    {
        final Map<String, Entry> entries = new TreeMap<String, Entry>();
        processFile( file, entries );
        return entries.values();
    }

    /**
     * Returns a map of hierarchically structured navigation actions for nodes and file entries for leaves.
     *
     * @param file    the root file to start parsing
     * @param entries a map containing the tree information
     */
    protected void processFile( final File file, final Map<String, Entry> entries )
    {
        if ( filter.accept( file.getParentFile(), file.getName() ) )
        {
            if ( file.isFile() )
            {
                addEntry( file, entries );
            }
            else
            {
                final File[] files = file.listFiles();
                for ( final File file1 : files )
                {
                    processFile( file1, entries );
                }
            }
        }
    }

    private void addEntry( final File file, final Map<String, Entry> entries )
    {
        final String path = file.getAbsolutePath();
        final String name = file.getName();
        final List<String> tokens = settings.getHierarchicTokens( name );
        Entry parent = null;
        final List<Entry> defaults = new ArrayList<Entry>();
        for ( final String token : tokens )
        {
            final Entry entry;
            if ( parent == null )
            {
                if ( entries.containsKey( token ) )
                {
                    entry = entries.get( token );
                }
                else
                {
                    entry = new Entry( token, parent, settings );
                    entries.put( token, entry );
                }
            }
            else
            {
                if ( parent.get( token ) == null )
                {
                    entry = new Entry( token, parent, settings );
                }
                else
                {
                    entry = parent.get( token );
                }
            }
            if ( settings.isDefault( name, token ) )
            {
                defaults.add( entry );
            }
            parent = entry;
        }
        final FileEntry fileEntry = new FileEntry( path, parent, settings );
        for ( final Entry entry : defaults )
        {
            entry.setDefault( fileEntry );
        }
    }
}
