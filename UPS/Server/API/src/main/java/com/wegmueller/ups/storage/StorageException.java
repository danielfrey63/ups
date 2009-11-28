package com.wegmueller.ups.storage;

import com.wegmueller.ups.UPSServerException;

/**
 * Created by: Thomas Wegmueller Date: 26.09.2005,  18:23:57
 */
public class StorageException extends UPSServerException
{
    public StorageException( final Throwable e )
    {
        super( e );
    }

    public StorageException( final String e )
    {
        super( e );
    }
}
