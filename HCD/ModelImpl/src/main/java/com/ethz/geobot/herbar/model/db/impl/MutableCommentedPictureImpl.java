/*
 * Herbar CD-ROM version 2
 *
 * MutableCommentedPictureImpl.java
 *
 * Created on 17. October 2002, 14:38
 * Created by Daniel Frey
 */
package com.ethz.geobot.herbar.model.db.impl;

import com.ethz.geobot.herbar.model.CommentedPicture;
import com.ethz.geobot.herbar.model.Picture;
import com.ethz.geobot.herbar.model.Taxon;

/**
 * Adds a comment to a picture.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $
 * @created $Date: 2007/09/17 11:07:18 $
 */
public class MutableCommentedPictureImpl extends MutableGraphNodeImpl
        implements CommentedPicture {

    private String comment;

    private Picture picture;

    private int id;

    private Taxon tax;

    //---------------//
    //- Constructor -//
    //---------------//

    /**
     * Creates a new instance of CommentedPicture
     */
    public MutableCommentedPictureImpl() {
    }

    public MutableCommentedPictureImpl(Taxon tax, Picture pic, String com) {
        this.setPicture(pic);
        this.setComment(com);
        this.setTaxon(tax);
    }

    //-----------//
    //- Methods -//
    //-----------//

    public void setId(int id) {
        this.id = id;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }

    public void setComment(String com) {
        this.comment = com;
    }

    public void setTaxon(Taxon tax) {
        this.tax = tax;
    }

    public int getId() {
        return this.id;
    }

    public Picture getPicture() {
        return this.picture;
    }

    public String getComment() {
        return this.comment;
    }

    public Taxon getTaxon() {
        return this.tax;
    }

    public String toString() {
        return picture.toString();
    }
}

// $Log: MutableCommentedPictureImpl.java,v $
// Revision 1.1  2007/09/17 11:07:18  daniel_frey
// - Version 3.0.20070401
//
// Revision 1.3  2002/08/28 12:56:22  Dani
// - Changed id from String to integer
//
// Revision 1.2  2002/05/31 20:03:33  Dani
// Refactored tax tree components
//
// Revision 1.1  2002/05/23 23:51:49  dirk
// initial checkin
//
// Revision 1.1  2002/05/17 09:34:43  Dani
// Added CommentedPicture to model
//

