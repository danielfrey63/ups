/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.component.splash;

import com.jgoodies.uif.splash.Splash;
import com.jgoodies.uif.splash.SplashProvider;
import com.jgoodies.uif.util.ScreenUtils;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Window;

/**
 * Copy of JGoodies ImageSplash to add support for version.
 *
 * @author Daniel Frey
 * @version $Revision: 1.3 $
 * @see com.jgoodies.uif.splash.ImageSplash
 * @see Splash
 * @see SplashProvider
 */

public final class ImageSplash extends Window implements SplashProvider
{
    private static final int DEFAULT_BAR_WIDTH = 100;

    private static final int DEFAULT_BAR_HEIGHT = 10;

    private static final int VPAD = 10;

    private final Image image;

    private String note;

    private boolean noteEnabled;

    private boolean progressVisible;

    private Color textColor;

    private Rectangle progressBarBounds;

    private int percent;

    private String version;

    private int versionPad;

    // Instance Creation ****************************************************

    /**
     * Constructs an AWT based splash for the given <code>image</code> using a default <code>Frame</code>. Progress is invisible and note changes will be ignored.
     *
     * @param image the splash image to display
     */
    public ImageSplash( final Image image )
    {
        this( image, false );
    }

    /**
     * Constructs an AWT based splash for the given <code>image</code> using a default <code>Frame</code> that has an optional progress bar. The initial note is 'Loading...'.
     *
     * @param image           the splash image to display
     * @param progressVisible true to show progress, false to hide it
     */
    public ImageSplash( final Image image, final boolean progressVisible )
    {
        this( new Frame(), image, "Loading\u2026", progressVisible, null );
    }

    /**
     * Constructs an AWT based splash for the given Frame, Image, initial note and progress visibility.
     *
     * @param owner           this window's parent frame
     * @param image           the splash image to display
     * @param initialNote     the note that will be displayed first
     * @param progressVisible true to show progress, false to hide it
     */
    public ImageSplash( final Frame owner, final Image image, final String initialNote, final boolean progressVisible, final String version )
    {
        super( owner );
        this.image = image;
        this.note = initialNote;
        this.percent = 0;
        this.progressVisible = progressVisible;
        this.setVersion( version );
        setSize( image.getWidth( null ), image.getHeight( null ) );
        setProgressBarBounds( VPAD );
        setVersionHeight( 2 * VPAD );
        setForeground( Color.DARK_GRAY );
        setBackground( Color.LIGHT_GRAY );
        textColor = Color.BLACK;
        ScreenUtils.locateOnScreenCenter( this );
    }

    // Setting Properties ***************************************************

    /**
     * Answers whether this splash honors or ignores note changes.
     *
     * @return true if this splash changes the displayed note in {@link #setNote(String)}, false to ignore it
     */
    public boolean isNoteEnabled()
    {
        return noteEnabled;
    }

    /**
     * Sets whether this splash shall honor or ignore note changes.
     *
     * @param noteEnabled true if this splash changes the displayed note in {@link #setNote(String)}, false to ignore it
     */
    public void setNoteEnabled( final boolean noteEnabled )
    {
        this.noteEnabled = noteEnabled;
    }

    /**
     * Answers whether this splash shows a progress bar or not.
     *
     * @return true if this splash shows a progress, false if not
     */
    public boolean isProgressVisible()
    {
        return progressVisible;
    }

    /**
     * Shows or hides the progress bar.
     *
     * @param progressVisible true to show the progress, false to hide it
     */
    public void setProgressVisible( final boolean progressVisible )
    {
        this.progressVisible = progressVisible;
    }

    /**
     * Answers whether this splash shows a progress bar or not.
     *
     * @return true if this splash shows a progress, false if not
     * @deprecated Replaced by {@link #isProgressVisible()}.
     */
    public boolean isShowingProgress()
    {
        return isProgressVisible();
    }

    /**
     * Shows or hides the progress bar.
     *
     * @param showingProgress true to show the progress
     * @deprecated Replaced by {@link #setProgressVisible(boolean)}.
     */
    public void setShowingProgress( final boolean showingProgress )
    {
        setProgressVisible( showingProgress );
    }

    /**
     * Returns the note color.
     *
     * @return the note color
     */
    public Color getTextColor()
    {
        return textColor;
    }

    /**
     * Sets a new note color.
     *
     * @param newTextColor the new note color
     */
    public void setTextColor( final Color newTextColor )
    {
        textColor = newTextColor;
    }

    /**
     * Sets the bounds for the progress bar using the given Rectangle.
     *
     * @param r the Rectangle that describes the progress bar bounds
     */
    public void setProgressBarBounds( final Rectangle r )
    {
        progressBarBounds = new Rectangle( r );
    }

    /**
     * Sets the bounds for the progress bar using a pad from the dialog's bottom.
     *
     * @param bottomPad the distance in pixel from the splash's bottom
     */
    public void setProgressBarBounds( final int bottomPad )
    {
        setProgressBarBounds( defaultProgressBarBounds( bottomPad ) );
    }

    public void setVersionHeight( final int bottomPad )
    {
        this.versionPad = bottomPad;
    }

    /** Answers the progress bar's default bounds using a pad from the dialog's bottom. */
    private Rectangle defaultProgressBarBounds( final int bottomPad )
    {
        final int x = ( getWidth() - DEFAULT_BAR_WIDTH ) / 2;
        final int y = getHeight() - DEFAULT_BAR_HEIGHT - bottomPad;
        return new Rectangle( x, y, DEFAULT_BAR_WIDTH, DEFAULT_BAR_HEIGHT );
    }

    // Painting *************************************************************

    /** Renders the image and optionally a progress bar with a note. */
    public void paint( final Graphics graphics )
    {
        final Graphics2D g = (Graphics2D) graphics;
        // Check whether we are about to refresh the progress bar.
        final boolean clipIsProgressRect =
                progressBarBounds.equals( g.getClipBounds() );

        if ( image != null && ( !progressVisible || !clipIsProgressRect ) )
        {
            g.drawImage( image, 0, 0, this );
        }
        if ( progressVisible )
        {
            final int x = progressBarBounds.x;
            final int y = progressBarBounds.y;
            final int w = progressBarBounds.width;
            final int h = progressBarBounds.height;
            final int progressWidth = ( w - 2 ) * percent / 100;
            final int progressHeight = h - 2;

            g.translate( x, y );
            // Paint border
            g.setColor( Color.GRAY );
            g.drawLine( 0, 0, w - 2, 0 );
            g.drawLine( 0, 0, 0, h - 1 );
            g.setColor( Color.WHITE );
            g.drawLine( 0, h - 1, w - 1, h - 1 );
            g.drawLine( w - 1, 0, w - 1, h - 1 );
            // Paint background
            g.setColor( getBackground() );
            g.fillRect( 1, 1, w - 2, progressHeight );
            // Paint progress bar
            g.setColor( getForeground() );
            g.fillRect( 1, 1, progressWidth, progressHeight );
            g.translate( -x, -y );

            if ( !clipIsProgressRect )
            {
                g.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
                final FontMetrics fm = getFontMetrics( g.getFont() );
                final int textWidth = fm.stringWidth( note );
                final int textX = ( getWidth() - textWidth ) / 2;
                g.setColor( textColor );
                g.drawString( note, textX, progressBarBounds.y - VPAD / 2 );
            }
        }
        if ( getVersion() != null && !"".equals( getVersion() ) )
        {
            g.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
            final FontMetrics fm = getFontMetrics( g.getFont() );
            final int textWidth = fm.stringWidth( getVersion() );
            final int textX = ( getWidth() - textWidth ) / 2;
            g.setColor( textColor );
            g.drawString( getVersion(), textX, progressBarBounds.y - versionPad );
        }
    }

    /** Opens the splash window. */
    public void openSplash()
    {
        setVisible( true );
    }

    /** Closes and disposes the splash window. */
    public void closeSplash()
    {
        dispose();
    }

    /** Sets a new progress value. */
    public void setProgress( final int percent )
    {
        if ( !progressVisible )
        {
            return;
        }
        this.percent = percent;
        repaint(
                progressBarBounds.x,
                progressBarBounds.y,
                progressBarBounds.width,
                progressBarBounds.height );
    }

    /**
     * Sets a new note if and only if {@link #isNoteEnabled()} returns true.
     *
     * @param newNote the note to set
     */
    public void setNote( final String newNote )
    {
        if ( isNoteEnabled() )
        {
            note = newNote;
            repaint();
        }
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion( final String version )
    {
        this.version = version;
    }
}