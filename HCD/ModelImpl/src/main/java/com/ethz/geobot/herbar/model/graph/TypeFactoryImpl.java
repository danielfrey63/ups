package com.ethz.geobot.herbar.model.graph;

import ch.jfactory.model.graph.GraphNode;
import ch.jfactory.model.graph.TypeFactory;
import com.ethz.geobot.herbar.model.db.impl.MutableEcologyAttributeImpl;
import com.ethz.geobot.herbar.model.db.impl.MutableEcologyImpl;
import com.ethz.geobot.herbar.model.db.impl.MutableEcologySubjectImpl;
import com.ethz.geobot.herbar.model.db.impl.MutableEcologyTextImpl;
import com.ethz.geobot.herbar.model.db.impl.MutableEcologyValueImpl;
import com.ethz.geobot.herbar.model.db.impl.MutableMedicineAttributeImpl;
import com.ethz.geobot.herbar.model.db.impl.MutableMedicineImpl;
import com.ethz.geobot.herbar.model.db.impl.MutableMedicineSubjectImpl;
import com.ethz.geobot.herbar.model.db.impl.MutableMedicineTextImpl;
import com.ethz.geobot.herbar.model.db.impl.MutableMedicineValueImpl;
import com.ethz.geobot.herbar.model.db.impl.MutableMorphologyAttributeImpl;
import com.ethz.geobot.herbar.model.db.impl.MutableMorphologyImpl;
import com.ethz.geobot.herbar.model.db.impl.MutableMorphologySubjectImpl;
import com.ethz.geobot.herbar.model.db.impl.MutableMorphologyValueImpl;
import com.ethz.geobot.herbar.model.db.impl.MutablePictureImpl;
import com.ethz.geobot.herbar.model.db.impl.MutablePictureTextImpl;
import com.ethz.geobot.herbar.model.db.impl.MutablePictureThemeImpl;
import com.ethz.geobot.herbar.model.db.impl.MutablePicturesImpl;
import com.ethz.geobot.herbar.model.db.impl.MutableRootImpl;
import com.ethz.geobot.herbar.model.db.impl.MutableTaxonImpl;
import com.ethz.geobot.herbar.model.db.impl.MutableTaxonLevelImpl;
import com.ethz.geobot.herbar.model.db.impl.MutableTaxonSynonymImpl;
import com.ethz.geobot.herbar.model.db.impl.MutableTextImpl;
import java.util.HashMap;
import java.util.Iterator;
import org.apache.log4j.Logger;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:20 $
 */
public class TypeFactoryImpl implements TypeFactory
{
    private static final Logger LOG = Logger.getLogger( TypeFactoryImpl.class );

    /**
     * Used for the TYPE field in the db.
     */
    private static final HashMap typeMapping = new HashMap();

    private static final HashMap implMapping = new HashMap();

    static
    {
        try
        {
            typeMapping.put( "ROOT", MutableRootImpl.class );
            typeMapping.put( "TAXON", MutableTaxonImpl.class );
            typeMapping.put( "TAXONLEVEL", MutableTaxonLevelImpl.class );
            typeMapping.put( "TAXONSYNONYM", MutableTaxonSynonymImpl.class );

            typeMapping.put( "MORPHOLOGY", MutableMorphologyImpl.class );
            typeMapping.put( "MORSUBJECT", MutableMorphologySubjectImpl.class );
            typeMapping.put( "MORATTRIBUTE", MutableMorphologyAttributeImpl.class );
            typeMapping.put( "MORVALUE", MutableMorphologyValueImpl.class );
            typeMapping.put( "MORTEXT", MutableTextImpl.class );

            typeMapping.put( "MEDICINE", MutableMedicineImpl.class );
            typeMapping.put( "MEDSUBJECT", MutableMedicineSubjectImpl.class );
            typeMapping.put( "MEDATTRIBUTE", MutableMedicineAttributeImpl.class );
            typeMapping.put( "MEDVALUE", MutableMedicineValueImpl.class );
            typeMapping.put( "MEDTEXT", MutableMedicineTextImpl.class );

            typeMapping.put( "ECOLOGY", MutableEcologyImpl.class );
            typeMapping.put( "ECOSUBJECT", MutableEcologySubjectImpl.class );
            typeMapping.put( "ECOATTRIBUTE", MutableEcologyAttributeImpl.class );
            typeMapping.put( "ECOVALUE", MutableEcologyValueImpl.class );
            typeMapping.put( "ECOTEXT", MutableEcologyTextImpl.class );

            typeMapping.put( "PICTURE", MutablePictureImpl.class );
            typeMapping.put( "PICTURES", MutablePicturesImpl.class );
            typeMapping.put( "PICTURETEXT", MutablePictureTextImpl.class );
            typeMapping.put( "PICTURETHEME", MutablePictureThemeImpl.class );
        }
        catch ( Exception e )
        {
            LOG.fatal( "Fatal error during initialization of model", e );
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
     * @see ch.jfactory.model.graph.TypeFactory#getInstance(Class)
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
                    LOG.fatal( "Mapping for " + type + " not found." );
                }
            }
            try
            {
                node = (GraphNode) impl.newInstance();
            }
            catch ( InstantiationException ex )
            {
                LOG.fatal( "Fatal error during creation of new node of type " + type, e );
            }
            catch ( IllegalAccessException ex )
            {
                LOG.fatal( "Fatal error during creation of new node of type " + type, e );
            }
        }
        catch ( IllegalAccessException e )
        {
            LOG.fatal( "fatal error during creation of new node of type " + type, e );
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

