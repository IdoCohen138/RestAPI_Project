package com.application.persistence;

import com.application.service.SlackChannel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@org.springframework.context.annotation.Configuration
public class HibernateUtil{
    private static final Log log = LogFactory.getLog(HibernateUtil.class);
    private static final SessionFactory sessionFactory;
    public static final ThreadLocal<Session> session;
    static String driverClass, connectionUrl, userName, password;

    public static Session currentSession() throws HibernateException {
        Session s = (Session)session.get();
        if (s == null) {
            s = sessionFactory.openSession();
            session.set(s);
        }
        return (Session)s;
    }

    public static void closeSession() throws HibernateException {
        Session s = (Session)session.get();
        session.set((Session) null);
        if (s != null) { s.close(); } }

    static {

        Properties properties = readProperties();

        properties.setProperty("hibernate.connection.driver_class", driverClass);
        properties.setProperty("hibernate.connection.url", connectionUrl);
        properties.setProperty("hibernate.connection.username", userName);
        properties.setProperty("hibernate.connection.password", password);

        Configuration configuration = new Configuration();

        configuration.setProperties(properties);

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties()).build();
        MetadataSources sources = new MetadataSources(serviceRegistry);
        sources.addAnnotatedClass(SlackChannel.class);
        Metadata metadata = sources.buildMetadata();

        try {
            sessionFactory = metadata.getSessionFactoryBuilder().build();
        } catch (Throwable var1) {
            log.error("Initial SessionFactory creation failed.", var1);
            throw new ExceptionInInitializerError(var1);
        }
        session = new ThreadLocal();
    }

    private static Properties readProperties() {
        InputStream inputStream = HibernateUtil.class.getClassLoader().getResourceAsStream("application.properties");
        Properties properties = new Properties();
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace(); }
        driverClass = properties.getProperty("spring.datasource.driver-class-name");
        connectionUrl = properties.getProperty("spring.datasource.url");
        userName = properties.getProperty("spring.datasource.username");
        password = properties.getProperty("spring.datasource.password");
        return properties;
    }
    public static SessionFactory getSessionFactory() {

        return sessionFactory;}

}

