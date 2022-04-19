package com.pledge.app.payload;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserSummary {

    private Long id;
    private String email;
    private String name;
    private String profilePicture;
}
