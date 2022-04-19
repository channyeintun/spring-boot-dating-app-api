package com.pledge.app.endpoint;

import com.pledge.app.entity.Like;
import com.pledge.app.entity.Match;
import com.pledge.app.entity.User;
import com.pledge.app.exception.CustomErrorException;
import com.pledge.app.payload.LikeResponse;
import com.pledge.app.service.LikesService;
import com.pledge.app.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/like")
@Slf4j
public class LikeEndpoint {

    @Autowired
    UserService userService;

    @Autowired
    LikesService likesService;

    @GetMapping
    public ResponseEntity<?> like(@RequestParam("likedUserId") Long likedUserId, Principal principal) {
        Optional<User> opt = userService.findByUsername(principal.getName());
        if (opt.isPresent()) {
            User user = opt.get();
            if (user.getPoint() <= 0) {
                throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Not enough point");
            }
            if (user.getUserId() == likedUserId) {
                throw new CustomErrorException(HttpStatus.BAD_REQUEST, "You cannot like yourself.");
            }
            Match match = likesService.saveLike(new Like(user.getUserId(), likedUserId));

            return ResponseEntity.ok(new LikeResponse(match != null));
        }
        throw new CustomErrorException(HttpStatus.NOT_FOUND, "Liker - User not exist");
    }
}
