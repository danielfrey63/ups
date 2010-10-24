/*
 * Herbar CD-ROM version 2
 *
 * PicturePanel.java
 *
 * Created on 30. April 2002
 * Created by dirk
 */
package ch.jfactory.resource;

import java.awt.Dimension;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PictureLoader
{
    private static final Dimension NULL_DIMENSION = new Dimension( 0, 0 );

    private static final Logger LOGGER = LoggerFactory.getLogger( PictureLoader.class );

    public static Image load( final String pictureURL, final boolean thumb )
    {
        try
        {
            final File file = new File( pictureURL );
            LOGGER.info( "loading of " + ( thumb ? "thumbnail \"" : "image \"" ) + file.getName() + "\" initiated" );
            if ( file.exists() )
            {
                Thread.sleep( 1000 );
                final ImageInputStream iis = ImageIO.createImageInputStream( file );
                final ImageReader reader = ImageIO.getImageReaders( iis ).next();
                reader.setInput( iis );
                final Image image;
                if ( thumb )
                {
                    final ImageReadParam parameter = new ImageReadParam();
                    parameter.setSourceSubsampling( 2, 2, 0, 0 );
                    image = reader.read( 0, parameter );
                }
                else
                {
                    image = reader.read( 0 );
                }
                reader.dispose();
                iis.close();
                LOGGER.info( "loading of image \"" + file.getName() + "\" finished" );
                return image;
            }
            else
            {
                LOGGER.warn( "no image file at \"" + file + "\"" );
            }
        }
        catch ( Exception ex )
        {
            ex.printStackTrace();
            LOGGER.warn( "loading of image " + pictureURL + " failed.", ex );
        }
        return null;
    }

    public static Dimension getSize( final String pictureURL )
    {
        try
        {
            final File file = new File( pictureURL );
            if ( file.exists() )
            {
                final ImageInputStream iis = ImageIO.createImageInputStream( file );
                final ImageReader reader = ImageIO.getImageReaders( iis ).next();
                reader.setInput( iis );
                final Dimension dim = new Dimension();
                dim.width = reader.getWidth( 0 );
                dim.height = reader.getHeight( 0 );
                reader.dispose();
                iis.close();
                return dim;
            }
            else
            {
                final String message = "Size of image " + file.getAbsoluteFile() + " is null because, file does not exist";
                final IllegalStateException e = new IllegalStateException( message );
                LOGGER.error( message, e );
                throw e;
            }
        }
        catch ( IOException e )
        {
            LOGGER.warn( "Exception occurred: " + e.getMessage(), e );
        }
        catch ( RuntimeException ex )
        {
            LOGGER.error( "Error while processing image " + pictureURL );
            throw ex;
        }
        return NULL_DIMENSION;
    }
}