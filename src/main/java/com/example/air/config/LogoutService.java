package com.example.air.config;

import com.example.air.token.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

  private final TokenRepository tokenRepository;
  private final JwtService jwtService;
  private final UserDetailsService userDetailsService;
  @Override
  public void logout(
          HttpServletRequest request,
          HttpServletResponse response,
          Authentication authentication
  ) {
    final String authHeader = request.getHeader("Authorization");
    final String jwt;

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      return;
    }

    jwt = authHeader.substring(7);
    final String userEmail = jwtService.extractUsername(jwt); // Extract username from JWT

    if (userEmail != null) {
      // Find the user associated with the token
      var user = userDetailsService.loadUserByUsername(userEmail);

      // Find tokens associated with the user
      var tokens = tokenRepository.findAllValidTokenByUser(user.getUsername());
      tokens.stream()
              .filter(t -> t.getToken().equals(jwt))
              .findFirst()
              .ifPresent(token -> {
                token.setExpired(true);
                token.setRevoked(true);
                tokenRepository.save(token);
              });

      SecurityContextHolder.clearContext();
    }
  }
}
