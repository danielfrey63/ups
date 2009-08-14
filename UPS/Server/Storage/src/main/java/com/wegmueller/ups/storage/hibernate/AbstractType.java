package com.wegmueller.ups.storage.hibernate;

import java.io.Serializable;
import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;

/** Created by: Thomas Wegmueller Date: 26.09.2005,  23:13:44 */
public abstract class AbstractType implements UserType
{

    public Serializable disassemble(final Object value) throws HibernateException
    {
        return (Serializable) value;
    }

    public Object assemble(final Serializable cached, final Object owner) throws HibernateException
    {
        return cached;
    }

    public Object replace(final Object original, final Object target, final Object owner) throws HibernateException
    {
        return original;
    }

    public int hashCode(final Object x) throws HibernateException
    {
        return x.hashCode();
    }

}
