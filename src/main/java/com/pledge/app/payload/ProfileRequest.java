package com.pledge.app.payload;

import com.pledge.app.dto.HobbyDto;
import com.pledge.app.entity.Gender;
import com.pledge.app.entity.InterestedIn;
import com.pledge.app.entity.Location;
import lombok.Data;

import java.util.Set;

@Data
public class ProfileRequest {
    private String name;
    private String birthday;
    private Gender gender;
    private String about;
    private String lookingFor;
    private Location location;
    private InterestedIn interestedIn;
    private Set<HobbyDto> hobbies;
}
