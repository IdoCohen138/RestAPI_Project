package com.application.persistence;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HiberanteUtil {
    private static final SessionFactory sessionFactory;
    public static final ThreadLocal<Session> session;

    public static Session currentSession() throws HibernateException {
        Session s = session.get();
        if (s == null) {
            s = sessionFactory.openSession();
            session.set(s);
        }
        return s;
    }

    public static void closeSession() throws HibernateException{
        Session s=session.get();
        session.set(null);
        if (s !=null){s.close();}
    }

    static {
        try {
            sessionFactory = (new Configuration()).configure().buildSessionFactory();
//            configuration.addClass(SlackChannelEntity.class);
//            configuration.addResource("SlackChannelEntity.hbm.xml");
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
        session=new ThreadLocal<>();
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }


}

