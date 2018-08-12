/*
 * Copyright (c) 2004-2011, Daniel Frey, www.xmatrix.ch
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under this License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS OF  ANY  KIND, either  express or
 * implied.  See  the  License  for  the  specific  language governing
 * permissions and limitations under the License.
 */
package ch.jfactory.resource;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.apache.commons.io.FileUtils;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2005/06/16 06:28:58 $
 */
public class PictureConverter
{
    public static void main( final String[] args )
    {
        final Dimension i1 = new Dimension( 200, 300 );
        final Dimension o1 = new Dimension( 300, 100 );
        System.out.println( "fitting " + i1 + " into " + o1 + " leads to " + getFittingDimension( o1, i1 ) );
    }

    public PictureConverter( String srcPath, String dstPath, final String filter, final int maxSize, final int quality, final boolean overwrite )
    {
        try
        {
            if ( !srcPath.endsWith( "/" ) )
            {
                srcPath += "/";
            }
            if ( !dstPath.endsWith( "/" ) )
            {
                dstPath += "/";
            }
            final File srcDir = new File( srcPath );
            if ( !srcDir.exists() )
            {
                throw new FileNotFoundException( "source path \"" + srcPath + "\" not found" );
            }
            if ( !srcDir.isDirectory() )
            {
                throw new FileNotFoundException( "source path \"" + srcPath + "\" is not a directory" );
            }
            final String[] content = srcDir.list( new FilenameFilter()
            {
                public boolean accept( final File dir, final String name )
                {
                    return name.endsWith( filter );
                }
            } );
            final File dstDir = new File( dstPath );
            if ( !dstDir.exists() )
            {
                throw new FileNotFoundException( "destination path \"" + dstPath + "\" not found" );
            }
            if ( !dstDir.isDirectory() )
            {
                throw new FileNotFoundException( "destination path \"" + dstPath + "\" is not a directory" );
            }
            for ( final String s : content )
            {
                final String srcName = srcPath + s;
                final String dstName = dstPath + s;
                final File srcFile = new File( srcName );
                final File dstFile = new File( dstName );
                if ( !overwrite && dstFile.exists() && dstFile.isFile() )
                {
                    System.out.println( "skipping " + srcName );
                    continue;
                }
                final BufferedImage image = ImageIO.read( srcFile );
                final int h = image.getHeight();
                final int w = image.getWidth();
                if ( w == maxSize && h < maxSize || h == maxSize && w < maxSize )
                {
                    System.out.println( "copying " + srcName + " to " + dstName );
                    FileUtils.copyFile( srcFile, new File( dstName ) );
                }
                else
                {
                    System.out.println( "resizing " + srcName + " to " + dstName );
                    scaleImage1( image, maxSize, maxSize );
                    scaleImage2( image, maxSize, maxSize );
                }
            }
        }
        catch ( IOException e )
        {
            System.err.println( "Error occured: " + e.getMessage() );
        }
    }

    public static Image scaleImage1( final Image image, int newW, int newH )
    {
        // determine thumbnail size from WIDTH and HEIGHT
        final double thumbRatio = (double) newW / (double) newH;
        final int imageWidth = image.getWidth( null );
        final int imageHeight = image.getHeight( null );
        final double imageRatio = (double) imageWidth / (double) imageHeight;
        if ( thumbRatio < imageRatio )
        {
            newH = (int) ( newW / imageRatio );
        }
        else
        {
            newW = (int) ( newH * imageRatio );
        }
        // draw original image to thumbnail image object and scale it to the new size on-the-fly
        final BufferedImage scaledImage = new BufferedImage( newW, newH, BufferedImage.TYPE_INT_RGB );
        final Graphics2D graphics2D = scaledImage.createGraphics();
        graphics2D.setRenderingHint( RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR );
        graphics2D.scale( (double) newW / (double) imageWidth, (double) newH / (double) imageHeight );
        graphics2D.drawImage( image, 0, 0, imageWidth, imageHeight, null );
        return scaledImage;
    }

    public static Image scaleImage2( final BufferedImage image, final int newH, final int newW )
    {
        // Calculate scale so image fits in a square area of thumbNailSize - e.g. 160
        final int imageW = image.getWidth();
        final int imageH = image.getHeight();
        final double scale = getFittingFactor( newW, newH, imageW, imageH );
        // Now create thumbnail
        final AffineTransform affineTransform = AffineTransform.getScaleInstance( scale, scale );
        final AffineTransformOp affineTransformOp = new AffineTransformOp( affineTransform, null );
        BufferedImage scaledImage = affineTransformOp.filter( image, null );
        // Now do fix to get rid of silly spurious line
        final int scaledWidth = scaledImage.getWidth();
        final int scaledHeight = scaledImage.getHeight();
        final int expectedWidth = (int) ( imageW * scale );
        final int expectedHeight = (int) ( imageH * scale );
        if ( scaledWidth > expectedWidth || scaledHeight > expectedHeight )
        {
            scaledImage = scaledImage.getSubimage( 0, 0, expectedWidth, expectedHeight );
        }
        // Now write out scaled image to file
        return scaledImage;
    }

    public static void saveJPG1( final BufferedImage image, final String name, int quality ) throws IOException
    {
        final BufferedOutputStream out = new BufferedOutputStream( new FileOutputStream( name ) );
        ImageIO.write(image, "JPG", out);
    }

    public static void saveJPG2( final BufferedImage image, final String name ) throws IOException
    {
        ImageIO.write( image, "JPG", new File( name ) );
    }

    public static Dimension getFittingDimension( final Dimension outer, final Dimension inner )
    {
        final float f = getFittingFactor( outer, inner );
        return new Dimension( (int) ( inner.width * f ), (int) ( inner.height * f ) );
    }

    public static float getFittingFactor( final Dimension outer, final Dimension inner )
    {
        return getFittingFactor( outer.width, outer.height, inner.width, inner.height );
    }

    public static float getFittingFactor( final int outerW, final int outerH, final int innerW, final int innerH )
    {
        return Math.min( (float) outerH / (float) innerH, (float) outerW / (float) innerW );
    }
}
