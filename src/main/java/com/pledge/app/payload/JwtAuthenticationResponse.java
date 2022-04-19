package com.pledge.app.payload;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class JwtAuthenticationResponse {

    @NonNull
    private String accessToken;
    @NonNull
    private String refreshToken;
    @NonNull
    private Long expireAt;
    private String tokenType = "Bearer";
}
