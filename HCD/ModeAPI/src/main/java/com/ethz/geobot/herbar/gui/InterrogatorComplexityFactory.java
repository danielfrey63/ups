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
public class InterrogatorComplexityFactory {

    public static Type getFilter(HerbarModel model, String name, VirtualGraphTreeNodeFilter filter) {
        Type type = new Type(model, name, filter);
        return type;
    }

    public static class Type implements StateModel {

        private String type;
        private VirtualGraphTreeNodeFilter filter;
        private GraphNode root;
        private HerbarModel model;

        protected Type(HerbarModel model, String name, VirtualGraphTreeNodeFilter filter) {
            this.type = name;
            this.filter = filter;
            this.model = model;
            Class type = filter.getType();
            if (type.isAssignableFrom(Taxon.class)) {
                root = model.getRootTaxon().getAsGraphNode();
            }
            else if (type.isAssignableFrom(MorSubject.class)) {
                root = model.getRootMorSubject().getAsGraphNode();
            }
            else if (type.isAssignableFrom(EcoSubject.class)) {
                root = model.getRootEcoSubject().getAsGraphNode();
            }
            else if (type.isAssignableFrom(MedSubject.class)) {
                root = model.getRootMedSubject().getAsGraphNode();
            }
        }

        public StateCompositeModel getComposite() {
            return null;
        }

        public void loadState(Preferences node) {
        }

        public void storeState(Preferences node) {
        }

        public void setTaxFocus(Taxon focus) {
            if (filter.getType().isAssignableFrom(Taxon.class)) {
                root = focus.getAsGraphNode();
            }
        }

        public VirtualGraphTreeNodeFilter getFilter() {
            return filter;
        }

        public String toString() {
            return type;
        }

        public GraphNode getRoot() {
            return root;
        }
    }
}
