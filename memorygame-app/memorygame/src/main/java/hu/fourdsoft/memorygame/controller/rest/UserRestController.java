package hu.fourdsoft.memorygame.controller.rest;

import hu.fourdsoft.memorygame.common.dto.UsersDTO;
import hu.fourdsoft.memorygame.exception.MyApplicationException;
import hu.fourdsoft.memorygame.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequestMapping("/game")
@Slf4j
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

	@RequestMapping(value = "/api-docs", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> swaggerUiPage() throws MyApplicationException {
		log.debug("UserController.swaggerUiPage >>>");
		String yaml = ResourceReader.readFileToString("api/memorygame-api.yaml");
		String json = ResourceReader.convertYamlToJson(yaml);
		log.debug("<<< UserController.swaggerUiPage");
		return ResponseEntity.ok(json);
	}

}
