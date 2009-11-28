/*
 * Herbar CD-ROM version 2
 *
 * EcologyDisplayer.java
 *
 * Created on Feb 13, 2003 2:39:33 PM
 * Created by Daniel
 */
package com.ethz.geobot.herbar.gui.lesson;

import ch.jfactory.model.graph.GraphNode;
import ch.jfactory.model.graph.tree.VirtualGraphTreeNodeFilter;
import ch.jfactory.resource.Strings;
import com.ethz.geobot.herbar.model.EcoText;
import com.ethz.geobot.herbar.model.HerbarModel;
import com.ethz.geobot.herbar.model.Level;
import com.ethz.geobot.herbar.model.Taxon;

/**
 * <Comments here>
 *
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:06:56 $
 */
public class EcologyDisplayer extends AttributeTreePanel
{
    EcologyDisplayer( final HerbarModel hModel, final Level stopper )
    {
        super( hModel, stopper, Strings.getString( "PROPERTY.ECOTEXT.TEXT" ) );
    }

    public VirtualGraphTreeNodeFilter registerFilter()
    {
        VirtualGraphTreeNodeFilter filter = new VirtualGraphTreeNodeFilter( EcoText.class,
                VirtualGraphTreeNodeFilter.VISIBILITY_VISIBLE,
                VirtualGraphTreeNodeFilter.SELF_FLAT,
                VirtualGraphTreeNodeFilter.CONSTRAINT_FREE,
                null,
                VirtualGraphTreeNodeFilter.LINE_RELATED );

        filter = new VirtualGraphTreeNodeFilter( Taxon.class,
                VirtualGraphTreeNodeFilter.VISIBILITY_VISIBLE,
                VirtualGraphTreeNodeFilter.SELF_FLAT,
                VirtualGraphTreeNodeFilter.CONSTRAINT_FREE,
                new VirtualGraphTreeNodeFilter[]{filter},
                VirtualGraphTreeNodeFilter.LINE_DESCENDANT );

        filter = new VirtualGraphTreeNodeFilter( GraphNode.class,
                VirtualGraphTreeNodeFilter.VISIBILITY_VISIBLE,
                VirtualGraphTreeNodeFilter.SELF_FLAT,
                VirtualGraphTreeNodeFilter.CONSTRAINT_FREE,
                new VirtualGraphTreeNodeFilter[]{filter},
                VirtualGraphTreeNodeFilter.LINE_DESCENDANT );

        return filter;
    }
}

