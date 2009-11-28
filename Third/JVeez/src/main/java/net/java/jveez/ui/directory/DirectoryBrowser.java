package net.java.jveez.ui.directory;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import net.java.jveez.utils.Utils;
import net.java.jveez.vfs.Directory;
import net.java.jveez.vfs.Vfs;

public class DirectoryBrowser extends JComponent
{
    private static final BufferedImage leftImage = Utils.loadImage( "net/java/jveez/icons/browser_left.png" );

    private static final BufferedImage rightImage = Utils.loadImage( "net/java/jveez/icons/browser_right.png" );

    private static final BufferedImage rightHighlightedImage = Utils.loadImage( "net/java/jveez/icons/browser_right_highlighted.png" );

    private static final BufferedImage splitterImage = Utils.loadImage( "net/java/jveez/icons/browser_splitter.png" );

    private static final BufferedImage splitterHighLightedImage = Utils.loadImage( "net/java/jveez/icons/browser_splitter_highlighted.png" );

    private static final BufferedImage patternImage = Utils.loadImage( "net/java/jveez/icons/browser_pattern.png" );

    private static final BufferedImage patternHightlighedImage = Utils.loadImage( "net/java/jveez/icons/browser_pattern_highlighted.png" );

    private Directory currentDirectory;

    private int selectedEntry = -1;

    private boolean selectedChangeThis = false;

    private final DirectoryListPopup popupMenu = new DirectoryListPopup( this );

    private final List<DirectoryEntry> items = new LinkedList<DirectoryEntry>();

    public DirectoryBrowser()
    {
        super();
        setPreferredSize( new Dimension( 50, leftImage.getHeight() ) );

        setCurrentDirectory( Vfs.getInstance().getRootDirectories().iterator().next() );

        addMouseMotionListener( new MouseMotionAdapter()
        {
            public void mouseMoved( final MouseEvent e )
            {
                final int index = getIndexAt( e.getX(), e.getY() );
                final boolean changeThis = index < 0 || items.get( index ).changeThisArea.contains( e.getX(), e.getY() );
                if ( selectedEntry != index || selectedChangeThis != changeThis )
                {
                    selectedEntry = index;
                    selectedChangeThis = changeThis;
                    repaint();
                }
            }
        } );

        addMouseListener( new MouseAdapter()
        {
            public void mouseExited( final MouseEvent e )
            {
                if ( selectedEntry != -1 )
                {
                    selectedEntry = -1;
                    repaint();
                }
            }

            public void mouseClicked( final MouseEvent e )
            {
                final DirectoryEntry selection = getEntryAt( e.getX(), e.getY() );
                if ( selection == null )
                {
                    return;
                }

                final boolean changeThis = selection.changeThisArea.contains( e.getX(), e.getY() );

                Utils.executeAsyncIfDisptachThread( new Runnable()
                {
                    public void run()
                    {
                        final Collection<? extends Directory> subdirs = Vfs.getInstance().getSubDirectories( changeThis ? selection.directory.getParent() : selection.directory );
//            popupMenu.show(subdirs, e.getXOnScreen(), e.getYOnScreen());
                        final Point p = new Point( e.getPoint() );
                        SwingUtilities.convertPointToScreen( p, e.getComponent() );
                        popupMenu.show( subdirs, p.x, p.y );
                    }
                } );
            }
        } );
    }

    private DirectoryEntry getEntryAt( final int x, final int y )
    {
        for ( final DirectoryEntry entry : items )
        {
            if ( entry.changeThisArea.contains( x, y ) || entry.changeNextArea.contains( x, y ) )
            {
                return entry;
            }
        }
        return null;
    }

    private int getIndexAt( final int x, final int y )
    {
        int i = 0;
        for ( final DirectoryEntry entry : items )
        {
            if ( entry.changeThisArea.contains( x, y ) || entry.changeNextArea.contains( x, y ) )
            {
                return i;
            }
            i++;
        }
        return -1;
    }

    public Directory getCurrentDirectory()
    {
        return currentDirectory;
    }

    public void setCurrentDirectory( final Directory directory )
    {
//    if (this.currentDirectory != directory) {
        final Directory oldValue = this.currentDirectory;
        this.currentDirectory = directory;
        items.clear();
        Directory d = directory;
        while ( d != null )
        {
            items.add( 0, new DirectoryEntry( d ) );
            d = d.getParent();
        }

        firePropertyChange( "directory", oldValue, directory );

        revalidate();
        repaint();
//    }
    }

    protected void paintComponent( final Graphics g )
    {
        paintBorder( g );

        // TODO Fix me !
//    Graphics2D g2d = (Graphics2D)g;
//    g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

        final FontMetrics fontMetrics = g.getFontMetrics();
        int x = 0;

        g.drawImage( leftImage, x, 0, this );
        x += leftImage.getWidth();

        int currentIndex = 0;
        final Iterator<DirectoryEntry> it = items.iterator();
        DirectoryEntry entry = null;
        while ( it.hasNext() )
        {
            entry = it.next();
            final String directoryName = entry.directory.getName();

//      System.out.printf("selectedEntry=%d,selectedChangeThis=%b%n", selectedEntry, selectedChangeThis);

            final int advance = fontMetrics.stringWidth( directoryName );

            entry.changeThisArea.x = x;
            entry.changeThisArea.y = 0;
            entry.changeThisArea.width = 4 + advance;
            entry.changeThisArea.height = patternImage.getHeight();

            if ( selectedEntry == currentIndex && selectedChangeThis )
            {
                g.drawImage( patternHightlighedImage, x, 0, x + 4 + advance, patternHightlighedImage.getHeight(), 0, 0, patternHightlighedImage.getWidth(), patternHightlighedImage.getHeight(), this );
            }
            else
            {
                g.drawImage( patternImage, x, 0, x + 4 + advance, patternImage.getHeight(), 0, 0, patternImage.getWidth(), patternImage.getHeight(), this );
            }

            g.drawString( directoryName, x + 2, 2 + fontMetrics.getAscent() );
            x = x + 4 + advance;
            if ( it.hasNext() )
            {
                entry.changeNextArea.x = x;
                entry.changeNextArea.y = 0;
                entry.changeNextArea.width = splitterImage.getWidth();
                entry.changeNextArea.height = splitterImage.getHeight();

                if ( selectedEntry == currentIndex && !selectedChangeThis )
                {
                    g.drawImage( splitterHighLightedImage, x, 0, this );
                }
                else
                {
                    g.drawImage( splitterImage, x, 0, this );
                }
                x += splitterImage.getWidth();
                currentIndex++;
            }
        }
        if ( entry != null )
        {
            entry.changeNextArea.x = x;
            entry.changeNextArea.y = 0;
            entry.changeNextArea.width = rightImage.getWidth();
            entry.changeNextArea.height = rightImage.getHeight();
        }
        if ( selectedEntry == currentIndex && !selectedChangeThis )
        {
            g.drawImage( rightHighlightedImage, x, 0, this );
        }
        else
        {
            g.drawImage( rightImage, x, 0, this );
        }
    }

    private static class DirectoryEntry
    {
        public Directory directory;

        public Rectangle changeThisArea = new Rectangle();

        public Rectangle changeNextArea = new Rectangle();

        DirectoryEntry( final Directory directory )
        {
            this.directory = directory;
        }
    }
}
