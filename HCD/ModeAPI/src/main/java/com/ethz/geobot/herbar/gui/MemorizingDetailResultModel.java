package com.ethz.geobot.herbar.gui;

import ch.jfactory.lang.ReferencableBool;
import ch.jfactory.model.graph.GraphNode;
import ch.jfactory.model.graph.GraphNodeList;
import com.ethz.geobot.herbar.model.HerbarModel;
import com.ethz.geobot.herbar.model.Taxon;
import java.util.HashMap;
import java.util.Map;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:08 $
 */
public class MemorizingDetailResultModel extends DetailResultModel {

    public static final int FALSE = 0;
    public static final int NEARBY = 1;
    public static final int CORRECT = 2;

    private Map answerTextsMap;
    private Map answerAttributesMap;
    private Map guessMap;
    private Map completes;

    public MemorizingDetailResultModel(Class typeToDisplay, Class halfRight, HerbarModel model) {
        super(typeToDisplay, halfRight, model);
        reset();
    }

    protected void initModel(Taxon focus) {
        answerTexts = (GraphNodeList) answerTextsMap.get(focus);
        answerAttributes = (GraphNodeList) answerAttributesMap.get(focus);
        guesses = (GraphNodeList) guessMap.get(focus);
        complete = (ReferencableBool) completes.get(focus);
        if (answerTexts == null) {
            GraphNode focusNode = focus.getAsGraphNode();
            complete = new ReferencableBool(false);
            guesses = new GraphNodeList();
            answerAttributes = new GraphNodeList();
            answerTexts = focusNode.getChildren(typeToDisplay);
            for (int i = 0; i < answerTexts.size(); i++) {
                answerAttributes.add(getAttribute(answerTexts.get(i)));
            }
            answerTextsMap.put(focus, answerTexts);
            answerAttributesMap.put(focus, answerAttributes);
            guessMap.put(focus, guesses);
            completes.put(focus, complete);
        }
    }

    public void reset() {
        super.reset();
        complete.setBool(false);
        answerTextsMap = new HashMap();
        answerAttributesMap = new HashMap();
        guessMap = new HashMap();
        completes = new HashMap();
    }

    public Map getAnswerTextsMap() {
        return answerTextsMap;
    }

    public Map getAnswerAttributesMap() {
        return answerAttributesMap;
    }

    public Map getGuessMap() {
        return guessMap;
    }

    public Map getCompletes() {
        return completes;
    }
}
