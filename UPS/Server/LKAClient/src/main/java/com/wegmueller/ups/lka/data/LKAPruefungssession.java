package com.wegmueller.ups.lka.data;

import ch.ethz.id.bi.soaplka.web.soap.data.xsd.WsPruefungssession;
import com.wegmueller.ups.lka.IPruefungsSession;
import java.util.Calendar;

public class LKAPruefungssession implements IPruefungsSession
{
    private final String sessionsname;

    private final Calendar sessionsende;

    private final String seskez;

    private final Calendar planungFreigabe;

    public LKAPruefungssession( final WsPruefungssession s )
    {
        sessionsname = s.getSessionsname();
        sessionsende = CalendarUtils.parseSessionsEnded( s.getSessionsende() );
        seskez = s.getSeskez();
        planungFreigabe = CalendarUtils.parsePlanungFreigabe( s.getPlanungFreigabe() );

    }

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

}
