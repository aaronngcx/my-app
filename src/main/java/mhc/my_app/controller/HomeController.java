package mhc.my_app.controller;

import org.slf4j.Logger; // Import SLF4J Logger
import org.slf4j.LoggerFactory; // Import SLF4J LoggerFactory
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class HomeController {

    // Create a logger instance for this class
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @GetMapping("/")
    public String index(Model model, Principal principal) {
        if (principal != null) {
            // Log the username when the user is logged in
            logger.info("User logged in: {}", principal.getName());
            model.addAttribute("username", principal.getName());
        } else {
            // Log when no user is logged in
            logger.info("No user logged in.");
            model.addAttribute("username", "Guest");
        }
        return "home/index"; // This should match your Thymeleaf template name
    }
}
