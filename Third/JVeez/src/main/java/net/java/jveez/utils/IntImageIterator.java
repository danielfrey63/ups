package net.java.jveez.utils;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class IntImageIterator extends ImageIterator {

    private int[] buffer;
    private int offset = 0;

    public IntImageIterator(BufferedImage image) {
        super(image);
        buffer = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
    }

    public void goTo(int x, int y) {
        offset = y * width + x;
    }

    public void push(Pixel p) {
        buffer[ offset++ ] = p.asInt();
    }

    public void pop(Pixel p) {
        p.fromInt(buffer[ offset++ ]);
    }

    public void goToXrel(int xShift) {
        offset += xShift;
    }

    public void getXrel(Pixel p, int xShift) {
        p.fromInt(buffer[ offset + xShift ]);
    }

    public void getYrel(Pixel p, int yShift) {
        p.fromInt(buffer[ offset + yShift * width ]);
    }

    public void setXrel(Pixel p, int xShift) {
        buffer[ offset + xShift ] = p.asInt();
    }

    public void setYrel(Pixel p, int yShift) {
        buffer[ offset + yShift * width ] = p.asInt();
    }

    public void get(Pixel p) {
        p.fromInt(buffer[ offset ]);
    }

    public void set(Pixel p) {
        buffer[ offset ] = p.asInt();
    }

    public void incY() {
        offset += width;
    }

    public void incX() {
        offset += 1;
    }

    public void decY() {
        offset -= width;
    }

    public void decX() {
        offset -= 1;
    }
}
