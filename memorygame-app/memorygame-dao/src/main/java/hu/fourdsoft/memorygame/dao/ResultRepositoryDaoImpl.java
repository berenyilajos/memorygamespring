package hu.fourdsoft.memorygame.dao;

import hu.fourdsoft.memorygame.common.model.Result;
import hu.fourdsoft.memorygame.qualifier.MemorygameDb;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class ResultRepositoryDaoImpl implements ResultRepositoryDao {

    @Autowired
    @MemorygameDb
    private EntityManager em;

    @Override
    public List<Result> getResultsBetterOrEquals(long seconds) {
        String queryText = "SELECT r FROM Result r WHERE r.seconds <= :seconds";
        TypedQuery<Result> query = em.createQuery(queryText, Result.class);
        query.setParameter("seconds", seconds);
        return query.getResultList();
    }
}
