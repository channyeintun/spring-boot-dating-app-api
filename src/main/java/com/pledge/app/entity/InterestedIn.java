package com.pledge.app.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity(name="interested_in")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class InterestedIn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Enumerated(EnumType.STRING)
    private InterestedGender gender;
    private int age;
    @OneToOne(mappedBy = "interestedIn")
    private User user;
}
