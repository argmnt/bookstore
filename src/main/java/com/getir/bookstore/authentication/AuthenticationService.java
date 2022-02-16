package com.getir.bookstore.authentication;

import com.getir.bookstore.customers.CustomerServiceImpl;
import com.getir.bookstore.jwt.JWTService;
import com.getir.bookstore.jwt.JWTToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final CustomerServiceImpl customerServiceImpl;

    private final PasswordEncoder passwordEncoder;

    private final JWTService jwtService;

    public JWTToken createJWTToken(LoginDTO loginDTO) {
        try {
            Optional<UserDetails> optionalUserDetails = Optional.of(customerServiceImpl.loadUserByUsername(loginDTO.getEmail()));
            UserDetails userDetails = optionalUserDetails.orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User credentials are wrong"));
            if (passwordEncoder.matches(loginDTO.getPassword(), userDetails.getPassword()) && userDetails.isEnabled()) {
                Map<String, String> claims = jwtService.getClaims(loginDTO, userDetails);
                String jwt = jwtService.createJwtForClaims(loginDTO.getEmail(), claims);
                JWTToken jwtToken = new JWTToken();
                jwtToken.setToken(jwt);
                return jwtToken;
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User credentials are wrong");
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User credentials are wrong");
    }
}
