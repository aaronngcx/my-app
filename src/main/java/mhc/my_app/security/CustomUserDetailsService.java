package mhc.my_app.security;

import mhc.my_app.domain.User; // Ensure this imports your User entity correctly
import mhc.my_app.repos.UserRepository; // Make sure to import your UserRepository
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository; // Inject your UserRepository

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Retrieve the User entity by username
        User user = userRepository.findByUsername(username); // Use UserEntity

        // Check if the user exists
        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + username); // Provide specific username in the exception message
        }

        // Debug output to log the retrieved user and role
        System.out.println("Retrieved User: " + user.getUsername() + ", Role: " + user.getRole());

        // Return your custom UserDetails implementation
        return new CustomUserDetails(user); // Return an instance of CustomUserDetails
    }
}
