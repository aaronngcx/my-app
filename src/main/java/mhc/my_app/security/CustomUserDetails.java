package mhc.my_app.security;

import mhc.my_app.model.Role;
import mhc.my_app.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    // Constructor
    public CustomUserDetails(User user) {
        this.username = user.getUsername(); // Get username from User entity
        this.password = user.getPassword(); // Get password from User entity

        // Assuming user.getRole() returns a single Role enum value
        this.authorities = Collections.singletonList(
                new SimpleGrantedAuthority(user.getRole().name()) // Ensure role has 'ROLE_' prefix
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities; // Return user's roles as GrantedAuthority
    }

    @Override
    public String getPassword() {
        return password; // Return user's password
    }

    @Override
    public String getUsername() {
        return username; // Return user's username
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Set to true for simplicity, modify as needed
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Set to true for simplicity, modify as needed
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Set to true for simplicity, modify as needed
    }

    @Override
    public boolean isEnabled() {
        return true; // Set to true for simplicity, modify as needed
    }
}
