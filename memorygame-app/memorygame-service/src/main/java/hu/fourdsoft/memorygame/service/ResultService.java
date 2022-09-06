package hu.fourdsoft.memorygame.service;

import hu.fourdsoft.memorygame.common.dto.ResultDTO;
import hu.fourdsoft.memorygame.common.dto.UserDTO;
import hu.fourdsoft.memorygame.common.dto.helper.DtoHelper;
import hu.fourdsoft.memorygame.common.model.Result;
import hu.fourdsoft.memorygame.common.model.User;
import hu.fourdsoft.memorygame.dao.ResultRepository;
import hu.fourdsoft.memorygame.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class ResultService {
    @Autowired
    private ResultRepository resultRepository;

    @Autowired
	private UserRepository userRepository;

    @Transactional
    public List<ResultDTO> findAll() {
        return DtoHelper.resultsToDTO(resultRepository.findAll());
    }

	@Transactional
	public List<ResultDTO> getResultsByUser(UserDTO user) {
    	return DtoHelper.resultsToDTO(resultRepository.findByUserOrderBySecondsAsc(DtoHelper.toEntity(user), PageRequest.of(0, 20)));
	}

	@Transactional
	public List<ResultDTO> getResultsByUser(long userId) {
		return DtoHelper.resultsToDTO(resultRepository.findByUserIdOrderBySecondsAsc(userId, PageRequest.of(0, 20)));
	}

	@Transactional
	public List<ResultDTO> getAllResults() {
    	//return DtoHelper.resultsToDTO(resultRepository.findAllByOrderBySecondsAscResultDateDesc(PageRequest.of(0, 20)));
		Pageable pageable = PageRequest.of(0, 20);
		return DtoHelper
				.resultsToDTO(resultRepository.findAllByOrderBySecondsAscResultDateDescUserNameAscJoinFetchUser(pageable));
	}

	@Transactional
	public void saveResult(int seconds, UserDTO userDto) {
		User user = DtoHelper.toEntity(userDto);
		Result result = new Result();
		result.setSeconds(seconds);
		result.setUser(user);
		result.setResultDate(new Date());
		resultRepository.saveAndFlush(result);
	}
}
