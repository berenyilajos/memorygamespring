package hu.fourdsoft.memorygame.dao;

import hu.fourdsoft.memorygame.common.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	User findOneByUsernameAndPassword(String username, String password);
	Optional<User> findByUsername(String username);
}
