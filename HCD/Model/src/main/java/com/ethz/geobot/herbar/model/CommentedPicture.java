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
 */
public interface CommentedPicture
{
    public String getComment();

    public Picture getPicture();

    public Taxon getTaxon();
}
