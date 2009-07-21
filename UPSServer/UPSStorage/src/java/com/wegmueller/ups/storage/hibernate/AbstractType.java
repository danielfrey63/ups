package com.wegmueller.ups.storage.hibernate;

import org.hibernate.usertype.UserType;
import org.hibernate.HibernateException;

import java.io.Serializable;

/**
 * Created by: Thomas Wegmueller
 * Date: 26.09.2005,  23:13:44
 */
public abstract class AbstractType implements UserType {

    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) value;
    }

    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return cached;
    }

    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }

    public int hashCode(Object x) throws HibernateException {
        return x.hashCode();
    }

}
