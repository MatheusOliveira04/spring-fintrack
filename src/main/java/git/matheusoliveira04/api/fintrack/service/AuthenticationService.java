package git.matheusoliveira04.api.fintrack.service;

import git.matheusoliveira04.api.fintrack.dto.request.LoginRequest;
import git.matheusoliveira04.api.fintrack.dto.response.LoginResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthenticationService {

    public LoginResponse authenticate(LoginRequest loginRequest);

    public LoginResponse refreshToken(HttpServletRequest request, HttpServletResponse response);
}
