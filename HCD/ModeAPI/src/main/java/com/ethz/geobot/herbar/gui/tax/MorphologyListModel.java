/*
 * Herbar CD-ROM version 2
 *
 * MorphologyListModel.java
 *
 * Created on 2. April 2002
 * Created by Daniel Frey
 */
package com.ethz.geobot.herbar.gui.tax;

import com.ethz.geobot.herbar.model.MorValue;
import com.ethz.geobot.herbar.model.Taxon;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.DefaultListModel;
import org.apache.log4j.Logger;

/**
 * Holds the actual morphological values to display.
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:08 $
 */
public class MorphologyListModel extends DefaultListModel
        implements RelevanceFilter
{
    private final static Logger LOG;

    private Taxon actualTaxon;

    private Taxon[] siblings;

    private final Vector allValues;

    private final Vector theValues;

    private int bitset;

    public MorphologyListModel()
    {
        allValues = new Vector();
        theValues = new Vector();
        bitset = 15;
    }

    /**
     * Sets the current taxon for the list. The model retrieves the appropriate morphology items from that Taxon object.
     * The corresponding siblings have to be set as well. Make sure that the Taxon is part of the given siblings,
     * otherwise an {link MorphologyListModel InconsistentModelException} is thrown.
     *
     * @param tax      the new Taxon object which has the morphological traits to display. May be null, indicating that
     *                 the list is empty.
     * @param siblings the siblings including the given Taxon object, for which the comparison will be made, when
     *                 evaluating the relevance of the morphological traits. May be null, indicating that the list is
     *                 emtpy.
     */
    public void setTaxon( final Taxon tax, final Taxon[] siblings )
    {
        if ( LOG.isDebugEnabled() )
        {
            LOG.debug( "Taxon set to " + tax );
        }
        this.actualTaxon = tax;
        this.siblings = siblings;
        if ( !Arrays.asList( siblings ).contains( tax ) )
        {
            throw new InconsistentModelException( tax + " is not part of "
                    + Arrays.asList( siblings ) );
        }
        allValues.removeAllElements();
        if ( tax != null )
        {
            allValues.addAll( Arrays.asList( tax.getMorValues() ) );
        }
        setFilter( bitset );
        super.removeAllElements();
        for ( final Object theValue : theValues )
        {
            super.addElement( theValue );
        }
        super.fireContentsChanged( this, 0, allValues.size() - 1 );
    }

    /**
     * Aims to set the displayed relevances by mean of a bitmask. The lower a bit in the mask is set, the more relevant
     * MorValue objects are displayed.<p> Here is an example: Assume you have 4 relevance levels. The most important
     * relevance will have rank 0, the next 1 a.s.o. The last one has rank 3. Assume further that you want to display
     * MorValue objects of relevance ranks 1 and 2. In this case you would enable bits 2 and 3 of the bitset. <code>
     * Position       1    2    3    4 Rank           0    1    2    3 Bitset         0    1    1    0 MorValue of bit 1
     * 2    4    8 </code> You therefore set the bitset to 6.
     *
     * @see com.ethz.geobot.herbar.gui.tax.RelevanceFilter#setFilter(int)
     * @see com.ethz.geobot.herbar.model.relevance.AbsRelevance
     */
    public void setFilter( final int bitset )
    {
        LOG.info( "seting state to " + bitset );
        this.bitset = bitset;
        theValues.clear();
        theValues.addAll( allValues );
        for ( Iterator iter = theValues.iterator(); iter.hasNext(); )
        {
            final MorValue value = (MorValue) iter.next();
            final int rel = actualTaxon.getRelevance( value ).getRank();
            final int mask = 2 ^ rel;
            if ( ( bitset & mask ) != mask )
            {
                iter.remove();
            }
        }
        super.fireContentsChanged( this, 0, theValues.size() - 1 );
        if ( LOG.isDebugEnabled() )
        {
            LOG.debug( "the " + theValues.size() + " new mors are  " + theValues );
        }
    }

    public Taxon[] getSiblings()
    {
        return siblings;
    }

    public Object getElementAt( final int param )
    {
        return theValues.elementAt( param );
    }

    public int getSize()
    {
        LOG.debug( "returning " + theValues.size() );
        return theValues.size();
    }

    public Object[] toArray()
    {
        return allValues.toArray( new MorValue[0] );
    }

    static
    {
        LOG = Logger.getLogger( com.ethz.geobot.herbar.gui.tax.MorphologyListModel.class );
    }
}
// $Log: MorphologyListModel.java,v $
// Revision 1.1  2007/09/17 11:07:08  daniel_frey
// - Version 3.0.20070401
//
// Revision 1.8  2003/05/25 21:38:46  daniel_frey
// - Optimized imports
// - Replaced static access by proper class access instead of object access
//
// Revision 1.7  2003/04/24 23:12:51  daniel_frey
// - Integrated complete db
//
// Revision 1.6  2002/11/05 11:21:58  daniel_frey
// - Level with tree from GraphNode
//
// Revision 1.5  2002/09/25 14:41:35  daniel_frey
// - Introduced dynamic relevance object model
// - Replaced roles with relevances  by class types for each comination
// - Removed some caching issues
//
// Revision 1.4  2002/06/18 07:30:50  Dani
// Removed reformating confilict
//
