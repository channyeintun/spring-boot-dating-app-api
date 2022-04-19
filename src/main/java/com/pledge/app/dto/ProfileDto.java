package com.pledge.app.dto;

import com.pledge.app.entity.Gender;
import com.pledge.app.entity.Location;
import com.pledge.app.entity.ProfilePicture;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.Set;

@Data
@NoArgsConstructor
public class ProfileDto {
    private Long id;
    private String displayName;
    private ImageDto profilePicture;
    private Set<ImageDto> images;
    private Date birthday;
    private Gender gender;
    private String about;
    private String lookingFor;
    private Location location;
    private int age;
    private Set<HobbyDto> hobbies;
}
