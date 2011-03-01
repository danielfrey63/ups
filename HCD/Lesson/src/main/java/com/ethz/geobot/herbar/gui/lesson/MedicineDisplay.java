/*
 * Herbar CD-ROM version 2
 *
 * MedicineDisplayer.java
 *
 * Created on Feb 13, 2003 2:39:58 PM
 * Created by Daniel
 */
package com.ethz.geobot.herbar.gui.lesson;

import ch.jfactory.model.graph.GraphNode;
import ch.jfactory.model.graph.tree.VirtualGraphTreeNodeFilter;
import ch.jfactory.resource.Strings;
import com.ethz.geobot.herbar.modeapi.HerbarContext;
import com.ethz.geobot.herbar.model.Level;
import com.ethz.geobot.herbar.model.Taxon;
import com.ethz.geobot.herbar.model.trait.MedicineAttribute;
import com.ethz.geobot.herbar.model.trait.MedicineSubject;
import com.ethz.geobot.herbar.model.trait.MedicineText;
import com.ethz.geobot.herbar.model.trait.MedicineValue;

/**
 * <Comments here>
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:06:56 $
 */
public class MedicineDisplay extends AttributeTreePanel
{
    MedicineDisplay( final HerbarContext herbarContext, final Level stopper, final TaxStateModel taxStateModel )
    {
        super( herbarContext, stopper, taxStateModel, Strings.getString( "PROPERTY.MEDICINETEXT.TEXT" ) );
    }

    public VirtualGraphTreeNodeFilter registerFilter()
    {
        return VirtualGraphTreeNodeFilter.getFilter(
                new Class[]{GraphNode.class, Taxon.class, MedicineText.class, MedicineValue.class, MedicineAttribute.class, MedicineSubject.class, MedicineAttribute.class, MedicineValue.class, MedicineText.class},
                new int[][]{{1, 0, 0, 2}, {1, 0, 0, 2}, {0, 0, 0, 2}, {0, 0, 0, 1}, {0, 0, 0, 1}, {1, 0, 0, 1}, {1, 0, 1, 2}, {0, 0, 1, 2}, {1, 0, 1, 2}} );
    }
}

