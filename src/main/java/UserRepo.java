import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import java.util.List;
import java.util.Optional;

public class UserRepo {

    public void saveUser(User user) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void updateUser(User user) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // start a transaction
            transaction = session.beginTransaction();
            // save object
            session.update(user);
            // commit transaction
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public User getUser(String user_name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Criteria criteria = session.createCriteria(User.class);
            return (User) criteria.add(Restrictions.eq("name", user_name))
                    .uniqueResult();
        }
    }

    public Optional getUser(Long user_id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return (Optional) session.get(Optional.class, user_id);
        }
    }

    public List<User> getUser() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            if (session.createQuery("from User", User.class).list() == null) {
                return null;
            }
            return session.createQuery("from User", User.class).list();
        }
    }
}
