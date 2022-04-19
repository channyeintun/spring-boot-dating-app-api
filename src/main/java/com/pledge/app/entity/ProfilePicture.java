package com.pledge.app.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity(name="profile_picture")
@NoArgsConstructor
@Getter
@Setter
public class ProfilePicture extends BaseImage{
    @OneToOne(mappedBy = "profilePicture")
    private Profile profile;
}
