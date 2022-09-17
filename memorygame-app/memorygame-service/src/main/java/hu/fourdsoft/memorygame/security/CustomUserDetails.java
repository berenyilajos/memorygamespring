package hu.fourdsoft.memorygame.security;

import hu.fourdsoft.memorygame.common.model.User;
import java.util.Collections;

public class CustomUserDetails extends org.springframework.security.core.userdetails.User {

    private final long id;
    private final String email;

    public CustomUserDetails(User user) {
        super(user.getUsername(), user.getPassword(), Collections.singleton(() -> "ROLE_USER"));
        this.id = user.getId();
        this.email = user.getEmail();
    }

    public String getEmail() {
        return email;
    }

    public long getId() {
        return id;
    }
}
