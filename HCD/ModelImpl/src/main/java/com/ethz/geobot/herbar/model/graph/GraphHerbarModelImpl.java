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
import com.ethz.geobot.herbar.model.trait.MorphologyText;
import com.ethz.geobot.herbar.model.trait.MorphologyValue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    public MorphologyValue[] getValues( final String name )
    {
        return getValues( AbsGraphModel.getModel().getRoot(), name );
    }

    private MorphologyValue[] getValues( final GraphNode sub, final String name )
    {
        final List<MorphologyValue> result = new ArrayList<MorphologyValue>();
        final GraphNodeList children = sub.getAllChildren( MorphologyValue.class );
        for ( int i = 0; i < children.size(); i++ )
        {
            final GraphNode child = children.get( i );
            if ( child.toString().equals( name ) )
            {
                result.add( (MorphologyValue) child );
            }
        }
        return result.toArray( new MorphologyValue[result.size()] );
    }

    public Taxon[] getTaxa( final MorphologyValue morphologyValue )
    {
        final List<GraphNode> taxa = new ArrayList<GraphNode>();
        final GraphNode morNode = (GraphNode) morphologyValue;
        final GraphNodeList texts = morNode.getChildren( MorphologyText.class );
        for ( int i = 0; i < texts.size(); i++ )
        {
            final GraphNode text = texts.get( i );
            final GraphNodeList values = text.getParents( Taxon.class );
            taxa.addAll( Arrays.asList( values.getAll() ) );
        }
        return taxa.toArray( new Taxon[taxa.size()] );
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
