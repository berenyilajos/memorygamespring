package hu.fourdsoft.memorygame.common.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserDTO {
	private long id;
	private String username;
	private String email;
	private String password;
	private List<ResultDTO> results = new ArrayList<>();

	public UserDTO add(ResultDTO resultDTO) {
		results.add(resultDTO);
		return this;
	}

}
