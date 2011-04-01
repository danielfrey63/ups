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

import ch.jfactory.lang.BooleanReference;
import ch.jfactory.model.graph.GraphNode;
import ch.jfactory.model.graph.GraphNodeList;
import com.ethz.geobot.herbar.model.HerbarModel;
import com.ethz.geobot.herbar.model.Taxon;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:08 $
 */
public abstract class DetailResultModel extends ResultModel
{
    private String typeToDisplay;

    protected Class halfRight;

    protected HerbarModel model;

    protected BooleanReference complete = new BooleanReference( false );

    protected GraphNodeList answerTexts;

    protected GraphNodeList answerAttributes;

    protected GraphNodeList guesses = new GraphNodeList();

    public DetailResultModel( final String typeToDisplay, final Class halfRight, final HerbarModel model )
    {
        this.typeToDisplay = typeToDisplay;
        this.halfRight = halfRight;
        this.model = model;
    }

    public HerbarModel getModel()
    {
        return model;
    }

    public String getTypeToDisplay()
    {
        return typeToDisplay;
    }

    protected GraphNode getAttribute( final GraphNode text )
    {
        final GraphNodeList attributes = new GraphNodeList();
        final GraphNodeList values = text.getParents();
        for ( int j = 0; j < values.size(); j++ )
        {
            attributes.addAll( values.get( j ).getParents( halfRight ) );
        }
        return attributes.get( 0 );
    }

    public void addGuess( final GraphNode guess )
    {
        // Register guess
        guesses.add( guess );
        // Check whether complete
        final List<GraphNode> copy = new ArrayList<GraphNode>( Arrays.asList( answerTexts.getAll() ) );
        for ( Iterator<GraphNode> iterator = copy.iterator(); iterator.hasNext(); )
        {
            if ( guesses.contains( iterator.next() ) )
            {
                iterator.remove();
            }
        }
        complete.setBool( copy.size() == 0 );
    }

    public boolean isComplete()
    {
        return complete.isTrue();
    }

    /**
     * Returns the list of all answers.
     *
     * @return a GraphNodeList with all answers
     */
    public GraphNodeList getGuesses()
    {
        return guesses;
    }

    /**
     * Returns the list of correct answers.
     *
     * @return a GraphNodeList with the correct answers
     */
    public GraphNodeList getAnswers()
    {
        return answerTexts;
    }

    public InterrogatorComplexityFactory.Type[] getSubModels()
    {
        final Enumeration e = subStateModels();
        final List<InterrogatorComplexityFactory.Type> list = new ArrayList<InterrogatorComplexityFactory.Type>();
        while ( e.hasMoreElements() )
        {
            final InterrogatorComplexityFactory.Type type = (InterrogatorComplexityFactory.Type) e.nextElement();
            list.add( type );
        }
        return list.toArray( new InterrogatorComplexityFactory.Type[list.size()] );
    }

    public int getCorrectness( final GraphNode guess )
    {
        // Return whether false, nearby or correct
        final GraphNode attribute = getAttribute( guess );
        return answerTexts.contains( guess ) ?
                MemorizingDetailResultModel.CORRECT :
                answerAttributes.contains( attribute ) ?
                        MemorizingDetailResultModel.NEARBY :
                        MemorizingDetailResultModel.FALSE;
    }

    public void setTaxFocus( final Taxon focus )
    {
        // Retrieve answers and guesses for focus assuming texts and attributes are synchronously null or initialized
        initModel( focus );
        // Inform sub models
        final Enumeration e = subStateModels();
        while ( e.hasMoreElements() )
        {
            final InterrogatorComplexityFactory.Type type = (InterrogatorComplexityFactory.Type) e.nextElement();
            type.setTaxFocus( focus );
        }
    }

    public void reset()
    {
        complete = new BooleanReference( false );
        answerTexts = new GraphNodeList();
        answerAttributes = new GraphNodeList();
        guesses = new GraphNodeList();
    }

    protected abstract void initModel( Taxon focus );
}
