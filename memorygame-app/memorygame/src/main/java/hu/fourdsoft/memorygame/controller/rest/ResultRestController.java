package hu.fourdsoft.memorygame.controller.rest;

import hu.fourdsoft.memorygame.common.api.dto.ResultRequest;
import hu.fourdsoft.memorygame.common.api.dto.ResultResponse;
import hu.fourdsoft.memorygame.common.api.dto.SuccessType;
import hu.fourdsoft.memorygame.common.dto.ResultDTO;
import hu.fourdsoft.memorygame.common.dto.UserDTO;
import hu.fourdsoft.memorygame.data.service.ResultDataService;
import hu.fourdsoft.memorygame.jwt.JwtSecure;
import hu.fourdsoft.memorygame.validator.XSDValidator;
import hu.fourdsoft.memorygame.exception.MyApplicationException;
import hu.fourdsoft.memorygame.service.ResultService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/game/result")
@Slf4j
public class ResultRestController implements XSDValidator {

	@Autowired
	private ResultService resultService;
	
	@Autowired
	private ResultDataService resultDataService;

	private static final String XSD_POJO = "xsd_wsdl/hu/fourdsoft/xsdpojo/pojo.xsd";

//	@Value("classpath:" + XSD_POJO)
//	private Resource xsdResource;

	@Autowired
	ResourceLoader resourceLoader;

	@PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@JwtSecure
	public ResponseEntity<ResultResponse> saveAction(HttpSession session, @RequestBody ResultRequest resultRequest) /*throws MyApplicationException*/ {

		log.debug("ResultRestController.saveAction >>>");
//		try {
//			validateByXSD(resultRequest, XSD_POJO);
//		} catch (Exception e) {
//			log.warn(e.getMessage(), e);
//			throw new MyApplicationException("Save unsuccessful: " + e.getMessage());
//		}

		ResultResponse resultResponse = new ResultResponse();
		int seconds = resultRequest.getSeconds();
		long userId = resultRequest.getUserId();
		resultResponse.setSeconds(seconds);
		UserDTO user = (UserDTO)session.getAttribute("user");
		if (user.getId() != userId) {
			resultResponse.setSuccess(SuccessType.ERROR);
			resultResponse.setMessage("User is not logged in!");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resultResponse);
		}
		resultService.saveResult(seconds, user);
		resultDataService.saveResultData(seconds, userId);
		log.info("ResultDats: " + resultDataService.getResultDatas());
		resultResponse.setSuccess(SuccessType.SUCCESS);
		resultResponse.setUserId(userId);
		Date now = new Date();
		resultResponse.setResultDate(now);
		resultResponse.setResultDateTime(now);
		//validateByXSD(resultResponse, XSD_POJO);
		log.debug("<<< ResultRestController.saveAction");
		return ResponseEntity.ok(resultResponse);
	}

	@GetMapping(value = "/betterorequals/{seconds:\\d+}", produces = MediaType.APPLICATION_JSON_VALUE)
	@JwtSecure
	public List<ResultDTO> getResultsBetterOrEquals(@PathVariable("seconds") long seconds) {
		return resultService.getResultsBetterOrEquals(seconds);
	}


	@Override
	public ResourceLoader getResourceLoader() {
		return resourceLoader;
	}

	@Override
	public Logger getLog() {
		return log;
	}
}
