package com.ethz.geobot.herbar.gui;

import ch.jfactory.lang.ReferencableBool;
import ch.jfactory.model.graph.GraphNode;
import ch.jfactory.model.graph.GraphNodeList;
import com.ethz.geobot.herbar.model.HerbarModel;
import com.ethz.geobot.herbar.model.Taxon;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:08 $
 */
public class TransientDetailResultModel extends DetailResultModel {

    public TransientDetailResultModel(Class typeToDisplay, Class halfRight, HerbarModel model) {
        super(typeToDisplay, halfRight, model);
    }

    protected void initModel(Taxon focus) {
        GraphNode focusNode = focus.getAsGraphNode();
        complete = new ReferencableBool(false);
        guesses = new GraphNodeList();
        answerAttributes = new GraphNodeList();
        answerTexts = focusNode.getChildren(typeToDisplay);
        for (int i = 0; i < answerTexts.size(); i++) {
            answerAttributes.add(getAttribute(answerTexts.get(i)));
        }
    }

    public void reset() {
    }
}
