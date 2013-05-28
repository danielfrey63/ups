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
package com.ethz.geobot.herbar.model.graph;

import ch.jfactory.model.graph.AbsGraphModel;
import ch.jfactory.model.graph.GraphNode;
import ch.jfactory.model.graph.GraphNodeList;
import com.ethz.geobot.herbar.model.HerbarModel;
import com.ethz.geobot.herbar.model.Level;
import com.ethz.geobot.herbar.model.PictureTheme;
import com.ethz.geobot.herbar.model.Taxon;
import com.ethz.geobot.herbar.model.db.impl.LevelImpl;
import com.ethz.geobot.herbar.model.db.impl.PictureThemeImpl;
import com.ethz.geobot.herbar.model.event.ModelChangeListener;
import com.ethz.geobot.herbar.model.trait.Ecology;
import com.ethz.geobot.herbar.model.trait.Medicine;
import com.ethz.geobot.herbar.model.trait.Morphology;
import com.ethz.geobot.herbar.model.trait.Name;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:20 $
 */
public class GraphHerbarModelImpl implements HerbarModel
{
    private String name;

    public Level getRootLevel()
    {
        final GraphNode root = AbsGraphModel.getModel().getRoot();
        return (Level) root.getChildren( Level.class ).get( 0 );
    }

    public Level getLastLevel()
    {
        final GraphNode root = AbsGraphModel.getModel().getRoot();
        final Level level = (Level) root.getChildren( Level.class ).get( 0 );
        final Level[] levels = level.getSubLevels();
        return levels[levels.length - 1];
    }

    public Taxon getRootTaxon()
    {
        final GraphNode root = AbsGraphModel.getModel().getRoot();
        return (Taxon) root.getChildren( Taxon.class ).get( 0 );
    }

    public Morphology getMorphology()
    {
        final GraphNode root = AbsGraphModel.getModel().getRoot();
        return (Morphology) root.getChildren( Morphology.class ).get( 0 );
    }

    public Ecology getEcology()
    {
        final GraphNode root = AbsGraphModel.getModel().getRoot();
        return (Ecology) root.getChildren( Ecology.class ).get( 0 );
    }

    public Medicine getMedicine()
    {
        final GraphNode root = AbsGraphModel.getModel().getRoot();
        return (Medicine) root.getChildren( Medicine.class ).get( 0 );
    }

    public Name getSynonyms()
    {
        final GraphNode root = AbsGraphModel.getModel().getRoot();
        return (Name) root.getChildren( Name.class ).get( 0 );
    }

    public PictureTheme[] getPictureThemes()
    {
        final GraphNode root = AbsGraphModel.getModel().getRoot();
        final GraphNodeList list = root.getChildren( PictureTheme.class );
        return (PictureTheme[]) list.getAll( new PictureThemeImpl[0] );
    }

    public PictureTheme getPictureTheme( final String name )
    {
        final GraphNode root = AbsGraphModel.getModel().getRoot();
        final GraphNodeList list = root.getChildren( PictureTheme.class );
        for ( int i = 0; i < list.size(); i++ )
        {
            final GraphNode node = list.get( i );
            if ( name.equals( node.toString() ) )
            {
                return (PictureTheme) node;
            }
        }
        return null;
    }

    public Taxon getTaxon( final String name )
    {
        final GraphNode root = AbsGraphModel.getModel().getRoot();
        return getTaxon( root, name );
    }

    private Taxon getTaxon( final GraphNode node, final String name )
    {
        final GraphNodeList childTaxa = node.getChildren( Taxon.class );
        Taxon taxon = null;
        if ( node.toString().equals( name ) )
        {
            taxon = (Taxon) node;
        }
        for ( int i = 0; i < childTaxa.size() && taxon == null; i++ )
        {
            final GraphNode child = childTaxa.get( i );
            taxon = getTaxon( child, name );
        }
        return taxon;
    }

    public Level getLevel( final String name )
    {
        final GraphNode root = AbsGraphModel.getModel().getRoot();
        GraphNode node = root.getChildren( Level.class ).get( 0 );
        while ( node != null )
        {
            if ( node.toString().equals( name ) )
            {
                return (Level) node;
            }
            node = node.getChildren( Level.class ).get( 0 );
        }
        return null;
    }

    public Level[] getLevels()
    {
        final GraphNode root = AbsGraphModel.getModel().getRoot();
        GraphNodeList list = root.getChildren( Level.class );
        final GraphNodeList ret = new GraphNodeList();
        while ( list.size() > 0 )
        {
            final GraphNode node = list.get( 0 );
            ret.add( node );
            list = node.getChildren( Level.class );
        }
        return (Level[]) ret.getAll( new LevelImpl[0] );
    }

    public String getName()
    {
        return "GraphHerbarModel";
    }

    public void setName( final String name )
    {
        this.name = name;
    }

    public void addModelChangeListener( final ModelChangeListener listener )
    {
        throw new NoSuchMethodError( "ModelChangeListener not supported yet." );
    }

    public String toString()
    {
        return name;
    }
}
