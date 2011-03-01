package com.ethz.geobot.herbar.gui;

import ch.jfactory.model.graph.GraphNode;
import ch.jfactory.model.graph.tree.VirtualGraphTreeNodeFilter;
import com.ethz.geobot.herbar.modeapi.state.StateCompositeModel;
import com.ethz.geobot.herbar.modeapi.state.StateModel;
import com.ethz.geobot.herbar.model.HerbarModel;
import com.ethz.geobot.herbar.model.Taxon;
import com.ethz.geobot.herbar.model.trait.Ecology;
import com.ethz.geobot.herbar.model.trait.Medicine;
import com.ethz.geobot.herbar.model.trait.Morphology;
import java.util.prefs.Preferences;
import org.slf4j.LoggerFactory;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:08 $
 */
public class InterrogatorComplexityFactory
{
    /** This class logger. */
    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger( InterrogatorComplexityFactory.class );

    public static Type getFilter( final HerbarModel model, final String name, final VirtualGraphTreeNodeFilter filter )
    {
        return new Type( model, name, filter );
    }

    public static class Type implements StateModel
    {
        private final String type;

        private final VirtualGraphTreeNodeFilter filter;

        private GraphNode root;

        protected Type( final HerbarModel model, final String name, final VirtualGraphTreeNodeFilter filter )
        {
            this.type = name;
            this.filter = filter;
            final Class type = filter.getType();
            if ( type.isAssignableFrom( Taxon.class ) )
            {
                root = model.getRootTaxon().getAsGraphNode();
            }
            else if ( type.isAssignableFrom( Morphology.class ) )
            {
                root = model.getMorphology().getAsGraphNode();
            }
            else if ( type.isAssignableFrom( Ecology.class ) )
            {
                root = model.getEcology().getAsGraphNode();
            }
            else if ( type.isAssignableFrom( Medicine.class ) )
            {
                root = model.getMedicine().getAsGraphNode();
            }
            else
            {
                LOG.error( "cannot find root for " + type, new Exception( "Type not found" ) );
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
