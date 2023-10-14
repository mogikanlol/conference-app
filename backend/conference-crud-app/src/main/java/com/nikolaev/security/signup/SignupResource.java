package com.nikolaev.security.signup;

import com.nikolaev.security.service.JwtAuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth/signup")
@RequiredArgsConstructor
public class SignupResource {

    private final SignupService signupService;

    @PostMapping
    public JwtAuthenticationResponse createUser(@RequestBody SignupRequest signupRequest) throws SignupException {
        return signupService.signup(signupRequest);
    }
}
