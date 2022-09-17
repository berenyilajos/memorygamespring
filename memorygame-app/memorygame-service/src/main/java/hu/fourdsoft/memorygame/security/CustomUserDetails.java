package hu.fourdsoft.memorygame.security;

import hu.fourdsoft.memorygame.common.model.User;
import java.util.Collections;

public class CustomUserDetails extends org.springframework.security.core.userdetails.User {

    private final static String ROLE_PREFIX = "ROLE_";

    private final long id;
    private final String email;

    public CustomUserDetails(User user) {
        super(user.getUsername(), user.getPassword(), Collections.singleton(() -> "ROLE_USER"));
        this.id = user.getId();
        this.email = user.getEmail();
    }

    public boolean hasRole(String role) {
        return getAuthorities().stream()
                .anyMatch(a -> filterRolePrefix(a.getAuthority()).equals(filterRolePrefix(role)));
    }

    private String filterRolePrefix(String role) {
        return role.startsWith(ROLE_PREFIX) ? role.substring(ROLE_PREFIX.length()) : role;
    }

    public String getEmail() {
        return email;
    }

    public long getId() {
        return id;
    }
}
