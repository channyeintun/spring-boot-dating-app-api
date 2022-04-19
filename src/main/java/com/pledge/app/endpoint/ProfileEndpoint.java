package com.pledge.app.endpoint;

import com.pledge.app.dto.*;
import com.pledge.app.entity.*;
import com.pledge.app.exception.CustomErrorException;
import com.pledge.app.payload.ProfileRequest;
import com.pledge.app.service.ProfilePictureService;
import com.pledge.app.service.UserService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Type;
import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import java.util.Set;

@RestController
public class ProfileEndpoint {
    @Autowired
    UserService userService;

    @Autowired
    ModelMapper mapper;

    @Autowired
    ProfilePictureService profilePictureService;

    @PutMapping("/profile/update")
    public ResponseEntity<?> updateProfile(@RequestBody ProfileRequest request, Principal principal) {
        Optional<User> opt = userService.findByUsername(principal.getName());
        try {
            if (opt.isPresent()) {
                User user = opt.get();
                Profile profile = user.getUserProfile();
                if (request.getName() != null) {
                    user.setName(request.getName());
                    profile.setDisplayName(request.getName());
                }
                if (request.getGender() != null) {
                    profile.setGender(request.getGender());
                }
                if (request.getAbout() != null) {
                    profile.setAbout(request.getAbout());
                }
                if (request.getBirthday() != null) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                    LocalDate date = LocalDate.parse(request.getBirthday(), formatter);
                    profile.setBirthday(java.sql.Date.valueOf(date));
                }
                if (request.getLookingFor() != null) {
                    profile.setLookingFor(request.getLookingFor());
                }
                if (request.getLocation() != null) {
                    profile.setLocation(request.getLocation());
                }
                if (request.getHobbies() != null) {
                    Type targetListType = new TypeToken<Set<Hobby>>() {
                    }.getType();
                    profile.setHobbies(mapper.map(request.getHobbies(), targetListType));
                }
                if (request.getInterestedIn() != null) {
                    if (request.getInterestedIn().getGender() == null) {
                        throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Gender should not be blanked.");
                    }
                    if (request.getInterestedIn().getAge() < 16) {
                        throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Age should be equal to or older than 16years.");
                    }
                    InterestedIn interestedIn = new InterestedIn();
                    interestedIn.setAge(request.getInterestedIn().getAge());
                    interestedIn.setGender(request.getInterestedIn().getGender());
                    user.setInterestedIn(interestedIn);
                }
                user.setUserProfile(profile);
                User saved = userService.save(user);
                UserDto response = mapper.map(saved, UserDto.class);
                response.setInterestedIn(mapper.map(saved.getInterestedIn(), InterestedInDto.class));
                return ResponseEntity.ok(response);
            }
        } catch (DateTimeParseException e) {
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Date format must be yyyy/MM/dd.");
        } catch (HttpClientErrorException.BadRequest e) {
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Bad Request");
        }
        return null;
    }

    @PutMapping("/profile/profile-picture")
    public ResponseEntity<?> updateProfilePicture(@RequestParam("file") MultipartFile file, Principal principal) {
        ProfilePicture saved = profilePictureService.save(file, principal.getName());
        ImageDto image = mapper.map(saved, ImageDto.class);
        return ResponseEntity.ok(image);
    }
}
