package com.wegmueller.ups.lka;

import com.wegmueller.ups.UPSServerException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;

/**
 * Created by: Thomas Wegmueller
 * Date: 08.09.2005,  12:39:06
 */
public class LKABusinessDelegateException extends UPSServerException {
    public LKABusinessDelegateException(String name, Throwable e) {
        super(name,e);
    }

    public LKABusinessDelegateException(Throwable e) {
        super(e);
    }
}
