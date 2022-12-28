package com.application.persistence;

import com.application.persistence.exceptions.ChannelAlreadyExitsInDataBaseException;
import com.application.persistence.exceptions.ChannelNotExitsInDataBaseException;
import com.application.service.EnumStatus;
import com.application.service.Repository;
import com.application.service.SlackChannel;
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
    Session session;
    Transaction transaction;

    @Override
    public void createChannel(SlackChannel newChannel) throws ChannelAlreadyExitsInDataBaseException {
        session = com.application.persistence.HibernateUtil.currentSession();
        transaction = session.beginTransaction();
        try {
            session.save(newChannel);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new ChannelAlreadyExitsInDataBaseException("This channel already exits in database");
        } finally {
            HibernateUtil.closeSession();
        }
    }

    @Override
    public SlackChannel deleteChannel(UUID id) throws ChannelNotExitsInDataBaseException {
        session = com.application.persistence.HibernateUtil.currentSession();
        transaction = session.beginTransaction();
        SlackChannel slackChannel;
        try {
            slackChannel = session.get(SlackChannel.class, id);
            session.delete(slackChannel);
            transaction.commit();
        } catch (IllegalArgumentException e) {
            if (transaction != null) transaction.rollback();
            throw new ChannelNotExitsInDataBaseException("This channel not exits in the database");
        } finally {
            HibernateUtil.closeSession();
        }
        return slackChannel;
    }

    @Override
    public SlackChannel getChannel(UUID uuid) throws ChannelNotExitsInDataBaseException {
        session = com.application.persistence.HibernateUtil.currentSession();
        transaction = session.beginTransaction();
        SlackChannel slackChannel;
        try {
            slackChannel = session.get(SlackChannel.class, uuid);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new ChannelNotExitsInDataBaseException("This channel not exits in the database");
        } finally {
            HibernateUtil.closeSession();
        }
        return slackChannel;
    }

    @Override
    public List<SlackChannel> getChannels(EnumStatus filter) {
        session = com.application.persistence.HibernateUtil.currentSession();
        transaction = session.beginTransaction();
        List<SlackChannel> list = loadAllData(session, filter);
        transaction.commit();
        HibernateUtil.closeSession();
        return list;
    }

    @Override
    public List<SlackChannel> getAllChannels() {
        session = com.application.persistence.HibernateUtil.currentSession();
        transaction = session.beginTransaction();
        List<SlackChannel> list = loadAllData(session, null);
        transaction.commit();
        HibernateUtil.closeSession();
        return list;
    }

    private List<SlackChannel> loadAllData(Session session, EnumStatus filter) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<SlackChannel> criteria = builder.createQuery(SlackChannel.class);
        Root<SlackChannel> rootEntry = criteria.from(SlackChannel.class);
        CriteriaQuery<SlackChannel> all;
        if (filter != null) all = criteria.select(rootEntry).where(builder.equal(rootEntry.get("status"), filter));
        else all = criteria.select(rootEntry);
        TypedQuery<SlackChannel> allQuery = session.createQuery(all);
        return allQuery.getResultList();
    }

    @Override
    public SlackChannel updateChannel(UUID uuid, EnumStatus status) throws ChannelNotExitsInDataBaseException {
        session = com.application.persistence.HibernateUtil.currentSession();
        transaction = session.beginTransaction();
        SlackChannel slackChannel;
        try {
            slackChannel = session.get(SlackChannel.class, uuid);
            slackChannel.setStatus(status);
            session.update(slackChannel);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new ChannelNotExitsInDataBaseException("This channel not exits in the database");
        } finally {
            HibernateUtil.closeSession();
        }
        return slackChannel;
    }


}
