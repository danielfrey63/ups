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
import ch.xmatrix.ups.pmb.domain.SpeciesEntry;
import ch.xmatrix.ups.pmb.ui.model.Settings;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.3 $ $Date: 2008/01/23 22:18:50 $
 */
public class SpeciesParser extends Parser
{
    public SpeciesParser( final Settings settings )
    {
        super( settings );
    }

    /**
     * Processes the given files species directories into a map of {@link SpeciesEntry}s (for each species directory)
     * and mapping of hierarchic entries (entry path -> entry object). The hieraric entries are added to the species
     * entries children list as well.
     *
     * @param file the directory
     * @return set of species entries
     */
    public Set<SpeciesEntry> processFile( final File file )
    {
        // Map containing the species entry (basically a comparable directory) on the key side, and a map of hierarchic
        // entries (entry path -> entry object).
        final Set<SpeciesEntry> species = new TreeSet<SpeciesEntry>();
        final Set<String> speciesDirs = new TreeSet<String>();
        findSpeciesDirs( file, speciesDirs );
        for ( final String speciesDir : speciesDirs )
        {
            final Map<String, Entry> map = new TreeMap<String, Entry>();
            final File[] speciesFiles = new File( speciesDir ).listFiles( filter );
            final SpeciesEntry speciesEntry = new SpeciesEntry( speciesDir, null, settings );
            for ( final File speciesFile : speciesFiles )
            {
                addEntry( speciesFile, map );
            }
            final Collection<Entry> entries = map.values();
            for ( final Entry entry : entries )
            {
                speciesEntry.add( entry );
            }
            species.add( speciesEntry );
        }
        return species;
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

    /**
     * Adds recursively all parent directories containing a file to the list.
     *
     * @param file the file to start
     * @param list the list to add the directories to
     */
    private void findSpeciesDirs( final File file, final Set<String> list )
    {
        if ( filter.accept( file.getParentFile(), file.getName() ) )
        {
            if ( file.isFile() )
            {
                list.add( file.getParent().replaceAll( "\\\\", "/" ) );
            }
            else
            {
                final File[] files = file.listFiles();
                for ( final File file1 : files )
                {
                    findSpeciesDirs( file1, list );
                }
            }
        }
    }

    /**
     * Collects all {@link FileEntry}s within the given Entry ant puts
     *
     * @param entry
     * @param mapping
     */
    public static void findFileEntriesWithin( final Entry entry, final Map<Entry, FileEntry> mapping )
    {
        final List<Entry> entries = entry.getList();
        for ( final Entry child : entries )
        {
            if ( child instanceof FileEntry )
            {
                mapping.put( entry, (FileEntry) child );
            }
            else
            {
                final List<FileEntry> fileEntries = findFileEntriesFor( child );
                if ( fileEntries.size() > 0 )
                {
                    mapping.put( child, fileEntries.get( 0 ) );
                }
                findFileEntriesWithin( child, mapping );
            }
        }
    }

    /**
     * Filters all {@link FileEntry}s within the given entry and returns them.
     *
     * @param entry the entry of which the children should be searched for {@link FileEntry}s
     * @return a list of {@link FileEntry}s
     */
    static List<FileEntry> findFileEntriesFor( final Entry entry )
    {
        final List<Entry> children = entry.getList();
        final List<FileEntry> fileEntries = new ArrayList<FileEntry>();
        for ( final Entry child : children )
        {
            if ( child instanceof FileEntry )
            {
                final FileEntry fileEntry = (FileEntry) child;
                fileEntries.add( fileEntry );
            }
        }
        return fileEntries;
    }
}
