package fr.parisnanterre.greentrip.backend.controller.auth;

import fr.parisnanterre.greentrip.backend.config.JwtService;
import fr.parisnanterre.greentrip.backend.service.LogoutService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService service;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final LogoutService logoutService;

    public AuthenticationController(AuthenticationService service, UserDetailsService userDetailsService, JwtService jwtService, LogoutService logoutService) {
        this.service = service;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
        this.logoutService = logoutService;
    }

    /*@PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register (
            @RequestBody RegisterRequest request
    ){
        return ResponseEntity.ok(service.register(request));
    }*/

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            AuthenticationResponse response = service.register(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("l'adresse mail est inexistante");
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate (
            @RequestBody AuthenticationRequest request
    ){
        try {
            AuthenticationResponse response = service.authenticate(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return ResponseEntity.ok(service.authenticate(request));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<Map<String, String>> refreshToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");

        if (refreshToken == null || refreshToken.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Refresh token is required"));
        }

        try {
            String username = jwtService.extractUserName(refreshToken);

            if (username == null || !jwtService.isTokenValid(refreshToken, userDetailsService.loadUserByUsername(username))) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid refresh token"));
            }

            // Générer un nouveau access token
            String newAccessToken = jwtService.generateToken(userDetailsService.loadUserByUsername(username));
            return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Refresh token expired"));
        }
    }



    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("No token provided");
        }

        String jwt = authHeader.substring(7);
        logoutService.logout(jwt);

        return ResponseEntity.ok("Successfully logged out");
    }
}
