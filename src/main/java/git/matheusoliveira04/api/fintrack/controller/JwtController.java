package git.matheusoliveira04.api.fintrack.controller;

import git.matheusoliveira04.api.fintrack.config.jwts.JwtService;
import git.matheusoliveira04.api.fintrack.config.jwts.JwtUserDetailsService;
import git.matheusoliveira04.api.fintrack.dto.request.LoginRequest;
import git.matheusoliveira04.api.fintrack.dto.response.LoginResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class JwtController {

    private JwtService jwtService;
    private AuthenticationManager authenticationManager;
    private JwtUserDetailsService jwtUserDetailsService;

    public JwtController(JwtService jwtService, AuthenticationManager authenticationManager, JwtUserDetailsService jwtUserDetailsService) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.jwtUserDetailsService = jwtUserDetailsService;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<LoginResponse> authenticateAndGetToken(@RequestBody @Valid LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        if (authentication.isAuthenticated()) {
            var refreshToken = jwtService.generateRefreshToken(loginRequest.getUsername());
            var token = jwtService.generateToken(loginRequest.getUsername());
            return ResponseEntity.ok(new LoginResponse(token, refreshToken));
        }

        throw new UsernameNotFoundException("Invalid user");
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<LoginResponse> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String refreshToken = null;
        String username = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            refreshToken = authHeader.substring(7);
            username = jwtService.extractUsername(refreshToken);
        }

        if (username != null) {
            UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(username);

            if (jwtService.validateToken(refreshToken, userDetails)) {
                var accessToken = jwtService.generateToken(userDetails.getUsername());
                return ResponseEntity.ok(new LoginResponse(accessToken, refreshToken));
            }
        }

        throw new UsernameNotFoundException("Invalid user");
    }
}
