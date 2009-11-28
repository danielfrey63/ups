package net.java.jveez.vfs;

public interface ExifPicture extends Picture
{
    public String getExifDate();

    public String getExifFocal();

    public String getExifExposure();

    public String getExifAperture();

    public String getExifCamera();

    public String getExifModel();
}
