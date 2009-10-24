package com.ethz.geobot.herbar.model.filter;

import com.ethz.geobot.herbar.model.Level;
import com.ethz.geobot.herbar.model.Taxon;
import java.util.Arrays;
import org.apache.log4j.Category;

/**
 * This class contains a definition detail.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $
 */
public class FilterDefinitionDetail implements Cloneable {

    private final static Category cat = Category.getInstance(FilterDefinitionDetail.class);

    private Taxon scope;
    private Level[] levels;
    private FilterModel model;

    FilterDefinitionDetail(FilterModel model, Taxon scope, Level level) {
        this(model, scope, new Level[]{level});
    }

    FilterDefinitionDetail(FilterModel model, Taxon scope, Level[] levels) {
        this.model = model;
        this.scope = scope;
        this.levels = levels;
    }

    public Taxon getScope() {
        return scope;
    }

    public void setScope(Taxon scope) {
        this.scope = scope;
        model.notifyModelChange();
    }

    public Level[] getLevels() {
        return levels;
    }

    public void setLevels(Level[] levels) {
        this.levels = levels;
        model.notifyModelChange();
    }

    public boolean isIn(Taxon taxon) {
        if (cat.isDebugEnabled()) {
            cat.debug("check taxon: " + taxon);
            cat.debug("isChild of scope " + scope + ": " + isChild(taxon));
            cat.debug("levelcheck " + Arrays.asList(levels) + ": " + taxon.getLevel());
        }

        // is scope root ?
        if (isRootTaxon(taxon)) {
            return true;
        }

        if (scope.equals(taxon) || isChild(taxon)) {
            for (int i = 0; i < levels.length; i++) {
                if (taxon.getLevel().equals(levels[ i ])) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isChild(Taxon tax) {
        Taxon parent = tax.getParentTaxon();
        while (parent != null) {
            if (scope.equals(parent)) {
                return true;
            }
            parent = parent.getParentTaxon();
        }
        return false;
    }

    private boolean isRootTaxon(Taxon tax) {
        if (tax.getParentTaxon() == null) {
            return true;
        }
        else {
            return false;
        }
    }

    public Object clone() {
        try {
            return super.clone();
        }
        catch (CloneNotSupportedException ex) {
            cat.fatal("clone isn't supported by cloneable class !?!?!", ex);
            throw new RuntimeException(ex);
        }
    }

    public String toString() {
        return scope + " " + Arrays.asList(levels);
    }
}
