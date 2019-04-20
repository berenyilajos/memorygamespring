package hu.fourdsoft.memorygame.common.dto;

import lombok.Data;

import java.util.List;

@Data
public class UsersDTO {
	private List<UserDTO> users;

	public UsersDTO add(UserDTO user) {
		users.add(user);
		return this;
	}
}
