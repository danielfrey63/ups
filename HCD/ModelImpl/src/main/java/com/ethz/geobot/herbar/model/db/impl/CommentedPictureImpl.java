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
import com.ethz.geobot.herbar.model.CommentedPicture;
import com.ethz.geobot.herbar.model.Picture;
import com.ethz.geobot.herbar.model.Taxon;

/**
 * Adds a comment to a picture.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $
 */
public class CommentedPictureImpl extends GraphNodeImpl implements CommentedPicture
{
    private String comment;

    private Picture picture;

    private int id;

    private Taxon tax;

    //---------------//
    //- Constructor -//
    //---------------//

    public CommentedPictureImpl( final Taxon tax, final Picture pic, final String com )
    {
        this.setPicture( pic );
        this.setComment( com );
        this.setTaxon( tax );
    }

    //-----------//
    //- Methods -//
    //-----------//

    public void setId( final int id )
    {
        this.id = id;
    }

    public void setPicture( final Picture picture )
    {
        this.picture = picture;
    }

    public void setComment( final String com )
    {
        this.comment = com;
    }

    public void setTaxon( final Taxon tax )
    {
        this.tax = tax;
    }

    public int getId()
    {
        return this.id;
    }

    public Picture getPicture()
    {
        return this.picture;
    }

    public String getComment()
    {
        return this.comment;
    }

    public Taxon getTaxon()
    {
        return this.tax;
    }

    public String toString()
    {
        return picture == null ? "null:" + comment : picture.toString() + ":" + comment;
    }
}
