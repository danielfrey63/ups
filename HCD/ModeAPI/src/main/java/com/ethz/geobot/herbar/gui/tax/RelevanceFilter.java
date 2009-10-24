/*
 * Herbar CD-ROM version 2
 *
 * RelevanceFilter.java
 *
 * Created on 31.5.2002
 * Created by Daniel Frey
 */
package com.ethz.geobot.herbar.gui.tax;


public interface RelevanceFilter {

    /**
     * Set a four bits. From lower to higher order bit they stand for normal (<code>n</code>), weak (<code>w</code>),
     * different (<code>d</code>)
     * and unique (<code>u</code>) morhpological traits <pre>
     *     1 1 1 1
     *     u d w n
     * </pre> to filter. Is the bit set to 1, the trait is activated, set to
     * 0 deactivates it.
     *
     * @param bitset set of bits
     */
    public abstract void setFilter(int bitset);
}
