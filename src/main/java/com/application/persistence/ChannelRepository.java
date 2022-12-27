package com.application.persistence;

//import com.application.persistence.Entities.SlackChannelEntity;
import com.application.persistence.exceptions.ChannelAlreadyExitsInDataBaseException;
import com.application.persistence.exceptions.ChannelNotExitsInDataBaseException;
import com.application.service.SlackChannel;
import com.application.service.Repository;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.postgresql.util.PSQLException;
import org.springframework.stereotype.Service;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ChannelRepository implements Repository {

    @Override
    public void createChannel(SlackChannel newChannel) throws ChannelAlreadyExitsInDataBaseException {
                Session session = com.application.persistence.HiberanteUtil.currentSession();
                Transaction tx = session.beginTransaction();
                try {
                    session.save(newChannel);
                    tx.commit();
                }catch ( Exception e) {
                    if (tx != null) tx.rollback();
                    throw new ChannelAlreadyExitsInDataBaseException("This channel already exits in database");
                }
                finally {
                    HiberanteUtil.closeSession();
                }}
    @Override
    public void deleteChannel(UUID id) throws ChannelNotExitsInDataBaseException {
        Session session = com.application.persistence.HiberanteUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        try {
            session.delete(getChannel(id));
            tx.commit();
        }catch (IllegalArgumentException | ChannelNotExitsInDataBaseException e) {
            if (tx != null) tx.rollback();
            throw new ChannelNotExitsInDataBaseException("This channel not exits in the database");
        } finally {
            HiberanteUtil.closeSession();

        }

    }

    @Override
    public SlackChannel getChannel(UUID uuid) throws ChannelNotExitsInDataBaseException {
        Session session = com.application.persistence.HiberanteUtil.currentSession();
        Transaction tx = session.beginTransaction();
        SlackChannel slackChannel;
        try {
            slackChannel = session.get(SlackChannel.class, uuid);
            tx.commit();
        }catch ( Exception e) {
            if (tx != null) tx.rollback();
            throw new ChannelNotExitsInDataBaseException("This channel not exits in the database");
        }
        finally {
            HiberanteUtil.closeSession();
        }
        return slackChannel;
    }

    @Override
    public List<SlackChannel> getChannels(String filter) {
        Session session = com.application.persistence.HiberanteUtil.currentSession();
        Transaction tx = session.beginTransaction();
        List<SlackChannel> list = loadAllData(session,filter);
        tx.commit();
        HiberanteUtil.closeSession();
        return list ;}

    @Override
    public List<SlackChannel> getAllChannels() {
        Session session = com.application.persistence.HiberanteUtil.currentSession();
        Transaction tx = session.beginTransaction();
        List<SlackChannel> list = loadAllData(session,null);
        tx.commit();
        HiberanteUtil.closeSession();
        return list ;
    }
    private List<SlackChannel> loadAllData(Session session,String filter) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<SlackChannel> criteria = builder.createQuery(SlackChannel.class);
        Root<SlackChannel> rootEntry = criteria.from(SlackChannel.class);
        CriteriaQuery<SlackChannel> all;
        if(filter !=null)
            all= criteria.select(rootEntry).where(builder.equal(rootEntry.get("status"), filter));
        else
            all = criteria.select(rootEntry);
        TypedQuery<SlackChannel> allQuery = session.createQuery(all);
        return allQuery.getResultList();
    }

}
