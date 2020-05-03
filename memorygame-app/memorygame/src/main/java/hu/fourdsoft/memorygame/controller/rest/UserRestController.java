package hu.fourdsoft.memorygame.controller.rest;

import hu.fourdsoft.memorygame.common.dto.UserDTO;
import hu.fourdsoft.memorygame.common.dto.UsersDTO;
import hu.fourdsoft.memorygame.exception.MyApplicationException;
import hu.fourdsoft.memorygame.jwt.JwtSecure;
import hu.fourdsoft.memorygame.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Random;

@RestController
@RequestMapping(value = "/game", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class UserRestController {
	@Autowired
	private UserService userService;

	@Autowired
	private ResourceReader resourceReader;

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
		String yaml = resourceReader.readFileToString("api/memorygame-api.yaml");
		String json = resourceReader.convertYamlToJson(yaml);
		log.debug("<<< UserController.swaggerUiPage");
		return ResponseEntity.ok(json);
	}

	@GetMapping(value = "/users/{name}")
	@JwtSecure
	public ResponseEntity getUserByName(HttpSession session, @PathVariable("name") String name) {
		UserDTO user = (UserDTO)session.getAttribute("user");
		if (!user.getUsername().equals(name)) {
			log.debug("Username: " + user.getUsername() + " and name: " + name + " is not equal!");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		return ResponseEntity.ok(userService.getUserByName(name));
	}

}
