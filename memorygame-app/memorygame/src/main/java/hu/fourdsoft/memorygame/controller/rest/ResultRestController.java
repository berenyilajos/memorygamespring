package hu.fourdsoft.memorygame.controller.rest;

import hu.fourdsoft.memorygame.common.dto.UserDTO;
import hu.fourdsoft.memorygame.data.service.ResultDataService;
import hu.fourdsoft.memorygame.validator.XSDValidator;
import hu.fourdsoft.memorygame.exception.MyApplicationException;
import hu.fourdsoft.memorygame.service.ResultService;
import hu.fourdsoft.xsdpojo.common.common.SuccessType;
import hu.fourdsoft.xsdpojo.pojo.ResultRequest;
import hu.fourdsoft.xsdpojo.pojo.ResultResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/game/result", method = RequestMethod.POST,
		consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
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

	@RequestMapping("/save")
	public ResponseEntity<ResultResponse> saveAction(HttpServletRequest request, @RequestBody ResultRequest resultRequest) throws MyApplicationException {

		log.debug("ResultRestController.saveAction >>>");
		try {
			validateByXSD(resultRequest, XSD_POJO);
		} catch (Exception e) {
			log.warn(e.getMessage(), e);
			throw new MyApplicationException("Save unsuccessful: " + e.getMessage());
		}

		ResultResponse resultResponse = new ResultResponse();
		int seconds = resultRequest.getSeconds();
		long userId = resultRequest.getUserId();
		resultResponse.setSeconds(seconds);
		HttpSession session = request.getSession(false);
		UserDTO user;
		if (session == null || (user = (UserDTO)session.getAttribute("user")) == null || user.getId() != userId) {
			resultResponse.setSuccess(SuccessType.ERROR);
			resultResponse.setMessage("User is not logged in!");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resultResponse);
		}
		resultService.saveResult(seconds, user);
		resultDataService.saveResultData(seconds, userId);
		log.info("ResultDats: " + resultDataService.getResultDatas());
		resultResponse.setSuccess(SuccessType.SUCCESS);
		resultResponse.setUserId(userId);
		//validateByXSD(resultResponse, XSD_POJO);
		log.debug("<<< ResultRestController.saveAction");
		return ResponseEntity.ok(resultResponse);
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
