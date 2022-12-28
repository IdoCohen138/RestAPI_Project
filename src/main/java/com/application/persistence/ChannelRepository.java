package com.application.persistence;

import com.application.persistence.exceptions.ChannelAlreadyExitsInDataBaseException;
import com.application.persistence.exceptions.ChannelNotExitsInDataBaseException;
import com.application.service.EnumStatus;
import com.application.service.SlackChannel;
import com.application.service.Repository;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.UUID;

@Service
public class ChannelRepository implements Repository {

    @Override
    public void createChannel(SlackChannel newChannel) throws ChannelAlreadyExitsInDataBaseException {
                Session session = com.application.persistence.HibernateUtil.currentSession();
                Transaction tx = session.beginTransaction();
                try {
                    session.save(newChannel);
                    tx.commit();
                }catch ( Exception e) {
                    if (tx != null) tx.rollback();
                    throw new ChannelAlreadyExitsInDataBaseException("This channel already exits in database");
                }
                finally {
                    HibernateUtil.closeSession();
                }}
    @Override
    public SlackChannel deleteChannel(UUID id) throws ChannelNotExitsInDataBaseException {
        Session session = com.application.persistence.HibernateUtil.currentSession();
        Transaction tx = session.beginTransaction();
        SlackChannel slackChannel;
        try {
            slackChannel = session.get(SlackChannel.class, id);
            session.delete(slackChannel);
//            slackChannel= (SlackChannel) session.getDelegate();
            tx.commit();
        }catch (IllegalArgumentException  e) {
            if (tx != null) tx.rollback();
            throw new ChannelNotExitsInDataBaseException("This channel not exits in the database");
        } finally {
            HibernateUtil.closeSession();

        }
        return slackChannel;
    }

    @Override
    public SlackChannel getChannel(UUID uuid) throws ChannelNotExitsInDataBaseException {
        Session session = com.application.persistence.HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        SlackChannel slackChannel=null;
        try {
            slackChannel = session.get(SlackChannel.class, uuid);
            if(slackChannel==null){
                throw new ChannelNotExitsInDataBaseException("This channel not exits in the database");
            }
            tx.commit();
        }catch ( ObjectNotFoundException e) {
            if (tx != null) tx.rollback();
            throw new ChannelNotExitsInDataBaseException("This channel not exits in the database");
        }
        finally {
            HibernateUtil.closeSession();
        }
        return slackChannel;
    }

    @Override
    public List<SlackChannel> getChannels(EnumStatus filter) {
        Session session = com.application.persistence.HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        List<SlackChannel> list = loadAllData(session,filter);
        tx.commit();
        HibernateUtil.closeSession();
        return list ;}

    @Override
    public List<SlackChannel> getAllChannels() {
        Session session = com.application.persistence.HibernateUtil.currentSession();
        Transaction tx = session.beginTransaction();
        List<SlackChannel> list = loadAllData(session,null);
        tx.commit();
        HibernateUtil.closeSession();
        return list ;
    }
    private List<SlackChannel> loadAllData(Session session,EnumStatus filter) {
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

    @Override
    public SlackChannel updateChannel(UUID uuid,EnumStatus status) throws ChannelNotExitsInDataBaseException {
        Session session = com.application.persistence.HibernateUtil.currentSession();
        Transaction tx = session.beginTransaction();
        SlackChannel slackChannel;
        try {
            slackChannel = session.get(SlackChannel.class, uuid);
            slackChannel.setStatus(status);
            session.update(slackChannel);
            tx.commit();
        }catch ( Exception e) {
            if (tx != null) tx.rollback();
            throw new ChannelNotExitsInDataBaseException("This channel not exits in the database");
        }
        finally {
            HibernateUtil.closeSession();
        }
        return slackChannel;
    }


}
