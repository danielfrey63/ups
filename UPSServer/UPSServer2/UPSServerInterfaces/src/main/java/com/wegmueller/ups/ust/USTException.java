package com.wegmueller.ups.ust;

import com.wegmueller.ups.UPSServerException;

/**
 * Created by: Thomas Wegmueller
 * Date: 26.09.2005,  18:18:42
 */
public class USTException extends UPSServerException {
    private String name;

    public USTException(final String name) {
        super(name);
        this.name = name;
    }


    public String getName() {
        return name;
    }
}
