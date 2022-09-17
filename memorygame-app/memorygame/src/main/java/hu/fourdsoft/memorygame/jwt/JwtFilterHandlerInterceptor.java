package hu.fourdsoft.memorygame.jwt;

import hu.fourdsoft.memorygame.security.CustomUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class JwtFilterHandlerInterceptor implements HandlerInterceptor {

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
                CustomUserDetails userDetails = getUserDetails();
                String authorization;
                if (userDetails == null) {
                    log.debug("JwtFilter: user not logged in, method: " + handlerMethod.getMethod().getName());
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    return false;
                } else if (!userDetails.hasRole(filter.role())) {
                    log.debug("JwtFilter: user " + userDetails.getUsername() + " has no role: " + filter.role() + ", method: " + handlerMethod.getMethod().getName());
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    return false;
                } else if ((authorization = (String) request.getHeader(JwtUtil.AUTHORIZATION)) == null || !authorization.startsWith(JwtUtil.BEARER)) {
                    log.debug("JwtFilter: user " + userDetails.getUsername() + " has no Bearer token, method: " + handlerMethod.getMethod().getName());
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    return false;
                } else if (!jwtUtil.isValidToken(authorization.substring(JwtUtil.BEARER.length()), userDetails.getUsername())) {
                    log.debug("JwtFilter: user " + userDetails.getUsername() + " has no valid token, method: " + handlerMethod.getMethod().getName());
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    return false;
                }
                log.debug("JwtFilter: user " + userDetails.getUsername() + " is logged in, has role: " + filter.role() + " and has valid token, method: " + handlerMethod.getMethod().getName());
            }
        }
        return true;
    }

    private CustomUserDetails getUserDetails() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        Object principal = authentication.getPrincipal();
        return principal instanceof CustomUserDetails ? (CustomUserDetails) principal : null;
    }
}
