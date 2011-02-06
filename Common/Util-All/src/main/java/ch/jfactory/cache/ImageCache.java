package ch.jfactory.cache;

import java.awt.image.BufferedImage;

public interface ImageCache
{
    boolean isCached( String name );

    BufferedImage getImage( String name ) throws ImageCacheException;

    void setImage( String name, BufferedImage image ) throws ImageCacheException;

    void invalidateCache() throws ImageCacheException;
}
