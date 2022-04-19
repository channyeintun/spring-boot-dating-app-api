package com.pledge.app.endpoint;

import com.pledge.app.dto.MatchUserDto;
import com.pledge.app.entity.*;
import com.pledge.app.exception.CustomErrorException;
import com.pledge.app.payload.ApiResponse;
import com.pledge.app.service.LikesService;
import com.pledge.app.service.MatchService;
import com.pledge.app.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/match")
@Slf4j
public class MatchingEndpoint {

    @Autowired
    LikesService likesService;


    @Autowired
    UserService userService;

    @Autowired
    MatchService matchService;

    @Autowired
    ModelMapper mapper;

    @GetMapping
    public ResponseEntity<?> getAllMatches(Principal principal) {
        Optional<User> opt = userService.findByUsername(principal.getName());
        if (opt.isPresent()) {
            User user = opt.get();
            List<Match> matches = matchService.getAllMatches(user.getUserId());
            log.info("get matches {}", matches.size());
            List<User> matchedUsers = new ArrayList<>();
            for (Match match : matches) {
                Long matchedUserId = getMatchedUserId(user.getUserId(), match);
                userService.findById(matchedUserId).ifPresent(usr -> matchedUsers.add(usr));
            }
            log.info("matched users {}", matchedUsers.size());
            Type targetListType = new TypeToken<List<MatchUserDto>>() {
            }.getType();
            List<MatchUserDto> matchedUserDtos = mapper.map(matchedUsers, targetListType);
            return ResponseEntity.ok(matchedUserDtos);
        }
        throw new CustomErrorException(HttpStatus.NOT_FOUND, "User not found");
    }

    @GetMapping("/find")
    public ResponseEntity<?> getPotentialMatches(Principal principal) {

        try {
            Optional<User> opt = userService.findByUsername(principal.getName());
            List<MatchUserDto> matchUserDtos = new ArrayList<>();
            if (opt.isPresent()) {
                User user = opt.get();
                if (user.getPoint() <= 0) {
                    throw new CustomErrorException(HttpStatus.BAD_REQUEST, "No enough point.");
                }
                List<Filter> filters = new ArrayList<>();
                //Add Sex filter
                Filter sexFilter = new Filter();
                sexFilter.setType(FilterType.SEX);
                List<InterestedGender> sexValues = new ArrayList<>();
                sexValues.add(user.getInterestedIn().getGender());
                sexFilter.setValues(sexValues);
                filters.add(sexFilter);

                //Add Age filter
                Filter ageFilter = new Filter();
                ageFilter.setType(FilterType.AGE);
                List<Integer> ageValues = new ArrayList<>();
                ageValues.add(user.getInterestedIn().getAge());
                ageFilter.setValues(ageValues);
                filters.add(ageFilter);

                List<User> users = matchService.getPotentialMatches(
                        user,
                        filters);
                log.info("random from userService {}", users.size());
                Type targetListType = new TypeToken<List<MatchUserDto>>() {
                }.getType();
                matchUserDtos = mapper.map(users, targetListType);
            }
            return ResponseEntity.ok(matchUserDtos);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "You need to complete the profile. Check your profile.");
        }
    }

    /* Development purpose */
    //    @RolesAllowed({"ROLE_ADMIN", "ROLE_DEVELOPER"})
    @DeleteMapping
    public ResponseEntity<?> deleteAllMatchesAndLikes() {
        likesService.deleteAllLikes();
        matchService.deleteAllMatches();
        return ResponseEntity.ok(new ApiResponse(true, "Cleared all matches and likes in database."));
    }

    Long getMatchedUserId(Long userId, Match match) {
        return match.getUserId() != userId ? match.getUserId() : match.getMatchedUserId();
    }

}
