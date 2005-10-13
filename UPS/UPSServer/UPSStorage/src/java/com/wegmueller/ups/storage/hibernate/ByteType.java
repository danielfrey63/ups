package com.wegmueller.ups.storage.hibernate;

import org.hibernate.usertype.UserType;
import org.hibernate.HibernateException;

import java.sql.Types;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.io.Serializable;

import com.wegmueller.ups.storage.hibernate.AbstractType;
import com.wegmueller.ups.storage.beans.ByteTypeContent;

/**
 * Created by: Thomas Wegmueller
 * Date: 26.09.2005,  23:13:58
 */
public class ByteType extends AbstractType implements UserType {
    public int[] sqlTypes() {
        return new int[]{Types.BLOB};  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Class returnedClass() {
        return ByteTypeContent.class;
    }

    public boolean equals(Object x, Object y) throws HibernateException {
        if (x==y) return true;
        if ((x==null) && (y==null)) return true;
        if ((x==null) && (y!=null)) return false;
        if ((x!=null) && (y==null)) return false;
        if ((x instanceof ByteTypeContent) && (y instanceof ByteTypeContent)) {
            ByteTypeContent xi = (ByteTypeContent) x;
            ByteTypeContent yi = (ByteTypeContent) x;
            return xi.equals(yi);
        }
        return false;
    }

    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) value;
    }

    public Object nullSafeGet(ResultSet rs, String[] names, Object owner) throws HibernateException, SQLException {
        if (rs.getBytes(names[0])==null) return new ByteTypeContent();
        return new ByteTypeContent(rs.getBytes(names[0]));
    }

    public void nullSafeSet(PreparedStatement st, Object value, int index) throws HibernateException, SQLException {
        if (value==null) {
            st.setNull(index,sqlTypes()[0]);
        } else {
            ByteTypeContent val = (ByteTypeContent) value;
            byte[] bytes =null;
            if ((val!=null) && (val.length()>0)) {
                bytes = val.getBytes();
            }
            if (bytes==null) {
                st.setNull(index,sqlTypes()[0]);
            } else if (bytes.length==0) {
                st.setNull(index,sqlTypes()[0]);
            } else {
                st.setBytes(index,bytes);
            }
        }
    }

    public Object deepCopy(Object value) throws HibernateException {
        if (value==null) return null;
        if (value instanceof ByteTypeContent) {
            return new ByteTypeContent(((ByteTypeContent)value).getBytes());
        }
        if (value instanceof byte[]) {
            return new ByteTypeContent((byte[])value);
        }
        return null;
    }

    public boolean isMutable() {
        return true;
    }
}
