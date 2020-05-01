package hu.fourdsoft.memorygame.dao;

import hu.fourdsoft.memorygame.common.model.User;

public interface UserRepositoryDao {
    User getUserByName(String name);
}
