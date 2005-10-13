package com.wegmueller.ups.storage.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.HibernateException;
import org.hibernate.cfg.Configuration;
import org.apache.log4j.Logger;

import java.sql.SQLException;

/**
 * Created by: Thomas Wegmueller
 * Date: 26.09.2005,  20:41:33
 */
public class HibernateUtil {
    public static final SessionFactory sessionFactory;
    //public static final ThreadLocal session = new ThreadLocal();

    private static final Logger log = Logger.getLogger(HibernateUtil.class);
    private static Configuration cfg;
    private static final String[] HBM = {"upsstorage"};

    static {
        try {
            if (log.isDebugEnabled()) log.debug("HibernateUtil...init ");
            // Create the SessionFactory from hibernate.cfg.xml
            cfg = new Configuration();

            //String p = System.getProperty("storage.db.location", "database");
            /*
            if (p!=null) {
                //cfg.setProperty("hibernate.connection.url","jdbc:hsqldb:"+p);
                if (log.isDebugEnabled()) log.debug("HibernateUtil...setting Database to "+p);
            } else {
                if (log.isDebugEnabled()) log.debug("HibernateUtil...leaving Database, no properties");

            }
            */
            for (int i = 0; i < HBM.length; i++) {
                cfg.addInputStream(HibernateUtil.class.getResourceAsStream("/" + HBM[i] + ".hbm.xml"));
            }

            cfg.configure();
            sessionFactory = cfg.buildSessionFactory();
        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }


    public static Session newSession() {
        Session s = sessionFactory.openSession();
        try {
            s.connection().setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //session.set(s);
        return s;
    }


}
