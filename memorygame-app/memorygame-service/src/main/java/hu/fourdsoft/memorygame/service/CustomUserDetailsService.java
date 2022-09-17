package hu.fourdsoft.memorygame.service;

import hu.fourdsoft.memorygame.common.model.User;
import hu.fourdsoft.memorygame.dao.UserRepository;
import hu.fourdsoft.memorygame.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    static final String USER_NOT_FOUND_MSG = "User not found!";

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND_MSG));
        return new CustomUserDetails(user);
    }
}
