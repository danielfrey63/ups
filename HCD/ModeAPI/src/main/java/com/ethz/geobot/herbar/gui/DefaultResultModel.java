/*
 * Copyright (c) 2011.
 *
 * Nutzung und Rechte
 *
 * Die Applikation eBot wurde f�r Studierende der ETH Z�rich entwickelt. Sie  steht
 * allen   an   Hochschulen  oder   Fachhochschulen   eingeschriebenen Studierenden
 * (auch  ausserhalb  der  ETH  Z�rich)  f�r  nichtkommerzielle  Zwecke  im Studium
 * kostenlos zur Verf�gung. Nichtstudierende Privatpersonen, die die Applikation zu
 * ihrer  pers�nlichen  Weiterbildung  nutzen  m�chten,  werden  gebeten,  f�r  die
 * nichtkommerzielle Nutzung einen einmaligen Beitrag von Fr. 20.� zu bezahlen.
 *
 * Postkonto
 *
 * Unterricht, 85-761469-0, Vermerk "eBot"
 * IBAN 59 0900 0000 8576  1469 0; BIC POFICHBEXXX
 *
 * Jede andere Nutzung der Applikation  ist vorher mit dem Projektleiter  (Matthias
 * Baltisberger, Email:  balti@ethz.ch) abzusprechen  und mit  einer entsprechenden
 * Vereinbarung zu regeln. Die  Applikation wird ohne jegliche  Garantien bez�glich
 * Nutzungsanspr�chen zur Verf�gung gestellt.
 */
package com.ethz.geobot.herbar.gui;

import com.ethz.geobot.herbar.model.HerbarModel;
import com.ethz.geobot.herbar.model.Taxon;
import java.util.Enumeration;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:08 $
 */
public class DefaultResultModel extends ResultModel
{
    private final HerbarModel model;

    public DefaultResultModel( final HerbarModel model )
    {
        this.model = model;
    }

    public HerbarModel getModel()
    {
        return model;
    }

    public void setTaxFocus( final Taxon focus )
    {
        final Enumeration e = subStateModels();
        while ( e.hasMoreElements() )
        {
            final DetailResultModel detailResultModel = (DetailResultModel) e.nextElement();
            detailResultModel.setTaxFocus( focus );
        }
    }

    public void reset()
    {
        final Enumeration e = subStateModels();
        while ( e.hasMoreElements() )
        {
            final DetailResultModel detailResultModel = (DetailResultModel) e.nextElement();
            detailResultModel.reset();
        }
    }
}
