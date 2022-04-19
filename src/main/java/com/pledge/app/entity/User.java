package com.pledge.app.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.List;

@Entity(name = "user")
@Getter
@Setter
@NoArgsConstructor
@Table(indexes = {@Index(name = "userId", columnList = "id", unique = true),
        @Index(name = "username", columnList = "username", unique = true)})
@EqualsAndHashCode(of = {"userId", "name", "username"})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long userId;

    @NotBlank
    private String name;

    @NotBlank
    @Size(max = 40)
    @Email
    private String username;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_id", referencedColumnName = "id")
    private Profile userProfile;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "interestedIn_id", referencedColumnName = "id")
    private InterestedIn interestedIn;

    @Column(name = "password")
    private String password;

    @Column(name = "isVerified")
    private boolean isVerified = false;

    @Column(name = "isLocked")
    private boolean isLocked = false;

    @Column(name = "isDeactivated")
    private boolean isDeactivated = false;

    private Long point;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private OTP otp;

    @Column(name="fcm_token")
    private String fcmToken;

    public User(User user) {
        this.userId = user.userId;
        this.username = user.username;
        this.password = user.password;
        this.userProfile = user.userProfile;
        this.roles = user.roles;
    }

    @PrePersist
    public void setDefaultPoint() {
        if (point == null) {
            this.point = Long.valueOf(10);
        }
    }

}
