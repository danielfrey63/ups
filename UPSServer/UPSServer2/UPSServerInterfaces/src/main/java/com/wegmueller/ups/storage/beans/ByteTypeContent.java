package com.wegmueller.ups.storage.beans;

/**
 * Created by: Thomas Wegmueller
 * Date: 26.09.2005,  23:14:18
 */
public class ByteTypeContent {
    private byte[] bytes;

    public ByteTypeContent(final byte[] bytes) {
        this.bytes = bytes;
    }
    public ByteTypeContent(final String str) {
        this(str.getBytes());
    }

    public ByteTypeContent() {
    }

    public String toString() {
        if (bytes==null) return "";
        if (bytes.length==0) return "";
        return new String(bytes);
    }

    public int length() {
        return bytes==null?0:bytes.length;
    }

    public byte[] getBytes() {
        return bytes==null?new byte[0]:bytes;
    }
}
