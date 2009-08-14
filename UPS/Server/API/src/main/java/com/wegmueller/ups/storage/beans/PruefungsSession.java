package com.wegmueller.ups.storage.beans;

import com.wegmueller.ups.lka.IPruefungsSession;
import java.util.Calendar;
import java.util.GregorianCalendar;

/** Created by: Thomas Wegmueller Date: 26.09.2005,  20:48:56 */
public class PruefungsSession implements IPruefungsSession
{

    private java.lang.String sessionsname;

    private Calendar sessionsende;

    private java.lang.String seskez;

    private Calendar planungFreigabe;

    private Calendar storageDate = new GregorianCalendar();

    public String getSessionsname()
    {
        return sessionsname;
    }

    public Calendar getSessionsende()
    {
        return sessionsende;
    }

    public String getSeskez()
    {
        return seskez;
    }

    public Calendar getPlanungFreigabe()
    {
        return planungFreigabe;
    }

    public Calendar getStorageDate()
    {
        return storageDate;
    }

    public void setStorageDate(final Calendar storageDate)
    {
        this.storageDate = storageDate;
    }

    public void setPlanungFreigabe(final Calendar planungFreigabe)
    {
        this.planungFreigabe = planungFreigabe;
    }

    public void setSeskez(final String seskez)
    {
        this.seskez = seskez;
    }

    public void setSessionsende(final Calendar sessionsende)
    {
        this.sessionsende = sessionsende;
    }

    public void setSessionsname(final String sessionsname)
    {
        this.sessionsname = sessionsname;
    }
}
