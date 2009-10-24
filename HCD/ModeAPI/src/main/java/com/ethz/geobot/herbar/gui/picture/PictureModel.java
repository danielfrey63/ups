package com.ethz.geobot.herbar.gui.picture;

import ch.jfactory.collection.cursor.Cursor;
import ch.jfactory.collection.cursor.DefaultCursor;
import com.ethz.geobot.herbar.model.CommentedPicture;
import com.ethz.geobot.herbar.model.HerbarModel;
import com.ethz.geobot.herbar.model.PictureTheme;
import com.ethz.geobot.herbar.model.Taxon;
import java.util.Iterator;
import org.apache.log4j.Category;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $
 */
public class PictureModel {

    private static final Category cat = Category.getInstance(PictureModel.class);

    private PictureTheme[] themes;
    private Taxon taxon;
    private Cursor[] cursors;
    private int selected;
    private boolean zoomed = false;

    public PictureModel(HerbarModel model) {
        this.themes = model.getPictureThemes();
    }

    public boolean isZoomed() {
        return zoomed;
    }

    public void setZoomed(boolean b) {
        zoomed = b;
    }

    public void setPicture(String name) {
        Iterator it = getPictureCursor().getIterator();
        while (it.hasNext()) {
            CommentedPicture pic = (CommentedPicture) it.next();
            if (pic.getPicture().getName().equals(name)) {
                getPictureCursor().setCurrent(pic);
            }
        }
    }

    public CommentedPicture getPicture() {
        return (CommentedPicture) getPictureCursor().getCurrent();
    }

    public PictureTheme[] getPictureThemes() {
        return themes;
    }

    public int getSelectedIndex() {
        return selected;
    }

    public PictureTheme getPictureTheme() {
        return themes[ selected ];
    }

    public void setSelectedIndex(int idx) {
        selected = idx;
    }

    public int getIndex(PictureTheme t) {
        for (int i = 0; i < themes.length; i++) {
            if (themes[ i ] == t) {
                return i;
            }
        }
        return -1;
    }

    public Cursor getPictureCursor() {
        return getPictureCursor(getPictureTheme());
    }

    public Cursor getPictureCursor(PictureTheme t) {
        int idx = getIndex(t);
        if (idx >= 0) {
            return ensureCursor(idx);
        }
        return null;
    }

    public Taxon getTaxon() {
        return taxon;
    }

    private Cursor ensureCursor(int idx) {
        if (cursors == null) {
            cursors = new Cursor[themes.length];
        }
        if (cursors[ idx ] == null) {
            cursors[ idx ] = new DefaultCursor(getTaxon().getCommentedPictures(themes[ idx ]));
        }
        return cursors[ idx ];
    }

    public void setTaxon(Taxon taxon) {
        cat.debug("setTaxon(" + taxon + ")");
        try {
            this.taxon = taxon;
            cursors = null;
        }
        catch (Exception e) {
            cat.error("Error in setTaxon ", e);
        }
    }

    public void setPictureTheme(PictureTheme shownTheme) {
        for (int i = 0; i < themes.length; i++) {
            if (themes[ i ] == shownTheme) {
                setSelectedIndex(i);
                return;
            }
        }
    }


}