package hu.fourdsoft.memorygame.dao;

import hu.fourdsoft.memorygame.common.model.Result;

import java.util.List;

public interface ResultRepositoryDao {

    List<Result> getResultsBetterOrEquals(long seconds);

}
