package com.pledge.app.util;

import com.pledge.app.entity.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/*
 * Util's for applying different filters
 */

@Slf4j
public class FilterUtils {

    public static List<User> applyAgeFilter(List<User> filteredUsers, List<?> values, User currentUser) {
        log.info("entered applyAgeFilter {}",filteredUsers.size());
        int interestedAge = (Integer) values.get(0);
        List<User> ageFiltered = new ArrayList<User>();
        filteredUsers.forEach(user -> {
            log.info("filteredUsers.forEach age {}",user.getUserId());
            if (user.getUserProfile().getBirthday() != null
                    && user.getUserId() != currentUser.getUserId()
                    && user.isLocked() == false
                    && user.getInterestedIn() !=null) {
                log.info("inside applyAgeFilter start if");
                if (user.getUserProfile().getAge() <= interestedAge
                        && currentUser.getUserProfile().getAge() <= user.getInterestedIn().getAge()
                ) {
                    ageFiltered.add(user);
                    log.info("inside applyAgeFilter worked{}",ageFiltered.size());
                }
            }
        });
        return ageFiltered;
    }

    public static List<User> applySexFilter(List<User> filteredUsers, List<?> values, User currentUser) {
        InterestedGender thisUserInterestedIn = (InterestedGender) values.get(0);
        Gender thisUserGender = currentUser.getUserProfile().getGender();
        List<User> sexFiltered = new ArrayList<>();
        filteredUsers.forEach(user -> {
            log.info("inside filterUsers.forEach sex {}",user.getUserId());
            if (user.getUserProfile().getGender() != null
                    && thisUserGender != null
                    && user.getUserId() != currentUser.getUserId()
                    && user.isLocked() == false
                    && user.getInterestedIn()!=null) {
                InterestedGender interestedGender = user.getInterestedIn().getGender();
                Gender userGender = user.getUserProfile().getGender();
                log.info("inside applySexFilter start if");
                log.info("thisUserInterestedIn {}",thisUserInterestedIn.toString());
                log.info("thisUserGender {}",thisUserGender.toString());
                log.info("interestedGender {}",interestedGender.toString());
                log.info("userGender {}",userGender.toString());
                if(thisUserInterestedIn.toString().equals(userGender.toString())
                    && interestedGender.toString().equals(thisUserGender.toString())){
                    sexFiltered.add(user);
                   // log.info("new Condition ");
                }
                if (thisUserInterestedIn == InterestedGender.EVERYONE
                        && interestedGender == InterestedGender.EVERYONE) {
                    sexFiltered.add(user);
                    log.info("EVERYONE");
                }
            }
        });
        log.info("after sexFiltered {}",sexFiltered.size());
        return sexFiltered;
    }

    public static List<User> filterLikedUsers(List<User> filteredUsers, List<Like> likes) {
        Set<Long> userIdSet = new HashSet<>();
        filteredUsers.forEach(user -> userIdSet.add(user.getUserId()));
        likes.forEach(like -> {
            if (userIdSet.contains(like.getLikedUserId())) {
                userIdSet.remove(like.getLikedUserId());
            }
        });
        filteredUsers = filteredUsers.stream().filter(user -> userIdSet.contains(user.getUserId()))
                .collect(Collectors.toList());
        return filteredUsers;
    }

    public static List<Like> filterMatchedUsers(List<Like> filteredLikens, List<Match> matches) {
        Set<Long> userIdSet = new HashSet<>();
        filteredLikens.forEach(liken -> userIdSet.add(liken.getUserId()));
        matches.forEach(match -> {
            if (userIdSet.contains(match.getUserId())) {
                userIdSet.remove(match.getUserId());
            }
            if (userIdSet.contains(match.getMatchedUserId())) {
                userIdSet.remove(match.getMatchedUserId());
            }
        });
        filteredLikens = filteredLikens.stream().filter(liken -> userIdSet.contains(liken.getUserId()))
                .collect(Collectors.toList());
        return filteredLikens;
    }
}
