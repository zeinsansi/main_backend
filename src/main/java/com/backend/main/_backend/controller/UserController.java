package com.backend.main._backend.controller;

import com.backend.main._backend.model.User;
import com.backend.main._backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin("http://localhost:3000/")
public class UserController {
    private ClientRegistration registration;

    private final UserService userService;

    public UserController(ClientRegistrationRepository registrations, UserService userService) {
        this.registration = registrations.findByRegistrationId("auth0");
        this.userService = userService;
    }

    @GetMapping("/api/user")
    public ResponseEntity<?> getUser(@AuthenticationPrincipal OAuth2User user) {
        if (user == null) {
            return new ResponseEntity<>("", HttpStatus.OK);
        } else {
            SaveUser(user);
            return ResponseEntity.ok().body(user.getAttributes());
        }
    }

    @GetMapping("/api/register")
    public ResponseEntity<?> SaveUser(@AuthenticationPrincipal OAuth2User user) {
        if (user != null) {
            String userEmail = user.getAttribute("email");
            if (userService.getUserByemail(userEmail) == null) {
                User appUser = new User();
                appUser.setId(user.getAttribute("sub"));
                appUser.setEmail(userEmail);
                appUser.setUsername(user.getAttribute("nickname"));
                userService.saveUser(appUser);
                return ResponseEntity.ok("User "+appUser.getUsername()+" is toegevoegd");
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("User bestaat al");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Geen user meegegeven");
        }
    }


    @PostMapping("/api/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        // send logout URL to client so they can initiate logout
        StringBuilder logoutUrl = new StringBuilder();
        String issuerUri = this.registration.getProviderDetails().getIssuerUri();
        logoutUrl.append(issuerUri.endsWith("/") ? issuerUri + "v2/logout" : issuerUri + "/v2/logout");
        logoutUrl.append("?client_id=").append(this.registration.getClientId());

        Map<String, String> logoutDetails = new HashMap<>();
        logoutDetails.put("logoutUrl", logoutUrl.toString());
        request.getSession(false).invalidate();
        return ResponseEntity.ok().body(logoutDetails);
    }
}
