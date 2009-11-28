package net.java.jveez.utils;

/**
 * Created by IntelliJ IDEA. User: spetrucc Date: Jul 12, 2005 Time: 10:18:42 AM To change this template use File |
 * Settings | File Templates.
 */
public class Pixel
{
    public int a;

    public int r;

    public int g;

    public int b;

    public void mult( final float factor )
    {
        a = (int) ( a * factor );
        r = (int) ( r * factor );
        g = (int) ( g * factor );
        b = (int) ( b * factor );
    }

    public int asInt()
    {
        return a << 24 | r << 16 | g << 8 | b;
    }

    public void fromInt( final int argb )
    {
        a = argb >>> 24;
        r = ( argb & 0x00ff0000 ) >>> 16;
        g = ( argb & 0x0000ff00 ) >>> 8;
        b = argb & 0x000000ff;
    }

    public void set( final Pixel p )
    {
        a = p.a;
        r = p.r;
        g = p.g;
        b = p.b;
    }

    public void clear()
    {
        a = r = g = b = 0;
    }

    public void add( final Pixel p )
    {
        a += p.a;
        r += p.r;
        g += p.g;
        b += p.b;
    }

    public void mix( final int r, final int g, final int b, final int a, final float factor )
    {
        final float oneMinusFactor = 1.0f - factor;
        this.r = (int) ( this.r * oneMinusFactor + r * factor );
        this.g = (int) ( this.g * oneMinusFactor + g * factor );
        this.b = (int) ( this.b * oneMinusFactor + b * factor );
        this.a = (int) ( this.a * oneMinusFactor + a * factor );
    }

    public void multAndCopy( final float factor, final Pixel target )
    {
        target.a = (int) ( a * factor );
        target.r = (int) ( r * factor );
        target.g = (int) ( g * factor );
        target.b = (int) ( b * factor );
    }

    public void sub( final Pixel p )
    {
        a -= p.a;
        r -= p.r;
        g -= p.g;
        b -= p.b;
    }
}
