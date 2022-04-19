package com.pledge.app.endpoint;

import com.pledge.app.entity.User;
import com.pledge.app.exception.CustomErrorException;
import com.pledge.app.payload.ApiResponse;
import com.pledge.app.payload.ConfirmationRequest;
import com.pledge.app.service.OTPService;
import com.pledge.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class VerificationEndpoint {
    @Autowired
    private UserService userService;

    @Autowired
    private OTPService otpService;

    @PostMapping("/account/verify")
    public ResponseEntity<?> confirmUser(@RequestBody ConfirmationRequest request) {
        Optional<User> opt = userService.findByUsername(request.getEmail());
        if (opt.isEmpty()) {
            throw new CustomErrorException(HttpStatus.NOT_FOUND, "User not found");
        }
        boolean isValidToken = otpService.validateOTPToken(request.getToken(), opt.get());
        if (isValidToken) {
            User user = opt.get();
            user.setVerified(true);
            userService.save(user);
            return ResponseEntity.ok(new ApiResponse(true, "Verification Successful"));
        }
        return ResponseEntity.badRequest().body(new ApiResponse(false,
                "Expired verification code"));
    }

    @PostMapping("/resend/verification-code")
    public ResponseEntity<?> sendOTPCode(@RequestParam("email") String email) {
        Optional<User> opt = userService.findByUsername(email);
        if (opt.isEmpty()) {
            throw new CustomErrorException(HttpStatus.NOT_FOUND, "User not found");
        }
        userService.sendMail(opt.get());
        return ResponseEntity
                .ok(new ApiResponse(true, "Verification code is sent to " + email + "."));
    }
}
