package net.java.jveez.ui.directory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.AbstractListModel;
import net.java.jveez.vfs.Directory;

public class DirectoryListModel extends AbstractListModel
{
    private final List<Directory> directories = new ArrayList<Directory>();

    public void setContent( final Collection<? extends Directory> collection )
    {
        final int previousSize = directories.size();

        directories.clear();
        directories.addAll( collection );

        fireContentsChanged( this, 0, previousSize );
    }

    public int getSize()
    {
        return directories.size();
    }

    public Directory getElementAt( final int index )
    {
        return directories.get( index );
    }
}
