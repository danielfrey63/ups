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
import com.ethz.geobot.herbar.model.MedAttribute;
import com.ethz.geobot.herbar.model.MedSubject;
import com.ethz.geobot.herbar.model.MedText;
import com.ethz.geobot.herbar.model.MedValue;
import com.ethz.geobot.herbar.model.Taxon;

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
        super( herbarContext, stopper, taxStateModel, Strings.getString( "PROPERTY.MEDTEXT.TEXT" ) );
    }

    public VirtualGraphTreeNodeFilter registerFilter()
    {
        return VirtualGraphTreeNodeFilter.getFilter( new Class[]{GraphNode.class, Taxon.class, MedText.class, MedValue.class, MedAttribute.class,
                MedSubject.class, MedAttribute.class, MedValue.class, MedText.class},
                new int[][]{{1, 0, 0, 2}, {1, 0, 0, 2}, {0, 0, 0, 2}, {0, 0, 0, 1}, {0, 0, 0, 1},
                        {1, 0, 0, 1}, {1, 0, 1, 2}, {0, 0, 1, 2}, {1, 0, 1, 2}} );
    }
}

