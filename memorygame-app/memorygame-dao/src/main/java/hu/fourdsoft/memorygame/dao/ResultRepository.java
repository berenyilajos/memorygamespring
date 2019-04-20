package hu.fourdsoft.memorygame.dao;

import hu.fourdsoft.memorygame.common.model.Result;
import hu.fourdsoft.memorygame.common.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {

	List<Result> findByUserOrderBySecondsAsc(User user, Pageable pageable);

	List<Result> findAllByOrderBySecondsAscResultDateDesc(Pageable pageable);

}
