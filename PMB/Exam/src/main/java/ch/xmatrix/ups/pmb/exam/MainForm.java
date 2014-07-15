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
package ch.xmatrix.ups.pmb.exam;

import ch.jfactory.math.RandomUtils;
import ch.xmatrix.ups.model.ExamsetModel;
import ch.xmatrix.ups.model.SetTaxon;
import ch.xmatrix.ups.pmb.domain.Entry;
import ch.xmatrix.ups.pmb.domain.FileEntry;
import ch.xmatrix.ups.pmb.domain.SpeciesEntry;
import ch.xmatrix.ups.pmb.ui.controller.AdjustSizeLoadedListener;
import ch.xmatrix.ups.pmb.ui.controller.NavigationSelectionHandler;
import ch.xmatrix.ups.pmb.ui.controller.PMBController;
import ch.xmatrix.ups.pmb.ui.model.EntryTreeModel;
import ch.xmatrix.ups.pmb.ui.model.FileEntryThumbnailListModel;
import ch.xmatrix.ups.pmb.ui.model.Settings;
import ch.xmatrix.ups.pmb.util.SpeciesParser;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.TreeSelectionModel;
import javax.xml.transform.TransformerException;
import net.java.jveez.cache.ImageStore;
import net.java.jveez.vfs.Picture;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Exam main form. Uses the following system properties:
 * <ul>
 * <li><code>speciesDirectory</code>: The directory containing the species XML files (used in {@link ch.xmatrix.ups.pmb.exam.MainForm#initModel()})</li>
 * <li><code>species</code>: The name of the XML file for this session (used in {@link ch.xmatrix.ups.pmb.exam.MainForm#initModel()})</li>
 * <li><code>imageDirectory</code>: The directory of the top picture directory</li>
 * <li><code>duration</code>: The time to run the examination</li>
 * </ul>
 *
 * @author Daniel Frey
 * @version $Revision: 1.3 $ $Date: 2008/01/23 22:18:44 $
 */
public class MainForm extends ExamForm
{
    private static final Logger LOG = LoggerFactory.getLogger( MainForm.class );

    private final PMBExamModel model;

    private final PMBController controller;

    private boolean adjusting = false;

    private final FileEntry currentFileEntry = null;

    private List<SpeciesEntry> examsetModelSpeciesEntries;

    private long cacheSize;

    public MainForm( final String s )
    {
        super( true );
        initCacheSize();
        setTitle( s );
        model = new PMBExamModel();
        controller = new PMBController( model, this );
        try
        {
            initMyComponents();
            initListeners();
            initModel();
        }
        catch ( PropertyVetoException e )
        {
            e.printStackTrace();
        }
        catch ( TransformerException e )
        {
            e.printStackTrace();
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
        if ( !"".equals( System.getProperty( "password" ) ) )
        {
            final StartDialog startDialog = new StartDialog( this );
            startDialog.setDefaultCloseOperation( DISPOSE_ON_CLOSE );
            startDialog.setVisible( true );
        }
    }

    private void initCacheSize()
    {
        final Runtime runtime = Runtime.getRuntime();
        final long max = runtime.maxMemory();
        final long free = runtime.freeMemory();
        final long total = runtime.totalMemory();
        final int memoryPerPicture = 1024 * 1024 * 25;
        cacheSize = (max - total + free) / memoryPerPicture;
        ImageStore.setInitialMemoryCacheSize( (int) cacheSize );
        LOG.info( "initialized cache size to " + cacheSize );
    }

    private void initMyComponents() throws PropertyVetoException
    {
        statusBar.setVisible( false );
        imageRight.setThumnailList( thumbnailList );
        imageRight.setLoadingText( "Lade Bild..." );
        imageRight.setNoImageText( "Kein Merkmal im Baum gewählt" );
        imageRight.setToolbarVisible( false );
        imageLeft.setLoadingText( "Lade Bild..." );
        imageLeft.setNoImageText( "Kein Merkmal im Baum gewählt" );
        imageLeft.setToolbarVisible( false );
        thumbnailScroller.getViewport().setOpaque( false );
        thumbnailList.setThumbnailSize( 128 );
        thumbnailList.setFixedCellHeight( 136 );
        thumbnailList.setFixedCellWidth( 136 );
        thumbnailList.setCellRenderer( new ExamPictureListCellRenderer() );
        navigationScroller.getViewport().setOpaque( false );
        navigation.setCellRenderer( new NavigationRenderer() );
        navigation.getSelectionModel().setSelectionMode( TreeSelectionModel.SINGLE_TREE_SELECTION );
    }

    private void initListeners()
    {
        setDefaultCloseOperation( DO_NOTHING_ON_CLOSE );
        imageLeft.addLoadedListener( new AdjustSizeLoadedListener( model.getInfoModel() ) );
        imageRight.addLoadedListener( new AdjustSizeLoadedListener( model.getInfoModel() ) );
        thumbnailList.addListSelectionListener( new ListSelectionListener()
        {
            public void valueChanged( final ListSelectionEvent e )
            {
                if ( !adjusting && !e.getValueIsAdjusting() )
                {
                    adjusting = true;
                    final int index = thumbnailList.getSelectedIndex();
                    if ( index >= 0 )
                    {
                        controller.setCurrentPicture( (Picture) null, imageRight );
                        final SpeciesEntry speciesEntry = examsetModelSpeciesEntries.get( index );
                        model.setCurrentSpeciesEntry( speciesEntry );
                        initSelection();
                    }
                    adjusting = false;
                }
            }
        } );
        navigation.addTreeSelectionListener( new NavigationSelectionHandler( imageRight, model, controller )
        {
            @Override
            public void valueChanged( final TreeSelectionEvent e )
            {
                if ( !adjusting )
                {
                    adjusting = true;
                    super.valueChanged( e );
                    if ( thumbnailList != null && currentFileEntry != null )
                    {
                        thumbnailList.setSelectedValue( currentFileEntry.getPicture(), false );
                    }
                    adjusting = false;
                }
            }
        } );
        model.addPropertyChangeListener( PMBExamModel.PROPERTY_CURRENT_EXAMSET, new PropertyChangeListener()
        {
            public void propertyChange( final PropertyChangeEvent evt )
            {
                try
                {
                    initAllSpeciesEntries();
                    initAllExamsetModelSpeciesEntries();
                    if ( examsetModelSpeciesEntries != null && examsetModelSpeciesEntries.size() > 0 )
                    {
                        model.setCurrentSpeciesEntry( examsetModelSpeciesEntries.get( 0 ) );
                        initStudent();
                    }
                }
                catch ( ConfigurationException e )
                {
                    LOG.error( "could not configure", e );
                }
            }
        } );
    }

    private void initModel() throws TransformerException, IOException
    {
        model.getSettings().setActivePicturePath( System.getProperty( "imagesDirectory" ) );

        final String speciesDirectory = System.getProperty( "speciesDirectory" );
        final String species = System.getProperty( "species" );
        final InputStreamReader reader = new FileReader( new File( speciesDirectory, species ) );
        model.setDemo( false );
        model.setCurrentExamsetModel( StudentDataLoader.getExamsetModels( reader ).getExamsetModels().get( 0 ) );
        IOUtils.closeQuietly( reader );
    }

    private void initStudent()
    {
        initHabitusFileEntries();
        initThumbnailList();
        initSelection();
        initCache();
    }

    /** Inits the model by collecting the "Habitus" file for each species. */
    private void initHabitusFileEntries()
    {
        model.getFileEntries().clear();
        for ( final SpeciesEntry examsetModelSpeciesEntry : examsetModelSpeciesEntries )
        {
            final FileEntry fileEntry = getHabitusFileEntries( examsetModelSpeciesEntry );
            if ( fileEntry == null )
            {
                LOG.error( "no \"Herbar\" file found in \"" + examsetModelSpeciesEntry + "\"" );
            }
            else
            {
                model.getFileEntries().add( fileEntry );
                LOG.info( "adding files for " + examsetModelSpeciesEntry );
            }
        }
    }

    private void initThumbnailList()
    {
        final FileEntryThumbnailListModel thumbnailModel = model.getThumbnailModel();
        final ArrayList<FileEntry> pictures = model.getFileEntries();
        thumbnailModel.setFileEntries( pictures );
        thumbnailList.setModel( thumbnailModel );
        thumbnailList.setSelectedIndex( 0 );
    }

    private void initSelection()
    {
        final SpeciesEntry currentSpeciesEntry = model.getCurrentSpeciesEntry();
        controller.setCurrentPicture( (Picture) null, imageRight );
        navigation.setModel( new EntryTreeModel( currentSpeciesEntry.getList() ) );
        // findHabitus needs the hierarchical mapping
        final Map<Entry, FileEntry> hierarchicalMapping = model.getHierarchicalMapping();
        hierarchicalMapping.clear();
        SpeciesParser.findFileEntriesWithin( currentSpeciesEntry, hierarchicalMapping );
        controller.setCurrentPicture( controller.findHabitus(), imageLeft );
    }

    /** Precaches the habitus pictures. */
    private void initCache()
    {
        try
        {
            final Runnable runnable = new Runnable()
            {
                public void run()
                {
                    final ArrayList<FileEntry> pictures = model.getFileEntries();
                    final long start = System.currentTimeMillis();
                    final long count = Math.min( pictures.size(), cacheSize );
                    for ( int i = 0; i < count; i++ )
                    {
                        final FileEntry fileEntry = pictures.get( i );
                        if ( fileEntry != null )
                        {
                            ImageStore.getInstance().getImage( fileEntry.getPicture() );
                        }
                    }
                    final double delta = (System.currentTimeMillis() - start) / 1000.0;
                    LOG.debug( "finished caching " + count + " images in " + delta + "secs" );
                }
            };
            final Thread thread = new Thread( runnable );
            thread.start();
            thread.join();
        }
        catch ( InterruptedException e )
        {
            LOG.error( "interrupted", e );
        }
    }

    /**
     * Searches recursively for a file entry starting with "Habitus" and returns it. If none is found, returns {@code null}.
     *
     * @param entry the top entry to start search
     * @return the "Habitus" file entry or null
     */
    private FileEntry getHabitusFileEntries( final Entry entry )
    {
        for ( int i = 0; i < entry.size(); i++ )
        {
            final Entry child = entry.get( i );
            if ( child instanceof FileEntry && new File( child.getPath() ).getName().startsWith( "Herbar" ) )
            {
                return (FileEntry) child;
            }
            else
            {
                final FileEntry fileEntry = getHabitusFileEntries( child );
                if ( fileEntry != null )
                {
                    return fileEntry;
                }
            }
        }
        return null;
    }

    /**
     * Reads all directories from the active picture path and keeps those matching the species criteria. The result is stored in {@link PMBExamModel#getSpeciesEntries()}.
     *
     * @throws ConfigurationException passed through
     */
    private void initAllSpeciesEntries() throws ConfigurationException
    {
        final Settings settings = model.getSettings();
        final String directory = settings.getActivePicturePath();
        model.setSpeciesEntries( new SpeciesParser( settings ).processFile( new File( directory ) ) );
    }

    /** Collects all {@link SpeciesEntry} objects for the current {@link ExamsetModel}. If not all {@link SetTaxon} objects of {@code ExamsetModel} can be mapped to a {@code SpeciesEntry} object, a warning is issued. The result is stored in {@link #examsetModelSpeciesEntries}. */
    private void initAllExamsetModelSpeciesEntries()
    {
        final Settings settings = model.getSettings();
        final ExamsetModel examModel = model.getCurrentExamsetModel();
        final List<SetTaxon> examTaxa = examModel.getSetTaxa();
        examsetModelSpeciesEntries = new ArrayList<SpeciesEntry>( examTaxa.size() );
        final StringBuffer missing = new StringBuffer();
        for ( final SetTaxon setTaxon : examTaxa )
        {
            final List<SpeciesEntry> aspects = new ArrayList<SpeciesEntry>();
            for ( final SpeciesEntry entry : model.getSpeciesEntries() )
            {
                final String entrySpeciesName = settings.getCleanedSpeciesName( entry );
                final String setSpeciesName = setTaxon.getSpecimenModel().getTaxon();
                if ( entrySpeciesName.equals( setSpeciesName ) || entrySpeciesName.startsWith( setSpeciesName + "_" ) )
                {
                    aspects.add( entry );
                    if ( entrySpeciesName.equals( setSpeciesName ) )
                    {
                        break;
                    }
                }
            }
            final int size = aspects.size();
            if ( size == 0 )
            {
                LOG.warn( "pictures or directory for \"" + setTaxon + "\" " +
                        "not found in active pictures path \"" + settings.getActivePicturePath() + "\"" );
                missing.append( setTaxon.getSpecimenModel().getTaxon() ).append( "\\n" );
            }
            else if ( size > 0 )
            {
                LOG.info( "found " + size + " aspects for " + setTaxon );
                final SpeciesEntry[] entries = new SpeciesEntry[size];
                RandomUtils.randomize( aspects.toArray( entries ) );
                examsetModelSpeciesEntries.add( entries[0] );
                LOG.info( "using " + entries[0] + " for aspect" );
            }
        }
        if ( !"".equals( missing.toString().trim() ) )
        {
            final JTextPane field = new JTextPane();
            field.setOpaque( false );
            final EmptyBorder border = new EmptyBorder( 0, 0, 0, 0 );
            field.setBorder( border );
            field.setText( "Folgende Arten konnten im Bilderverzeichnis nicht gefunden werden:\r\n" + missing.toString() );
            final JScrollPane scroll = new JScrollPane( field );
            scroll.setBorder( border );
            JOptionPane.showOptionDialog( this, scroll, "Fehlende Bilder", JOptionPane.DEFAULT_OPTION,
                    JOptionPane.WARNING_MESSAGE, null, new Object[0], null );
        }
    }

    protected void doQuit()
    {
        System.exit( 0 );
    }

    protected void doSaveHabitusPosition()
    {
        final String newPath = controller.savePositionAndZoom( imageLeft );
        final FileEntry fileEntry = controller.findHabitus();
        if ( newPath != null )
        {
            fileEntry.setPath( newPath );
        }
        controller.setCurrentPicture( fileEntry, imageLeft );
    }

    protected void doSaveOtherPosition()
    {
        final String newPath = controller.savePositionAndZoom( imageRight );
        final int index = thumbnailList.getSelectedIndex();
        navigation.setModel( new EntryTreeModel( model.getCurrentSpeciesEntry().getList() ) );
        if ( index > -1 )
        {
            final FileEntry fileEntry = model.getFileEntries().get( index );
            if ( newPath != null )
            {
                fileEntry.setPath( newPath );
            }
            controller.setCurrentPicture( fileEntry, imageRight );
        }
    }
}