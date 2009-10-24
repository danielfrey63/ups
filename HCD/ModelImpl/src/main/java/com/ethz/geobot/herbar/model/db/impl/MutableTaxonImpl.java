/*
 * Herbar CD-ROM Version 2
 *
 * MutableTaxonImpl.java
 */

package com.ethz.geobot.herbar.model.db.impl;

import ch.jfactory.lang.ArrayUtils;
import ch.jfactory.model.graph.GraphNode;
import ch.jfactory.model.graph.GraphNodeList;
import ch.jfactory.model.graph.GraphNodeRankComparator;
import com.ethz.geobot.herbar.model.CommentedPicture;
import com.ethz.geobot.herbar.model.Level;
import com.ethz.geobot.herbar.model.MorAttribute;
import com.ethz.geobot.herbar.model.MorText;
import com.ethz.geobot.herbar.model.MorValue;
import com.ethz.geobot.herbar.model.MutableTaxon;
import com.ethz.geobot.herbar.model.Picture;
import com.ethz.geobot.herbar.model.PictureText;
import com.ethz.geobot.herbar.model.PictureTheme;
import com.ethz.geobot.herbar.model.Taxon;
import com.ethz.geobot.herbar.model.relevance.AbsRelevance;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Category;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:18 $
 */
public class MutableTaxonImpl extends MutableGraphNodeImpl implements MutableTaxon {

    private static final Category CAT = Category.getInstance(MutableTaxonImpl.class);
    private static final Comparator RANK_COMPARATOR = new GraphNodeRankComparator();

    public static GraphNodeList getNodes(Taxon[] list) {
        GraphNodeList result = new GraphNodeList();
        for (int i = 0; i < list.length; i++) {
            result.add((MutableTaxonImpl) list[ i ]);
        }
        return result;
    }

    /**
     * @see com.ethz.geobot.herbar.model.Taxon#getRelevance(MorValue)
     */
    public AbsRelevance getRelevance(MorValue value) {
        return AbsRelevance.getRelevance(getSiblings(), value);
    }


    /**
     * @see com.ethz.geobot.herbar.model.MutableTaxon#setChildTaxa(Taxon[])
     */
    public void setChildTaxa(Taxon[] children) {
        GraphNodeList newChildren = new GraphNodeList();
        for (int i = 0; i < children.length; i++) {
            newChildren.add((MutableTaxonImpl) children[ i ]);
        }
        setChildren(newChildren, MutableTaxonImpl.class);
    }

    /**
     * @see com.ethz.geobot.herbar.model.MutableTaxon#setLevel(Level)
     */
    public void setLevel(Level newLevel) {
        // Replace all level type children by the new one
        setChildren(new GraphNodeList((MutableTaxonLevelImpl) newLevel),
                MutableTaxonLevelImpl.class);
    }

    /**
     * @see com.ethz.geobot.herbar.model.MutableTaxon#setParentTaxon(Taxon)
     */
    public void setParentTaxon(Taxon parent) {
        setParents(new GraphNodeList((MutableTaxonImpl) parent), MutableTaxonImpl.class);
    }

    /**
     * @see com.ethz.geobot.herbar.model.Taxon#getLevel()
     */
    public Level getLevel() {
        GraphNodeList list = getChildren(MutableTaxonLevelImpl.class);
        return (MutableTaxonLevelImpl) list.get(0);
    }

    /**
     * @see com.ethz.geobot.herbar.model.Taxon#getParentTaxon()
     */
    public Taxon getParentTaxon() {
        GraphNode parent = super.getParents(MutableTaxonImpl.class).get(0);
        if (parent == null) {
            return null;
        }
        else {
            return (MutableTaxonImpl) parent;
        }
    }

    /**
     * @see com.ethz.geobot.herbar.model.Taxon#getChildTaxa()
     */
    public Taxon[] getChildTaxa() {
        GraphNodeList childTaxa = getChildren(MutableTaxonImpl.class);
        return (Taxon[]) childTaxa.getAll(new MutableTaxonImpl[0]);
    }

    /**
     * @see com.ethz.geobot.herbar.model.Taxon#getChildTaxon(int)
     */
    public Taxon getChildTaxon(int index) throws IndexOutOfBoundsException {
        return getChildTaxa()[ index ];
    }

    /**
     * @see com.ethz.geobot.herbar.model.Taxon#getMorValues()
     */
    public MorValue[] getMorValues() {
        GraphNodeList texts = getChildren(MorText.class);
        GraphNodeList values = new GraphNodeList();
        for (int i = 0; i < texts.size(); i++) {
            values.addAll(texts.get(i).getParents(MorValue.class));
        }
        return (MorValue[]) values.getAll(new MutableMorphologyValueImpl[0]);
    }

    /**
     * @see com.ethz.geobot.herbar.model.Taxon#getMorValue(int)
     */
    public MorValue getMorValue(int index) {
        return getMorValues()[ index ];
    }

    /**
     * @see com.ethz.geobot.herbar.model.Taxon#getMorAttributes()
     */
    public MorAttribute[] getMorAttributes() {
        MutableMorphologyValueImpl[] values = (MutableMorphologyValueImpl[]) getMorValues();
        GraphNodeList attributes = new GraphNodeList();
        for (int i = 0; i < values.length; i++) {
            if (!attributes.contains(values[ i ])) {
                attributes.add((MutableMorphologyAttributeImpl) values[ i ].getParentAttribute());
            }
        }
        return (MorAttribute[]) attributes.getAll(new MutableMorphologyAttributeImpl[0]);
    }

    /**
     * @see com.ethz.geobot.herbar.model.Taxon#getChildTaxon(Taxon)
     */
    public int getChildTaxon(Taxon child) {
        Taxon[] taxa = getChildTaxa();
        for (int i = 0; i < taxa.length; i++) {
            if (child == taxa[ i ]) {
                return i;
            }
        }
        return -1;
    }

    /**
     * @see com.ethz.geobot.herbar.model.Taxon#getChildTaxa(Level)
     */
    public Taxon[] getChildTaxa(Level level) {
        GraphNodeList all = getChildren(MutableTaxonImpl.class);
        GraphNodeList children = all.getChildren(MutableTaxonLevelImpl.class);
        GraphNodeList result = new GraphNodeList();
        for (int i = 0; i < all.size(); i++) {
            if (children.get(i) == level) {
                result.add(all.get(i));
            }
        }
        List list = Arrays.asList(result.getAll());
        return (MutableTaxonImpl[]) list.toArray(new MutableTaxonImpl[0]);
    }

    /**
     * @see com.ethz.geobot.herbar.model.Taxon#getAllChildTaxa(Level)
     */
    public Taxon[] getAllChildTaxa(Level level) {
        GraphNodeList result = new GraphNodeList();
        GraphNodeList children = getChildren(MutableTaxonImpl.class);
        result.addAll(getNodes((MutableTaxonImpl[]) getChildTaxa(level)));
        for (int i = 0; i < children.size(); i++) {
            MutableTaxonImpl child = (MutableTaxonImpl) children.get(i);
            Taxon[] subchilds = child.getAllChildTaxa(level);
            result.addAll(getNodes((Taxon[]) subchilds));
        }
        return (Taxon[]) result.getAll(new MutableTaxonImpl[0]);
    }

    /**
     * @see com.ethz.geobot.herbar.model.Taxon#isIn(Taxon[])
     */
    public boolean isIn(Taxon[] list) {
        return ArrayUtils.contains(list, this);
    }

    /**
     * @see com.ethz.geobot.herbar.model.Taxon#getSubLevels()
     */
    public Level[] getSubLevels() {
        GraphNodeList children = getChildren(MutableTaxonImpl.class);
        Set result = new HashSet();

        // add this level
        GraphNodeList levels = getChildren(MutableTaxonLevelImpl.class);
        result.addAll(Arrays.asList(levels.getAll()));

        // add all direct childrens levels first
        GraphNodeList sublevels = children.getChildren(MutableTaxonLevelImpl.class);
        result.addAll(Arrays.asList(sublevels.getAll()));

        // add subsequently descendants
        for (int i = 0; i < children.size(); i++) {
            MutableTaxonImpl tax = (MutableTaxonImpl) children.get(i);
            result.addAll(Arrays.asList(tax.getSubLevels()));
        }
        List list = new ArrayList(result);
        Collections.sort(list, RANK_COMPARATOR);
        return (Level[]) list.toArray(new MutableTaxonLevelImpl[0]);
    }

    /**
     * @see com.ethz.geobot.herbar.model.Taxon#getCommentedPictures(PictureTheme)
     */
    public CommentedPicture[] getCommentedPictures(PictureTheme theme) {
        List result = new ArrayList();
        GraphNodeList texts = getChildren(PictureText.class);
        for (int i = 0; i < texts.size(); i++) {
            GraphNode text = texts.get(i);
            GraphNode pic = text.getParents(Picture.class).get(0);
            if (text.getChildren(PictureTheme.class).get(0) == theme) {
                result.add(new MutableCommentedPictureImpl(this, (Picture) pic, text.getName()));
            }
        }
        CAT.debug("Pictures for " + this + " and " + theme + " are " + result);
        return (CommentedPicture[]) result.toArray(new MutableCommentedPictureImpl[0]);
    }

    /**
     * @see com.ethz.geobot.herbar.model.Taxon#getPictureThemes()
     */
    public PictureTheme[] getPictureThemes() {
        List result = new ArrayList();
        GraphNodeList texts = getChildren(PictureText.class);
        for (int i = 0; i < texts.size(); i++) {
            GraphNode text = texts.get(i);
            result.add(text.getChildren(PictureTheme.class).get(0));
        }
        return (PictureTheme[]) result.toArray(new MutablePictureThemeImpl[0]);
    }

    /**
     * @see com.ethz.geobot.herbar.model.Taxon#getSiblings()
     */
    public Taxon[] getSiblings() {
        GraphNode parent = getParents(MutableTaxonImpl.class).get(0);
        if (parent == null) {
            return new Taxon[0];
        }
        else {
            GraphNodeList children = parent.getChildren(MutableTaxonImpl.class);
            return (MutableTaxonImpl[]) children.getAll(new MutableTaxonImpl[0]);
        }
    }

    /**
     * @see com.ethz.geobot.herbar.model.Taxon#getScore()
     */
    public double getScore() {
        throw new NoSuchMethodError("getScore not implemented yet");
    }

    /**
     * @see com.ethz.geobot.herbar.model.Taxon#setScore(boolean)
     */
    public void setScore(boolean right) {
        throw new NoSuchMethodError("setScore not implemented yet");
    }
}
