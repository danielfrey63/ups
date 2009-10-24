package com.ethz.geobot.herbar.model.filter;


import ch.jfactory.model.graph.GraphNode;
import com.ethz.geobot.herbar.model.AbstractTaxon;
import com.ethz.geobot.herbar.model.CommentedPicture;
import com.ethz.geobot.herbar.model.Level;
import com.ethz.geobot.herbar.model.MorValue;
import com.ethz.geobot.herbar.model.PictureTheme;
import com.ethz.geobot.herbar.model.Taxon;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.log4j.Category;

/**
 * Taxon class implementing the filter. This class acts as a Proxy for a dependent Taxon. It add the functionallity to
 * filter its siblings and levels.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $
 */
class FilterTaxon extends AbstractTaxon {

    private static final Category cat = Category.getInstance(FilterTaxon.class);

    private Taxon dependentTaxon;
    private FilterModel filterModel;
    private List cachedChilds = null;
    private List cachedSubLevels = null;
    private Taxon cachedParent = null;

    /**
     * Construct a FilterTaxon
     *
     * @param filterModel    the model with the filter information
     * @param dependentTaxon the taxon on which this FilterTaxon depends
     */
    public FilterTaxon(FilterModel filterModel, Taxon dependentTaxon) {
        this.filterModel = filterModel;
        this.dependentTaxon = dependentTaxon;
    }

    public void setScore(boolean right) {
        dependentTaxon.setScore(right);
    }

    /**
     * set the dependent taxon reference
     *
     * @param dependentTaxon reference to the dependent taxon
     */
    public void setDependentTaxon(Taxon dependentTaxon) {
        this.dependentTaxon = dependentTaxon;
    }

    public int getId() {
        return dependentTaxon.getId();
    }

    public Level getLevel() {
        return dependentTaxon.getLevel();
    }

    public String getName() {
        return dependentTaxon.getName();
    }

    public Taxon getParentTaxon() {
        if (cachedParent == null) {
            Taxon parent = dependentTaxon.getParentTaxon();

            while (parent != null && !filterModel.isIn(parent)) {
                parent = parent.getParentTaxon();
            }
            // TODO: something more intelegent requeired
            if (parent != null) {
                cachedParent = filterModel.createFilterTaxon(parent);
            }
        }
        return cachedParent;
    }

    public Taxon[] getChildTaxa() {
        if (cachedChilds == null) {
            cachedChilds = new ArrayList();
            collectChilds(dependentTaxon, cachedChilds);
        }
        return (Taxon[]) cachedChilds.toArray(new Taxon[0]);
    }

    public MorValue[] getMorValues() {
        return dependentTaxon.getMorValues();
    }

    public Level[] getSubLevels() {
        if (cachedSubLevels == null) {
            if (cat.isDebugEnabled()) {
                cat.debug("create sublevellist for taxon: " + this);
            }
            cachedSubLevels = new ArrayList();
            Level level = getLevel();
            Level[] levels = dependentTaxon.getSubLevels();
            for (int i = 0; i < levels.length; i++) {
                if (cat.isDebugEnabled()) {
                    cat.debug("check level: " + levels[ i ]);
                }

                Taxon[] taxList = dependentTaxon.getAllChildTaxa(levels[ i ]);
                boolean found = false;
                for (int t = 0; t < taxList.length && !found; t++) {
                    if (filterModel.isIn(taxList[ t ])) {
                        found = true;
                    }
                }

                if (found || level == levels[ i ]) {
                    if (cat.isDebugEnabled()) {
                        cat.debug("add level " + levels[ i ] + " as sublevel");
                    }
                    cachedSubLevels.add(levels[ i ]);
                }
            }
            // sort levels
            Collections.sort(cachedSubLevels);
        }

        return (Level[]) cachedSubLevels.toArray(new Level[0]);
    }

    public CommentedPicture[] getCommentedPictures(PictureTheme theme) {
        return dependentTaxon.getCommentedPictures(theme);
    }

    public PictureTheme[] getPictureThemes() {
        return dependentTaxon.getPictureThemes();
    }

    public double getScore() {
        return dependentTaxon.getScore();
    }

    public int getRank() {
        return dependentTaxon.getRank();
    }

    /**
     * get a reference to the dependent taxon object.
     *
     * @return reference to dependent taxon
     */
    public Taxon getDependentTaxon() {
        return dependentTaxon;
    }

    private void collectChilds(Taxon tax, List childList) {
        if (cat.isDebugEnabled()) {
            cat.debug("collect childs for: " + tax);
        }
        Taxon[] childs = tax.getChildTaxa();

        for (int i = 0; i < childs.length; i++) {
            if (filterModel.isIn(childs[ i ])) {
                // ok, this element is a child
                if (cat.isDebugEnabled()) {
                    cat.debug("add child to filtered list: " + childs[ i ]);
                }
                childList.add(filterModel.createFilterTaxon(childs[ i ]));
            }
            else {
                if (cat.isDebugEnabled()) {
                    cat.debug("collect childs for taxon: " + childs[ i ]);
                }
                collectChilds(childs[ i ], childList);
            }
        }
    }

    public String toDebugString() {
        return dependentTaxon.getName() + "[" + dependentTaxon.getId() + "]";
    }

    public GraphNode getAsGraphNode() {
        return dependentTaxon.getAsGraphNode();
    }

    public void clearCachedSubLevels() {
    }
}
