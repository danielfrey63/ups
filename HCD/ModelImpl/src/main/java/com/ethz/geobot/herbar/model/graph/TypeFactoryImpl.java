package com.ethz.geobot.herbar.model.graph;

import ch.jfactory.model.graph.GraphNode;
import ch.jfactory.model.graph.TypeFactory;
import com.ethz.geobot.herbar.model.db.impl.EcologyAttributeImpl;
import com.ethz.geobot.herbar.model.db.impl.EcologyImpl;
import com.ethz.geobot.herbar.model.db.impl.EcologySubjectImpl;
import com.ethz.geobot.herbar.model.db.impl.EcologyTextImpl;
import com.ethz.geobot.herbar.model.db.impl.EcologyValueImpl;
import com.ethz.geobot.herbar.model.db.impl.LevelImpl;
import com.ethz.geobot.herbar.model.db.impl.MedicineAttributeImpl;
import com.ethz.geobot.herbar.model.db.impl.MedicineSubjectImpl;
import com.ethz.geobot.herbar.model.db.impl.MorphologyImpl;
import com.ethz.geobot.herbar.model.db.impl.MorphologySubjectImpl;
import com.ethz.geobot.herbar.model.db.impl.MorphologyValueImpl;
import com.ethz.geobot.herbar.model.db.impl.MedicineImpl;
import com.ethz.geobot.herbar.model.db.impl.MedicineTextImpl;
import com.ethz.geobot.herbar.model.db.impl.MedicineValueImpl;
import com.ethz.geobot.herbar.model.db.impl.MorphologyAttributeImpl;
import com.ethz.geobot.herbar.model.db.impl.PictureImpl;
import com.ethz.geobot.herbar.model.db.impl.PictureTextImpl;
import com.ethz.geobot.herbar.model.db.impl.PictureThemeImpl;
import com.ethz.geobot.herbar.model.db.impl.PicturesImpl;
import com.ethz.geobot.herbar.model.db.impl.RootImpl;
import com.ethz.geobot.herbar.model.db.impl.TaxonImpl;
import com.ethz.geobot.herbar.model.db.impl.TaxonSynonymImpl;
import com.ethz.geobot.herbar.model.db.impl.TextImpl;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:20 $
 */
public class TypeFactoryImpl implements TypeFactory
{
    private static final Logger LOG = LoggerFactory.getLogger( TypeFactoryImpl.class );

    /**
     * Used for the TYPE field in the db.
     */
    private static final HashMap typeMapping = new HashMap();

    private static final HashMap implMapping = new HashMap();

    static
    {
        try
        {
            typeMapping.put( "ROOT", RootImpl.class );
            typeMapping.put( "TAXON", TaxonImpl.class );
            typeMapping.put( "TAXONLEVEL", LevelImpl.class );
            typeMapping.put( "TAXONSYNONYM", TaxonSynonymImpl.class );

            typeMapping.put( "MORPHOLOGY", MorphologyImpl.class );
            typeMapping.put( "MORSUBJECT", MorphologySubjectImpl.class );
            typeMapping.put( "MORATTRIBUTE", MorphologyAttributeImpl.class );
            typeMapping.put( "MORVALUE", MorphologyValueImpl.class );
            typeMapping.put( "MORTEXT", TextImpl.class );

            typeMapping.put( "MEDICINE", MedicineImpl.class );
            typeMapping.put( "MEDSUBJECT", MedicineSubjectImpl.class );
            typeMapping.put( "MEDATTRIBUTE", MedicineAttributeImpl.class );
            typeMapping.put( "MEDVALUE", MedicineValueImpl.class );
            typeMapping.put( "MEDTEXT", MedicineTextImpl.class );

            typeMapping.put( "ECOLOGY", EcologyImpl.class );
            typeMapping.put( "ECOSUBJECT", EcologySubjectImpl.class );
            typeMapping.put( "ECOATTRIBUTE", EcologyAttributeImpl.class );
            typeMapping.put( "ECOVALUE", EcologyValueImpl.class );
            typeMapping.put( "ECOTEXT", EcologyTextImpl.class );

            typeMapping.put( "PICTURE", PictureImpl.class );
            typeMapping.put( "PICTURES", PicturesImpl.class );
            typeMapping.put( "PICTURETEXT", PictureTextImpl.class );
            typeMapping.put( "PICTURETHEME", PictureThemeImpl.class );
        }
        catch ( Exception e )
        {
            LOG.error( "Fatal error during initialization of model", e );
        }
    }

    public GraphNode getInstance( final String type )
    {
        final Class clazz = (Class) typeMapping.get( type );
        if ( clazz == null )
        {
            throw new IllegalStateException( "Class for type " + type + " not found" );
        }
        return getInstance( clazz );
    }

    /**
     * @see TypeFactory#getInstance(Class)
     */
    public GraphNode getInstance( final Class type )
    {
        GraphNode node = null;
        try
        {
            node = (GraphNode) type.newInstance();
        }
        catch ( InstantiationException e )
        {
            // Most propably an interface was about to instantiate, so screen
            // for appropriate implementation.
            Class impl = (Class) implMapping.get( type );
            if ( impl == null )
            {
                for ( final Object o : typeMapping.values() )
                {
                    final Class clazz = (Class) o;
                    if ( type.isAssignableFrom( clazz ) )
                    {
                        impl = clazz;
                        implMapping.put( type, clazz );
                    }
                }
                // Appropriate mapping has not been found.
                if ( impl == null )
                {
                    LOG.error( "Mapping for " + type + " not found." );
                }
            }
            try
            {
                node = (GraphNode) impl.newInstance();
            }
            catch ( InstantiationException ex )
            {
                LOG.error( "Fatal error during creation of new node of type " + type, e );
            }
            catch ( IllegalAccessException ex )
            {
                LOG.error( "Fatal error during creation of new node of type " + type, e );
            }
        }
        catch ( IllegalAccessException e )
        {
            LOG.error( "fatal error during creation of new node of type " + type, e );
        }
        return node;
    }

    public String getType( final GraphNode node )
    {
        final Class clazz = node.getClass();
        for ( final Object o : typeMapping.keySet() )
        {
            final String key = (String) o;
            final Class claz = (Class) typeMapping.get( key );
            if ( claz == clazz )
            {
                return key;
            }
        }
        throw new IllegalStateException( "type not found for " + node + " " + node.getClass().getName() );
    }
}

