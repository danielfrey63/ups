package com.ethz.geobot.herbar.model.graph;

import ch.jfactory.model.graph.AbsGraphModel;
import ch.jfactory.model.graph.GraphNode;
import ch.jfactory.model.graph.GraphNodeList;
import com.ethz.geobot.herbar.model.EcoSubject;
import com.ethz.geobot.herbar.model.HerbarModel;
import com.ethz.geobot.herbar.model.Level;
import com.ethz.geobot.herbar.model.MedSubject;
import com.ethz.geobot.herbar.model.MorSubject;
import com.ethz.geobot.herbar.model.MorText;
import com.ethz.geobot.herbar.model.MorValue;
import com.ethz.geobot.herbar.model.PictureTheme;
import com.ethz.geobot.herbar.model.Taxon;
import com.ethz.geobot.herbar.model.db.impl.MutablePictureThemeImpl;
import com.ethz.geobot.herbar.model.db.impl.MutableTaxonLevelImpl;
import com.ethz.geobot.herbar.model.event.ModelChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:20 $
 */
public class GraphHerbarModelImpl implements HerbarModel
{
    private boolean readOnly;

    private String name;

    /**
     * @see com.ethz.geobot.herbar.model.HerbarModel#getRootLevel()
     */
    public Level getRootLevel()
    {
        final GraphNode root = AbsGraphModel.getModel().getRoot();
        return (Level) root.getChildren( Level.class ).get( 0 );
    }

    /**
     * @see com.ethz.geobot.herbar.model.HerbarModel#getLastLevel()
     */
    public Level getLastLevel()
    {
        final GraphNode root = AbsGraphModel.getModel().getRoot();
        final Level level = (Level) root.getChildren( Level.class ).get( 0 );
        final Level[] levels = level.getSubLevels();
        return levels[levels.length - 1];
    }

    /**
     * @see com.ethz.geobot.herbar.model.HerbarModel#getRootTaxon()
     */
    public Taxon getRootTaxon()
    {
        final GraphNode root = AbsGraphModel.getModel().getRoot();
        return (Taxon) root.getChildren( Taxon.class ).get( 0 );
    }

    /**
     * @see com.ethz.geobot.herbar.model.HerbarModel#getRootMorSubject()
     */
    public MorSubject getRootMorSubject()
    {
        final GraphNode root = AbsGraphModel.getModel().getRoot();
        return (MorSubject) root.getChildren( MorSubject.class ).get( 0 );
    }

    public EcoSubject getRootEcoSubject()
    {
        final GraphNode root = AbsGraphModel.getModel().getRoot();
        return (EcoSubject) root.getChildren( EcoSubject.class ).get( 0 );
    }

    public MedSubject getRootMedSubject()
    {
        final GraphNode root = AbsGraphModel.getModel().getRoot();
        return (MedSubject) root.getChildren( MedSubject.class ).get( 0 );
    }

    /**
     * @see com.ethz.geobot.herbar.model.HerbarModel#getPictureThemes()
     */
    public PictureTheme[] getPictureThemes()
    {
        final GraphNode root = AbsGraphModel.getModel().getRoot();
        final GraphNodeList list = root.getChildren( PictureTheme.class );
        return (PictureTheme[]) list.getAll( new MutablePictureThemeImpl[0] );
    }

    /**
     * @see com.ethz.geobot.herbar.model.HerbarModel#getPictureTheme(String)
     */
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

    /**
     * @see com.ethz.geobot.herbar.model.HerbarModel#getValues(String)
     */
    public MorValue[] getValues( final String name )
    {
        return getValues( AbsGraphModel.getModel().getRoot(), name );
    }

    private MorValue[] getValues( final GraphNode sub, final String name )
    {
        final List result = new ArrayList();
        final GraphNodeList children = sub.getAllChildren( MorValue.class );
        for ( int i = 0; i < children.size(); i++ )
        {
            final GraphNode child = children.get( i );
            if ( child.toString().equals( name ) )
            {
                result.add( child );
            }
        }
        return (MorValue[]) result.toArray( new MorValue[0] );
    }

    /**
     * @see com.ethz.geobot.herbar.model.HerbarModel#getTaxa(MorValue)
     */
    public Taxon[] getTaxa( final MorValue mor )
    {
        final List taxa = new ArrayList();
        final GraphNode morNode = (GraphNode) mor;
        final GraphNodeList texts = morNode.getChildren( MorText.class );
        for ( int i = 0; i < texts.size(); i++ )
        {
            final GraphNode text = texts.get( i );
            final GraphNodeList values = text.getParents( Taxon.class );
            taxa.addAll( Arrays.asList( values.getAll() ) );
        }
        return (Taxon[]) taxa.toArray( new Taxon[0] );
    }

    /**
     * @see com.ethz.geobot.herbar.model.HerbarModel#getTaxon(String)
     */
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

    /**
     * @see com.ethz.geobot.herbar.model.HerbarModel#getLevel(String)
     */
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

    /**
     * @see com.ethz.geobot.herbar.model.HerbarModel#getLevels()
     */
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
        return (Level[]) ret.getAll( new MutableTaxonLevelImpl[0] );
    }

    /**
     * @see com.ethz.geobot.herbar.model.HerbarModel#getName()
     */
    public String getName()
    {
        return "GraphHerbarModel";
    }

    public void setName( final String name )
    {
        this.name = name;
    }

    /**
     * @see com.ethz.geobot.herbar.model.HerbarModel #addModelChangeListener(ModelChangeListener)
     */
    public void addModelChangeListener( final ModelChangeListener listener )
    {
        throw new NoSuchMethodError( "ModelChangeListener not supported yet." );
    }

    /**
     * @see com.ethz.geobot.herbar.model.HerbarModel #removeModelChangeListener(ModelChangeListener)
     */
    public void removeModelChangeListener( final ModelChangeListener listener )
    {
        throw new NoSuchMethodError( "ModelChangeListener not supported yet." );
    }

    public void setReadOnly()
    {
        AbsGraphModel.getModel().setReadOnly();
    }

    public String toString()
    {
        return name;
    }
}
