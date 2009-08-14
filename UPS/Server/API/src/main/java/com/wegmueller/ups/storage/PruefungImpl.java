package com.wegmueller.ups.storage;

import com.wegmueller.ups.lka.IPruefung;

/**
 * Created by IntelliJ IDEA. User: Thomas Date: 28.08.2006 Time: 09:12:14 To change this template use File | Settings |
 * File Templates.
 */
public class PruefungImpl implements IPruefung
{
    private String lkNummer;

    public PruefungImpl(final String lkNummer, final String title)
    {
        this.lkNummer = lkNummer;
        this.title = title;
    }

    private String title;

    public String getLKNummer()
    {
        return lkNummer;
    }

    public String getTitle()
    {
        return title;
    }
}
