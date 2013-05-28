/*
 * Copyright (c) 2011.
 *
 * Nutzung und Rechte
 *
 * Die Applikation eBot wurde für Studierende der ETH Zürich entwickelt. Sie  steht
 * allen   an   Hochschulen  oder   Fachhochschulen   eingeschriebenen Studierenden
 * (auch  ausserhalb  der  ETH  Zürich)  für  nichtkommerzielle  Zwecke  im Studium
 * kostenlos zur Verfügung. Nichtstudierende Privatpersonen, die die Applikation zu
 * ihrer  persönlichen  Weiterbildung  nutzen  möchten,  werden  gebeten,  für  die
 * nichtkommerzielle Nutzung einen einmaligen Beitrag von Fr. 20.– zu bezahlen.
 *
 * Postkonto
 *
 * Unterricht, 85-761469-0, Vermerk "eBot"
 * IBAN 59 0900 0000 8576  1469 0; BIC POFICHBEXXX
 *
 * Jede andere Nutzung der Applikation  ist vorher mit dem Projektleiter  (Matthias
 * Baltisberger, Email:  balti@ethz.ch) abzusprechen  und mit  einer entsprechenden
 * Vereinbarung zu regeln. Die  Applikation wird ohne jegliche  Garantien bezüglich
 * Nutzungsansprüchen zur Verfügung gestellt.
 */
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
import com.ethz.geobot.herbar.model.trait.Name;
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
            else if ( type.isAssignableFrom( Name.class ) )
            {
                root = model.getSynonyms().getAsGraphNode();
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
