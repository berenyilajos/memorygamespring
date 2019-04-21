package hu.fourdsoft.memorygame.controller;

import hu.fourdsoft.memorygame.common.dto.ResultDTO;
import hu.fourdsoft.memorygame.common.dto.UserDTO;
import hu.fourdsoft.memorygame.service.ResultService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping(value = "/game/result", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
@Slf4j
public class ResultController {

	@Autowired
	private ResultService resultService;

	@RequestMapping
	public ModelAndView listAction(HttpServletRequest request) throws ServletException, IOException {

		log.debug("ResultController.listAction >>>");
		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("user") == null) {
			return new ModelAndView("redirect:/game/login");
		}
		List<ResultDTO> list = resultService.getAllResults();

		ModelAndView view = new ModelAndView("result/results");
		view.addObject("list", list);
		log.debug("<<< ResultController.listAction");

		return view;
	}

	@RequestMapping(value = "/{userId:\\d+}")
	public ModelAndView showAction(HttpServletRequest request, @PathVariable("userId") String userId)
			throws ServletException, IOException {
		log.debug("ResultController.showAction, userId=[{}] >>>", userId);
		HttpSession session = request.getSession(false);
		UserDTO user;
		if (session == null || (user = (UserDTO) session.getAttribute("user")) == null || user.getId() != Long.parseLong(userId)) {
			return new ModelAndView("redirect:/game/login");
		}
		List<ResultDTO> list = resultService.getResultsByUser(user);
		ModelAndView view = new ModelAndView("result/userresults");

		view.addObject("list", list);
		log.debug("<<< ResultController.listAction");

		return view;
	}

}
