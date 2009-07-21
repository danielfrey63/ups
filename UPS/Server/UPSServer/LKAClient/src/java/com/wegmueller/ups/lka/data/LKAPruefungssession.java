package com.wegmueller.ups.lka.data;

import SoapLKAData.WsPruefungssession;
import com.wegmueller.ups.lka.IPruefungsSession;

import java.util.Calendar;

/**
 * Created by: Thomas Wegmueller
 * Date: 20.09.2005,  12:40:04
 */
public class LKAPruefungssession implements IPruefungsSession {
    private java.lang.String sessionsname;
    private Calendar sessionsende;
    private java.lang.String seskez;
    private Calendar planungFreigabe;


    public LKAPruefungssession(WsPruefungssession s) {
        sessionsname = s.getSessionsname();
        sessionsende = CalendarUtils.parseSessionsEnded(s.getSessionsende());
        seskez = s.getSeskez();
        planungFreigabe= CalendarUtils.parsePlanungFreigabe(s.getPlanungFreigabe());

    }

    public String getSessionsname() {
        return sessionsname;
    }

    public Calendar getSessionsende() {
        return sessionsende;
    }


    public String getSeskez() {
        return seskez;
    }

    public Calendar getPlanungFreigabe() {
        return planungFreigabe;
    }


}
