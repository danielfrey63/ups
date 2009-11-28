package com.wegmueller.ups.lka;

import com.wegmueller.ups.UPSServerException;

/**
 * Created by: Thomas Wegmueller Date: 08.09.2005,  12:39:06
 */
public class LKABusinessDelegateException extends UPSServerException
{
    public LKABusinessDelegateException( final String name, final Throwable e )
    {
        super( name, e );
    }

    public LKABusinessDelegateException( final Throwable e )
    {
        super( e );
    }
}
