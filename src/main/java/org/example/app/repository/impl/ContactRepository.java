package org.example.app.repository.impl;

import org.example.app.config.HibernateUtil;
import org.example.app.domain.contact.Contact;
import org.example.app.repository.AppRepository;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;

import java.util.logging.Logger;

public class ContactRepository implements AppRepository<Contact> {

    private static final Logger LOGGER = Logger.getLogger(ContactRepository.class.getName());

    @Override
    public void create(Contact contact) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(contact);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOGGER.log(Level.WARNING, e.getMessage(), e);
        }
    }

    @Override
    public Optional<List<Contact>> fetchAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Contact> query = session.createQuery("FROM Contact", Contact.class);
            List<Contact> list = query.list();
            return Optional.of(list);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Contact> fetchById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Contact contact = session.get(Contact.class, id);
            return Optional.ofNullable(contact);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Override
    public void update(Long id, Contact contact) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Contact existingContact = session.get(Contact.class, id);
            if (existingContact != null) {
                existingContact.setName(contact.getName());
                existingContact.setPhone(contact.getPhone());
                session.merge(existingContact);
                transaction.commit();
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOGGER.log(Level.WARNING, e.getMessage(), e);
        }
    }

    @Override
    public void delete(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Contact contact = session.get(Contact.class, id);
            if (contact != null) {
                session.remove(contact); // Use remove instead of delete
                transaction.commit();
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOGGER.log(Level.WARNING, e.getMessage(), e);
        }
    }

    public boolean isIdExists(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Contact.class, id) != null;
        }
    }

    public Optional<Contact> getLastEntity() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Contact> query = session.createQuery("FROM Contact ORDER BY id DESC", Contact.class);
            query.setMaxResults(1);
            return Optional.ofNullable(query.uniqueResult());
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, e.getMessage(), e);
            return Optional.empty();
        }
    }
}
