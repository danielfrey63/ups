package ch.jfactory.image;

import ch.jfactory.resource.CachedImagePicture;
import ch.jfactory.resource.PictureConverter;
import java.awt.Color;
import java.awt.Component;
import java.awt.image.BufferedImage;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 * // TODO: Comment
 *
 * @author Daniel Frey 06.02.11 17:20
 */
public class CachedImagePictureListCellRenderer extends DefaultListCellRenderer
{
    private final Border selectedBorder = new CompoundBorder(
            new CompoundBorder(
                    new EmptyBorder( 1, 1, 1, 1 ),
                    new LineBorder( Color.orange, 2 )
            ),
            new EmptyBorder( 1, 1, 1, 1 ) );

    private final Border unselectedBorder = new CompoundBorder(
            new CompoundBorder(
                    new EmptyBorder( 1, 1, 1, 1 ),
                    new LineBorder( Color.lightGray, 2 )
            ),
            new EmptyBorder( 1, 1, 1, 1 ) );

    public CachedImagePictureListCellRenderer()
    {
        super();
    }

    @Override
    public Component getListCellRendererComponent( final JList list, final Object value, final int index, final boolean isSelected, final boolean cellHasFocus )
    {
        final CachedImagePicture picture = (CachedImagePicture) value;
        setHorizontalAlignment( JLabel.CENTER );
        setVerticalAlignment( JLabel.CENTER );
        setOpaque( false );
        final BufferedImage image = picture.getImage( true );
        if ( image != null )
        {
            setText( null );
            setIcon( new ImageIcon( PictureConverter.scaleImage1( image, 92, 92 ) ) );
        }
        else
        {
            setText( "Loading..." );
            setIcon( null );
        }
        setBorder( isSelected ? selectedBorder : unselectedBorder );
        setToolTipText( picture.getName() );
        return this;
    }
}
