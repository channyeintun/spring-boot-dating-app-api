package com.pledge.app.service;

import com.pledge.app.client.FacebookClient;
import com.pledge.app.config.jwt.JwtTokenProvider;
import com.pledge.app.entity.*;
import com.pledge.app.entity.facebook.FacebookUser;
import com.pledge.app.entity.Profile;
import com.pledge.app.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service
@Slf4j
public class FacebookService {

    private FacebookClient facebookClient;
    private UserService userService;
    private JwtTokenProvider tokenProvider;

    @Autowired
    public FacebookService(FacebookClient facebookClient,
                           UserService userService,
                           JwtTokenProvider tokenProvider) {
        this.facebookClient = facebookClient;
        this.userService = userService;
        this.tokenProvider = tokenProvider;
    }

    public Map<String, Object> loginUser(String fbAccessToken) {
        var facebookUser = facebookClient.getUser(fbAccessToken);

        Optional<User> opt = userService.findById(facebookUser.getId())
                .or(() -> Optional.ofNullable(userService.registerUser(convertTo(facebookUser))));
        User user = opt.get();
        return tokenProvider.createToken(user.getUsername(), user.getRoles());
    }

    private User convertTo(FacebookUser facebookUser) {
        User user = new User();
        user.setUserId(facebookUser.getId());
        user.setUsername(facebookUser.getEmail());
        user.setName(String
                .format("%s %s", facebookUser.getFirstName(), facebookUser.getLastName()));
        user.setPassword(generatePassword(8));
        Profile profile = new Profile();
        profile.setDisplayName(user.getName());
        ProfilePicture image=new ProfilePicture();
        image.setUrl(facebookUser
                .getPicture().getData().getUrl());
        profile.setProfilePicture(image);
        user.setUserProfile(profile);
        return user;
    }

    private String generatePassword(int length) {
        String capitalCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
        String specialCharacters = "!@#$";
        String numbers = "1234567890";
        String combinedChars = capitalCaseLetters + lowerCaseLetters + specialCharacters + numbers;
        Random random = new Random();
        char[] password = new char[length];

        password[0] = lowerCaseLetters.charAt(random.nextInt(lowerCaseLetters.length()));
        password[1] = capitalCaseLetters.charAt(random.nextInt(capitalCaseLetters.length()));
        password[2] = specialCharacters.charAt(random.nextInt(specialCharacters.length()));
        password[3] = numbers.charAt(random.nextInt(numbers.length()));

        for (int i = 4; i < length; i++) {
            password[i] = combinedChars.charAt(random.nextInt(combinedChars.length()));
        }
        return new String(password);
    }
}
