package hu.fourdsoft.memorygame.jwt;

import hu.fourdsoft.memorygame.common.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
@Slf4j
public class JwtFilterHandlerInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws
            Exception {

        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            // Test if the controller-method is annotated with @CustomFilter
            JwtSecure filter = handlerMethod.getMethod().getAnnotation(JwtSecure.class);
            if (filter != null) {
                HttpSession session = request.getSession(false);
                UserDTO user;
                String authorization;
                if (session == null || (user = (UserDTO) session.getAttribute("user")) == null) {
                    log.debug("JwtFilter: user not logged in, method: " + handlerMethod.getMethod().getName());
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    return false;
                } else if ((authorization = (String) session.getAttribute(JwtUtil.AUTHORIZATION)) == null || !authorization.startsWith(JwtUtil.BEARER)) {
                    log.debug("JwtFilter: user " + user.getUsername() + " has no Bearer token, method: " + handlerMethod.getMethod().getName());
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    return false;
                } else if (!jwtUtil.isValidToken(authorization.substring(JwtUtil.BEARER.length()), user.getUsername())) {
                    log.debug("JwtFilter: user " + user.getUsername() + " has no valid token, method: " + handlerMethod.getMethod().getName());
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    return false;
                }
                log.debug("JwtFilter: user " + user.getUsername() + " is logged in and has valid token, method: " + handlerMethod.getMethod().getName());
            }
        }
        return true;
    }
}
