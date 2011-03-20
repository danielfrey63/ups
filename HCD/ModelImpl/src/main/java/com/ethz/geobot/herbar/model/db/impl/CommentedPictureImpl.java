/*
 * Herbar CD-ROM version 2
 *
 * MutableCommentedPictureImpl.java
 *
 * Created on 17. October 2002, 14:38
 * Created by Daniel Frey
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
        return picture == null ? "null" : picture.toString();
    }
}
