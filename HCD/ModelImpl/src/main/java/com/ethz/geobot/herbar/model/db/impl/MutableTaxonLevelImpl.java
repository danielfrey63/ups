package com.ethz.geobot.herbar.model.db.impl;

import ch.jfactory.model.graph.GraphNodeList;
import com.ethz.geobot.herbar.model.Level;
import com.ethz.geobot.herbar.model.Taxon;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:18 $
 */
public class MutableTaxonLevelImpl extends MutableGraphNodeImpl implements Level {

    /**
     * @see com.ethz.geobot.herbar.model.Level#isLower(Level)
     */
    public boolean isLower(Level level) {
        // root taxon has a null level.
        return level == null || getRank() > ((MutableTaxonLevelImpl) level).getRank();
    }

    /**
     * @see com.ethz.geobot.herbar.model.Level#isHigher(Level)
     */
    public boolean isHigher(Level level) {
        // root taxon has a null level.
        return level != null && getRank() < ((MutableTaxonLevelImpl) level).getRank();
    }

    /**
     * @see com.ethz.geobot.herbar.model.Level#getChildLevel()
     */
    public Level getChildLevel() {
        GraphNodeList list = getChildren(MutableTaxonLevelImpl.class);
        return (MutableTaxonLevelImpl) list.get(0);
    }

    /**
     * @see com.ethz.geobot.herbar.model.Level#getParentLevel()
     */
    public Level getParentLevel() {
        GraphNodeList list = getParents(MutableTaxonLevelImpl.class);
        return (MutableTaxonLevelImpl) list.get(0);
    }

    /**
     * @see com.ethz.geobot.herbar.model.Level#getTaxa()
     */
    public Taxon[] getTaxa() {
        GraphNodeList taxa = getChildren(MutableTaxonImpl.class);
        return (MutableTaxonImpl[]) taxa.getAll();
    }

    /**
     * @see com.ethz.geobot.herbar.model.Level#getSubLevels()
     */
    public Level[] getSubLevels() {
        // TODO: Unite as redundant with MutableLevelImpl
        int size = 0;
        Level level = this;
        while ((level = level.getChildLevel()) != null) {
            size++;
        }
        level = getChildLevel();
        Level[] result = new Level[size];
        for (int i = 0; i < result.length; i++) {
            result[ i ] = level;
            level = level.getChildLevel();
        }
        return result;
    }

    /**
     * @see com.ethz.geobot.herbar.model.Level#getSuperLevels()
     */
    public Level[] getSuperLevels() {
        // TODO: Unite as redundant with MutableLevelImpl
        int size = 0;
        Level level = this;
        while ((level = level.getParentLevel()) != null) {
            size++;
        }
        level = getParentLevel();
        Level[] result = new Level[size];
        for (int i = 0; i < result.length; i++) {
            result[ i ] = level;
            level = level.getParentLevel();
        }
        return result;
    }

}
