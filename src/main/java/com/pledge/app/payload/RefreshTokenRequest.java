package com.pledge.app.payload;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class RefreshTokenRequest {
    @NonNull
    String refreshToken;
}
