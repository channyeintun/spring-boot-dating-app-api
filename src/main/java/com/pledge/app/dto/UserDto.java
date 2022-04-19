package com.pledge.app.dto;

import lombok.Data;

@Data
public class UserDto {
    Long userId;
    String name;
    String username;
    boolean isLocked;
    Long point;
    ProfileDto profile;
    InterestedInDto interestedIn;
}
