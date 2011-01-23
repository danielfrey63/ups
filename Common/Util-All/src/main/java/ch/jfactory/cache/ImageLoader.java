package ch.jfactory.cache;

import java.awt.image.BufferedImage;

public interface ImageLoader
{
    public boolean isCached( String picture );

    public BufferedImage getImage( String picture );

    public void invalidateCache();

    public void close();

    void handleLoaderError( Throwable e );

    void registerLoaderErrorHandler( AbstractImageLoader.LoaderErrorHandler handler );
}
