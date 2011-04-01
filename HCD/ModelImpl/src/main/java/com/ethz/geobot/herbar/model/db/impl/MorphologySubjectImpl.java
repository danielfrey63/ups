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
package com.ethz.geobot.herbar.model.db.impl;

import ch.jfactory.model.graph.GraphNodeImpl;
import ch.jfactory.model.graph.GraphNodeList;
import com.ethz.geobot.herbar.model.trait.MorphologyAttribute;
import com.ethz.geobot.herbar.model.trait.MorphologySubject;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:18 $
 */
public class MorphologySubjectImpl extends GraphNodeImpl implements MorphologySubject
{
    public MorphologyAttribute[] getAttributes()
    {
        final GraphNodeList list = getChildren( MorphologyAttributeImpl.class );
        return (MorphologyAttributeImpl[]) list.getAll();
    }

    public MorphologyAttribute getAttribute( final int index ) throws IndexOutOfBoundsException
    {
        return getAttributes()[index];
    }

    public MorphologySubject[] getSubjects()
    {
        final GraphNodeList list = getChildren( MorphologySubjectImpl.class );
        return (MorphologySubjectImpl[]) list.getAll();
    }

    public MorphologySubject getSubject( final int index ) throws IndexOutOfBoundsException
    {
        return getSubjects()[index];
    }

    public MorphologySubject getParentSubject()
    {
        final GraphNodeList parents = getParents( MorphologySubjectImpl.class );
        return (MorphologySubjectImpl) parents.get( 0 );
    }
}
