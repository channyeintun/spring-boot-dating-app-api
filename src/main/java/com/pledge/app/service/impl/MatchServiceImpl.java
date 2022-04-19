package com.pledge.app.service.impl;

import java.util.*;

import com.pledge.app.dao.MatchDAO;
import com.pledge.app.entity.*;
import com.pledge.app.payload.Pagination;
import com.pledge.app.service.LikesService;
import com.pledge.app.service.FilterService;
import com.pledge.app.service.MatchService;
import com.pledge.app.service.UserService;
import com.pledge.app.util.FilterUtils;
import com.pledge.app.util.RandomNumber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MatchServiceImpl implements MatchService {

    @Autowired
    MatchDAO matchDAO;
    @Autowired
    FilterService filterService;
    @Autowired
    LikesService likesService;
    @Autowired
    MatchService matchService;

    @Autowired
    UserService userService;

    public List<Match> getAllMatches(Long userId) {
        return matchDAO.findByUserIdOrMatchedUserId(userId, userId);
    }

    public Match saveMatch(Match match) {
        try {
            Match saved = matchDAO.save(match);
            return saved;
        } catch (Exception exception) {
            log.error("error while saving match : {}", exception);
        }
        return null;
    }

    @Override
    public List<User> getPotentialMatches(User user, List<Filter> filters) {
        List<User> filteredUsers = getRandomUsers(filters, user);

        //Add some users who liked it. two Random users. May be in or may not in.
        List<Like> allLikens = likesService.getAllLiken(user.getUserId());

        List<Match> matches = matchService.getAllMatches(user.getUserId());
        // Filter already matched users
        List<Like> filteredLikens = FilterUtils.filterMatchedUsers(allLikens, matches);

        //Random to add or not likens to the response
        List<User> usersLikedMe = new ArrayList<>();

        int randomNumber = RandomNumber.generate(6); // 0-5 one in six times.
        int totalNumberOfLikens = filteredLikens.size();
        if (randomNumber == 0 && totalNumberOfLikens != 0) {
            Set<Long> userIdSet = new HashSet<>();
            for (int i = 0; i < 2; i++) {
                int random_index = totalNumberOfLikens == 0 ? 0 : RandomNumber.generate(totalNumberOfLikens);
                userIdSet.add(filteredLikens.get(random_index).getUserId());
            }
            for (Long likerId : userIdSet) {
                userService.findById(likerId).ifPresent(u -> usersLikedMe.add(u));
            }
        }

        List<Like> likes = likesService.getAllLikes(user.getUserId());
        // Filter already liked users
        filteredUsers = FilterUtils.filterLikedUsers(filteredUsers, likes);

        //Add users who liked this user
        filteredUsers.addAll(usersLikedMe);
        return filteredUsers;
    }

    @Override
    public void deleteAllMatches() {
        matchDAO.deleteAll();
    }

    private List<User> getRandomUsers(List<Filter> filters, User user) {
        int totalNumberOfUsers = userService.countAll();
        log.warn("total number of users in db : {}", totalNumberOfUsers);
        int totalNumberOfPages = totalNumberOfUsers / 10;
        log.info("totalNumberOfPages {}", totalNumberOfPages);
        int randomPageNo = totalNumberOfPages == 0 ? 0 : RandomNumber.generate(totalNumberOfPages);
        Pagination pagination = new Pagination(randomPageNo, 10);
        //Get Random users;
        List<User> filteredUsers = filterService.getUsers(filters, pagination, user);
        log.info("randomly generated users {}", filteredUsers.size());
        return filteredUsers;
    }

}
