/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.component.tree.dnd;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.util.Arrays;
import javax.swing.tree.TreePath;

/**
 * This represents a TreePath (a node in a JTree) that can be transferred between a drag source and a drop target.
 *
 * @author Andrew J Armstrong
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:58 $
 */
public class TransferableTreePaths implements Transferable
{
    /** The type of DnD object being dragged... */
    public static final DataFlavor TREEPATH_FLAVOR = new DataFlavor( DataFlavor.javaJVMLocalObjectMimeType, "TreePath" );

    private final DataFlavor[] flavors = {TREEPATH_FLAVOR};

    private final TreePath[] path;

    /**
     * Constructs a transferrable tree path object for the specified path.
     *
     * @param path the TreePath to wrap
     */
    public TransferableTreePaths( final TreePath[] path )
    {
        this.path = path;
    }

    /** @see Transferable#getTransferData(DataFlavor) */
    public synchronized Object getTransferData( final DataFlavor flavor ) throws UnsupportedFlavorException
    {
        if ( flavor.isMimeTypeEqual( TREEPATH_FLAVOR.getMimeType() ) )
        {
            // DataFlavor.javaJVMLocalObjectMimeType))
            return path;
        }
        else
        {
            throw new UnsupportedFlavorException( flavor );
        }
    }

    /** @see Transferable#getTransferDataFlavors() */
    public DataFlavor[] getTransferDataFlavors()
    {
        return flavors;
    }

    /** @see Transferable#isDataFlavorSupported(DataFlavor) */
    public boolean isDataFlavorSupported( final DataFlavor flavor )
    {
        return Arrays.asList( flavors ).contains( flavor );
    }
}

