package hu.fourdsoft.memorygame.data.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import hu.fourdsoft.memorygame.common.data.dto.ResultDataDTO;
import hu.fourdsoft.memorygame.common.data.model.ResultData;
import hu.fourdsoft.memorygame.common.dto.helper.DtoHelper;
import hu.fourdsoft.memorygame.data.dao.ResultDataRepository;
import hu.fourdsoft.memorygame.data.transactions.MemorygameDataTransactional;

@Service
public class ResultDataService {
	
	@Autowired
	private ResultDataRepository resultDataRepository;
	
	public List<ResultDataDTO> getResultDatas() {
		return DtoHelper.resultDatasToDTO(resultDataRepository.findAllByOrderBySecondsAscResultDateDesc(PageRequest.of(0, 20)));
	}
	
	@MemorygameDataTransactional
	public void saveResultData(int seconds, long userId) {
		ResultData resultData = new ResultData();
		resultData.setSeconds(seconds);
		resultData.setResultDate(new Date());
		resultData.setUserId(userId);
		
		resultDataRepository.saveAndFlush(resultData);
	}

}
