package hu.fourdsoft.memorygame.service;

import hu.fourdsoft.memorygame.common.dto.ResultDTO;
import hu.fourdsoft.memorygame.common.dto.UserDTO;
import hu.fourdsoft.memorygame.common.dto.helper.DtoHelper;
import hu.fourdsoft.memorygame.common.model.Result;
import hu.fourdsoft.memorygame.common.model.User;
import hu.fourdsoft.memorygame.dao.ResultRepository;
import hu.fourdsoft.memorygame.transactions.MemorygameTransactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ResultService {
    @Autowired
    private ResultRepository resultRepository;

    public List<ResultDTO> findAll() {
        return DtoHelper.resultsToDTO(resultRepository.findAll());
    }

	public List<ResultDTO> getResultsByUser(UserDTO user) {
    	return DtoHelper.resultsToDTO(resultRepository.findByUserOrderBySecondsAsc(DtoHelper.toEntity(user), PageRequest.of(0, 20)));
	}

	public List<ResultDTO> getAllResults() {
    	//return DtoHelper.resultsToDTO(resultRepository.findAllByOrderBySecondsAscResultDateDesc(PageRequest.of(0, 20)));
		Pageable pageable = PageRequest.of(0, 20);
		return DtoHelper
				.resultsToDTO(resultRepository.findAllByOrderBySecondsAscResultDateDescUserNameAscJoinFetchUser(pageable));
	}

	@MemorygameTransactional
	public void saveResult(int seconds, UserDTO userDto) {
		User user = DtoHelper.toEntity(userDto);
		Result result = new Result();
		result.setSeconds(seconds);
		result.setUser(user);
		result.setResultDate(new Date());
		resultRepository.saveAndFlush(result);
	}
}
