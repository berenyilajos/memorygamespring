package hu.fourdsoft.memorygame.controller.rest;

import hu.fourdsoft.memorygame.common.dto.UsersDTO;
import hu.fourdsoft.memorygame.exception.MyApplicationException;
import hu.fourdsoft.memorygame.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequestMapping("/game")
public class UserRestController {
	@Autowired
	private UserService userService;

	@RequestMapping(value = "/users", method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public UsersDTO findAll() throws MyApplicationException {
		if (new Random().nextBoolean()) {
			throw new MyApplicationException("Valamiiiiii!");
		}
		UsersDTO users = new UsersDTO();
		users.setUsers(userService.findAll());
		return users;
	}

}
