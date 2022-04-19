package com.pledge.app.entity;

import lombok.*;

import javax.persistence.*;

@Entity(name="matches")
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NonNull
    @Column(name="userId")
    private Long userId;
    @NonNull
    @Column(name="matchedUserId")
    private Long matchedUserId;
}
