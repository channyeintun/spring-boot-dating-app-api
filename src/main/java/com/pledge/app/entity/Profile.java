package com.pledge.app.entity;

import com.pledge.app.util.AgeCalculator;
import lombok.*;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Set;

@Entity(name = "profile")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String displayName;

    @OneToOne(cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinColumn(name = "profile_picture_id")
    private ProfilePicture profilePicture;

    private Date birthday;

    @Transient
    private int age;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(columnDefinition = "TEXT")
    private String about;

    private String lookingFor;

    @Embedded
    private Location location;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "profile_hobby", joinColumns = @JoinColumn(name = "profile_id"),
            inverseJoinColumns = @JoinColumn(name = "hobby_id"))
    private Set<Hobby> hobbies;

    @OneToOne(mappedBy = "userProfile")
    private User user;

    @OneToMany(
//            cascade= CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    @JoinColumn(name="profile_id")
    private Set<Image> images;

    @PostLoad
    @PostPersist
    @PostUpdate
    private void postLoad() {
        if (birthday != null) {
            this.age = AgeCalculator.calculateAge(birthday.toLocalDate(), LocalDate.now());
        }
    }
}
