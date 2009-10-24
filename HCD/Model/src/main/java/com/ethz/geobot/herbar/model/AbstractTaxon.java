/*
 * AbstractTaxon.java
 *
 * Created on 20. Juni 2002, 18:15
 */
package com.ethz.geobot.herbar.model;

import ch.jfactory.lang.ArrayUtils;
import com.ethz.geobot.herbar.model.relevance.AbsRelevance;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Category;

/**
 * Abstract implementation for Taxon. Implements standard behaviour.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:24 $
 */
public abstract class AbstractTaxon implements Taxon, Comparable {

    private final static Category cat = Category.getInstance(AbstractTaxon.class);

    /**
     * @see com.ethz.geobot.herbar.model.Taxon#getAllChildTaxa(Level)
     */
    public Taxon[] getAllChildTaxa(Level level) {
        List taxa = new ArrayList();
        Taxon child = null;
        for (int i = 0; i < getChildTaxa().length; i++) {
            child = getChildTaxon(i);
            Level childLevel = child.getLevel();
            if (childLevel == level) {
                taxa.add(child);
            }
            else {
                Taxon[] fromchild = child.getAllChildTaxa(level);
                taxa.addAll(Arrays.asList(fromchild));
            }
        }
        Taxon[] ret = (Taxon[]) taxa.toArray(new Taxon[0]);
        return ret;
    }

    /**
     * @see com.ethz.geobot.herbar.model.Taxon#getChildTaxa(Level)
     */
    public Taxon[] getChildTaxa(Level level) {
        List taxa = new ArrayList();
        Taxon curr = null;
        for (int i = 0; i < getChildTaxa().length; i++) {
            curr = getChildTaxon(i);
            if (curr.getLevel() == level) {
                taxa.add(curr);
            }
        }
        Taxon[] ret = new Taxon[taxa.size()];
        for (int i = 0; i < taxa.size(); i++) {
            ret[ i ] = (Taxon) taxa.get(i);
        }
        if (cat.isDebugEnabled()) {
            cat.debug(this.toDebugString() + " getChildTaxa(" + level + ") " +
                    Arrays.asList(ret));
        }
        return ret;
    }

    /**
     * @see com.ethz.geobot.herbar.model.Taxon#getChildTaxon(int)
     */
    public Taxon getChildTaxon(int index)
            throws IndexOutOfBoundsException {
        cat.debug(this.toDebugString() + " getChildTaxon(" + index + ")");
        return getChildTaxa()[ index ];
    }

    /**
     * @see com.ethz.geobot.herbar.model.Taxon#getChildTaxon(Taxon)
     */
    public int getChildTaxon(Taxon child) {
        int i = 0;
        for (i = 0; i < getChildTaxa().length; i++) {
            if (getChildTaxon(i) == child) {
                cat.debug(this.toDebugString() + " getChildTaxon(" + child + ")" + i);
                return i;
            }
        }
        return -1;
    }

    /**
     * @see com.ethz.geobot.herbar.model.Taxon#getMorAttributes()
     */
    public MorAttribute[] getMorAttributes() {
        MorValue[] vals = (MorValue[]) getMorValues();
        Set hs = new HashSet();
        for (int i = 0; i < vals.length; i++) {
            hs.add(vals[ i ].getParentAttribute());
        }
        return (MorAttribute[]) hs.toArray(new MorAttribute[0]);
    }

    /**
     * @see com.ethz.geobot.herbar.model.Taxon#getMorValue(int)
     */
    public MorValue getMorValue(int index) {
        cat.debug(this.toDebugString() + " getMorValue(" + index + ") " + this.getMorValues()[ index ]);
        return getMorValues()[ index ];
    }
//
//    /**
//     * @see com.ethz.geobot.herbar.model.Taxon#getRelMorValue(int)
//     */
//    public MorValue getRelMorValue( int index ) {
//        cat.debug( this.toDebugString() + " getRelMorValue(" + index + ") " +
//            getRelMorValues()[index] );
//        return getRelMorValues()[index];
//    }

    /**
     * @see com.ethz.geobot.herbar.model.Taxon#getSiblings()
     */
    /**
     * Returns the siblings of this Taxon, including this Taxon.
     *
     * @return The siblings value
     */
    public Taxon[] getSiblings() {
        Taxon parent = (Taxon) getParentTaxon();
        if (parent == null) {
            return new Taxon[]{this};
        }
        else {
            return parent.getChildTaxa();
        }
    }

    public Level getSubLevel(int index)
            throws IndexOutOfBoundsException {
        cat.debug(this.toDebugString() + " getSubLevel(" + index + ") " + getSubLevels()[ index ]);
        return getSubLevels()[ index ];
    }

    /**
     * @see com.ethz.geobot.herbar.model.Taxon#getSubLevels()
     */
    public Level[] getSubLevels() {
        return (Level[]) getSubLevels(this).toArray(new Level[0]);
    }
//
//    /**
//     * @see com.ethz.geobot.herbar.model.Taxon#getEquivalentMorValues()
//     */
//    public MorValue[] getEquivalentMorValues() {
//        return getEquivalentMorValues( this.getSiblings() );
//    }
//
//    /**
//     * @see com.ethz.geobot.herbar.model.Taxon#getEquivalentMorValues(Taxon[])
//     */
//    public MorValue[] getEquivalentMorValues(Taxon[] siblings) {
//        List list = new ArrayList();
//        MorValue[] myVals = this.getMorValues();
//        for ( int i = 0; i < myVals.length; i++ ) {
//            if ( ModelUtils.isEquivalent( siblings, myVals[i] ) ) {
//                list.add( myVals[i] );
//            }
//        }
//        return (MorValue[])list.toArray( new MorValue[0] );
//    }
//
//    /**
//     * @see com.ethz.geobot.herbar.model.Taxon#getUniqueMorValues()
//     */
//    public MorValue[] getUniqueMorValues() {
//        return getUniqueMorValues( this.getSiblings() );
//    }
//
//    /**
//     * @see com.ethz.geobot.herbar.model.Taxon#getUniqueMorValues(Taxon[])
//     */
//    public MorValue[] getUniqueMorValues( Taxon[] siblings ) {
//        List vUnique = new ArrayList();
//        MorValue[] myVals = this.getMorValues();
//        for ( int i = 0; i < myVals.length; i++ ) {
//            if ( ModelUtils.isUnique( siblings, myVals[i] ) ) {
//                vUnique.add( myVals[i] );
//            }
//        }
//        return (MorValue[])vUnique.toArray( new MorValue[0] );
//    }
//
//    /**
//     * @see com.ethz.geobot.herbar.model.Taxon#getDifferentMorValues()
//     */
//    public MorValue[] getDifferentMorValues() {
//        return getDifferentMorValues( this.getSiblings() );
//    }
//
//    /**
//     * @see com.ethz.geobot.herbar.model.Taxon#getDifferentMorValues(Taxon[])
//     */
//    public MorValue[] getDifferentMorValues( Taxon[] siblings ) {
//        List vDifferent = new ArrayList();
//        MorValue[] myVals = (MorValue[])this.getMorValues();
//        for ( int i = 0; i < myVals.length; i++ ) {
//            if ( ModelUtils.isDifferent( siblings, myVals[i] ) ) {
//                vDifferent.add( myVals[i] );
//            }
//        }
//        return (MorValue[])vDifferent.toArray( new MorValue[0] );
//    }
//
//    /**
//     * @see com.ethz.geobot.herbar.model.Taxon#getWeakMorValues()
//     */
//    public MorValue[] getWeakMorValues() {
//        return getWeakMorValues( this.getSiblings() );
//    }
//
//    /**
//     * @see com.ethz.geobot.herbar.model.Taxon#getWeakMorValues(Taxon[])
//     */
//    public MorValue[] getWeakMorValues( Taxon[] siblings ) {
//        List vWeak = new ArrayList();
//        MorValue[] myVals = this.getMorValues();
//        for ( int i = 0; i < myVals.length; i++ ) {
//            if ( ModelUtils.isWeak( siblings, myVals[i] ) ) {
//                vWeak.add( myVals[i] );
//            }
//        }
//        return (MorValue[])vWeak.toArray( new MorValue[0] );
//    }

    /**
     * @see com.ethz.geobot.herbar.model.Taxon#getRelevance(MorValue)
     */
    public AbsRelevance getRelevance(MorValue value) {
        return AbsRelevance.getRelevance(getSiblings(), value);
    }


    public boolean isAncestorOf(Taxon descendant) {
        while (descendant != null) {
            if (descendant == this) {
                return true;
            }
            descendant = descendant.getParentTaxon();
        }
        return false;
    }

    /**
     * @see com.ethz.geobot.herbar.model.Taxon#isIn(Taxon[])
     */
    public boolean isIn(Taxon[] list) {
        return ArrayUtils.contains(list, this);
    }

    /**
     * @see java.lang.Comparable#compareTo(Object)
     */
    public int compareTo(Object obj) {
        Taxon taxon = (Taxon) obj;
        return this.getRank() - taxon.getRank();
    }

    public boolean equals(Object compare) {
        Taxon taxon = (Taxon) compare;
        if (taxon == this) {
            // refrence compare
            return true;
        }
        return getId() == taxon.getId();
    }

    public int hashCode() {
        String id = "" + getId();
        int hash = id.hashCode();
        return hash;
    }

    public abstract String toDebugString();

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return this.getName();
    }

    private Set getSubLevels(Taxon tax) {
        Set levels = new HashSet();
        levels.add(this.getLevel());
        Taxon[] children = tax.getChildTaxa();
        for (int i = 0; i < children.length; i++) {
            levels.addAll(getSubLevels(children[ i ]));
        }
        levels.add(tax.getLevel());
        return levels;
    }
}
