package hu.fourdsoft.memorygame.dao;

import hu.fourdsoft.memorygame.common.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryDao {
	User findOneByUsernameAndPassword(String username, String password);
	Optional<User> findByUsername(String username);
}
