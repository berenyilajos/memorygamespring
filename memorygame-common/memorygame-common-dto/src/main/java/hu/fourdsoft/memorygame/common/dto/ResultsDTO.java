package hu.fourdsoft.memorygame.common.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ResultsDTO {
	private List<ResultDTO> results = new ArrayList<>();

	public ResultsDTO add(ResultDTO resultDTO) {
		results.add(resultDTO);
		return this;
	}

}
