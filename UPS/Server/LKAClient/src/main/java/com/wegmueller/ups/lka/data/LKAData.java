package com.wegmueller.ups.lka.data;

import com.wegmueller.ups.lka.IAnmeldedaten;
import com.wegmueller.ups.lka.ILKAData;
import com.wegmueller.ups.lka.IPruefungsSession;

/**
 * Created by: Thomas Wegmueller Date: 26.09.2005,  23:58:56
 */
public class LKAData implements ILKAData
{
    private IAnmeldedaten[] anmeldedaten;

    private IPruefungsSession pruefungsSession;

    public LKAData( final LKAPruefungssession ses, final LKAAnmeldedaten[] amd )
    {
        setAnmeldedaten( amd );
        setPruefungsSession( ses );
    }

    public IAnmeldedaten[] getAnmeldedaten()
    {
        return anmeldedaten;
    }

    public IPruefungsSession getPruefungsSession()
    {
        return pruefungsSession;
    }

    public void setAnmeldedaten( final IAnmeldedaten[] anmeldedaten )
    {
        this.anmeldedaten = anmeldedaten;
    }

    public void setPruefungsSession( final IPruefungsSession pruefungsSession )
    {
        this.pruefungsSession = pruefungsSession;
    }
}
