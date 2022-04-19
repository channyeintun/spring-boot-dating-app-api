package com.pledge.app.endpoint;

import com.pledge.app.exception.CustomErrorException;
import com.pledge.app.entity.Profile;
import com.pledge.app.entity.User;
import com.pledge.app.payload.*;
import com.pledge.app.service.FacebookService;
import com.pledge.app.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

@RestController
@Slf4j
public class AuthEndpoint {

    @Autowired
    private UserService userService;
    @Autowired
    private FacebookService facebookService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Map<String, Object> tokens = userService.loginUser(loginRequest.getEmail(), loginRequest.getPassword());
        return ResponseEntity.ok(mapToJwtResponse(tokens));
    }

    @PostMapping("/facebook/login")
    public ResponseEntity<?> facebookAuth(@Valid @RequestBody FacebookLoginRequest facebookLoginRequest) {
        log.info("facebook login {}", facebookLoginRequest);
        Map<String, Object> tokens = facebookService.loginUser(facebookLoginRequest.getAccessToken());

        return ResponseEntity.ok(mapToJwtResponse(tokens));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(HttpServletRequest httpServletRequest) {
        Map<String, Object> tokens = null;
        try {
            tokens = userService.refreshToken(httpServletRequest);
        } catch (HttpStatusCodeException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid token", null);
        }
        return ResponseEntity.ok(mapToJwtResponse(tokens));
    }

    @PostMapping(value = "/signup", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createUser(@Valid @RequestBody SignUpRequest payload) {
        log.info("creating user {}", payload.getEmail());

        User user = new User();
        user.setUsername(payload.getEmail());
        user.setName(payload.getName());
        user.setPassword(payload.getPassword());
        Profile profile = new Profile();
        profile.setDisplayName(payload.getName());
        user.setUserProfile(profile);

        try {
            userService.registerUser(user);
            log.info("registering is fine");
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "User Already Exists");
        }

        return ResponseEntity
                .ok(new ApiResponse(true, "Verification code is sent to your email."));
    }

    JwtAuthenticationResponse mapToJwtResponse(Map<String, Object> tokens) {
        JwtAuthenticationResponse response = new JwtAuthenticationResponse();
        response.setAccessToken(tokens.get("accessToken").toString());
        response.setRefreshToken(tokens.get("refreshToken").toString());
        response.setExpireAt(Long.valueOf(tokens.get("expireAt").toString()));
        return response;
    }
}
