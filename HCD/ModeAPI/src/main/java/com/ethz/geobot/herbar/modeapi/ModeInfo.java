/*
 * Copyright (c) 2011.
 *
 * Nutzung und Rechte
 *
 * Die Applikation eBot wurde für Studierende der ETH Zürich entwickelt. Sie  steht
 * allen   an   Hochschulen  oder   Fachhochschulen   eingeschriebenen Studierenden
 * (auch  ausserhalb  der  ETH  Zürich)  für  nichtkommerzielle  Zwecke  im Studium
 * kostenlos zur Verfügung. Nichtstudierende Privatpersonen, die die Applikation zu
 * ihrer  persönlichen  Weiterbildung  nutzen  möchten,  werden  gebeten,  für  die
 * nichtkommerzielle Nutzung einen einmaligen Beitrag von Fr. 20.– zu bezahlen.
 *
 * Postkonto
 *
 * Unterricht, 85-761469-0, Vermerk "eBot"
 * IBAN 59 0900 0000 8576  1469 0; BIC POFICHBEXXX
 *
 * Jede andere Nutzung der Applikation  ist vorher mit dem Projektleiter  (Matthias
 * Baltisberger, Email:  balti@ethz.ch) abzusprechen  und mit  einer entsprechenden
 * Vereinbarung zu regeln. Die  Applikation wird ohne jegliche  Garantien bezüglich
 * Nutzungsansprüchen zur Verfügung gestellt.
 */
package com.ethz.geobot.herbar.modeapi;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:08 $
 */
public class ModeInfo
{
    private String description;

    private String modeClass;

    private String modeGroup;

    private String modeName;

    private String icon;

    private String disabledIcon;

    public String getDescription()
    {
        return description;
    }

    public void setDescription( final String description )
    {
        this.description = description;
    }

    public String getDisabledIcon()
    {
        return disabledIcon;
    }

    public void setDisabledIcon( final String disabledIcon )
    {
        this.disabledIcon = disabledIcon;
    }

    public String getIcon()
    {
        return icon;
    }

    public void setIcon( final String icon )
    {
        this.icon = icon;
    }

    public String getModeClass()
    {
        return modeClass;
    }

    public void setModeClass( final String modeClass )
    {
        this.modeClass = modeClass;
    }

    public String getModeGroup()
    {
        return modeGroup;
    }

    public void setModeGroup( final String modeGroup )
    {
        this.modeGroup = modeGroup;
    }

    public String getModeName()
    {
        return modeName;
    }

    public void setModeName( final String modeName )
    {
        this.modeName = modeName;
    }
}
