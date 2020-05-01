package hu.fourdsoft.memorygame.dao;

import hu.fourdsoft.memorygame.common.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

public class UserRepositoryDaoImpl implements UserRepositoryDao {

    @Autowired
    @Qualifier("memorygameEntityManagerFactory")
    private EntityManager em;

    @Override
    public User getUserByName(String name) {
        String queryText = "SELECT u FROM User u WHERE u.username = :name";
        TypedQuery<User> query = em.createQuery(queryText, User.class);
        query.setParameter("name", name);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
