package com.ethz.geobot.herbar.gui;

import ch.jfactory.lang.BooleanReference;
import ch.jfactory.model.graph.GraphNode;
import ch.jfactory.model.graph.GraphNodeList;
import com.ethz.geobot.herbar.model.HerbarModel;
import com.ethz.geobot.herbar.model.Taxon;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:08 $
 */
public class TransientDetailResultModel extends DetailResultModel
{
    private final Class typeToDisplay;

    public TransientDetailResultModel( final Class typeToDisplay, final String base, final Class halfRight, final HerbarModel model )
    {
        super( base, halfRight, model );
        this.typeToDisplay = typeToDisplay;
    }

    protected void initModel( final Taxon focus )
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
    }

    public void reset()
    {
    }
}
