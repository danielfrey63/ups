package net.java.jveez.utils;

import java.awt.image.BufferedImage;

public abstract class ImageIterator {

    protected BufferedImage image;
    protected int width;
    protected int height;

    protected ImageIterator(BufferedImage image) {
        this.image = image;
        this.width = image.getWidth();
        this.height = image.getHeight();
    }

    public abstract void goTo(int x, int y);

    public abstract void push(Pixel p);

    public abstract void pop(Pixel p);

    public abstract void getXrel(Pixel p, int xShift);

    public abstract void getYrel(Pixel p, int yShift);

    public abstract void goToXrel(int xShift);

    public abstract void incY();

    public abstract void decY();

    public abstract void incX();

    public abstract void decX();

    public abstract void setXrel(Pixel p, int xShift);

    public abstract void setYrel(Pixel p, int yShift);

    public abstract void get(Pixel p);

    public abstract void set(Pixel p);

    public static ImageIterator iterator(BufferedImage image) {

        switch (image.getType()) {
            case BufferedImage.TYPE_INT_ARGB:
            case BufferedImage.TYPE_INT_RGB:
                return new IntImageIterator(image);

            default:
                throw new IllegalArgumentException("Unsupported image type : " + image);
        }
    }
}
