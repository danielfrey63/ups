package net.java.jveez.utils;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class IntImageIterator extends ImageIterator
{
    private final int[] buffer;

    private int offset = 0;

    public IntImageIterator( final BufferedImage image )
    {
        super( image );
        buffer = ( (DataBufferInt) image.getRaster().getDataBuffer() ).getData();
    }

    public void goTo( final int x, final int y )
    {
        offset = y * width + x;
    }

    public void push( final Pixel p )
    {
        buffer[offset++] = p.asInt();
    }

    public void pop( final Pixel p )
    {
        p.fromInt( buffer[offset++] );
    }

    public void goToXrel( final int xShift )
    {
        offset += xShift;
    }

    public void getXrel( final Pixel p, final int xShift )
    {
        p.fromInt( buffer[offset + xShift] );
    }

    public void getYrel( final Pixel p, final int yShift )
    {
        p.fromInt( buffer[offset + yShift * width] );
    }

    public void setXrel( final Pixel p, final int xShift )
    {
        buffer[offset + xShift] = p.asInt();
    }

    public void setYrel( final Pixel p, final int yShift )
    {
        buffer[offset + yShift * width] = p.asInt();
    }

    public void get( final Pixel p )
    {
        p.fromInt( buffer[offset] );
    }

    public void set( final Pixel p )
    {
        buffer[offset] = p.asInt();
    }

    public void incY()
    {
        offset += width;
    }

    public void incX()
    {
        offset += 1;
    }

    public void decY()
    {
        offset -= width;
    }

    public void decX()
    {
        offset -= 1;
    }
}
