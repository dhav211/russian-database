package com.havlin.daniel.russian.entities.users;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SecurityUser implements UserDetails {
    private final User user;

    public SecurityUser(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<String> authorities = new ArrayList<>();
        switch (user.getRole()) {
            case ADMIN:
                authorities.add(UserAuthority.REMOVE_USER.toString());
            case EDITOR:
                authorities.add(UserAuthority.ALTER_CONTENT.toString());
            case BASIC:
                authorities.addAll(List.of(
                        UserAuthority.DO_EXERCISE.toString(),
                        UserAuthority.UPLOAD_BOOK.toString(),
                        UserAuthority.USE_DICTIONARY.toString()
                ));
        }

        return authorities.stream().map(SimpleGrantedAuthority::new).toList();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    public User getUser() {
        return user;
    }
}
