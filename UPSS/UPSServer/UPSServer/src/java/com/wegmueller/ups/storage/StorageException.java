package com.wegmueller.ups.storage;

import com.wegmueller.ups.UPSServerException;

/**
 * Created by: Thomas Wegmueller
 * Date: 26.09.2005,  18:23:57
 */
public class StorageException extends UPSServerException {
    public StorageException(Throwable e) {
        super(e);
    }

    public StorageException(String e) {
        super(e);
    }
}
