package hu.fourdsoft.memorygame.common.dto.helper;

import hu.fourdsoft.memorygame.common.model.Result;
import hu.fourdsoft.memorygame.common.model.User;
import hu.fourdsoft.memorygame.common.data.dto.ResultDataDTO;
import hu.fourdsoft.memorygame.common.data.model.ResultData;
import hu.fourdsoft.memorygame.common.dto.ResultDTO;
import hu.fourdsoft.memorygame.common.dto.UserDTO;

import java.util.List;
import java.util.stream.Collectors;

public class DtoHelper {
	public static UserDTO toDTO(User user) {
		if (user == null) {
			return null;
		}
		UserDTO userDTO = toDTOWithoutResults(user);
		userDTO.setResults(resultsToDTO(user.getResults()));

		return userDTO;
	}

	public static List<UserDTO> usersToDTO(List<User> users) {
		if (users == null) {
			return null;
		}
		return users.stream().map(DtoHelper::toDTO).collect(Collectors.toList());
	}

	public static List<ResultDTO> resultsToDTO(List<Result> results) {
		if (results == null) {
			return null;
		}
		return results.stream().map(DtoHelper::toDTO).collect(Collectors.toList());
	}

	public static ResultDTO toDTO(Result result) {
		if (result == null) {
			return null;
		}
		ResultDTO resultDTO = new ResultDTO();
		resultDTO.setId(result.getId());
		resultDTO.setResultDate(result.getResultDate());
		resultDTO.setSeconds(result.getSeconds());
		resultDTO.setUsername(result.getUser().getUsername());
		return resultDTO;
	}

	public static UserDTO toDTOWithoutResults(User user) {
		if (user == null) {
			return null;
		}
		UserDTO userDTO = new UserDTO();
		userDTO.setId(user.getId());
		userDTO.setEmail(user.getEmail());
		userDTO.setUsername(user.getUsername());
		userDTO.setPassword(user.getPassword());

		return userDTO;
	}

	public static User toEntity(UserDTO userDto) {
		if (userDto == null) {
			return null;
		}
		return new User(userDto.getId(), userDto.getUsername(), userDto.getEmail(), userDto.getPassword());
	}
	
	public static ResultDataDTO toDTO(ResultData result) {
		if (result == null) {
			return null;
		}
		ResultDataDTO resultDTO = new ResultDataDTO();
		resultDTO.setId(result.getId());
		resultDTO.setResultDate(result.getResultDate());
		resultDTO.setSeconds(result.getSeconds());
		resultDTO.setUserId(result.getUserId());
		return resultDTO;
	}
	
	public static List<ResultDataDTO> resultDatasToDTO(List<ResultData> results) {
		if (results == null) {
			return null;
		}
		return results.stream().map(DtoHelper::toDTO).collect(Collectors.toList());
	}
}
