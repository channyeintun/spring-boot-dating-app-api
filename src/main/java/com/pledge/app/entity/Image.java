package com.pledge.app.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity(name = "image")
@NoArgsConstructor
@Getter
@Setter
public class Image extends BaseImage {

    @ToString.Exclude
    @ManyToOne(
            cascade = CascadeType.ALL
    )
    @JoinColumn(name = "profile_id")
    Profile profile;
}
