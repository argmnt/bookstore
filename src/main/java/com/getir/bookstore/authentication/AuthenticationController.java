package com.getir.bookstore.authentication;

import com.getir.bookstore.jwt.JWTToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<JWTToken> login(@RequestBody LoginDTO loginDTO) {
        JWTToken jwtToken = authenticationService.createJWTToken(loginDTO);
        return ResponseEntity.ok(jwtToken);
    }
}
