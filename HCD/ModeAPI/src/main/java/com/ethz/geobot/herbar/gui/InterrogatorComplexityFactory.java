package com.ethz.geobot.herbar.gui;

import ch.jfactory.model.graph.GraphNode;
import ch.jfactory.model.graph.tree.VirtualGraphTreeNodeFilter;
import com.ethz.geobot.herbar.modeapi.state.StateCompositeModel;
import com.ethz.geobot.herbar.modeapi.state.StateModel;
import com.ethz.geobot.herbar.model.EcoSubject;
import com.ethz.geobot.herbar.model.HerbarModel;
import com.ethz.geobot.herbar.model.MedSubject;
import com.ethz.geobot.herbar.model.MorSubject;
import com.ethz.geobot.herbar.model.Taxon;
import java.util.prefs.Preferences;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:08 $
 */
public class InterrogatorComplexityFactory
{
    public static Type getFilter( final HerbarModel model, final String name, final VirtualGraphTreeNodeFilter filter )
    {
        return new Type( model, name, filter );
    }

    public static class Type implements StateModel
    {
        private final String type;

        private final VirtualGraphTreeNodeFilter filter;

        private GraphNode root;

        private final HerbarModel model;

        protected Type( final HerbarModel model, final String name, final VirtualGraphTreeNodeFilter filter )
        {
            this.type = name;
            this.filter = filter;
            this.model = model;
            final Class type = filter.getType();
            if ( type.isAssignableFrom( Taxon.class ) )
            {
                root = model.getRootTaxon().getAsGraphNode();
            }
            else if ( type.isAssignableFrom( MorSubject.class ) )
            {
                root = model.getRootMorSubject().getAsGraphNode();
            }
            else if ( type.isAssignableFrom( EcoSubject.class ) )
            {
                root = model.getRootEcoSubject().getAsGraphNode();
            }
            else if ( type.isAssignableFrom( MedSubject.class ) )
            {
                root = model.getRootMedSubject().getAsGraphNode();
            }
        }

        public StateCompositeModel getComposite()
        {
            return null;
        }

        public void loadState( final Preferences node )
        {
        }

        public void storeState( final Preferences node )
        {
        }

        public void setTaxFocus( final Taxon focus )
        {
            if ( filter.getType().isAssignableFrom( Taxon.class ) )
            {
                root = focus.getAsGraphNode();
            }
        }

        public VirtualGraphTreeNodeFilter getFilter()
        {
            return filter;
        }

        public String toString()
        {
            return type;
        }

        public GraphNode getRoot()
        {
            return root;
        }
    }
}
