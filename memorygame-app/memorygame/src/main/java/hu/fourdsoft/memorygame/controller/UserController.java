package hu.fourdsoft.memorygame.controller;

import hu.fourdsoft.memorygame.common.dto.UserDTO;
import hu.fourdsoft.memorygame.exception.UserAllreadyExistException;
import hu.fourdsoft.memorygame.jwt.JwtUtil;
import hu.fourdsoft.memorygame.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
@RequestMapping("/game")
@Slf4j
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private JwtUtil jwtUtil;

	@RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
	public ModelAndView indexPage(HttpServletRequest request) throws IOException, ServletException {
		log.debug("UserController.indexPage >>>");
//		HttpSession session = request.getSession(false);
//		if (session == null || session.getAttribute("user") == null) {
//			return new ModelAndView("redirect:/game/login");
//		}
		log.debug("<<< UserController.indexPage");
		return new ModelAndView("index");
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public ModelAndView loginPage(HttpServletRequest request) throws ServletException, IOException {
		log.debug("UserController.loginPage >>>");
		String msg = "";
		if (request.getParameterMap().containsKey("error")) {
			msg = "Hibás felhasználónév vagy jelszó!!!!!!";
		}
		log.debug("<<< UserController.loginPage");
		ModelAndView view = new ModelAndView("login");
		view.addObject("msg", msg);
		return view;
	}

//	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = MediaType.TEXT_HTML_VALUE)
//	public ModelAndView login(HttpServletRequest request, @RequestParam("username") String username,
//								  @RequestParam("password") String password) throws ServletException, IOException {
//		log.debug("UserController.loginAction, username=[{}] >>>", username);
//		String msg = "";
//		if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password)) {
//			UserDTO user = userService.getUserByUsernameAndPassword(username, password);
//			if (user == null) {
//				msg = "Hibás felhasználónév vagy jelszó!";
//			} else {
//				HttpSession session = request.getSession(true);
//				session.setAttribute("user", user);
//				session.setAttribute(JwtUtil.AUTHORIZATION, JwtUtil.BEARER + jwtUtil.generateToken(user.getUsername()));
//				return new ModelAndView("redirect:/game");
//			}
//		}
//		log.debug("<<< UserController.loginAction");
//
//		ModelAndView view = new ModelAndView("login");
//		view.addObject("msg", msg);
//		return view;
//	}
//
//	@RequestMapping(value = "/logout", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
//	public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
//		HttpSession session = request.getSession(false);
//		log.debug("UserController.logoutAction, username=[{}] >>>", session != null ? (UserDTO)session.getAttribute("user") : null);
//		if (session != null) {
//			session.setAttribute("user", null);
//			session.setAttribute(JwtUtil.AUTHORIZATION, null);
//			session = null;
//		}
//		log.debug("<<< UserController.logoutAction");
//		return new ModelAndView("redirect:/game/login");
//	}

	@RequestMapping(value = "/register", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public ModelAndView registerPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.debug("UserController.registerPage >>>");
		String msg = "";
		ModelAndView view = new ModelAndView("register");
		view.addObject("msg", msg);
		log.debug("<<< UserController.registerPage");
		return view;
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST, produces = MediaType.TEXT_HTML_VALUE)
	public ModelAndView register(HttpServletRequest request, HttpServletResponse response,
							   @RequestParam("username") String username,
							   @RequestParam("password") String password, @RequestParam("password2") String password2) throws ServletException, IOException {
		log.debug("UserController.register, username=[{}] >>>", username);
		String msg = "";
		if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password) && StringUtils.isNotBlank(password2)) {
			if (!password.equals(password2)) {
				msg = "A jelszó és megerősítése nem egyezik!";
			}
			if (msg.isEmpty()) {
				try {
					userService.saveUser(username, password);
					return new ModelAndView("login");
				} catch(UserAllreadyExistException ex) {
					msg = ex.getMessage();
				} catch (Exception e) {
					msg = "Nem sikerült a felhasználó elmentése!";
				}
			}
		}
		ModelAndView view = new ModelAndView("register");
		view.addObject("msg", msg);
		log.debug("<<< UserController.register");
		return view;
	}

}
