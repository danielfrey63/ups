package com.ethz.geobot.herbar.gui;

import ch.jfactory.lang.BooleanReference;
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
public class MemorizingDetailResultModel extends DetailResultModel
{
    public static final int FALSE = 0;

    public static final int NEARBY = 1;

    public static final int CORRECT = 2;

    private Map<Taxon, GraphNodeList> answerTextsMap;

    private Map<Taxon, GraphNodeList> answerAttributesMap;

    private Map<Taxon, GraphNodeList> guessMap;

    private Map<Taxon, BooleanReference> completes;

    public MemorizingDetailResultModel( final Class typeToDisplay, final Class halfRight, final HerbarModel model )
    {
        super( typeToDisplay, halfRight, model );
        reset();
    }

    protected void initModel( final Taxon focus )
    {
        answerTexts = answerTextsMap.get( focus );
        answerAttributes = answerAttributesMap.get( focus );
        guesses = guessMap.get( focus );
        complete = completes.get( focus );
        if ( answerTexts == null )
        {
            final GraphNode focusNode = focus.getAsGraphNode();
            complete = new BooleanReference( false );
            guesses = new GraphNodeList();
            answerAttributes = new GraphNodeList();
            answerTexts = focusNode.getChildren( typeToDisplay );
            for ( int i = 0; i < answerTexts.size(); i++ )
            {
                answerAttributes.add( getAttribute( answerTexts.get( i ) ) );
            }
            answerTextsMap.put( focus, answerTexts );
            answerAttributesMap.put( focus, answerAttributes );
            guessMap.put( focus, guesses );
            completes.put( focus, complete );
        }
    }

    public void reset()
    {
        super.reset();
        complete.setBool( false );
        answerTextsMap = new HashMap<Taxon, GraphNodeList>();
        answerAttributesMap = new HashMap<Taxon, GraphNodeList>();
        guessMap = new HashMap<Taxon, GraphNodeList>();
        completes = new HashMap<Taxon, BooleanReference>();
    }
}
