/*
 * Herbar CD-ROM version 2
 *
 * CommentedPicture.java
 *
 * Created on 15. Mai 2002, 16:11
 * Created by Daniel Frey
 */
package com.ethz.geobot.herbar.model;

/**
 * Should deliver access to a picture ant its comment.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $
 * @created $Date: 2007/09/17 11:07:24 $
 */
public interface CommentedPicture
{
    public String getComment();

    public Picture getPicture();

    public Taxon getTaxon();
}

// $Log: CommentedPicture.java,v $
// Revision 1.1  2007/09/17 11:07:24  daniel_frey
// - Version 3.0.20070401
//
// Revision 1.2  2002/05/31 20:02:59  Dani
// Refactored tax tree components
//
// Revision 1.1  2002/05/23 23:51:36  dirk
// initial checkin
//
// Revision 1.1  2002/05/17 09:34:43  Dani
// Added CommentedPicture to model
//

