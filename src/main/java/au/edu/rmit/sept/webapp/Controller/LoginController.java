package au.edu.rmit.sept.webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import au.edu.rmit.sept.webapp.model.User;
import au.edu.rmit.sept.webapp.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    private Map<String, String> sessionTokens = new HashMap<>(); // Simulating a token store

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    @ResponseBody
    public String loginUser(@RequestParam String email, @RequestParam String password, @RequestParam(required = false) String sessionToken) {
        // Check if the user is already logged in by verifying the session token
        if (sessionToken != null && sessionTokens.containsKey(sessionToken)) {
            return "You are already logged in!";
        }
    
        // Find the user by email
        User user = userRepository.findByEmail(email);
    
        // Fix: use getPassword() instead of password()
        if (user == null || !user.getPassword().equals(password)) {
            // If no user is found or password doesn't match, return an error message
            return "Invalid email or password.";
        }
    
        // Generate a new session token
        String newSessionToken = UUID.randomUUID().toString();
        sessionTokens.put(newSessionToken, email); // Store the token with the user's email
    
        // Return the session token to the client
        return newSessionToken;
    }

    @GetMapping("/logout")
@ResponseBody
public String logout(@RequestParam(required = false) String sessionToken) {
    // Invalidate the session token
    if (sessionToken != null && sessionTokens.containsKey(sessionToken)) {
        sessionTokens.remove(sessionToken);
    }
    // Return a success message
    return "You have successfully logged out.";
}
}