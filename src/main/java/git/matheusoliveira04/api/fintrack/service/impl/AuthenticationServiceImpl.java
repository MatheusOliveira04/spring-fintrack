package git.matheusoliveira04.api.fintrack.service.impl;

import git.matheusoliveira04.api.fintrack.config.jwts.JwtService;
import git.matheusoliveira04.api.fintrack.config.jwts.JwtUserDetailsService;
import git.matheusoliveira04.api.fintrack.dto.request.LoginRequest;
import git.matheusoliveira04.api.fintrack.dto.response.LoginResponse;
import git.matheusoliveira04.api.fintrack.service.AuthenticationService;
import git.matheusoliveira04.api.fintrack.service.exception.UsernameNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Override
    public LoginResponse authenticate(LoginRequest loginRequest) {
        Authentication authentication = authenticateUser(loginRequest);

        if (authentication.isAuthenticated()) {
            var refreshToken = jwtService.generateRefreshToken(loginRequest.getUsername());
            var token = jwtService.generateToken(loginRequest.getUsername());
            return LoginResponse.builder()
                    .accessToken(token)
                    .refreshToken(refreshToken)
                    .build();
        }

        throw new UsernameNotFoundException("Invalid user");
    }

    private Authentication authenticateUser(LoginRequest loginRequest) {
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );
    }

    @Override
    public LoginResponse refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = extractRefreshToken(request);
        String username = extractUsernameFromToken(refreshToken);

        if (isValidRefreshToken(refreshToken, username)) {
            var accessToken = jwtService.generateToken(username);
            return LoginResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        }
        throw new UsernameNotFoundException("Invalid user");
    }

    private String extractRefreshToken(HttpServletRequest request) {
        final String authHeader = getHeader(request);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    private String getHeader(HttpServletRequest request) {
        return request.getHeader(HttpHeaders.AUTHORIZATION);
    }

    private String extractUsernameFromToken(String refreshToken) {
        if (refreshToken != null) {
            return jwtService.extractUsername(refreshToken);
        }
        return null;
    }

    private boolean isValidRefreshToken(String refreshToken, String username) {
        if (username != null || refreshToken != null) {
            UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(username);
            return jwtService.validateToken(refreshToken, userDetails);
        }
        return false;
    }
}
