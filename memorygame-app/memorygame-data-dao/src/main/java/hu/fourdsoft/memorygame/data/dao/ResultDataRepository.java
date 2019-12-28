package hu.fourdsoft.memorygame.data.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import hu.fourdsoft.memorygame.common.data.model.ResultData;

import java.util.List;

public interface ResultDataRepository extends JpaRepository<ResultData, Long> {

	List<ResultData> findAllByOrderBySecondsAscResultDateDesc(Pageable pageable);

}
