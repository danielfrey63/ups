package net.java.jveez.utils;

/**
 * Created by IntelliJ IDEA. User: spetrucc Date: Jul 12, 2005 Time: 10:18:42 AM To change this template use File |
 * Settings | File Templates.
 */
public class Pixel {

    public int a;
    public int r;
    public int g;
    public int b;

    public void mult(float factor) {
        a = (int) (a * factor);
        r = (int) (r * factor);
        g = (int) (g * factor);
        b = (int) (b * factor);
    }

    public int asInt() {
        return a << 24 | r << 16 | g << 8 | b;
    }

    public void fromInt(int argb) {
        a = argb >>> 24;
        r = (argb & 0x00ff0000) >>> 16;
        g = (argb & 0x0000ff00) >>> 8;
        b = argb & 0x000000ff;
    }

    public void set(Pixel p) {
        a = p.a;
        r = p.r;
        g = p.g;
        b = p.b;
    }

    public void clear() {
        a = r = g = b = 0;
    }

    public void add(Pixel p) {
        a += p.a;
        r += p.r;
        g += p.g;
        b += p.b;
    }

    public void mix(int r, int g, int b, int a, float factor) {
        float oneMinusFactor = 1.0f - factor;
        this.r = (int) (this.r * oneMinusFactor + r * factor);
        this.g = (int) (this.g * oneMinusFactor + g * factor);
        this.b = (int) (this.b * oneMinusFactor + b * factor);
        this.a = (int) (this.a * oneMinusFactor + a * factor);
    }

    public void multAndCopy(float factor, Pixel target) {
        target.a = (int) (a * factor);
        target.r = (int) (r * factor);
        target.g = (int) (g * factor);
        target.b = (int) (b * factor);
    }

    public void sub(Pixel p) {
        a -= p.a;
        r -= p.r;
        g -= p.g;
        b -= p.b;
    }
}
