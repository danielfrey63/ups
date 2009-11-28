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
package ch.xmatrix.ups.pmb.input;

import ch.jfactory.application.view.status.Message;
import ch.jfactory.binding.InfoModel;
import ch.jfactory.component.ToolTipManager;
import ch.jfactory.component.tree.TreeUtils;
import ch.xmatrix.ups.pmb.domain.Entry;
import ch.xmatrix.ups.pmb.domain.FileEntry;
import ch.xmatrix.ups.pmb.domain.SpeciesEntry;
import ch.xmatrix.ups.pmb.ui.controller.AlertDialogNoteHandler;
import ch.xmatrix.ups.pmb.ui.controller.Log4jNoteHandler;
import ch.xmatrix.ups.pmb.ui.controller.NavigationSelectionHandler;
import ch.xmatrix.ups.pmb.ui.controller.PMBController;
import ch.xmatrix.ups.pmb.ui.controller.StatusBarNoteHandler;
import ch.xmatrix.ups.pmb.ui.model.EntryTreeModel;
import ch.xmatrix.ups.pmb.ui.model.FileEntryThumbnailListModel;
import ch.xmatrix.ups.pmb.ui.model.Settings;
import ch.xmatrix.ups.pmb.ui.view.PictureListRenderer;
import ch.xmatrix.ups.pmb.ui.view.SpeciesListRenderer;
import ch.xmatrix.ups.pmb.util.PictureParser;
import ch.xmatrix.ups.pmb.util.SpeciesParser;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import javax.swing.AbstractListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import net.java.jveez.cache.ThumbnailStore;
import net.java.jveez.ui.thumbnails.ThumbnailListModel;
import net.java.jveez.vfs.Picture;
import org.apache.log4j.Logger;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.3 $ $Date: 2008/01/23 22:18:35 $
 */
public class MainForm extends EntryForm
{
    private static final Logger LOG = Logger.getLogger( MainForm.class );

    private static final int TAB_SINGLE_IMAGE = 1;

    private static final int TAB_IMAGES_LIST = 0;

    private final MainModel model;

    private final PMBController controller;

    private final FileEntryThumbnailListModel thumbnailModel = new FileEntryThumbnailListModel();

    private final ArrayList<FileEntry> fileEntries = new ArrayList<FileEntry>();

    private JButton[] moveButtons;

    private boolean silently = false;

    public MainForm( final MainModel model )
    {
        this.model = model;
        controller = new PMBController( model, this );
        initMyComponents();
        initListeners();
        setSize( 800, 600 );
        if ( model.getSettings().getShowSettingsOnStartup() )
        {
            doShowPrefs();
        }
        initModels();
    }

    private void initMyComponents()
    {
        setTreeIcons( treeSpecies );
        setTreeIcons( treeOverview );
        imagePanel.setThumnailList( imagesPanel );
        imagesPanel.setCellRenderer( new PictureListRenderer() );
        new ToolTipManager( listOverview );
        new ToolTipManager( listSpecies );
        new ToolTipManager( treeOverview );
        new ToolTipManager( treeSpecies );
        final Settings settings = model.getSettings();
        final List<Settings.NamedValue> paths = settings.getMovePaths();
        for ( final Settings.NamedValue path : paths )
        {
            final String name = path.getName();
            if ( !name.startsWith( "+" ) )
            {
                final JButton button = new JButton( name );
                button.addActionListener( new ActionListener()
                {
                    public void actionPerformed( final ActionEvent e )
                    {
                        final String fromPath = settings.getActivePicturePath();
                        final String toPath = path.getValue();
                        controller.move( fromPath, toPath );
                        initModels();
                    }
                } );
                moveButtonsPanel.add( button );
            }
        }
    }

    private void initModels()
    {
        model.getSelectedImageFiles().clear();
        model.getSelectedPictures().clear();

        listOverview.clearSelection();
        treeOverview.clearSelection();
        fieldName.setText( "" );
        imagesPanel.setModel( thumbnailModel );
        imagesPanel.getThumbnailListModel().clear();
        controller.setCurrentPicture( (FileEntry) null, imagePanel );

        treeOverview.getSelectionModel().setSelectionMode( TreeSelectionModel.SINGLE_TREE_SELECTION );
        treeSpecies.getSelectionModel().setSelectionMode( TreeSelectionModel.SINGLE_TREE_SELECTION );

        final Settings settings = model.getSettings();
        final String activePath = settings.getActivePicturePath();
        final File rootDir = new File( activePath );
        final Collection<Entry> entries = new PictureParser( settings ).processFile( rootDir );
        final EntryTreeModel entryModel = new EntryTreeModel( entries );
        treeOverview.setModel( entryModel );

        final Set<SpeciesEntry> species = new SpeciesParser( settings ).processFile( rootDir );
        listSpecies.setModel( new AbstractListModel()
        {
            public int getSize()
            {
                return species.size();
            }

            public Object getElementAt( final int index )
            {
                return species.toArray()[index];
            }
        } );
        listSpecies.setCellRenderer( new SpeciesListRenderer( settings ) );
    }

    private void initListeners()
    {
        addWindowListener( new WindowAdapter()
        {
            @Override
            public void windowClosing( final WindowEvent e )
            {
                doQuit();
            }
        } );
        fieldName.addFocusListener( new FocusListener()
        {
            private JButton last;

            public void focusGained( final FocusEvent e )
            {
                if ( model.getSelectedPictures().size() > 0 )
                {
                    last = getRootPane().getDefaultButton();
                    getRootPane().setDefaultButton( renameButton );
                }
            }

            public void focusLost( final FocusEvent e )
            {
                getRootPane().setDefaultButton( last );
            }
        } );
        fieldName.addCaretListener( new CaretListener()
        {
            public void caretUpdate( final CaretEvent e )
            {
                setStates();
            }
        } );
        treeOverview.addTreeSelectionListener( new TreeSelectionListener()
        {
            public void valueChanged( final TreeSelectionEvent e )
            {
                final TreePath path = e.getPath();
                final Entry entry = (Entry) path.getLastPathComponent();
                fileEntries.clear();
                if ( e.isAddedPath() )
                {
                    listFileEntries( entry, fileEntries, model.isRecursiveOverviewList() );
                }
                listOverview.setModel( new AbstractListModel()
                {
                    public int getSize()
                    {
                        return fileEntries.size();
                    }

                    public Object getElementAt( final int index )
                    {
                        return fileEntries.get( index );
                    }
                } );
            }
        } );
        listOverview.addListSelectionListener( new ListSelectionListener()
        {
            public void valueChanged( final ListSelectionEvent e )
            {
                if ( !e.getValueIsAdjusting() )
                {
                    final Object[] selected = listOverview.getSelectedValues();
                    model.getSelectedImageFiles().clear();
                    for ( int i = 0; selected != null && i < selected.length; i++ )
                    {
                        model.getSelectedImageFiles().add( (FileEntry) selected[i] );
                    }
                    displayImages();
                }
            }
        } );
        listSpecies.addListSelectionListener( new ListSelectionListener()
        {
            public void valueChanged( final ListSelectionEvent e )
            {
                if ( !e.getValueIsAdjusting() )
                {
                    final SpeciesEntry entry = (SpeciesEntry) listSpecies.getSelectedValue();
                    if ( entry != null )
                    {
                        treeSpecies.setModel( new EntryTreeModel( entry.getList() ) );
                        fileEntries.clear();
                        listFileEntries( entry, fileEntries, true );
                        model.getSelectedImageFiles().clear();
                        model.getSelectedImageFiles().addAll( fileEntries );
                        displayImages();
                        doRemix();
                    }
                }
            }
        } );
        treeSpecies.addTreeSelectionListener( new NavigationSelectionHandler( imagePanel, model, controller )
        {
            public void valueChanged( final TreeSelectionEvent e )
            {
                super.valueChanged( e );
                imagesTab.setSelectedIndex( TAB_SINGLE_IMAGE );
                setStates();
            }
        } );
        imagesPanel.addMouseListener( new MouseAdapter()
        {
            @Override
            public void mouseClicked( final MouseEvent e )
            {
                if ( e.getClickCount() == 2 )
                {
                    final Picture picture = (Picture) imagesPanel.getSelectedValue();
                    controller.setCurrentPicture( picture, imagePanel );
                    imagesTab.setSelectedIndex( TAB_SINGLE_IMAGE );
                }
            }
        } );
        imagesPanel.getSelectionModel().addListSelectionListener( new ListSelectionListener()
        {
            public void valueChanged( final ListSelectionEvent e )
            {
                if ( !e.getValueIsAdjusting() )
                {
                    final List<Picture> pictures = model.getSelectedPictures();
                    pictures.clear();
                    final Object[] selected = imagesPanel.getSelectedValues();
                    for ( final Object aSelected : selected )
                    {
                        final Picture picture = (Picture) aSelected;
                        pictures.add( picture );
                    }
                    if ( controller.areNamesOfSelectedFilesEqual( silently, model.getSelectedPicturesAsFile() ) && pictures.size() > 0 )
                    {
                        fieldName.setText( pictures.get( 0 ).getName() );
                    }
                    else
                    {
                        fieldName.setText( "" );
                    }
                    setStates();
                }
            }
        } );
        imagesPanel.addKeyListener( new KeyAdapter()
        {
            @Override
            public void keyTyped( final KeyEvent e )
            {
                if ( e.getKeyChar() == KeyEvent.VK_DELETE )
                {
                    final int answer = JOptionPane.showConfirmDialog( MainForm.this, "Selektierte Bilder wirklich löschen?",
                            "Löschen", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE );
                    if ( answer == JOptionPane.YES_OPTION )
                    {
                        doDelete();
                    }
                }
            }
        } );
        final InfoModel infoModel = model.getInfoModel();
        infoModel.addPropertyChangeListener( InfoModel.PROPERTYNAME_NOTE, new StatusBarNoteHandler( statusBar ) );
        infoModel.addPropertyChangeListener( InfoModel.PROPERTYNAME_NOTE, new AlertDialogNoteHandler( this, Message.Type.ERROR ) );
        infoModel.addPropertyChangeListener( InfoModel.PROPERTYNAME_NOTE, new Log4jNoteHandler( LOG ) );
    }

    private void listFileEntries( final Entry entry, final ArrayList<FileEntry> fileEntries, final boolean recursiveList )
    {
        for ( int i = 0; i < entry.size(); i++ )
        {
            final Entry child = entry.get( i );
            if ( child instanceof FileEntry )
            {
                fileEntries.add( (FileEntry) child );
            }
            else if ( recursiveList )
            {
                listFileEntries( child, fileEntries, recursiveList );
            }
        }
    }

    private void displayImages()
    {
        final int size = model.getSelectedImageFiles().size();
        imagesPanel.getThumbnailListModel().clear();
        if ( size == 0 )
        {
            controller.setCurrentPicture( (FileEntry) null, imagePanel );
        }
        else
        {
            final List<FileEntry> list = new ArrayList<FileEntry>();
            for ( final FileEntry entry : model.getSelectedImageFiles() )
            {
                list.add( entry );
            }
            thumbnailModel.setFileEntries( list );
            if ( size == 1 )
            {
                controller.setCurrentPicture( list.get( 0 ), imagePanel );
            }
            else
            {
                imagesTab.setSelectedIndex( TAB_IMAGES_LIST );
            }
        }
        imagesPanel.repaint();
        setStates();
    }

    private void setTreeIcons( final JTree tree )
    {
        final ImageIcon icon = new ImageIcon( MainForm.class.getResource( "/18x18/arrow.png" ) );
        final DefaultTreeCellRenderer speciesRenderer = (DefaultTreeCellRenderer) tree.getCellRenderer();
        speciesRenderer.setOpenIcon( icon );
        speciesRenderer.setClosedIcon( icon );
        speciesRenderer.setLeafIcon( icon );
    }

    private void setStates()
    {
        final int size = model.getSelectedPictures().size();
        final boolean hasSelection = size > 0;
        final boolean areEqual = controller.areNamesOfSelectedFilesEqual( true, model.getSelectedPicturesAsFile() );
        final boolean hasText = !"".equals( fieldName.getText().trim() );
        final boolean shouldEnable = hasSelection && areEqual;
        renameButton.setEnabled( shouldEnable && hasText );
//        deleteButton.setEnabled(hasSelection);
//        moveButton.setEnabled(hasSelection);
        fieldName.setEditable( shouldEnable );
        fieldName.setEnabled( shouldEnable );
        selectAll.setEnabled( hasSelection && controller.areNamesOfSelectedFilesEqual( true, model.getSelectedPicturesAsFile() ) );
        copyToClipboard.setEnabled( size == 1 );
    }

    protected void doShowPrefs()
    {
        final PrefsForm dialog = new PrefsForm( this, model.getSettings() );
        dialog.setVisible( true );
        if ( dialog.isOk() )
        {
            initModels();
        }
    }

    protected void doQuit()
    {
        controller.save();
        System.exit( 0 );
    }

    protected void doRename()
    {
        final File[] files = model.getSelectedPicturesAsFile();
        controller.renameSelected( fieldName.getText(), files );
        renameButton.setEnabled( false );
        initModels();
    }

    protected void doToBackup()
    {
//        final Settings settings = model.getSettings();
//        final String fromPath = settings.getActivePicturePath();
//        final String toPath = settings.getPassivePicturePath();
//        controller.move(fromPath, toPath);
//        initModels();
    }

    protected void doCollapseSpeciesTree()
    {
        TreeUtils.collapseAll( treeOverview );
    }

    protected void doExpandSpeciesTree()
    {
        TreeUtils.expandAll( treeOverview );
    }

    protected void doSelectAll()
    {
        if ( model.getSelectedPictures().size() > 0 )
        {
            final Picture picture = model.getSelectedPictures().get( 0 );
            final String name = picture.getName();
            final ThumbnailListModel model = imagesPanel.getThumbnailListModel();
            final ListSelectionModel selectionModel = imagesPanel.getSelectionModel();
            int count = 0;
            silently = true;
            for ( int i = 0; i < model.getSize(); i++ )
            {
                final Picture candidate = model.getPicture( i );
                if ( candidate.getName().equals( name ) )
                {
                    count++;
                    selectionModel.addSelectionInterval( i, i );
                }
            }
            silently = false;
            if ( count == 1 )
            {
                controller.showMessage( "1 Bild \"" + name + "\" ausgewählt" );
            }
            else
            {
                controller.showMessage( count + " Bilder \"" + name + "\" ausgewählt" );
            }
        }
    }

    protected void doCopyPath()
    {
        if ( model.getSelectedPictures().size() == 1 )
        {
            final Picture picture = model.getSelectedPictures().get( 0 );
            final StringSelection string = new StringSelection( picture.getFile().getAbsolutePath() );
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents( string, null );
        }
    }

    protected void doSwitchRecursiveOverviewList()
    {
        model.setRecursiveOverviewList( !model.isRecursiveOverviewList() );
        final TreePath selection = treeOverview.getSelectionPath();
        treeOverview.clearSelection();
        treeOverview.setSelectionPath( selection );
    }

    protected void doRemix()
    {
        model.getHierarchicalMapping().clear();
        final Object selected = listSpecies.getSelectedValue();
        if ( selected != null && selected instanceof SpeciesEntry )
        {
            final SpeciesEntry entry = (SpeciesEntry) selected;
            SpeciesParser.findFileEntriesWithin( entry, model.getHierarchicalMapping() );
        }
    }

    protected void doSetBigger( final ActionEvent e )
    {
        final JCheckBoxMenuItem source = (JCheckBoxMenuItem) e.getSource();
        ThumbnailStore.getInstance().invalidateCache();
        final int pixel = source.isSelected() ? 256 : 128;
        imagesPanel.setThumbnailSize( pixel );
    }

    protected void doClearCache()
    {
        ThumbnailStore.getInstance().invalidateCache();
    }

    protected void doDelete()
    {
//        final Settings settings = model.getSettings();
//        final String fromPath = settings.getActivePicturePath();
//        final String toPath = settings.getTrashPath();
//        controller.move(fromPath, toPath);
//        initModels();
    }

    protected void doSavePositionAndZoom()
    {
        final String newPath = controller.savePositionAndZoom( imagePanel );
        final int index = imagesPanel.getSelectedIndex();
        final FileEntry fileEntry;
//        navigation.setModel(new EntryTreeModel(currentSpeciesEntry.getList()));
        if ( index > -1 )
        {
            fileEntry = fileEntries.get( index );
            if ( newPath != null )
            {
                fileEntry.setPath( newPath );
            }
            controller.setCurrentPicture( fileEntry, imagePanel );
        }
    }
}