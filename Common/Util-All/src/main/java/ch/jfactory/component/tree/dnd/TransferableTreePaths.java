/*
 * Copyright 2002 by x-matrix Switzerland
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
    /**
     * The type of DnD object being dragged...
     */
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

    /**
     * @see java.awt.datatransfer.Transferable#getTransferData(java.awt.datatransfer.DataFlavor)
     */
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

    /**
     * @see java.awt.datatransfer.Transferable#getTransferDataFlavors()
     */
    public DataFlavor[] getTransferDataFlavors()
    {
        return flavors;
    }

    /**
     * @see java.awt.datatransfer.Transferable#isDataFlavorSupported(java.awt.datatransfer.DataFlavor)
     */
    public boolean isDataFlavorSupported( final DataFlavor flavor )
    {
        return Arrays.asList( flavors ).contains( flavor );
    }
}

