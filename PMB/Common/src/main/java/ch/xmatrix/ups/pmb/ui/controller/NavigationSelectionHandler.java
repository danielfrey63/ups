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
package ch.xmatrix.ups.pmb.ui.controller;

import ch.xmatrix.ups.pmb.domain.Entry;
import ch.xmatrix.ups.pmb.domain.FileEntry;
import ch.xmatrix.ups.pmb.ui.model.PMBModel;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import net.java.jveez.ui.viewer.ViewerPanel;
import org.apache.log4j.Logger;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.3 $ $Date: 2008/01/23 22:18:48 $
 */
public class NavigationSelectionHandler implements TreeSelectionListener
{
    private static final Logger LOG = Logger.getLogger( NavigationSelectionHandler.class );

    private final ViewerPanel panel;

    private final PMBModel model;

    private final PMBController controller;

    public NavigationSelectionHandler( final ViewerPanel panel, final PMBModel model, final PMBController controller )
    {
        this.panel = panel;
        this.model = model;
        this.controller = controller;
    }

    public void valueChanged( final TreeSelectionEvent e )
    {
        final JTree navigation = (JTree) e.getSource();
        final TreePath selectionPath = navigation.getSelectionPath();
        if ( selectionPath != null )
        {
            final TreePath oldSelection = e.getOldLeadSelectionPath();
            // Make sure old expanded sister path is collapsed
            PMBController.collapseSisterNode( oldSelection, selectionPath, navigation ); // TODO: Move to Utils
            final Entry entry = (Entry) selectionPath.getLastPathComponent();
            final FileEntry def = entry.getDefault();
            final FileEntry fileEntry = model.getHierarchicalMapping().get( entry );
            if ( fileEntry != null )
            {
                controller.setCurrentPicture( fileEntry, panel );
                controller.showMessage( fileEntry.getPath() );
                if ( def != null )
                {
                    controller.showError( "Kein Default", "Default gesetzt aber nicht verwendet!" );
                    LOG.warn( "Unused default picture \"" + def + "\" for \"" + fileEntry + "\"" );
                }
            }
            else if ( def != null )
            {
                // Construct a new TreePath for the default
                final TreePath path = getTreePathForFileEntry( def, navigation );
                navigation.setSelectionPath( path );
                controller.setCurrentPicture( def, panel );
                controller.showMessage( "Verwende Default " + def.getPath() );
            }
            else
            {
                controller.setCurrentPicture( (FileEntry) null, panel );
                controller.showMessage( "Kein Default gefunden für " + selectionPath );
                LOG.warn( "Default not found for \"" + fileEntry + "\"" );
            }
            PMBController.expandIfNotEmpty( navigation.getSelectionPath(), navigation );
        }
    }

    private static TreePath getTreePathForFileEntry( final FileEntry fileEntry, final JTree navigation )
    {
        final List<Object> pathEntries = new ArrayList<Object>();
        Entry parent = fileEntry.getParent();
        while ( parent != null )
        {
            pathEntries.add( 0, parent );
            parent = parent.getParent();
        }
        pathEntries.add( 0, navigation.getModel().getRoot() );
        final Object[] pathObjects = pathEntries.toArray();
        return new TreePath( pathObjects );
    }
}
