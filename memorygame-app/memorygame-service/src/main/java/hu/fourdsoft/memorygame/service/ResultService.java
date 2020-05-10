package hu.fourdsoft.memorygame.service;

import hu.fourdsoft.memorygame.common.data.model.ResultData;
import hu.fourdsoft.memorygame.common.dto.ResultDTO;
import hu.fourdsoft.memorygame.common.dto.UserDTO;
import hu.fourdsoft.memorygame.common.dto.helper.DtoHelper;
import hu.fourdsoft.memorygame.common.model.Result;
import hu.fourdsoft.memorygame.common.model.User;
import hu.fourdsoft.memorygame.dao.ResultRepository;

import hu.fourdsoft.memorygame.data.dao.ResultDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
public class ResultService {
    @Autowired
    private ResultRepository resultRepository;

	@Autowired
	private ResultDataRepository resultDataRepository;

    public List<ResultDTO> findAll() {
        return DtoHelper.resultsToDTO(resultRepository.findAll());
    }

	public List<ResultDTO> getResultsByUser(UserDTO user) {
    	return DtoHelper.resultsToDTO(resultRepository.findByUserOrderBySecondsAsc(DtoHelper.toEntity(user), PageRequest.of(0, 20)));
	}

	public List<ResultDTO> getAllResults() {
    	return DtoHelper.resultsToDTO(resultRepository.findAllByOrderBySecondsAscResultDateDescUserNameAscJoinFetchUser(PageRequest.of(0, 30)));
	}

	public List<ResultDTO> getResultsBetterOrEquals(long seconds) {
    	return DtoHelper
				.resultsToDTO(resultRepository.getResultsBetterOrEquals(seconds));
	}

	@Transactional
	public void saveResult(int seconds, UserDTO userDto) {
		User user = DtoHelper.toEntity(userDto);
		Result result = new Result();
		result.setSeconds(seconds);
		result.setUser(user);
		result.setResultDate(new Date());
		resultRepository.save(result);
		ResultData resultData = new ResultData(0, user.getId(), new Date(), seconds);
		resultDataRepository.save(resultData);
//		throw new NullPointerException("TESZT FOR ROLLBACK!!!!!!!!");
	}
}
