package com.pledge.app.service.impl;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.pledge.app.dao.LikeDAO;
import com.pledge.app.dto.MatchUserDto;
import com.pledge.app.entity.Like;
import com.pledge.app.entity.Match;
import com.pledge.app.entity.User;
import com.pledge.app.exception.CustomErrorException;
import com.pledge.app.service.FirebaseMessagingService;
import com.pledge.app.service.LikesService;
import com.pledge.app.service.MatchService;
import com.pledge.app.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class LikesServiceImpl implements LikesService {

    @Autowired
    LikeDAO likeDAO;
    @Autowired
    MatchService matchService;
    @Autowired
    UserService userService;
    @Autowired
    FirebaseMessagingService firebaseMessagingService;
    @Autowired
    ModelMapper mapper;

    public List<Like> getAllLikes(Long userId) {
        return likeDAO.findByUserId(userId);
    }

    @Override
    public List<Like> getAllLiken(Long userId) {
        return likeDAO.findByLikedUserId(userId);
    }

    public Match saveLike(Like like) {
        try {
            like.setUserId(like.getUserId());
            like.setLikedUserId(like.getLikedUserId());

            //this is for Development validation purpose
            Like isDone = likeDAO.findByUserIdAndLikedUserId(like.getUserId(), like.getLikedUserId());
            if (isDone != null) {
                //throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Already liked to this user.");
                return null;
            }

            likeDAO.save(like);
            log.warn("{} Liked {}", like.getUserId(), like.getLikedUserId());
            // Check if user is also liked by likedUser
            Like isLiked = likeDAO.findByUserIdAndLikedUserId(like.getLikedUserId(), like.getUserId());
            log.info("isLikes : {}", isLiked);
            if (isLiked != null) {
                // create match
                Match match = new Match(like.getUserId(), like.getLikedUserId());
                Match found = matchService.saveMatch(match);
                log.warn("Match Found between {} and {}", like.getUserId(), like.getLikedUserId());
                Optional<User> optUser = userService.findById(like.getUserId());
                optUser.ifPresent(usr -> {
                    String fcmToken = usr.getFcmToken();
                    log.info("fcmToken in matched {}", fcmToken);
                    Map<String, Object> payload = new HashMap<>();
                    payload.put("title", "New Match");
                    payload.put("body", usr.getUserProfile().getDisplayName() + " matched with you!");
                    MatchUserDto matchedUser = mapper.map(usr, MatchUserDto.class);
                    payload.put("data", matchedUser);
                    try {
                        firebaseMessagingService.sendNotificationToToken(payload, fcmToken);
                    } catch (FirebaseMessagingException e) {
                        e.printStackTrace();
                    }
                    log.info("Notification sent to {}", like.getLikedUserId());
                });
                return found;
            } else {
                return null;
            }
        } catch (CustomErrorException e) {
            e.printStackTrace();
            throw e;
        } catch (Exception exception) {
            exception.printStackTrace();
            log.error("error while saving like : {}", exception);
            throw new CustomErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error.");
        }
    }

    @Override
    public void deleteAllLikes() {
        likeDAO.deleteAll();
    }
}
