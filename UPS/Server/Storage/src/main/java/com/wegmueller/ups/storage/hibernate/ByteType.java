package com.wegmueller.ups.storage.hibernate;

import com.wegmueller.ups.storage.beans.ByteTypeContent;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;

/**
 * Created by: Thomas Wegmueller Date: 26.09.2005,  23:13:58
 */
public class ByteType extends AbstractType implements UserType
{
    public int[] sqlTypes()
    {
        return new int[]{Types.BLOB};
    }

    public Class returnedClass()
    {
        return ByteTypeContent.class;
    }

    public boolean equals( final Object x, final Object y ) throws HibernateException
    {
        if ( x == y )
        {
            return true;
        }
        if ( ( x == null ) && ( y == null ) )
        {
            return true;
        }
        if ( ( x == null ) && ( y != null ) )
        {
            return false;
        }
        if ( ( x != null ) && ( y == null ) )
        {
            return false;
        }
        if ( ( x instanceof ByteTypeContent ) && ( y instanceof ByteTypeContent ) )
        {
            final ByteTypeContent xi = (ByteTypeContent) x;
            final ByteTypeContent yi = (ByteTypeContent) y;
            return xi.equals( yi );
        }
        return false;
    }

    public Serializable disassemble( final Object value ) throws HibernateException
    {
        return (Serializable) value;
    }

    public Object nullSafeGet( final ResultSet rs, final String[] names, final Object owner ) throws HibernateException, SQLException
    {
        if ( rs.getBytes( names[0] ) == null )
        {
            return new ByteTypeContent();
        }
        return new ByteTypeContent( rs.getBytes( names[0] ) );
    }

    public void nullSafeSet( final PreparedStatement st, final Object value, final int index ) throws HibernateException, SQLException
    {
        if ( value == null )
        {
            st.setNull( index, sqlTypes()[0] );
        }
        else
        {
            final ByteTypeContent val = (ByteTypeContent) value;
            byte[] bytes = null;
            if ( ( val != null ) && ( val.length() > 0 ) )
            {
                bytes = val.getBytes();
            }
            if ( bytes == null )
            {
                st.setNull( index, sqlTypes()[0] );
            }
            else if ( bytes.length == 0 )
            {
                st.setNull( index, sqlTypes()[0] );
            }
            else
            {
                st.setBytes( index, bytes );
            }
        }
    }

    public Object deepCopy( final Object value ) throws HibernateException
    {
        if ( value == null )
        {
            return null;
        }
        if ( value instanceof ByteTypeContent )
        {
            return new ByteTypeContent( ( (ByteTypeContent) value ).getBytes() );
        }
        if ( value instanceof byte[] )
        {
            return new ByteTypeContent( (byte[]) value );
        }
        return null;
    }

    public boolean isMutable()
    {
        return true;
    }
}
