package git.matheusoliveira04.api.fintrack.controller;

import git.matheusoliveira04.api.fintrack.config.jwts.JwtService;
import git.matheusoliveira04.api.fintrack.dto.request.LoginRequest;
import git.matheusoliveira04.api.fintrack.dto.response.LoginResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class JwtController {

    private JwtService jwtService;
    private AuthenticationManager authenticationManager;

    public JwtController(JwtService jwtService, AuthenticationManager authenticationManager) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticateAndGetToken(@RequestBody @Valid LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        if (authentication.isAuthenticated()) {
            return ResponseEntity.ok(new LoginResponse(jwtService.generateToken(loginRequest.getUsername())));
        }

        throw new UsernameNotFoundException("Invalid user");
    }
}
