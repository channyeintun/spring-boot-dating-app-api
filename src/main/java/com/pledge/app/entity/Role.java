package com.pledge.app.entity;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Set;

@Entity(name = "role")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(indexes = {@Index(name = "roleName", columnList = "name", unique = true)})
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private RoleName name;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> users;

    public Role(RoleName name){
        this.name=name;
    }

    public String getAuthority() {
        return this.name.name();
    }
}
