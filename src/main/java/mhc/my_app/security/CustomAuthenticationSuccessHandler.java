package mhc.my_app.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // Check if the authenticated user has the 'ROLE_HR' authority
        boolean isHR = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_HR"));
        System.out.println("User Authorities: " + authentication.getAuthorities());
        System.out.println("isHR: " + isHR);

        if (isHR) {
            // Redirect HR users to the event requests page
            response.sendRedirect(request.getContextPath() + "/eventRequests");
        } else {
            // Default behavior: redirect to the homepage or another appropriate page
            response.sendRedirect(request.getContextPath() + "/home");
        }
    }
}
