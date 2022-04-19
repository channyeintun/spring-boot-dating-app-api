package com.pledge.app.endpoint;

import com.pledge.app.dto.ImageDto;
import com.pledge.app.dto.ProfileDto;
import com.pledge.app.dto.UserDto;
import com.pledge.app.entity.Image;
import com.pledge.app.entity.Profile;
import com.pledge.app.entity.User;
import com.pledge.app.exception.CustomErrorException;
import com.pledge.app.payload.ApiResponse;
import com.pledge.app.payload.UserSummary;
import com.pledge.app.service.ImageService;
import com.pledge.app.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.Optional;


@RestController
@Slf4j
@RequestMapping("/users")
public class UserEndpoint {

    @Autowired
    private UserService userService;

    @Autowired
    private ImageService imageService;

    @Autowired
    ModelMapper mapper;

    //    @RolesAllowed({"ROLE_ADMIN", "ROLE_DEVELOPER"})
    @GetMapping
    public ResponseEntity<?> findUser(@RequestParam("email") String username) {
        log.info("retrieving user {}", username);

        return userService
                .findByUsername(username)
                .map(user -> ResponseEntity.ok(mapper.map(user, UserDto.class)))
                .orElseThrow(() -> new CustomErrorException(HttpStatus.NOT_FOUND, "User not found"));
    }

    @PutMapping
    public ResponseEntity<?> lockUser(@RequestParam("locked") boolean locked,
                                      @RequestParam("email") String username) {
        Optional<User> opt = userService.findByUsername(username);
        if (opt.isEmpty()) {
            throw new CustomErrorException(HttpStatus.NOT_FOUND, "User not found.");
        } else {
            User user = opt.get();
            user.setLocked(locked);
            User updated = userService.save(user);
            return ResponseEntity.ok(mapper.map(updated, UserDto.class));
        }
    }

    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getCurrentUser(Principal principal) {
        Optional<User> opt = userService.findByUsername(principal.getName());
        if (opt.isPresent()) {
            User user = opt.get();
            Profile profile = user.getUserProfile();
            ProfileDto profileDto = mapper.map(profile, ProfileDto.class);
            UserDto userDto = mapper.map(user, UserDto.class);
            userDto.setProfile(profileDto);
            return ResponseEntity.ok(userDto);
        } else {
            throw new CustomErrorException(HttpStatus.NOT_FOUND, "User Not Found");
        }
    }

    @GetMapping(value = "/summary/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserSummary(@PathVariable("username") String username) {
        log.info("retrieving user {}", username);

        return userService
                .findByUsername(username)
                .map(user -> ResponseEntity.ok(convertTo(user)))
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found", null));
    }

    private UserSummary convertTo(User user) {
        return UserSummary
                .builder()
                .id(user.getUserId())
                .email(user.getUsername())
                .name(user.getUserProfile().getDisplayName())
                .build();
    }

    @PutMapping("/images")
    public ResponseEntity<?> updateImages(Principal principal, @RequestParam("files") MultipartFile[] files) {
        User saved = imageService.save(principal.getName(), files);
        UserDto user = mapper.map(saved, UserDto.class);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{email}/images")
    public ResponseEntity<?> deleteImageById(@PathVariable("email") String username, @RequestBody ImageDto request) {
        Optional<User> opt = userService.findByUsername(username);
        if (opt.isEmpty()) {
            throw new CustomErrorException(HttpStatus.NOT_FOUND, "User not found.");
        } else {
            User user = opt.get();
            user.getUserProfile().getImages().remove(mapper.map(request, Image.class));
            User updated = userService.save(user);
            return ResponseEntity.ok(mapper.map(updated, UserDto.class));
        }
    }

    @PutMapping("/update/fcm-token")
    public ResponseEntity<?> updateFcmToken(Principal principal, @RequestParam("fcmToken") String fcmToken) {
        userService.updateFcmToken(fcmToken, principal.getName());
        return ResponseEntity.ok(new ApiResponse(true, "Updated FCM Token"));
    }
}