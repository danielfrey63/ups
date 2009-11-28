package com.ethz.geobot.herbar.model.graph;

import ch.jfactory.model.graph.AbsGraphModel;
import ch.jfactory.model.graph.GraphNode;
import ch.jfactory.model.graph.Role;
import ch.jfactory.model.graph.RoleFactory;
import com.ethz.geobot.herbar.model.origin.RoleAssignedImpl;
import com.ethz.geobot.herbar.model.origin.RoleInheritedImpl;
import com.ethz.geobot.herbar.model.origin.RoleUnitedImpl;
import com.ethz.geobot.herbar.model.relevance.marker.DifferentAssigned;
import com.ethz.geobot.herbar.model.relevance.marker.DifferentInherited;
import com.ethz.geobot.herbar.model.relevance.marker.DifferentUnited;
import com.ethz.geobot.herbar.model.relevance.marker.IdentifyingAssigned;
import com.ethz.geobot.herbar.model.relevance.marker.IdentifyingInherited;
import com.ethz.geobot.herbar.model.relevance.marker.IdentifyingUnited;
import com.ethz.geobot.herbar.model.relevance.marker.UniqueAssigned;
import com.ethz.geobot.herbar.model.relevance.marker.UniqueInherited;
import com.ethz.geobot.herbar.model.relevance.marker.UniqueUnited;
import com.ethz.geobot.herbar.model.relevance.marker.UnrelevantAssigned;
import com.ethz.geobot.herbar.model.relevance.marker.UnrelevantInherited;
import com.ethz.geobot.herbar.model.relevance.marker.UnrelevantUnited;
import com.ethz.geobot.herbar.model.relevance.marker.WeakAssigned;
import com.ethz.geobot.herbar.model.relevance.marker.WeakInherited;
import com.ethz.geobot.herbar.model.relevance.marker.WeakUnited;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:20 $
 */
public class RoleFactoryImpl implements RoleFactory
{
    private static final Logger LOG = Logger.getLogger( RoleFactoryImpl.class );

    private final Map nameRoleMapping = new HashMap();

    private final Map typeNameMapping = new HashMap();

    private final Map typeRoleMapping = new HashMap();

    private final Object[][] mapping = {
            {"INHERITED", RoleInheritedImpl.class},
            {"UNITED", RoleUnitedImpl.class},
            {"ASSIGNED", RoleAssignedImpl.class},
            {"DIFFERENT_INHERITED", DifferentInherited.class},
            {"DIFFERENT_UNITED", DifferentUnited.class},
            {"DIFFERENT_ASSIGNED", DifferentAssigned.class},
            {"IDENTIFYING_INHERITED", IdentifyingInherited.class},
            {"IDENTIFYING_UNITED", IdentifyingUnited.class},
            {"IDENTIFYING_ASSIGNED", IdentifyingAssigned.class},
            {"UNIQUE_INHERITED", UniqueInherited.class},
            {"UNIQUE_UNITED", UniqueUnited.class},
            {"UNIQUE_ASSIGNED", UniqueAssigned.class},
            {"WEAK_INHERITED", WeakInherited.class},
            {"WEAK_UNITED", WeakUnited.class},
            {"WEAK_ASSIGNED", WeakAssigned.class},
            {"UNRELEVANT_INHERITED", UnrelevantInherited.class},
            {"UNRELEVANT_UNITED", UnrelevantUnited.class},
            {"UNRELEVANT_ASSIGNED", UnrelevantAssigned.class}
    };

    public RoleFactoryImpl()
    {
        for ( final Object[] aMapping : mapping )
        {
            final Class clazz = (Class) aMapping[1];
            final Role role = (Role) AbsGraphModel.getTypeFactory().getInstance( clazz );
            final String name = (String) aMapping[0];
            role.setName( name );
            nameRoleMapping.put( name, role );
            typeNameMapping.put( clazz, name );
            typeRoleMapping.put( clazz, role );
            LOG.info( "RoleFactoryImpl role " + role + "[" + role.getId() + "]" );
        }
    }

    /**
     * Looks up the role instance in the internal mapping.<p> If none can be found, a new one is generated and the and a
     * warning is logged. The name of this unkonwn role is set to the types name converted to uppercase and separated
     * with underscores.<p> Note: The constructor does initialize all known roles.
     *
     * @see ch.jfactory.model.graph.RoleFactory#getRole(String)
     */
    public GraphNode getRole( final Class type )
    {
        GraphNode role = (GraphNode) typeRoleMapping.get( type );
        if ( role == null )
        {
            // This is a new not-configured role.
            // Make sure the role is saved if needed
            LOG.warn( "New role of type " + type.getName() + " created." );
            role = AbsGraphModel.getModel().createNode( null, type );
            String cName = (String) typeNameMapping.get( type );
            final String name = cName;
            if ( cName == null || cName.equals( "" ) )
            {
                final StringBuffer buffer = new StringBuffer();
                cName = type.getName();
                cName = cName.substring( cName.lastIndexOf( '.' ) + 1, cName.length() );
                for ( int i = 0; i < cName.length(); i++ )
                {
                    final String c = cName.substring( i, i + 1 );
                    if ( c.toUpperCase().equals( c ) && i > 0 )
                    {
                        buffer.append( "_" );
                        buffer.append( c );
                    }
                    else
                    {
                        buffer.append( c.toUpperCase() );
                    }
                }
                nameRoleMapping.put( buffer.toString(), role );
                typeNameMapping.put( cName, buffer.toString() );
                typeRoleMapping.put( cName, role );
                LOG.warn( "New role has generated name " + buffer );
            }
            role.setName( name );
        }
        return role;
    }

    public GraphNode getRole( final String type )
    {
        final GraphNode role = (GraphNode) nameRoleMapping.get( type );
        if ( role == null )
        {
            final String message = "Role for type " + type + " not known";
            final RuntimeException e = new IllegalStateException( message );
            LOG.fatal( message, e );
            throw e;
        }
        return role;
    }
}
