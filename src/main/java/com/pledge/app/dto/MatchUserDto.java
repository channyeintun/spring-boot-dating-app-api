package com.pledge.app.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MatchUserDto {
    Long userId;
    String name;
    String username;
    ProfileDto profile;
    String fcmToken;
}
